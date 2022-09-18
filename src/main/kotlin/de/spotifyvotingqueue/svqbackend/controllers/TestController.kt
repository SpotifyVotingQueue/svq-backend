package de.spotifyvotingqueue.svqbackend.controllers

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1")
class TestController {

    @GetMapping("/ping")
    @ResponseBody
    fun ping() = "pong"

    @GetMapping("/user/me")
    @ResponseBody
    fun me(): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipalName: String = authentication.name
        println("$currentPrincipalName is there!")
        return "Hello, $currentPrincipalName"
    }

}