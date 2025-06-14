/*
 * @(#) OffsetTimeTest.kt
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

import java.time.OffsetTime

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

import io.kjson.test.OffsetDateTimeTest.Companion.offset10

class OffsetTimeTest {

    @Test fun `should read nodeAsOffsetTime`() {
        val json = "\"17:05:09.123+10:00\""
        JSONExpect.expectJSON(json) {
            nodeAsOffsetTime shouldBe OffsetTime.of(17, 5, 9, 123_000_000, offset10)
        }
    }

    @Test fun `should fail on invalid nodeAsOffsetTime`() {
        val json = "\"not an OffsetTime\""
        shouldThrow<AssertionError>("JSON string is not an OffsetTime - \"not an OffsetTime\"") {
            JSONExpect.expectJSON(json) {
                nodeAsOffsetTime
            }
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
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"17:05:09.123+10:00\"," +
                " was \"17:05:09.124+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(OffsetTime.of(17, 5, 9, 123_000_000, offset10))
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
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"17:05:09.123+10:00\"," +
                " was \"17:05:09.124+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetTime.of(17, 5, 9, 123_000_000, offset10))
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
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"17:05:09.123+10:00\"," +
                " was \"17:05:09.124+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, OffsetTime.of(17, 5, 9, 123_000_000, offset10))
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
        shouldThrow<AssertionError>("JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                " - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
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
        shouldThrow<AssertionError>("/abc: JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                " - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
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
        shouldThrow<AssertionError>("/0: JSON value not in range \"17:05:10+10:00\"..\"17:05:11+10:00\"" +
                " - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, OffsetTime.of(17, 5, 10, 0, offset10)..OffsetTime.of(17, 5, 11, 0, offset10))
            }
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
        shouldThrow<AssertionError>("JSON value not in collection - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10), OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
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
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10),
                        OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
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
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"17:05:09.123+10:00\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(OffsetTime.of(17, 5, 9, 0, offset10),
                        OffsetTime.of(17, 5, 9, 200_000_000, offset10)))
            }
        }
    }

    @Test fun `should test that any item has OffsetTime value`() {
        val json = """["19:30:15+10:00","19:30:30+10:00","19:30:45+10:00"]"""
        JSONExpect.expectJSON(json) {
            anyItem(OffsetTime.of(19, 30, 45, 0, offset10))
        }
    }

    @Test fun `should fail on incorrect test that any item has OffsetTime value`() {
        val json = """["19:30:15","19:30:30","19:30:45"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"19:30:55+10:00\"") {
            JSONExpect.expectJSON(json) {
                anyItem(OffsetTime.of(19, 30, 55, 0, offset10))
            }
        }
    }

    @Test fun `should test multiple OffsetTime values`() {
        val json = """["20:30:15+10:00","20:30:30+10:00","20:30:45+10:00"]"""
        JSONExpect.expectJSON(json) {
            items(OffsetTime.of(20, 30, 15, 0, offset10), OffsetTime.of(20, 30, 30, 0, offset10),
                    OffsetTime.of(20, 30, 45, 0, offset10))
        }
    }

    @Test fun `should fail on incorrect multiple OffsetTime values`() {
        val json = """["20:30:15+10:00","20:30:30+10:00","20:30:45+10:00"]"""
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected \"20:30:35+10:00\", " +
                "was \"20:30:30+10:00\"") {
            JSONExpect.expectJSON(json) {
                items(OffsetTime.of(20, 30, 15, 0, offset10), OffsetTime.of(20, 30, 35, 0, offset10),
                        OffsetTime.of(20, 30, 45, 0, offset10))
            }
        }
    }

    @Test fun `should fail on incorrect number of multiple OffsetTime values`() {
        val json = """["20:30:15+10:00","20:30:30+10:00","20:30:45+10:00"]"""
        shouldThrow<AssertionError>("JSON array size not the same as number of values - expected 2, was 3") {
            JSONExpect.expectJSON(json) {
                items(OffsetTime.of(20, 30, 15, 0, offset10), OffsetTime.of(20, 30, 30, 0, offset10))
            }
        }
    }

    @Test fun `should fail on multiple OffsetTime values applied to an object`() {
        val json = "{}"
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was object") {
            JSONExpect.expectJSON(json) {
                items(OffsetTime.of(20, 30, 15, 0, offset10), OffsetTime.of(20, 30, 30, 0, offset10),
                        OffsetTime.of(20, 30, 45, 0, offset10))
            }
        }
    }

}
