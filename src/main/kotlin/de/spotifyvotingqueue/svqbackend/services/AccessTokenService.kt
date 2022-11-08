package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.controllers.OAuthController
import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccessTokenService {

    @Autowired
    private lateinit var accessRepository: AccessJpaRepository;

    @Autowired
    private lateinit var oAuthController: OAuthController;

    public fun getNewestAccessEntity(): AccessEntity {
        val newestToken = accessRepository.findFirstByOrderByCreatedDesc();
        newestToken?.let {
                safeToken -> return if(isTokenExpired(safeToken)) refreshToken(safeToken.refresh_token) else safeToken;
        }?:run {
            //TODO how should we deal with requests before the first token was created?
            return refreshToken("");
        }
    }

    private fun isTokenExpired(newestToken: AccessEntity) =
        newestToken.created.isBefore(LocalDateTime.now().minusMinutes((newestToken.expires_in / 60).toLong()))

    private fun refreshToken(code: String): AccessEntity {
        if(oAuthController.redirect(code, "").statusCode.is2xxSuccessful) {
            return accessRepository.findFirstByOrderByCreatedDesc()!!;
        }
        throw Exception("Could not refresh token");
    }
}