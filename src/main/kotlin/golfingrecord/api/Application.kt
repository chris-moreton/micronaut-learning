package golfingrecord.api

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("golfingrecord.api")
		.start()
}

