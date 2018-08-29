package net.designed4device.test

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import kotlin.system.measureTimeMillis

class Spike {

    private val small = Small(
            field1 = "alfj32",
            field2 = "fj32j9olskd"
    )

    private val large = Large(
            field3 = "asdfa434as",
            field4 = "jgri349iasa",
            field5 = ";akvjpewed",
            field6 = "asdnivjpisd3"
    )

    private val common = Common(
            small = small,
            large = large
    )

    private val commonJson = jacksonObjectMapper().writeValueAsString(common)

    @Test
    fun small() {
        printTimeMillis("small") {
            val test = jacksonObjectMapper().readValue<Small>(commonJson)
        }
    }

    @Test
    fun large() {
        printTimeMillis("large") {
            val test = jacksonObjectMapper().readValue<Large>(commonJson)
        }
    }

    private fun printTimeMillis(prefix: String, f: () -> Unit) {
        println("$prefix: " + measureTimeMillis {
            (0..10000).forEach {
                f()
            }
        })
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Small (val field1: String, val field2: String)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Large (val field3: String, val field4: String, val field5: String, val field6: String)
data class Common (@JsonUnwrapped val small: Small, @JsonUnwrapped val large: Large)