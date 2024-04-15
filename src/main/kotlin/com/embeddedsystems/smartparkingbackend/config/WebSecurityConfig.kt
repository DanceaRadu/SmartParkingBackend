package com.embeddedsystems.smartparkingbackend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Value("\${cors.origin.url}")
    private lateinit var corsOriginUrl: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors { cors ->
                cors.configurationSource {
                    val configuration = org.springframework.web.cors.CorsConfiguration()
                    configuration.allowedOrigins = listOf(corsOriginUrl)
                    configuration.allowedMethods = listOf("GET", "POST", "DELETE", "PUT", "PATCH")
                    configuration.allowedHeaders = listOf("*")
                    configuration.allowCredentials = true
                    configuration
                }
            }

            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS).permitAll()

                    .requestMatchers("/api/v1/test/insecure").permitAll()
                    .requestMatchers("/api/v1/test/secure").authenticated()

                    .requestMatchers("/api/v1/user-profile/me").authenticated()
                    .requestMatchers("/api/v1/user-profile/**").permitAll()

                    .requestMatchers("/api/v1/parking-config/**").permitAll()
                    .requestMatchers("/config/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt {
                    it.jwtAuthenticationConverter(JwtAuthConverter())
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
}
