package de.spotifyvotingqueue.svqbackend.models

import de.spotifyvotingqueue.svqbackend.models.enums.ObjectType

class SpotifySearchResult {

    val type : ObjectType? = null;

    val entrys: List<SpotifyEntry> = ArrayList();
}