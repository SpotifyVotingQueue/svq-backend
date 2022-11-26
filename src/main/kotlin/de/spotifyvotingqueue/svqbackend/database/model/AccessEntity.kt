package de.spotifyvotingqueue.svqbackend.database.model;

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "access")
public class AccessEntity(
    @Column val accesstoken: String,
    @Column val refreshtoken: String,
    @Column val expiresin: Int,
    @Column val created: LocalDateTime
) {
    @GeneratedValue
    @Id
    var accessId: UUID? = null;
}
