package de.spotifyvotingqueue.svqbackend

import de.spotifyvotingqueue.svqbackend.services.SearchService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class SvqBackendApplicationTests {

	@Autowired
	private var searchService: SearchService? = null

	@Test
	fun contextLoads() {
	}

	@Test
	fun testSearchForSong() {
		val songName = "The Scientist"
		val searchResult = searchService?.searchForSong(songName, UUID.fromString("0").toString())
		if (searchResult == null) {
			fail("Search result is null")
		}
		if (searchResult.isEmpty()) {
			fail("Search result is empty")
		}
	}

}
