package zoo.insightnote.domain.speaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.repository.SpeakerRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class SpeakerService {
    private final SpeakerRepository speakerRepository;

    public Speaker findById(Long speakerId) {
        return speakerRepository.findById(speakerId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPEAKER_NOT_FOUND));
    }
}
