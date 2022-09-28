package de.spotifyvotingqueue.svqbackend.dto

import se.michaelthelin.spotify.model_objects.specification.Track
import javax.persistence.*

@Entity
class QueueTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null;

    @Column(name = "track_id", nullable = false)
    val trackId: String;

    @Column(name = "upvotes", nullable = false)
    var upvotes: Int;

    @Column(name = "downvotes", nullable = false)
    var downvotes: Int;

    constructor(trackId: String) {
        this.trackId = trackId;
        this.upvotes = 1;
        this.downvotes = 0;
    }


    fun getScore(): Int {
        return upvotes?.minus(downvotes!!) ?: 0;
    }

    fun downvote() {
        this.downvotes = this.downvotes!! + 1;
    }

    fun upvote() {
        this.upvotes = this.upvotes!! + 1;
    }
}