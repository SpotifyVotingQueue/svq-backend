package de.spotifyvotingqueue.svqbackend

import de.spotifyvotingqueue.svqbackend.services.SearchService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SvqBackendApplicationTests {

	@Autowired
	private var searchService: SearchService? = null;
	@Test
	fun contextLoads() {
	}

	@Test
	fun searchSong() {
		val song = searchService?.searchForSong("abcdefu");
		assert(song?.isNotEmpty() ?: false);
	}
}
