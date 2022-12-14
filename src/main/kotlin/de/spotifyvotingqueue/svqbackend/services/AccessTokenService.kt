package de.spotifyvotingqueue.svqbackend.services

import de.spotifyvotingqueue.svqbackend.controllers.OAuthController
import de.spotifyvotingqueue.svqbackend.database.AccessJpaRepository
import de.spotifyvotingqueue.svqbackend.database.PartyJpaRepository
import de.spotifyvotingqueue.svqbackend.database.model.AccessEntity
import de.spotifyvotingqueue.svqbackend.database.model.PartyEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccessTokenService {

    @Autowired
    private lateinit var accessRepository: AccessJpaRepository

    @Autowired
    private lateinit var partyJpaRepository: PartyJpaRepository;

    @Autowired
    private lateinit var oAuthController: OAuthController;

    fun getNewestAccessEntity(): AccessEntity {
        //TODO this method should work with different users, so the server can get access tokens for multiple parties
        val newestToken = accessRepository.findFirstByOrderByCreatedDesc()
        newestToken?.let {
                safeToken -> return if(isTokenExpired(safeToken)) refreshToken(safeToken.refreshtoken) else safeToken;
        }?:run {
            //TODO how should we deal with requests before the first token was created?
            return refreshToken("")
        }
    }

    private fun isTokenExpired(newestToken: AccessEntity) =
        newestToken.created.isBefore(LocalDateTime.now().minusMinutes((newestToken.expiresin / 60).toLong()))

    private fun refreshToken(code: String): AccessEntity {
        if(oAuthController.redirect(code, "").statusCode.is2xxSuccessful) {
            return accessRepository.findFirstByOrderByCreatedDesc()!!;
        }
        throw Exception("Could not refresh token")
    }

    private fun refreshToken(refreshToken: String, partyId: String): AccessEntity {
        val newAccess: AccessEntity = oAuthController.refreshToken(refreshToken);
        partyJpaRepository.deleteByCode(partyId);
        partyJpaRepository.save(PartyEntity(partyId, newAccess.accesstoken!!));
        return newAccess;
    }

    fun getMatchingToken(partyId: String): AccessEntity {
        val token: String = partyJpaRepository.findByCode(partyId)?.hostAccessToken ?: throw Exception("No token found for party");
        val accessEntity: AccessEntity = accessRepository.findByAccesstoken(token) ?: throw Exception("No access found for token");
        accessEntity?.let {
            safeToken -> return if(isTokenExpired(safeToken)) refreshToken(safeToken.refreshtoken, partyId) else safeToken;
        }?:run {
            throw Exception("Cant refresh token");
        }
    }
}