/*
 * @(#) LocalTimeTest.kt
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

import java.time.LocalTime

import net.pwall.util.MiniSet

class LocalTimeTest {

    @Test fun `should read nodeAsLocalTime`() {
        val json = "\"17:05:09.123\""
        JSONExpect.expectJSON(json) {
            if (nodeAsLocalTime != LocalTime.of(17, 5, 9, 123_000_000))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsLocalTime`() {
        val json = "\"not a LocalTime\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsLocalTime
            }
        }.let {
            expect("JSON string is not a LocalTime - \"not a LocalTime\"") { it.message }
        }
    }

    @Test fun `should test LocalTime value`() {
        val json = "\"17:05:09.123\""
        JSONExpect.expectJSON(json) {
            value(LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime value`() {
        val json = "\"17:05:09\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("JSON value doesn't match - expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime property`() {
        val json = """{"abc":"17:05:09.123"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime property`() {
        val json = """{"abc":"17:05:09"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime array item`() {
        val json = """["17:05:09.123"]"""
        JSONExpect.expectJSON(json) {
            item(0, LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime array item`() {
        val json = """["17:05:09"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime value in range`() {
        val json = "\"17:05:09\""
        JSONExpect.expectJSON(json) {
            value(LocalTime.of(17, 5, 8)..LocalTime.of(17, 5, 10))
        }
    }

    @Test fun `should fail on incorrect LocalTime value in range`() {
        val json = "\"17:05:09\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(LocalTime.of(17, 5, 10)..LocalTime.of(17, 5, 11))
            }
        }.let {
            expect("JSON value not in range \"17:05:10\"..\"17:05:11\" - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime property in range`() {
        val json = """{"abc":"17:05:09"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalTime.of(17, 5, 8)..LocalTime.of(17, 5, 10))
        }
    }

    @Test fun `should fail on incorrect LocalTime property in range`() {
        val json = """{"abc":"17:05:09"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", LocalTime.of(17, 5, 10)..LocalTime.of(17, 5, 11))
            }
        }.let {
            expect("/abc: JSON value not in range \"17:05:10\"..\"17:05:11\" - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime array item in range`() {
        val json = "[\"17:05:09\"]"
        JSONExpect.expectJSON(json) {
            item(0, LocalTime.of(17, 5, 8)..LocalTime.of(17, 5, 10))
        }
    }

    @Test fun `should fail on incorrect LocalTime array item in range`() {
        val json = "[\"17:05:09\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, LocalTime.of(17, 5, 10)..LocalTime.of(17, 5, 11))
            }
        }.let {
            expect("/0: JSON value not in range \"17:05:10\"..\"17:05:11\" - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime value in collection`() {
        val json = "\"17:05:09\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 9)))
        }
    }

    @Test fun `should fail on incorrect LocalTime value in collection`() {
        val json = "\"17:05:09\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 10)))
            }
        }.let {
            expect("JSON value not in collection - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime property in collection`() {
        val json = """{"abc":"17:05:09"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 9)))
        }
    }

    @Test fun `should fail on incorrect LocalTime property in collection`() {
        val json = """{"abc":"17:05:09"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 10)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime array item in collection`() {
        val json = "[\"17:05:09\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 9)))
        }
    }

    @Test fun `should fail on incorrect LocalTime array item in collection`() {
        val json = "[\"17:05:09\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(LocalTime.of(17, 5, 8), LocalTime.of(17, 5, 10)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test that any item has LocalTime value`() {
        val json = """["19:30:15","19:30:30","19:30:45"]"""
        JSONExpect.expectJSON(json) {
            anyItem(LocalTime.of(19, 30, 45))
        }
    }

    @Test fun `should fail on incorrect test that any item has LocalTime value`() {
        val json = """["19:30:15","19:30:30","19:30:45"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem(LocalTime.of(19, 30, 55))
            }
        }.let { expect("No JSON array item has value \"19:30:55\"") { it.message } }
    }

}
