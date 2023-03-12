package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.dtos.StateDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class SocketController {

    @Autowired
    private lateinit var messagebroker: SimpMessagingTemplate;

    @SendTo("/sockets/state")
    public fun sendState(state: StateDto): StateDto {
        messagebroker.convertAndSend("/sockets/state", state);
        return state;
    }
}