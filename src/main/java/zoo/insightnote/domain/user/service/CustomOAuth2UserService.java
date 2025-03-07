package zoo.insightnote.domain.user.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.dto.CustomOAuth2User;
import zoo.insightnote.domain.user.dto.GoogleResponse;
import zoo.insightnote.domain.user.dto.KakaoResponse;
import zoo.insightnote.domain.user.dto.OAuth2Response;
import zoo.insightnote.domain.user.dto.UserDto;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response =  getOAuth2User(registrationId, oAuth2User);

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        UserDto userDto = UserDto.builder()
                .username(username)
                .name(oAuth2Response.getName())
                .role("ROLE_USER")
                .build();

        return new CustomOAuth2User(userDto);
    }

    private static OAuth2Response getOAuth2User(String registrationId, OAuth2User oAuth2User) {
        if (registrationId.equals("kakao")){
            return new KakaoResponse(oAuth2User.getAttributes());
        }
        if (registrationId.equals("google")){
            return new GoogleResponse(oAuth2User.getAttributes());
        }
        return null;
    }
}
