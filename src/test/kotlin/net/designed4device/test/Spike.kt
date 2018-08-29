package net.designed4device.test

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.awaitAll
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import kotlin.system.measureTimeMillis

class Spike {

    private val mapper = jacksonObjectMapper()
    private val range = (0..1000000)
    private val smallJsonList = range.map { mapper.writeValueAsString(randomSmall()) }
    private val largeJsonList = range.map { mapper.writeValueAsString(randomLarge()) }
    private val full = range.map { mapper.writeValueAsString(randomFull()) }

    @Test
    fun `small from small`() {
        val deferred = listOf(
                printTimeMillis("small from small", smallJsonList) {
                    mapper.readValue<Small>(it)
                },
                printTimeMillis("large from large", largeJsonList) {
                    mapper.readValue<Large>(it)
                },

                printTimeMillis("small from full", full) {
                    mapper.readValue<Large>(it)
                },
                printTimeMillis("large from full", full) {
                    mapper.readValue<Large>(it)
                }
        )
        runBlocking {
            deferred.awaitAll()
        }
    }

    private fun printTimeMillis(prefix: String, dataSet: List<String>, f: (String) -> Unit) =
            async {
                println("$prefix: " + measureTimeMillis {
                    dataSet.forEach {
                        f(it)
                    }
                })
            }

    private fun randomFull() = Full(
            small = randomSmall(),
            large = randomLarge(),
            ignored1 = randomString(),
            ignored2 = randomString(),
            ignored3 = randomString(),
            ignored4 = randomString()
    )

    private fun randomSmall() = Small(
            field1 = randomString(),
            field2 = randomString()
    )

    private fun randomLarge() = Large(
            field3 = randomString(),
            field4 = randomString(),
            field5 = randomString(),
            field6 = randomString(),
            field7 = randomString(),
            field8 = randomString(),
            field9 = randomString(),
            field10 = randomString()
    )

    private fun randomString() = RandomStringUtils.randomAlphanumeric(10)
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Small(
        val field1: String,
        val field2: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Large(
        val field3: String,
        val field4: String,
        val field5: String,
        val field6: String,
        val field7: String,
        val field8: String,
        val field9: String,
        val field10: String
)

data class Full(
        @JsonUnwrapped val small: Small,
        @JsonUnwrapped val large: Large,
        val ignored1: String,
        val ignored2: String,
        val ignored3: String,
        val ignored4: String
)