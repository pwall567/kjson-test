/*
 * @(#) DurationTest.kt
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
import kotlin.time.Duration.Companion.seconds

import net.pwall.util.MiniSet

class DurationTest {

    @Test fun `should read nodeAsDuration`() {
        val json = "\"34s\""
        JSONExpect.expectJSON(json) {
            if (nodeAsDuration != 34.seconds)
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsDuration`() {
        val json = "\"not a Duration\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsDuration
            }
        }.let {
            expect("JSON string is not a Duration - \"not a Duration\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(34.seconds)
            }
        }.let {
            expect("JSON value doesn't match - expected \"34s\", was \"33s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", 34.seconds)
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"34s\", was \"33s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, 34.seconds)
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"34s\", was \"33s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(35.seconds..36.seconds)
            }
        }.let {
            expect("JSON value not in range \"35s\"..\"36s\" - \"34s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", 35.seconds..36.seconds)
            }
        }.let {
            expect("/abc: JSON value not in range \"35s\"..\"36s\" - \"34s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, 35.seconds..36.seconds)
            }
        }.let {
            expect("/0: JSON value not in range \"35s\"..\"36s\" - \"34s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(33.seconds, 36.seconds))
            }
        }.let {
            expect("JSON value not in collection - \"34s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(33.seconds, 36.seconds))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"34s\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(33.seconds, 36.seconds))
            }
        }.let {
            expect("/0: JSON value not in collection - \"34s\"") { it.message }
        }
    }

}
