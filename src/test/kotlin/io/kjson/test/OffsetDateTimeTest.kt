/*
 * @(#) OffsetDateTimeTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022, 2023, 2024 Peter Wall
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

import java.time.OffsetDateTime
import java.time.ZoneOffset

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class OffsetDateTimeTest {

    @Test fun `should read nodeAsOffsetDateTime`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            nodeAsOffsetDateTime shouldBe OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10)
        }
    }

    @Test fun `should fail on invalid nodeAsOffsetDateTime`() {
        val json = "\"not an OffsetDateTime\""
        shouldThrow<AssertionError>("JSON string is not an OffsetDateTime - \"not an OffsetDateTime\"") {
            JSONExpect.expectJSON(json) {
                nodeAsOffsetDateTime
            }
        }
    }

    @Test fun `should test OffsetDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"2022-06-15T18:05:09.123+11:00\"," +
                " was \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, offset11))
            }
        }
    }

    @Test fun `should test OffsetDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"2022-06-15T18:05:09.123+11:00\"," +
                " was \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, offset11))
            }
        }
    }

    @Test fun `should test OffsetDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00"]"""
        JSONExpect.expectJSON(json) {
            item(0, OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"2022-06-15T18:05:09.123+11:00\"," +
                " was \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, offset11))
            }
        }
    }

    @Test fun `should test OffsetDateTime value in range`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10)..
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime value in range`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        shouldThrow<AssertionError>("JSON value not in range \"2022-06-15T17:05:10+10:00\".." +
                "\"2022-06-15T17:05:11+10:00\" - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(OffsetDateTime.of(2022, 6, 15, 17, 5, 10, 0, offset10)..
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 11, 0, offset10))
            }
        }
    }

    @Test fun `should test OffsetDateTime property in range`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10)..
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime property in range`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"2022-06-15T17:05:10+10:00\".." +
                "\"2022-06-15T17:05:11+10:00\" - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetDateTime.of(2022, 6, 15, 17, 5, 10, 0, offset10)..
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 11, 0, offset10))
            }
        }
    }

    @Test fun `should test OffsetDateTime array item in range`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10)..
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime array item in range`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"2022-06-15T17:05:10+10:00\".." +
                "\"2022-06-15T17:05:11+10:00\" - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, OffsetDateTime.of(2022, 6, 15, 17, 5, 10, 0, offset10)..
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 11, 0, offset10))
            }
        }
    }

    @Test fun `should test OffsetDateTime value in collection`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime value in collection`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        shouldThrow<AssertionError>("JSON value not in collection - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10)))
            }
        }
    }

    @Test fun `should test OffsetDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10)))
            }
        }
    }

    @Test fun `should test OffsetDateTime array item in collection`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                    OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime array item in collection`() {
        val json = "[\"2022-06-15T17:05:09.123+10:00\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"2022-06-15T17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 0, offset10),
                        OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 200_000_000, offset10)))
            }
        }
    }

    @Test fun `should test that any item has OffsetDateTime value`() {
        val json = """["2023-09-18T19:30:15+10:00","2023-09-18T19:30:30+10:00","2023-09-18T19:30:45+10:00"]"""
        JSONExpect.expectJSON(json) {
            anyItem(OffsetDateTime.of(2023, 9, 18, 19, 30, 45, 0, offset10))
        }
    }

    @Test fun `should fail on incorrect test that any item has OffsetDateTime value`() {
        val json = """["2023-09-18T19:30:15","2023-09-18T19:30:30","2023-09-18T19:30:45"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"2023-09-18T19:30:55+10:00\"") {
            JSONExpect.expectJSON(json) {
                anyItem(OffsetDateTime.of(2023, 9, 18, 19, 30, 55, 0, offset10))
            }
        }
    }

    companion object {

        val offset10: ZoneOffset = ZoneOffset.ofHours(10)
        val offset11: ZoneOffset = ZoneOffset.ofHours(11)

    }

}
