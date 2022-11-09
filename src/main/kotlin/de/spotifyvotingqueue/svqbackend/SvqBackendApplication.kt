package de.spotifyvotingqueue.svqbackend

import org.h2.tools.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SvqBackendApplication

fun main(args: Array<String>) {
	runApplication<SvqBackendApplication>(*args)
}