package bg.springadvancedexam.mainapp.context;

import bg.springadvancedexam.mainapp.security.JwtAuthFilter;
import bg.springadvancedexam.mainapp.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationProvider authenticationProvider,
                                            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/manuscripts/*/notes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/manuscripts", "/api/manuscripts/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/me/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me/profile").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/role").hasRole("ADMIN")

                        .requestMatchers("/api/manage/**", "/api/manuscripts/*/digitize",
                                "/api/manuscripts/*/digitization-status").hasAnyRole("CURATOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/manuscripts").hasAnyRole("CURATOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/manuscripts/*").hasAnyRole("CURATOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/manuscripts/*/visibility").hasAnyRole("CURATOR", "ADMIN")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/reservations").hasAnyRole("CURATOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/reservations/mine").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/reservations/*").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/access-requests").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/access-requests/mine").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/access-requests").hasAnyRole("CURATOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/access-requests/*/decision").hasAnyRole("CURATOR", "ADMIN")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}