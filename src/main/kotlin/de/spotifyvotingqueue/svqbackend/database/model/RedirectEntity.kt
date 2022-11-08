package de.spotifyvotingqueue.svqbackend.database.model

import java.util.*
import javax.persistence.*

@Table(name = "redirect")
@Entity
class RedirectEntity(
    @Column val uri: String,
    @Column val session: String
) {
    @GeneratedValue
    @Id
    var accessId: UUID? = null;
}