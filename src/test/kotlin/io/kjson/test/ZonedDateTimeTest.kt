/*
 * @(#) ZonedDateTimeTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022 Peter Wall
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
import kotlin.test.assertFailsWith
import kotlin.test.expect
import kotlin.test.fail

import java.time.ZoneId
import java.time.ZonedDateTime

import net.pwall.util.MiniSet

class ZonedDateTimeTest {

    @Test fun `should read nodeAsZonedDateTime`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        JSONExpect.expectJSON(json) {
            if (nodeAsZonedDateTime != ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, zoneIdSydney))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsZonedDateTime`() {
        val json = "\"not a ZonedDateTime\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsZonedDateTime
            }
        }.let {
            expect("JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }.let {
            expect("JSON value doesn't match - expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                    " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                    " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, zoneIdSydney))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\", was " +
                    "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney))
            }
        }.let {
            expect("JSON value not in range \"2022-06-15T18:05:10+10:00[Australia/Sydney]\".." +
                    "\"2022-06-15T18:05:10+10:00[Australia/Sydney]\" - " +
                    "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", ZonedDateTime.of(2022, 6, 15, 18, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 18, 5, 11, 0, zoneIdSydney))
            }
        }.let {
            expect("/abc: JSON value not in range \"2022-06-15T18:05:10+10:00[Australia/Sydney]\".." +
                    "\"2022-06-15T18:05:11+10:00[Australia/Sydney]\" - " +
                    "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, ZonedDateTime.of(2022, 6, 15, 17, 5, 10, 0, zoneIdSydney)..
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 11, 0, zoneIdSydney))
            }
        }.let {
            expect("/0: JSON value not in range \"2022-06-15T17:05:10+10:00[Australia/Sydney]\".." +
                    "\"2022-06-15T17:05:11+10:00[Australia/Sydney]\" - " +
                    "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
            }
        }.let {
            expect("JSON value not in collection - \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, zoneIdSydney)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
                it.message
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 0, zoneIdSydney),
                        ZonedDateTime.of(2022, 6, 15, 17, 5, 10, 0, zoneIdSydney)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") {
                it.message
            }
        }
    }

    companion object {

        val zoneIdSydney: ZoneId = ZoneId.of("Australia/Sydney")

    }

}
