package de.spotifyvotingqueue.svqbackend.controllers

import de.spotifyvotingqueue.svqbackend.dtos.TrackDto
import de.spotifyvotingqueue.svqbackend.services.SearchService
import io.swagger.v3.oas.annotations.Operation
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1")
class TrackController {

    @Autowired
    private lateinit var searchService: SearchService

    @Operation(summary = "Get information for a track")
    @GetMapping("/track/{id}")
    @ResponseBody
    fun getTrack(@PathVariable id: String): TrackDto {
        return searchService
            .getSong(id)
            .let { TrackDto(it.id, it.name, it.artists.map { artist -> artist.name }, it.album.images[0].url) }
    }

    @Operation(summary = "Get cover art for a track")
    @GetMapping("/track/{id}/cover")
    @ResponseBody
    fun getTrackCover(@PathVariable id: String, @RequestBody maxHeight: Int, @RequestBody minHeight: Int): String {
        return searchService
            .getSong(id)
            .album
            .images
            .first { image -> image.height in (minHeight + 1) until maxHeight }
            .url
    }

    @Operation(summary = "Search for tracks by name")
    @GetMapping("/search/{query}")
    @ResponseBody
    fun searchTracks(@PathVariable query: String, @RequestParam @NotNull partyId: String): List<TrackDto> {
        return searchService
            .searchForSong(query, partyId)
            .map { TrackDto(it.id, it.name, it.artists.map { artist -> artist.name }, it.album.images[0].url) }
    }

    @Operation(summary = "Get recommended songs")
    @GetMapping("/recommendations")
    @ResponseBody
    fun getRecommendations(@RequestParam @NotNull partyId: String): List<TrackDto> {
        return searchService.getSongs(
            searchService.getRecommendations(partyId).map { it.id },
            partyId
        ).map { TrackDto(it.id, it.name, it.artists.map { artist -> artist.name }, it.album.images[0].url) }

    }
}