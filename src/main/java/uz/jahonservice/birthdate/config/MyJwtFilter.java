package uz.jahonservice.birthdate.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.jahonservice.birthdate.entity.User;
import uz.jahonservice.birthdate.repository.UserRepository;
import uz.jahonservice.birthdate.service.jwtService.JwtService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyJwtFilter implements Filter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtService.extractEmail(token);
                Optional<User> optionalUser = userRepository.findByEmail(username);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    GrantedAuthority role = () -> "ROLE_" + user.getRole().name();//ROLE_ADMIN
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(role)
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ignored) {
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
