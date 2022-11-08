package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.dtos.Href
import de.spotifyvotingqueue.svqbackend.dtos.TextDto
import de.spotifyvotingqueue.svqbackend.dtos.UserDto
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import net.minidev.json.JSONArray
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/api/v1")
class TestController {

    @Operation(summary = "Ping the server")
    @ApiResponse(responseCode = "200", description = "Server is available - the server responds with \"pong\".")
    @GetMapping("/ping")
    @ResponseBody
    fun ping(): TextDto {
        return TextDto("pong")
    }

    @Operation(summary = "Get information about the current user")
    @GetMapping("/user/me")
    @ResponseBody
    fun me(): UserDto {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipalName: String = authentication.name
        println("$currentPrincipalName is there!")
        return UserDto(currentPrincipalName, Href("COMING SOON"))
    }

    @RequestMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User): Map<String?, Any?>? {
        return Collections.singletonMap("name", principal.getAttribute("display_name"))
    }

    @Operation(summary = "Steal the current user's secrets")
    @GetMapping("/user/secrets")
    @ResponseBody
    fun secrets(@RegisteredOAuth2AuthorizedClient("spotify") authorizedClient: OAuth2AuthorizedClient): String {
        return "hehe, your access token is ${authorizedClient.accessToken.tokenValue} and your refresh token is ${authorizedClient.refreshToken?.tokenValue}"
    }
}
