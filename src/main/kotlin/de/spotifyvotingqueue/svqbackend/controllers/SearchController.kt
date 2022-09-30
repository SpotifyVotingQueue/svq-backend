package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.dtos.TrackDto
import de.spotifyvotingqueue.svqbackend.services.SearchService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SearchController {

    @Autowired
    private lateinit var searchService: SearchService;

    @Operation(summary = "Get information for a track")
    @GetMapping("/track/{id}")
    @ResponseBody
    fun getTrack(@PathVariable id: String): TrackDto {
        return searchService
            .getSong(id)
            .let { TrackDto(it.id, it.name, it.artists.map { artist -> artist.name }) }
    }

    @Operation(summary = "Search for tracks by name")
    @GetMapping("/search/{query}")
    @ResponseBody
    fun searchTracks(@PathVariable query: String): List<TrackDto> {
        return searchService
            .searchForSong(query)
            .map { TrackDto(it.id, it.name, it.artists.map { artist -> artist.name }) }
    }
}