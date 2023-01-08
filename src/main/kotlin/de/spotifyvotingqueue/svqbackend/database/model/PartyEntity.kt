package de.spotifyvotingqueue.svqbackend.database.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "party")
class PartyEntity(
    @Column @Id val code: String,
    @Column val hostAccessToken: String,
) {
    @OneToMany(mappedBy = "partyEntity", fetch = FetchType.EAGER) var queueTracks = mutableListOf<QueueTrack>();

    @OneToOne
    @JoinColumn(name = "access_entity_access_token")
    lateinit var accessEntity: AccessEntity

    fun addTrack(track: QueueTrack): Boolean {
        if (queueTracks.any { it.trackId == track.trackId }) {
            return false;
        }
        queueTracks.add(track);
        return true;
    }

    fun removeTrack(track: QueueTrack) {
        queueTracks.remove(track)
    }

    fun getTrack(trackId: String): QueueTrack? {
        return queueTracks.find { it.trackId == trackId }
    }
}
