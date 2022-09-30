package de.spotifyvotingqueue.svqbackend.database.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "party")
class PartyEntity(
    @Column val code: String,
    @Column val hostAccessToken: String,
    @Column val hostRefreshToken: String,
) {
    @GeneratedValue @Id var partyId: UUID? = null;
    @OneToMany(mappedBy = "partyEntity") var queueTracks: List<QueueTrack> = emptyList()

    fun addTrack(trackId: String) {
        queueTracks += QueueTrack(trackId, this)
    }

    fun removeTrack(trackId: String) {
        queueTracks = queueTracks.filter { it.trackId != trackId }
    }

    fun getTrack(trackId: String): QueueTrack? {
        return queueTracks.find { it.trackId == trackId }
    }
}
