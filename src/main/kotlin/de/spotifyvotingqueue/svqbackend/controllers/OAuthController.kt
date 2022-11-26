package de.spotifyvotingqueue.svqbackend.controllers

import antlr.Token
import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import de.spotifyvotingqueue.svqbackend.database.RedirectJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import de.spotifyvotingqueue.svqbackend.database.model.RedirectEntity
import de.spotifyvotingqueue.svqbackend.dtos.TokenResponseDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/v1")
class OAuthController {
    @Value("\${spring.security.oauth2.client.registration.spotify.client-id}")
    private val clientId: String? = null;

    @Value("\${spring.security.oauth2.client.registration.spotify.client-secret}")
    private val clientSecret: String? = null;

    @Value("\${spring.security.oauth2.client.provider.spotify-provider.authorization-uri}")
    private val authorizationUri: String? = null;

    @Value("\${oauthcontroller.params.backend-url}")
    private val backendUri: String? = null;

    @Value("\${oauthcontroller.params.frontend-dev-url}")
    private lateinit var frontendDevUri: String;

    @Value("\${oauthcontroller.params.frontend-prod-url}")
    private lateinit var  frontendProdUri: String;

    @Value("\${oauthcontroller.isDev}")
    private val isDev: String? = null;

    @Autowired
    private val accessrepository: AccessJpaRepository? = null;

    @Autowired
    private lateinit var redirectrepository: RedirectJpaRepository;

    @Operation(summary = "Login")
    @GetMapping("/login")
    fun login(@RequestParam("redirect") redirectUri: String, @RequestParam("session") session: String): ResponseEntity<Any> {
        RedirectEntity(redirectUri, session).let { redirectrepository.save(it) }

        var redirectUriSpotify: String = "${backendUri}/api/v1/loggedIn/redirect";

        var uri: URI = URI.create("${authorizationUri}?response_type=code&client_id=${clientId}&redirect_uri=${redirectUriSpotify}&state=${session}");
        var headers: HttpHeaders = HttpHeaders();
        headers.location = uri;

        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER);
    }

    @Operation(summary = "Redirect")
    @GetMapping("/loggedIn/redirect")
    @ResponseBody
    fun redirect(@RequestParam("code") code: String, @RequestParam("state") session: String): ResponseEntity<Any> {
        var body: MultiValueMap<String, String>  = LinkedMultiValueMap();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", "${backendUri}/api/v1/loggedIn/redirect");

        var tokenheaders: MultiValueMap<String, String>  = LinkedMultiValueMap();
        tokenheaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray()));
        tokenheaders.add("Content-Type", "application/x-www-form-urlencoded");

        var resttemplate: RestTemplate = RestTemplate();
        var response: ResponseEntity<TokenResponseDto> = resttemplate.exchange(
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
            response.body!!.refresh_token!!,
            response.body!!.expires_in,
            LocalDateTime.now()
        ).let { accessrepository!!.save(it) }

        var redirectentity: RedirectEntity? = redirectrepository.findBySession(session);
        var frontendUri: String;
        if (isDev == "true") {
            frontendUri = frontendDevUri;
        } else {
            frontendUri = frontendProdUri;
        }
        var uri: URI = URI.create("${frontendUri}/login?token=${response.body?.access_token}&redirect=${redirectentity?.uri}");
        var headers: HttpHeaders = HttpHeaders();
        headers.location = uri;
        return ResponseEntity<Any>(headers, org.springframework.http.HttpStatus.SEE_OTHER)
    }

    fun refreshToken(refreshToken: String): AccessEntity {
        var body: MultiValueMap<String, String>  = LinkedMultiValueMap();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        var tokenheaders: MultiValueMap<String, String>  = LinkedMultiValueMap();
        tokenheaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(("$clientId:$clientSecret").toByteArray()));
        tokenheaders.add("Content-Type", "application/x-www-form-urlencoded");

        var resttemplate: RestTemplate = RestTemplate();
        var response: ResponseEntity<TokenResponseDto> = resttemplate.exchange(
            URI("https://accounts.spotify.com/api/token"),
            HttpMethod.POST,
            HttpEntity(
                body,
                tokenheaders
            ),
            TokenResponseDto::class.java
        )
        return AccessEntity(
            response.body!!.access_token,
            refreshToken,
            response.body!!.expires_in,
            LocalDateTime.now()
        )
    }
}