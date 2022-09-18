package de.spotifyvotingqueue.svqbackend

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SvqBackendApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun failTest() {
		fail("This should make the CI pipeline red");
	}

}
