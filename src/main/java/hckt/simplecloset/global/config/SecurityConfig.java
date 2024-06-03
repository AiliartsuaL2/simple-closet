package hckt.simplecloset.global.config;

import hckt.simplecloset.global.application.service.GetTokenInfoProvider;
import hckt.simplecloset.global.application.service.JwtService;
import hckt.simplecloset.global.handler.JwtAccessDeniedHandler;
import hckt.simplecloset.global.handler.JwtAuthenticationEntryPoint;
import hckt.simplecloset.global.handler.JwtAuthenticationFilter;
import hckt.simplecloset.global.handler.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] OAUTH_V1_WHITELIST = {
            "/api/v1.0/oauth/google",
            "/api/v1.0/oauth/kakao",
            "/api/v1.0/oauth/apple",
            "/api/v1.0/oauth/info"
    };
    private static final String[] SIGN_IN_WHITELIST = {
            "/api/v1.0/sign-in",
            "/api/v1.0/sign-up",
            "/api/v1.0/token/refresh"
    };

    private static final String AUTHENTICATION_TEST_URL = "/api/v1/auth/not-use/test";

    private final JwtService jwtService;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/resources/templates/member/login.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/member/login").permitAll()
                        .requestMatchers(OAUTH_V1_WHITELIST).permitAll()
                        .requestMatchers(SIGN_IN_WHITELIST).permitAll()
                        .requestMatchers(AUTHENTICATION_TEST_URL).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e ->
                        e.accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }
}
