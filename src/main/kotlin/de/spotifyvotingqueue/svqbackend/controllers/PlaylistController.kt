package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.dtos.Href
import de.spotifyvotingqueue.svqbackend.dtos.PlaylistDto
import de.spotifyvotingqueue.svqbackend.dtos.TrackDto
import de.spotifyvotingqueue.svqbackend.services.PlaylistService
import io.swagger.v3.oas.annotations.Operation
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PlaylistController {

    @Autowired
    private lateinit var playlistService: PlaylistService;

    @Operation(summary = "Get first four playlists of the host")
    @GetMapping("/playlists")
    @ResponseBody
    fun getPlaylists(@RequestParam @NotNull partyId: String): List<PlaylistDto>   {
        return playlistService.getFourPlaylists(partyId).map { PlaylistDto(it.name, it.owner.displayName, it.tracks.total, Href(it.images[0].url)) }
    }
}