package zoo.insightnote.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import zoo.insightnote.domain.user.service.CustomUserDetailsService;

@RequiredArgsConstructor
public class GuestAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = (String) authentication.getPrincipal();
        String email = (String) authentication.getCredentials();
        UserDetails userDetails = customUserDetailsService.loadUserByNameAndEmail(name, email);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GuestAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
