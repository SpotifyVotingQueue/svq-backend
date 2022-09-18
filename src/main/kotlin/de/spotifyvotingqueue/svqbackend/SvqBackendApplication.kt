package de.spotifyvotingqueue.svqbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SvqBackendApplication

fun main(args: Array<String>) {
	runApplication<SvqBackendApplication>(*args)
}
