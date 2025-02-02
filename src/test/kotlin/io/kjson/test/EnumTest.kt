/*
 * @(#) EnumTest.kt
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

import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class EnumTest {

    @Test fun `should test Enum value`() {
        val json = "\"OPEN\""
        JSONExpect.expectJSON(json) {
            value(Status.OPEN)
        }
    }

    @Test fun `should fail on incorrect Enum value`() {
        val json = "\"WRONG\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"OPEN\", was \"WRONG\"") {
            JSONExpect.expectJSON(json) {
                value(Status.OPEN)
            }
        }
    }

    @Test fun `should test Enum property`() {
        val json = """{"abc":"CLOSED"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Status.CLOSED)
        }
    }

    @Test fun `should fail on incorrect Enum property`() {
        val json = """{"abc":"WRONG"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"CLOSED\", was \"WRONG\"") {
            JSONExpect.expectJSON(json) {
                property("abc", Status.CLOSED)
            }
        }
    }

    @Test fun `should test Enum array item`() {
        val json = """["OPEN"]"""
        JSONExpect.expectJSON(json) {
            item(0, Status.OPEN)
        }
    }

    @Test fun `should fail on incorrect Enum array item`() {
        val json = """["BAD"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"OPEN\", was \"BAD\"") {
            JSONExpect.expectJSON(json) {
                item(0, Status.OPEN)
            }
        }
    }

    @Test fun `should test Enum value in collection`() {
        val json = "\"OPEN\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(Status.OPEN, Status.INDETERMINATE))
        }
    }

    @Test fun `should fail on incorrect Enum value in collection`() {
        val json = "\"OPEN\""
        shouldThrow<AssertionError>("JSON value not in collection - \"OPEN\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(Status.CLOSED, Status.INDETERMINATE))
            }
        }
    }

    @Test fun `should test Enum property in collection`() {
        val json = """{"abc":"OPEN"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(Status.OPEN, Status.INDETERMINATE))
        }
    }

    @Test fun `should fail on incorrect Enum property in collection`() {
        val json = """{"abc":"OPEN"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"OPEN\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(Status.CLOSED, Status.INDETERMINATE))
            }
        }
    }

    @Test fun `should test Enum array item in collection`() {
        val json = "[\"OPEN\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(Status.OPEN, Status.INDETERMINATE))
        }
    }

    @Test fun `should fail on incorrect Enum array item in collection`() {
        val json = "[\"OPEN\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"OPEN\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(Status.CLOSED, Status.INDETERMINATE))
            }
        }
    }

    @Test fun `should test that any item has Enum value`() {
        val json = """["OPEN","CLOSED","INDETERMINATE"]"""
        JSONExpect.expectJSON(json) {
            anyItem(Status.INDETERMINATE)
        }
    }

    @Test fun `should fail on incorrect test that any item has Enum value`() {
        val json = """["OPEN","CLOSED"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"INDETERMINATE\"") {
            JSONExpect.expectJSON(json) {
                anyItem(Status.INDETERMINATE)
            }
        }
    }

    enum class Status { OPEN, CLOSED, INDETERMINATE }

}
