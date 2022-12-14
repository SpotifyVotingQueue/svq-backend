package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import de.spotifyvotingqueue.svqbackend.dtos.TokenResponseDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/v1")
class OAuthController {
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null

    @Autowired
    private val accessrepository: AccessJpaRepository? = null

    @Operation(summary = "Login")
    @GetMapping("/login")
    fun login(@RequestParam("redirect") redirectUri: String): ResponseEntity<Void> {
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Redirect")
    @GetMapping("/loggedIn/redirect")
    @ResponseBody
    fun redirect(@RequestParam("code") code: String): ResponseEntity<Any> {
        val body: MultiValueMap<String, String>  = LinkedMultiValueMap()
        body.add("grant_type", "authorization_code")
        body.add("code", code)
        body.add("redirect_uri", "http://localhost:8080/api/v1/loggedIn/redirect")

        val tokenheaders: MultiValueMap<String, String>  = LinkedMultiValueMap()
        tokenheaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray()))
        tokenheaders.add("Content-Type", "application/x-www-form-urlencoded")

        val resttemplate = RestTemplate()
        val response: ResponseEntity<TokenResponseDto> = resttemplate.exchange(
            URI("https://accounts.spotify.com/api/token"),
            HttpMethod.POST,
            HttpEntity(
                body,
                tokenheaders
            ),
            TokenResponseDto::class.java
        )
        AccessEntity(
            response.body!!.access_token,
            response.body!!.refresh_token,
            response.body!!.expires_in,
            LocalDateTime.now()
        ).let { accessrepository!!.save(it) }

        val uri: URI = URI.create("http://localhost:3000/login?token=${response.body?.access_token}&refresh_token=${response.body?.refresh_token}")
        val headers: HttpHeaders = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, org.springframework.http.HttpStatus.SEE_OTHER)
    }
}