package hello.world

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import javax.inject.Inject

@MicronautTest
class JwtAuthenticationTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        val e = Executable {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/hello").accept(MediaType.TEXT_PLAIN))
        }
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(thrown.status, HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() {
        val creds = UsernamePasswordCredentials("watson", "mypass")
        val request: HttpRequest<Any> = HttpRequest.POST("/login", creds)
        val rsp: HttpResponse<BearerAccessRefreshToken> =
            client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
        assertEquals(HttpStatus.OK, rsp.status)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = rsp.body()!!
        assertEquals("watson", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)

        val accessToken: String = bearerAccessRefreshToken.accessToken
        val requestWithAuthorization = HttpRequest.GET<Any>("/hello/name")
            .accept(MediaType.TEXT_PLAIN)
            .bearerAuth(accessToken)
        val response: HttpResponse<String> = client.toBlocking().exchange(requestWithAuthorization, String::class.java)
        assertEquals(HttpStatus.OK, rsp.status)
        assertEquals("watson", response.body()!!)
    }
}