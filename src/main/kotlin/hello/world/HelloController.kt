package hello.world

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import java.security.Principal

@Controller("/hello")
class HelloController(private val e: List<Engine>) {

    @Get("/", produces = [MediaType.TEXT_PLAIN])
    fun index(): String {
        var s = ""
        e.forEach {
            val v = Vehicle(it)
            s += v.start()
        }
        return s
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Secured("isAuthenticated()")
    @Get("/name")
    fun index(principal: Principal): String {
        return principal.name
    }
}
