package de.spotifyvotingqueue.svqbackend.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1")
class TestController {

    @Operation(summary = "Ping the server")
    @ApiResponse(responseCode = "200", description = "Server is available - the server responds with \"pong\".")
    @GetMapping("/ping")
    @ResponseBody
    fun ping() = "pong"

    @Operation(summary = "Get information about the current user")
    @GetMapping("/user/me")
    @ResponseBody
    fun me(): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipalName: String = authentication.name
        println("$currentPrincipalName is there!")
        return "Hello, $currentPrincipalName"
    }

}