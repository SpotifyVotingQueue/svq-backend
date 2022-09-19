package de.spotifyvotingqueue.svqbackend.models

class SpotifyEntry {

    val href: String? = null;

    val items: List<SpotifyItem>? = null;

    val limit: Int? = null;

    val next: String? = null;

    val offset: Int? = null;

    val previous: String? = null;

    val total: Int? = null;



    override fun toString(): String {
        return "SpotifyEntry(href=$href, items=$items, limit=$limit, next=$next, offset=$offset, previous=$previous, total=$total)"
    }

}