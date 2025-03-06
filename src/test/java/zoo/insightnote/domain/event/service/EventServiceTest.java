package zoo.insightnote.domain.event.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.insightnote.domain.event.dto.req.EventRequestDto;
import zoo.insightnote.domain.event.dto.req.EventUpdateRequestDto;
import zoo.insightnote.domain.event.dto.res.EventResponseDto;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.repository.EventRepository;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.image.entity.Image;
import zoo.insightnote.domain.image.repository.ImageRepository;
import zoo.insightnote.domain.s3.service.S3Service;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventRequestDto requestDto;
    private EventUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .name("기존 이벤트")
                .description("기존 설명")
                .location("서울")
                .startTime(null)
                .endTime(null)
                .build();

        // 신규 생성시 image1,image2 이미지 있다고 가정
        requestDto = new EventRequestDto(
                "새로운 이벤트",
                "새로운 설명",
                "부산",
                null,
                null,
                List.of("image1.jpg", "image2.jpg")
        );

        // 업데이트 경우 기존의 image1 삭제하고 새로운 image3 추가
        updateRequestDto = new EventUpdateRequestDto(
                "수정된 이벤트",
                "수정된 설명",
                "제주",
                null,
                null,
                List.of("image2.jpg", "image3.jpg")
        );
    }

    @Test
    @DisplayName("[이벤트 생성 성공] 새로운 이벤트를 생성하면 요청한 정보를 저장하고, 클라이언트가 전달한 이미지도 함께 저장된다.")
    void createEvent_success() {
        // Given
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        EventResponseDto response = eventService.createEvent(requestDto);

        // Then
        assertThat(response.name()).isEqualTo("새로운 이벤트");
        assertThat(response.description()).isEqualTo("새로운 설명");
        assertThat(response.location()).isEqualTo("부산");

        verify(imageRepository, times(1)).saveAll(
                argThat((List<Image> images) ->
                        images != null && images.stream()
                                .map(Image::getFileUrl)
                                .equals(List.of("image1.jpg", "image2.jpg"))
                )
        );
    }

    @Test
    @DisplayName("[이벤트 수정 성공] 기존 이미지 중 삭제된 이미지는 제거하고, 새로운 이미지는 추가 저장된다.")
    void updateEvent_success() {
        // Given
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));

        // 기존 저장된 이미지 리스트 (image1, image2)
        List<Image> existingImages = new ArrayList<>(List.of(
                Image.of("event-image", "image1.jpg", 1L, EntityType.EVENT),
                Image.of("event-image", "image2.jpg", 1L, EntityType.EVENT)
        ));

        when(imageRepository.findByEntityIdAndEntityType(any(Long.class), eq(EntityType.EVENT)))
                .thenReturn(existingImages);

        // When
        EventResponseDto response = eventService.updateEvent(1L, updateRequestDto);

        // Then
        assertThat(response.name()).isEqualTo("수정된 이벤트");
        assertThat(response.description()).isEqualTo("수정된 설명");
        assertThat(response.location()).isEqualTo("제주");

        // 삭제된 이미지 (image1) 검증
        // verify라 실제로 s3에서 이미지가 삭제되었는지 확인하지는 않습니다.
        verify(s3Service, times(1)).deleteImages(List.of("image1.jpg"));
        verify(imageRepository, times(1)).deleteByFileUrlIn(List.of("image1.jpg"));

        // 추가된 이미지 (image3) 검증
        verify(imageRepository, times(1)).saveAll(
                argThat((List<Image> images) -> images != null && images.stream()
                        .map(Image::getFileUrl)
                        .anyMatch(url -> url.equals("image3.jpg"))
                )
        );
    }

    @Test
    @DisplayName("[이벤트 수정 실패] 존재하지 않는 이벤트 ID로 요청하면 EVENT_NOT_FOUND 예외 발생한다 ")
    void updateEvent_fail_notFound() {
        // Given
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> eventService.updateEvent(1L, updateRequestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EVENT_NOT_FOUND)
                .hasMessage(ErrorCode.EVENT_NOT_FOUND.getErrorMessage());
    }
}