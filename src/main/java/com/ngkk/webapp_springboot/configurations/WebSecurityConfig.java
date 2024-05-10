package com.ngkk.webapp_springboot.configurations;

import com.ngkk.webapp_springboot.filters.JwtTokenFilter;
import com.ngkk.webapp_springboot.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String ENDPOINT_CATEGORY = "%s/categories/**";
    private static final String ENDPOINT_PRODUCT = "%s/products/**";
    private static final String ENDPOINT_ORDER = "%s/orders/**";
    private static final String ENDPOINT_ORDER_DETAIL = "%s/order_details/**";


    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                String.format("%s/users/register", apiPrefix),
                                String.format("%s/users/login", apiPrefix)
                        )
                        .permitAll()

                        .requestMatchers(GET,
                                String.format("%s/roles**", apiPrefix)).permitAll()

                        .requestMatchers(GET,
                                String.format("%s/categories**", apiPrefix)).permitAll()

                        .requestMatchers(POST,
                                String.format(ENDPOINT_CATEGORY, apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(PUT,
                                String.format(ENDPOINT_CATEGORY, apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(DELETE,
                                String.format(ENDPOINT_CATEGORY, apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(GET,
                                String.format("%s/products**", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format(ENDPOINT_PRODUCT, apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format("%s/products/images/*", apiPrefix)).permitAll()

                        .requestMatchers(POST,
                                String.format("%s/products**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(PUT,
                                String.format(ENDPOINT_PRODUCT, apiPrefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(DELETE,
                                String.format(ENDPOINT_PRODUCT, apiPrefix)).hasAnyRole(Role.ADMIN)


                        .requestMatchers(POST,
                                String.format(ENDPOINT_ORDER, apiPrefix)).hasAnyRole(Role.USER)

                        .requestMatchers(GET,
                                String.format(ENDPOINT_ORDER, apiPrefix)).permitAll()

                        .requestMatchers(PUT,
                                String.format(ENDPOINT_ORDER, apiPrefix)).hasRole(Role.ADMIN)

                        .requestMatchers(DELETE,
                                String.format(ENDPOINT_ORDER, apiPrefix)).hasRole(Role.ADMIN)

                        .requestMatchers(POST,
                                String.format(ENDPOINT_ORDER_DETAIL, apiPrefix)).hasAnyRole(Role.USER)

                        .requestMatchers(GET,
                                String.format(ENDPOINT_ORDER_DETAIL, apiPrefix)).permitAll()

                        .requestMatchers(PUT,
                                String.format(ENDPOINT_ORDER_DETAIL, apiPrefix)).hasRole(Role.ADMIN)

                        .requestMatchers(DELETE,
                                String.format(ENDPOINT_ORDER_DETAIL, apiPrefix)).hasRole(Role.ADMIN)
                        .anyRequest().authenticated())

                .csrf(AbstractHttpConfigurer::disable);

        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });

        return http.build();
    }
}
