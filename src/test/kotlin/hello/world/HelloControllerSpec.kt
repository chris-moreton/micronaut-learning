package hello.world

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class HelloControllerSpec {

    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testHelloWorldResponse() {
        val rsp: String = client.toBlocking()
            .retrieve("/hello")
        assertEquals("Starting V7Starting V8", rsp)
    }
}
