package de.spotifyvotingqueue.svqbackend.models.enums

import java.util.*

enum class ObjectType {

    TRACK, ARTIST, ALBUM, PLAYLIST, SHOW, EPISODE;

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}