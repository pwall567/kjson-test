/*
 * @(#) LocalDateTimeTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022, 2023 Peter Wall
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

import java.time.LocalDateTime

import net.pwall.util.MiniSet

class LocalDateTimeTest {

    @Test fun `should read nodeAsLocalDateTime`() {
        val json = "\"2022-06-15T17:05:09.123\""
        JSONExpect.expectJSON(json) {
            if (nodeAsLocalDateTime != LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsLocalDateTime`() {
        val json = "\"not a LocalDateTime\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsLocalDateTime
            }
        }.let {
            expect("JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123\""
        JSONExpect.expectJSON(json) {
            value(LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime value`() {
        val json = "\"2022-06-15T17:05:09.124\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("JSON value doesn't match - expected \"2022-06-15T17:05:09.123\", was \"2022-06-15T17:05:09.124\"") {
                it.message
            }
        }
    }

    @Test fun `should test LocalDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.124"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"2022-06-15T17:05:09.123\"," +
                    " was \"2022-06-15T17:05:09.124\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123"]"""
        JSONExpect.expectJSON(json) {
            item(0, LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime array item`() {
        val json = """["2022-06-15T17:05:09.124"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"2022-06-15T17:05:09.123\"," +
                    " was \"2022-06-15T17:05:09.124\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime value in range`() {
        val json = "\"2022-06-15T12:00:00\""
        JSONExpect.expectJSON(json) {
            value(LocalDateTime.of(2022, 6, 14, 0, 0, 0)..LocalDateTime.of(2022, 6, 16, 0, 0, 0))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime value in range`() {
        val json = "\"2022-06-15T12:00:00\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(LocalDateTime.of(2022, 6, 16, 0, 0, 0)..LocalDateTime.of(2022, 6, 18, 0, 0, 0))
            }
        }.let {
            expect("JSON value not in range \"2022-06-16T00:00:00\"..\"2022-06-18T00:00:00\"" +
                    " - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime property in range`() {
        val json = """{"abc":"2022-06-15T12:00:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalDateTime.of(2022, 6, 14, 0, 0, 0)..LocalDateTime.of(2022, 6, 16, 0, 0, 0))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime property in range`() {
        val json = """{"abc":"2022-06-15T12:00:00"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", LocalDateTime.of(2022, 6, 16, 0, 0, 0)..LocalDateTime.of(2022, 6, 18, 0, 0, 0))
            }
        }.let {
            expect("/abc: JSON value not in range \"2022-06-16T00:00:00\"..\"2022-06-18T00:00:00\"" +
                    " - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime array item in range`() {
        val json = "[\"2022-06-15T12:00:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, LocalDateTime.of(2022, 6, 14, 0, 0, 0)..LocalDateTime.of(2022, 6, 16, 0, 0, 0))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime array item in range`() {
        val json = "[\"2022-06-15T12:00:00\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, LocalDateTime.of(2022, 6, 16, 0, 0, 0)..LocalDateTime.of(2022, 6, 18, 0, 0, 0))
            }
        }.let {
            expect("/0: JSON value not in range \"2022-06-16T00:00:00\"..\"2022-06-18T00:00:00\"" +
                    " - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime value in collection`() {
        val json = "\"2022-06-15T12:00:00\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(LocalDateTime.of(2022, 6, 15, 12, 0, 0), LocalDateTime.of(2022, 6, 16, 12, 0, 0)))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime value in collection`() {
        val json = "\"2022-06-15T12:00:00\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(LocalDateTime.of(2022, 6, 14, 12, 0, 0), LocalDateTime.of(2022, 6, 16, 12, 0, 0)))
            }
        }.let {
            expect("JSON value not in collection - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T00:00:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(LocalDateTime.of(2022, 6, 15, 0, 0, 0), LocalDateTime.of(2022, 6, 16, 0, 0, 0)))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime property in collection`() {
        val json = """{"abc":"2022-06-15T12:00:00"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc",
                    MiniSet.of(LocalDateTime.of(2022, 6, 14, 0, 0, 0), LocalDateTime.of(2022, 6, 16, 0, 0, 0)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime array item in collection`() {
        val json = "[\"2022-06-15T12:00:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(LocalDateTime.of(2022, 6, 15, 12, 0, 0), LocalDateTime.of(2022, 6, 16, 12, 0, 0)))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime array item in collection`() {
        val json = "[\"2022-06-15T12:00:00\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(LocalDateTime.of(2022, 6, 14, 0, 0, 0), LocalDateTime.of(2022, 6, 16, 0, 0, 0)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"2022-06-15T12:00:00\"") { it.message }
        }
    }

    @Test fun `should test that any item has LocalDateTime value`() {
        val json = """["2023-09-18T19:30:15","2023-09-18T19:30:30","2023-09-18T19:30:45"]"""
        JSONExpect.expectJSON(json) {
            anyItem(LocalDateTime.of(2023, 9, 18, 19, 30, 45))
        }
    }

    @Test fun `should fail on incorrect test that any item has LocalDateTime value`() {
        val json = """["2023-09-18T19:30:15","2023-09-18T19:30:30","2023-09-18T19:30:45"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem(LocalDateTime.of(2023, 9, 18, 19, 30, 55))
            }
        }.let { expect("No JSON array item has value \"2023-09-18T19:30:55\"") { it.message } }
    }

}
