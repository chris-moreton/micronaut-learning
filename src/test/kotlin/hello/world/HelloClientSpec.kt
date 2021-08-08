package hello.world

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class HelloClientSpec {

    @Inject
    lateinit var client: HelloClient

    @Test
    fun testHelloWorldResponse() {
        assertEquals("Hello World", client.hello().blockingGet())
    }
}