package de.spotifyvotingqueue.svqbackend.adapters

import de.spotifyvotingqueue.svqbackend.models.SpotifyEntry
import de.spotifyvotingqueue.svqbackend.models.SpotifySearchResult
import de.spotifyvotingqueue.svqbackend.models.enums.ObjectType
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration

import java.util.concurrent.TimeUnit


class SpotifyApiClient() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private val webClient: WebClient
    private val spotifyApiUrl: String = "https://api.spotify.com";

    init {
        webClient = createWebClient()
    }



    private fun createHttpClient(): HttpClient {
        return HttpClient.create() //
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MILISECONDS) //
            .responseTimeout(Duration.ofMillis(TIMEOUT_MILISECONDS)) //
            .doOnConnected { conn ->
                conn.addHandlerLast(
                    ReadTimeoutHandler(
                        TIMEOUT_MILISECONDS,
                        TimeUnit.MILLISECONDS
                    )
                ) //
                    .addHandlerLast(
                        WriteTimeoutHandler(
                            TIMEOUT_MILISECONDS,
                            TimeUnit.MILLISECONDS
                        )
                    )
            }
    }

    private fun createWebClient(): WebClient {
        return WebClient.builder().baseUrl(spotifyApiUrl) //
            .clientConnector(ReactorClientHttpConnector(createHttpClient())) //
            .build()
    }

    fun getAccount(iban: String): org.jetbrains.kotlin.com.intellij.util.Function.Mono<AccountResponse> {
        return webClient //
            .get() //
            .uri(getAccountUriBuilder(iban)) //
            .retrieve().bodyToMono(AccountResponse::class.java)
    }

    fun getSearch(searchString: String, searchedObjects: List<ObjectType>): Mono<SpotifySearchResult> {
        var objects = "";
        for (objectType in searchedObjects) {
            objects += "$objectType,";
        }
        objects = objects.substring(0, objects.length - 1);

        return webClient //
            .get() //
            .uri("/v1/search?type=$objects&include_external=audio") //
            .retrieve()
            .onStatus({ status -> status.isError }, { response -> Mono.error(Exception(response.statusCode().toString())) })
            .bodyToMono(SpotifySearchResult::class.java); //
    }

    fun updateAccount(
        iban: String,
        updateAccountRequest: UpdateAccountRequest?
    ): org.jetbrains.kotlin.com.intellij.util.Function.Mono<AccountResponse> {
        return webClient //
            .patch() //
            .uri(updateAccountUriBuilder(iban)) //
            .body(BodyInserters.fromValue(updateAccountRequest)) //
            .retrieve() //
            .bodyToMono(AccountResponse::class.java)
    }

    fun isIbanValid(iban: String, person: String?): org.jetbrains.kotlin.com.intellij.util.Function.Mono<String> {
        return webClient //
            .get() //
            .uri(isIbanValidUriBuilder(iban)) //
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals) { response ->
                response.bodyToMono(String::class.java).map { reason ->
                    IbanNotValidException(
                        iban,
                        person,
                        reason
                    )
                }
            }
            .bodyToMono(String::class.java)
    }

    private fun isIbanValidUriBuilder(iban: String): String {
        return "/iban/$iban"
    }

    private fun getAccountUriBuilder(iban: String): String {
        return "/accounts/$iban"
    }

    private fun updateAccountUriBuilder(iban: String): String {
        return "/accounts/$iban"
    }

    companion object {
        private const val TIMEOUT_MILISECONDS = 5000
    }
}