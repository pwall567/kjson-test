/*
 * @(#) MonthDayTest.kt
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

import java.time.MonthDay

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class MonthDayTest {

    @Test fun `should read nodeAsMonthDay`() {
        val json = "\"--06-15\""
        JSONExpect.expectJSON(json) {
            nodeAsMonthDay shouldBe MonthDay.of(6, 15)
        }
    }

    @Test fun `should fail on invalid nodeAsMonthDay`() {
        val json = "\"not a MonthDay\""
        shouldThrow<AssertionError>("JSON string is not a MonthDay - \"not a MonthDay\"") {
            JSONExpect.expectJSON(json) {
                nodeAsMonthDay
            }
        }
    }

    @Test fun `should test MonthDay value`() {
        val json = "\"--06-15\""
        JSONExpect.expectJSON(json) {
            value(MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay value`() {
        val json = "\"--06-14\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"--06-15\", was \"--06-14\"") {
            JSONExpect.expectJSON(json) {
                value(MonthDay.of(6, 15))
            }
        }
    }

    @Test fun `should test MonthDay property`() {
        val json = """{"abc":"--06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay property`() {
        val json = """{"abc":"--06-14"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"--06-15\", was \"--06-14\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MonthDay.of(6, 15))
            }
        }
    }

    @Test fun `should test MonthDay array item`() {
        val json = """["--06-15"]"""
        JSONExpect.expectJSON(json) {
            item(0, MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay array item`() {
        val json = """["--06-14"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"--06-15\", was \"--06-14\"") {
            JSONExpect.expectJSON(json) {
                item(0, MonthDay.of(6, 15))
            }
        }
    }

    @Test fun `should test MonthDay value in range`() {
        val json = "\"--06-15\""
        JSONExpect.expectJSON(json) {
            value(MonthDay.of(6, 14)..MonthDay.of(6, 16))
        }
    }

    @Test fun `should fail on incorrect MonthDay value in range`() {
        val json = "\"--06-15\""
        shouldThrow<AssertionError>("JSON value not in range \"--06-16\"..\"--06-17\" - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                value(MonthDay.of(6, 16)..MonthDay.of(6, 17))
            }
        }
    }

    @Test fun `should test MonthDay property in range`() {
        val json = """{"abc":"--06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MonthDay.of(6, 14)..MonthDay.of(6, 16))
        }
    }

    @Test fun `should fail on incorrect MonthDay property in range`() {
        val json = """{"abc":"--06-15"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"--06-16\"..\"--06-17\" - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MonthDay.of(6, 16)..MonthDay.of(6, 17))
            }
        }
    }

    @Test fun `should test MonthDay array item in range`() {
        val json = "[\"--06-15\"]"
        JSONExpect.expectJSON(json) {
            item(0, MonthDay.of(6, 14)..MonthDay.of(6, 16))
        }
    }

    @Test fun `should fail on incorrect MonthDay array item in range`() {
        val json = "[\"--06-15\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"--06-16\"..\"--06-17\" - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                item(0, MonthDay.of(6, 16)..MonthDay.of(6, 17))
            }
        }
    }

    @Test fun `should test MonthDay value in collection`() {
        val json = "\"--06-15\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(MonthDay.of(6, 15), MonthDay.of(6, 16)))
        }
    }

    @Test fun `should fail on incorrect MonthDay value in collection`() {
        val json = "\"--06-15\""
        shouldThrow<AssertionError>("JSON value not in collection - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(MonthDay.of(6, 14), MonthDay.of(6, 16)))
            }
        }
    }

    @Test fun `should test MonthDay property in collection`() {
        val json = """{"abc":"--06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(MonthDay.of(6, 15), MonthDay.of(6, 16)))
        }
    }

    @Test fun `should fail on incorrect MonthDay property in collection`() {
        val json = """{"abc":"--06-15"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(MonthDay.of(6, 14), MonthDay.of(6, 16)))
            }
        }
    }

    @Test fun `should test MonthDay array item in collection`() {
        val json = "[\"--06-15\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(MonthDay.of(6, 15), MonthDay.of(6, 16)))
        }
    }

    @Test fun `should fail on incorrect MonthDay array item in collection`() {
        val json = "[\"--06-15\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"--06-15\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(MonthDay.of(6, 14), MonthDay.of(6, 16)))
            }
        }
    }

    @Test fun `should test that any item has MonthDay value`() {
        val json = """["--09-18","--09-19","--09-20"]"""
        JSONExpect.expectJSON(json) {
            anyItem(MonthDay.of(9, 20))
        }
    }

    @Test fun `should fail on incorrect test that any item has MonthDay value`() {
        val json = """["--09-18","--09-19","--09-20"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"--09-21\"") {
            JSONExpect.expectJSON(json) {
                anyItem(MonthDay.of(9, 21))
            }
        }
    }

}
