/*
 * @(#) PeriodTest.kt
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

import java.time.Period

import net.pwall.util.MiniSet

class PeriodTest {

    @Test fun `should read nodeAsPeriod`() {
        val json = "\"P45D\""
        JSONExpect.expectJSON(json) {
            if (nodeAsPeriod != Period.ofDays(45))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsPeriod`() {
        val json = "\"not a Period\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsPeriod
            }
        }.let {
            expect("JSON string is not a Period - \"not a Period\"") { it.message }
        }
    }

    @Test fun `should test Period value`() {
        val json = "\"P45D\""
        JSONExpect.expectJSON(json) {
            value(Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period value`() {
        val json = "\"P44D\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(Period.ofDays(45))
            }
        }.let {
            expect("JSON value doesn't match - expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should test Period property`() {
        val json = """{"abc":"P45D"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period property`() {
        val json = """{"abc":"P44D"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", Period.ofDays(45))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should test Period array item`() {
        val json = """["P45D"]"""
        JSONExpect.expectJSON(json) {
            item(0, Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period array item`() {
        val json = """["P44D"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, Period.ofDays(45))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should test Period value in collection`() {
        val json = "\"P45D\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(Period.ofDays(45), Period.ofDays(46)))
        }
    }

    @Test fun `should fail on incorrect Period value in collection`() {
        val json = "\"P45D\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(Period.ofDays(44), Period.ofDays(46)))
            }
        }.let {
            expect("JSON value not in collection - \"P45D\"") { it.message }
        }
    }

    @Test fun `should test Period property in collection`() {
        val json = """{"abc":"P45D"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(Period.ofDays(45), Period.ofDays(46)))
        }
    }

    @Test fun `should fail on incorrect Period property in collection`() {
        val json = """{"abc":"P45D"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(Period.ofDays(44), Period.ofDays(46)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"P45D\"") { it.message }
        }
    }

    @Test fun `should test Period array item in collection`() {
        val json = "[\"P45D\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(Period.ofDays(45), Period.ofDays(46)))
        }
    }

    @Test fun `should fail on incorrect Period array item in collection`() {
        val json = "[\"P45D\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(Period.ofDays(44), Period.ofDays(46)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"P45D\"") { it.message }
        }
    }

}
