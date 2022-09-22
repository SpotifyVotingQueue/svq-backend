package de.spotifyvotingqueue.svqbackend.dto

import se.michaelthelin.spotify.model_objects.specification.Track
import javax.persistence.*

@Entity
class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "party", nullable = false)
    open var party: Long? = null

    @OneToMany(
        cascade = arrayOf(CascadeType.ALL),
        orphanRemoval = true
    )
    private var queueTracks: MutableList<QueueTrack> = mutableListOf();

    fun addTrack(trackId: String) {
        queueTracks.add(QueueTrack(trackId));
    }

    fun removeTrack(track: QueueTrack) {
        queueTracks.remove(track);
    }

    fun getQueue(): List<QueueTrack> {
        return queueTracks;
    }
}