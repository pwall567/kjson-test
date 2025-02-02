/*
 * @(#) DurationTest.kt
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
import kotlin.time.Duration.Companion.seconds

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class DurationTest {

    @Test fun `should read nodeAsDuration`() {
        val json = "\"34s\""
        JSONExpect.expectJSON(json) {
            nodeAsDuration shouldBe 34.seconds
        }
    }

    @Test fun `should fail on invalid nodeAsDuration`() {
        val json = "\"not a Duration\""
        shouldThrow<AssertionError>("JSON string is not a Duration - \"not a Duration\"") {
            JSONExpect.expectJSON(json) {
                nodeAsDuration
            }
        }
    }

    @Test fun `should test Duration value`() {
        val json = "\"34s\""
        JSONExpect.expectJSON(json) {
            value(34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration value`() {
        val json = "\"33s\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"34s\", was \"33s\"") {
            JSONExpect.expectJSON(json) {
                value(34.seconds)
            }
        }
    }

    @Test fun `should test Duration property`() {
        val json = """{"abc":"34s"}"""
        JSONExpect.expectJSON(json) {
            property("abc", 34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration property`() {
        val json = """{"abc":"33s"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"34s\", was \"33s\"") {
            JSONExpect.expectJSON(json) {
                property("abc", 34.seconds)
            }
        }
    }

    @Test fun `should test Duration array item`() {
        val json = """["34s"]"""
        JSONExpect.expectJSON(json) {
            item(0, 34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration array item`() {
        val json = """["33s"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"34s\", was \"33s\"") {
            JSONExpect.expectJSON(json) {
                item(0, 34.seconds)
            }
        }
    }

    @Test fun `should test Duration value in range`() {
        val json = "\"34s\""
        JSONExpect.expectJSON(json) {
            value(33.seconds..35.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration value in range`() {
        val json = "\"34s\""
        shouldThrow<AssertionError>("JSON value not in range \"35s\"..\"36s\" - \"34s\"") {
            JSONExpect.expectJSON(json) {
                value(35.seconds..36.seconds)
            }
        }
    }

    @Test fun `should test Duration property in range`() {
        val json = """{"abc":"34s"}"""
        JSONExpect.expectJSON(json) {
            property("abc", 33.seconds..35.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration property in range`() {
        val json = """{"abc":"34s"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"35s\"..\"36s\" - \"34s\"") {
            JSONExpect.expectJSON(json) {
                property("abc", 35.seconds..36.seconds)
            }
        }
    }

    @Test fun `should test Duration array item in range`() {
        val json = "[\"34s\"]"
        JSONExpect.expectJSON(json) {
            item(0, 33.seconds..35.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration array item in range`() {
        val json = "[\"34s\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"35s\"..\"36s\" - \"34s\"") {
            JSONExpect.expectJSON(json) {
                item(0, 35.seconds..36.seconds)
            }
        }
    }

    @Test fun `should test Duration value in collection`() {
        val json = "\"34s\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(34.seconds, 36.seconds))
        }
    }

    @Test fun `should fail on incorrect Duration value in collection`() {
        val json = "\"34s\""
        shouldThrow<AssertionError>("JSON value not in collection - \"34s\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(33.seconds, 36.seconds))
            }
        }
    }

    @Test fun `should test Duration property in collection`() {
        val json = """{"abc":"34s"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(34.seconds, 36.seconds))
        }
    }

    @Test fun `should fail on incorrect Duration property in collection`() {
        val json = """{"abc":"34s"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"34s\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(33.seconds, 36.seconds))
            }
        }
    }

    @Test fun `should test Duration array item in collection`() {
        val json = "[\"34s\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(34.seconds, 36.seconds))
        }
    }

    @Test fun `should fail on incorrect Duration array item in collection`() {
        val json = "[\"34s\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"34s\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(33.seconds, 36.seconds))
            }
        }
    }

    @Test fun `should test that any item has Duration value`() {
        val json = """["10s","20s","30s"]"""
        JSONExpect.expectJSON(json) {
            anyItem(20.seconds)
        }
    }

    @Test fun `should fail on incorrect test that any item has Duration value`() {
        val json = """["10s","20s","30s"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"45s\"") {
            JSONExpect.expectJSON(json) {
                anyItem(45.seconds)
            }
        }
    }

}
