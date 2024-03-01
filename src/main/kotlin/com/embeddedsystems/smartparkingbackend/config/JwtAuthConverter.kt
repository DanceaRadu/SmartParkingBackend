package com.embeddedsystems.smartparkingbackend.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class JwtAuthConverter: Converter<Jwt, AbstractAuthenticationToken> {

    private fun convertRolesToGrantedAuthorities(roles: List<String>): List<SimpleGrantedAuthority> =
        roles.stream()
            .map { role: String ->
                SimpleGrantedAuthority(
                    role.uppercase(Locale.getDefault())
                )
            }
            .collect(Collectors.toList())
    private fun getBasicAuthToken(source: Jwt): AbstractAuthenticationToken =
        JwtAuthenticationToken(
            source,
            Collections.singletonList(SimpleGrantedAuthority("USER")),
            source.subject
        )
    override fun convert(source: Jwt): AbstractAuthenticationToken? {
        val resourceAccess = source.getClaim<Map<String, Any>>("resource_access")

        if (resourceAccess == null || !resourceAccess.containsKey("betting")) {
            return getBasicAuthToken(source)
        }

        val bettingApp = resourceAccess["betting"] as Map<String, String>
        val roles = bettingApp["roles"] as List<String>?
            ?: return getBasicAuthToken(source)

        val authorities = convertRolesToGrantedAuthorities(roles)
        return JwtAuthenticationToken(source, authorities, source.subject)
    }
}
