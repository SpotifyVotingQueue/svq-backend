package de.spotifyvotingqueue.svqbackend.database.model

import java.util.*
import javax.persistence.*

@Entity
class QueueTrack(
    @Column(nullable = false) val trackId: String,
    @ManyToOne @JoinColumn(name = "party_entity_party_id")
    var partyEntity: PartyEntity? = null,
) {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    val id: UUID? = null

    @Column(nullable = false)
    var upvotes: Int = 1

    @Column(nullable = false)
    var downvotes: Int = 0

    fun getScore(): Int {
        return upvotes - downvotes
    }

    fun downvote() {
        this.downvotes++
    }

    fun upvote() {
        this.upvotes++
    }
}