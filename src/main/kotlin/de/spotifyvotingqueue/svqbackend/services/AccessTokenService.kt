package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccessTokenService {

    @Autowired
    private lateinit var accessRepository: AccessJpaRepository;

    public fun getNewestAccessEntity(): AccessEntity {
        val newestToken = accessRepository.findFirstByOrderByCreatedeDesc();
        newestToken?.let {
                safeToken -> return if(isTokenExpired(safeToken)) refreshToken() else safeToken;
        }?:run {
            return refreshToken();
        }
    }

    private fun isTokenExpired(newestToken: AccessEntity) =
        newestToken.created.isBefore(LocalDateTime.now().minusMinutes((newestToken.expires_in / 60).toLong()))

    private fun refreshToken(): AccessEntity {
        //TODO call redirect to refresh access token
        return AccessEntity("","",0, LocalDateTime.now());
    }
}