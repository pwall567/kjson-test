/*
 * @(#) ZonedDateTimeTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022, 2023, 2024, 2025 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.kjson.test

import kotlin.test.Test

import java.time.ZoneId
import java.time.ZonedDateTime

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class ZonedDateTimeTest {

    @Test fun `should read nodeAsZonedDateTime`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        JSONExpect.expectJSON(json) {
            nodeAsZonedDateTime shouldBe ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney)
        }
    }

    @Test fun `should fail on invalid nodeAsZonedDateTime`() {
        val json = "\"not a ZonedDateTime\""
        shouldThrow<AssertionError>("JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") {
            JSONExpect.expectJSON(json) {
                nodeAsZonedDateTime
            }
        }
    }

    @Test fun `should test ZonedDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        JSONExpect.expectJSON(json) {
            value(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        shouldThrow<AssertionError>("JSON value doesn't match -" +
                " expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                value(ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        JSONExpect.expectJSON(json) {
            property("abc", ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match -" +
                " expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                property("abc", ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00[Australia/Sydney]"]"""
        JSONExpect.expectJSON(json) {
            item(0, ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00[Australia/Sydney]"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match -" +
                " expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                item(0, ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime value in range`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        JSONExpect.expectJSON(json) {
            value(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney)..
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime value in range`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        shouldThrow<AssertionError>("JSON value not in range \"2022-06-15T18:05:10+10:00[Australia/Sydney]\".." +
                "\"2022-06-15T18:05:10+10:00[Australia/Sydney]\" - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                value(ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime property in range`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        JSONExpect.expectJSON(json) {
            property("abc", ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney)..
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime property in range`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"2022-06-15T18:05:10+10:00[Australia/Sydney]\".." +
                "\"2022-06-15T18:05:11+10:00[Australia/Sydney]\" - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                property("abc", ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 18, 5, 11, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime array item in range`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"]"
        JSONExpect.expectJSON(json) {
            item(0, ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney)..
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime array item in range`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"2022-06-15T17:05:10+10:00[Australia/Sydney]\".." +
                "\"2022-06-15T17:05:11+10:00[Australia/Sydney]\" - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                item(0, ZonedDateTime.of(2022, 6, 15, 17, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 11, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should test ZonedDateTime value in collection`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney),
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime value in collection`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        shouldThrow<AssertionError>("JSON value not in collection - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
            }
        }
    }

    @Test fun `should test ZonedDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney),
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 10, 0, zoneIdSydney)))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
            }
        }
    }

    @Test fun `should test ZonedDateTime array item in collection`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney),
                    ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime array item in collection`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - " +
                "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 10, 0, zoneIdSydney)))
            }
        }
    }

    @Test fun `should test that any item has ZonedDateTime value`() {
        val json = "[\"2023-09-18T19:30:15+10:00[Australia/Sydney]\",\"2023-09-18T19:30:30+10:00[Australia/Sydney]\"," +
                "\"2023-09-18T19:30:45+10:00[Australia/Sydney]\"]"
        JSONExpect.expectJSON(json) {
            anyItem(ZonedDateTime.of(2023, 9, 18, 19, 30, 45, 0, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect test that any item has ZonedDateTime value`() {
        val json = "[\"2023-09-18T19:30:15+10:00[Australia/Sydney]\",\"2023-09-18T19:30:30+10:00[Australia/Sydney]\"," +
                "\"2023-09-18T19:30:45+10:00[Australia/Sydney]\"]"
        shouldThrow<AssertionError>("No JSON array item has value \"2023-09-18T19:30:55+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                anyItem(ZonedDateTime.of(2023, 9, 18, 19, 30, 55, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should test multiple ZonedDateTime values`() {
        val json = "[\"2025-05-10T20:30:15+10:00[Australia/Sydney]\",\"2025-05-10T20:30:30+10:00[Australia/Sydney]\"," +
                "\"2025-05-10T20:30:45+10:00[Australia/Sydney]\"]"
        JSONExpect.expectJSON(json) {
            items(ZonedDateTime.of(2025, 5, 10, 20, 30, 15, 0, zoneIdSydney),
                    ZonedDateTime.of(2025, 5, 10, 20, 30, 30, 0, zoneIdSydney),
                    ZonedDateTime.of(2025, 5, 10, 20, 30, 45, 0, zoneIdSydney))
        }
    }

    @Test fun `should fail on incorrect multiple ZonedDateTime values`() {
        val json = "[\"2025-05-10T20:30:15+10:00[Australia/Sydney]\",\"2025-05-10T20:30:30+10:00[Australia/Sydney]\"," +
                "\"2025-05-10T20:30:45+10:00[Australia/Sydney]\"]"
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected " +
                "\"2025-05-10T20:30:35+10:00[Australia/Sydney]\", " +
                "was \"2025-05-10T20:30:30+10:00[Australia/Sydney]\"") {
            JSONExpect.expectJSON(json) {
                items(ZonedDateTime.of(2025, 5, 10, 20, 30, 15, 0, zoneIdSydney),
                        ZonedDateTime.of(2025, 5, 10, 20, 30, 35, 0, zoneIdSydney),
                        ZonedDateTime.of(2025, 5, 10, 20, 30, 45, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should fail on incorrect number of multiple ZonedDateTime values`() {
        val json = "[\"2025-05-10T20:30:15+10:00[Australia/Sydney]\",\"2025-05-10T20:30:30+10:00[Australia/Sydney]\"," +
                "\"2025-05-10T20:30:45+10:00[Australia/Sydney]\"]"
        shouldThrow<AssertionError>("JSON array size not the same as number of values - expected 2, was 3") {
            JSONExpect.expectJSON(json) {
                items(ZonedDateTime.of(2025, 5, 10, 20, 30, 15, 0, zoneIdSydney),
                        ZonedDateTime.of(2025, 5, 10, 20, 30, 30, 0, zoneIdSydney))
            }
        }
    }

    @Test fun `should fail on multiple ZonedDateTime values applied to an object`() {
        val json = "{}"
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was object") {
            JSONExpect.expectJSON(json) {
                items(ZonedDateTime.of(2025, 5, 10, 20, 30, 15, 0, zoneIdSydney),
                        ZonedDateTime.of(2025, 5, 10, 20, 30, 30, 0, zoneIdSydney),
                        ZonedDateTime.of(2025, 5, 10, 20, 30, 45, 0, zoneIdSydney))
            }
        }
    }

    companion object {

        val zoneIdSydney: ZoneId = ZoneId.of("Australia/Sydney")

    }

}
