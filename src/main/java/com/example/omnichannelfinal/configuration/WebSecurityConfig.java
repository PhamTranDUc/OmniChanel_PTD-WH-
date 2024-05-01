package com.example.omnichannelfinal.configuration;

import com.example.omnichannelfinal.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer:: disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        requests ->{
                            requests.requestMatchers(
                                            "/omni-chat",
                                            "/api/v1/users/login","/test",
                                            "/api/v1/users/register"

                                    ).permitAll()
                                    //.requestMatchers(HttpMethod.GET,"/omni-chat").permitAll()
                                    .requestMatchers(HttpMethod.GET,"/api/v1/products/**",
                                            "/api/v1/categories/**").permitAll()
                                    .requestMatchers(HttpMethod.GET,"/upload/**"
                                    ).permitAll()
//                                    .requestMatchers(HttpMethod.POST,"api/v1/users/detail"
//                                    ).authenticated()
//                                    .requestMatchers(HttpMethod.GET,"/test2"
//                                    ).authenticated()
                                    .requestMatchers(HttpMethod.GET,"/api/v1/omni-chanel/**").hasRole("PAGE1")
                                    .requestMatchers(HttpMethod.POST,"/api/v1/omni-chanel/**").hasRole("PAGE1")
                                    .requestMatchers(HttpMethod.PUT,"/api/v1/omni-chanel/**").hasRole("PAGE1")
                                    .requestMatchers(HttpMethod.DELETE,"/api/v1/omni-chanel/**").hasRole("PAGE1")
                                    .requestMatchers(HttpMethod.GET,"/api/v1/admin/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.POST,"/api/v1/admin/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.PUT,"/api/v1/admin/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/**").hasRole("ADMIN")
                                    .anyRequest().permitAll();
                        }

                );

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();

//        http.cors(cors -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Thay thế với nguồn cụ thể
//            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//            configuration.setExposedHeaders(List.of("x-auth-token"));
//            configuration.setAllowCredentials(true); // Cho phép credentials
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", configuration);
//            cors.configurationSource(source);
//        });
//
//        return http.build();

    }
}
