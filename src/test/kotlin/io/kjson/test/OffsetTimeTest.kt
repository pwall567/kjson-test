/*
 * @(#) OffsetTimeTest.kt
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

import java.time.OffsetTime

import io.kjson.test.OffsetDateTimeTest.Companion.offset10
import net.pwall.util.MiniSet

class OffsetTimeTest {

    @Test fun `should read nodeAsOffsetTime`() {
        val json = "\"17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            if (nodeAsOffsetTime != OffsetTime.of(17, 5, 9, 123_000_000, offset10))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsOffsetTime`() {
        val json = "\"not an OffsetTime\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsOffsetTime
            }
        }.let {
            expect("JSON string is not an OffsetTime - \"not an OffsetTime\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime value`() {
        val json = "\"17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(OffsetTime.of(17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime value`() {
        val json = "\"17:05:09.124+10:00\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(OffsetTime.of(17, 5, 9, 123_000_000, offset10))
            }
        }.let {
            expect("JSON value doesn't match - expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should test OffsetTime property`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", OffsetTime.of(17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime property`() {
        val json = """{"abc":"17:05:09.124+10:00"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetTime.of(17, 5, 9, 123_000_000, offset10))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should test OffsetTime array item`() {
        val json = """["17:05:09.123+10:00"]"""
        JSONExpect.expectJSON(json) {
            item(0, OffsetTime.of(17, 5, 9, 123_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime array item`() {
        val json = """["17:05:09.124+10:00"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, OffsetTime.of(17, 5, 9, 123_000_000, offset10))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should test OffsetTime value in range`() {
        val json = "\"17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(OffsetTime.of(17, 5, 9, 0, offset10)..OffsetTime.of(17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime value in range`() {
        val json = "\"17:05:09.123+10:00\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
        }.let {
            expect("JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                    " - \"17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime property in range`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", OffsetTime.of(17, 5, 9, 0, offset10)..OffsetTime.of(17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime property in range`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
        }.let {
            expect("/abc: JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                    " - \"17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime array item in range`() {
        val json = "[\"17:05:09.123+10:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, OffsetTime.of(17, 5, 9, 0, offset10)..OffsetTime.of(17, 5, 9, 200_000_000, offset10))
        }
    }

    @Test fun `should fail on incorrect OffsetTime array item in range`() {
        val json = "[\"17:05:09.123+10:00\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
        }.let {
            expect("/0: JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                    " - \"17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime value in collection`() {
        val json = "\"17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10), OffsetTime.of(17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime value in collection`() {
        val json = "\"17:05:09.123+10:00\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10), OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
        }.let {
            expect("JSON value not in collection - \"17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime property in collection`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10),
                    OffsetTime.of(17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime property in collection`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10),
                        OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime array item in collection`() {
        val json = "[\"17:05:09.123+10:00\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10), OffsetTime.of(17, 5, 9, 123_000_000, offset10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime array item in collection`() {
        val json = "[\"17:05:09.123+10:00\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10),
                        OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"17:05:09.123+10:00\"") { it.message }
        }
    }

}
