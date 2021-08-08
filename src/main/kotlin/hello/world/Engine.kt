package hello.world

import javax.inject.Singleton

interface Engine {

    val cylinders: Int

    fun start(): String
}

@Singleton
class V8Engine : Engine {

    override var cylinders = 8

    override fun start(): String {
        return "Starting V8"
    }
}

@Singleton
class V7Engine : Engine {

    override var cylinders = 7

    override fun start(): String {
        return "Starting V7"
    }
}

@Singleton
class Vehicle(private val engine: Engine) {
    fun start(): String {
        return engine.start()
    }
}
