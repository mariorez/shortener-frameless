package org.seariver.shortener.lib

import java.util.Properties

object PropertiesFactory {

    fun load() {
        val propFile = this.javaClass.classLoader.getResourceAsStream("application.properties")
        val properties = Properties()
        properties.load(propFile)

        properties.stringPropertyNames().forEach { key ->
            val propValue = System.getenv(key.replace('.', '_').uppercase()) ?: properties.getProperty(key)
            System.setProperty(key, propValue)
        }
    }
}
