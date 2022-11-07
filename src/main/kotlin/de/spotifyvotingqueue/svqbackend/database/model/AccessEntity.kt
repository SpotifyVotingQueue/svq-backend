package de.spotifyvotingqueue.svqbackend.database.model;

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "access")
public class AccessEntity(
    @Column val access_token: String,
    @Column val refresh_token: String,
    @Column val expires_in: Int,
    @Column val created: LocalDateTime
) {
    @GeneratedValue
    @Id
    var accessId: UUID? = null;
}
