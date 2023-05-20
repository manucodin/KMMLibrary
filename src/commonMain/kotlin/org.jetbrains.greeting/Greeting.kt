package org.jetbrains.greeting

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}