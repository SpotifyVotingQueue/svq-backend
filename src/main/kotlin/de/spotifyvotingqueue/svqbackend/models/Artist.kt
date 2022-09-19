package de.spotifyvotingqueue.svqbackend.models

class Artist(id: String,
             val genres: List<String>,
             val href: String,
             val images: List<Image>,
             val name: String,
             val popularity: Int,
             val uri: String) : SpotifyItem(id) {
}