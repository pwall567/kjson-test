/*
 * @(#) YearMonthTest.kt
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

import java.time.YearMonth

import net.pwall.util.MiniSet

class YearMonthTest {

    @Test fun `should read nodeAsYearMonth`() {
        val json = "\"2022-06\""
        JSONExpect.expectJSON(json) {
            if (nodeAsYearMonth != YearMonth.of(2022, 6))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsYearMonth`() {
        val json = "\"not a YearMonth\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsYearMonth
            }
        }.let {
            expect("JSON string is not a YearMonth - \"not a YearMonth\"") { it.message }
        }
    }

    @Test fun `should test YearMonth value`() {
        val json = "\"2022-06\""
        JSONExpect.expectJSON(json) {
            value(YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth value`() {
        val json = "\"2022-05\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(YearMonth.of(2022, 6))
            }
        }.let {
            expect("JSON value doesn't match - expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should test YearMonth property`() {
        val json = """{"abc":"2022-06"}"""
        JSONExpect.expectJSON(json) {
            property("abc", YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth property`() {
        val json = """{"abc":"2022-05"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", YearMonth.of(2022, 6))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should test YearMonth array item`() {
        val json = """["2022-06"]"""
        JSONExpect.expectJSON(json) {
            item(0, YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth array item`() {
        val json = """["2022-05"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, YearMonth.of(2022, 6))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should test YearMonth value in range`() {
        val json = "\"2022-06\""
        JSONExpect.expectJSON(json) {
            value(YearMonth.of(2022, 5)..YearMonth.of(2022, 7))
        }
    }

    @Test fun `should fail on incorrect YearMonth value in range`() {
        val json = "\"2022-06\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(YearMonth.of(2022, 7)..YearMonth.of(2022, 8))
            }
        }.let {
            expect("JSON value not in range \"2022-07\"..\"2022-08\" - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test YearMonth property in range`() {
        val json = """{"abc":"2022-06"}"""
        JSONExpect.expectJSON(json) {
            property("abc", YearMonth.of(2022, 5)..YearMonth.of(2022, 7))
        }
    }

    @Test fun `should fail on incorrect YearMonth property in range`() {
        val json = """{"abc":"2022-06"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", YearMonth.of(2022, 7)..YearMonth.of(2022, 8))
            }
        }.let {
            expect("/abc: JSON value not in range \"2022-07\"..\"2022-08\" - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test YearMonth array item in range`() {
        val json = "[\"2022-06\"]"
        JSONExpect.expectJSON(json) {
            item(0, YearMonth.of(2022, 5)..YearMonth.of(2022, 7))
        }
    }

    @Test fun `should fail on incorrect YearMonth array item in range`() {
        val json = "[\"2022-06\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, YearMonth.of(2022, 7)..YearMonth.of(2022, 8))
            }
        }.let {
            expect("/0: JSON value not in range \"2022-07\"..\"2022-08\" - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test YearMonth value in collection`() {
        val json = "\"2022-06\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(YearMonth.of(2022, 6), YearMonth.of(2022, 7)))
        }
    }

    @Test fun `should fail on incorrect YearMonth value in collection`() {
        val json = "\"2022-06\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(YearMonth.of(2022, 7), YearMonth.of(2022, 8)))
            }
        }.let {
            expect("JSON value not in collection - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test YearMonth property in collection`() {
        val json = """{"abc":"2022-06"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(YearMonth.of(2022, 6), YearMonth.of(2022, 7)))
        }
    }

    @Test fun `should fail on incorrect YearMonth property in collection`() {
        val json = """{"abc":"2022-06"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(YearMonth.of(2022, 7), YearMonth.of(2022, 8)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test YearMonth array item in collection`() {
        val json = "[\"2022-06\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(YearMonth.of(2022, 6), YearMonth.of(2022, 7)))
        }
    }

    @Test fun `should fail on incorrect YearMonth array item in collection`() {
        val json = "[\"2022-06\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(YearMonth.of(2022, 7), YearMonth.of(2022, 8)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"2022-06\"") { it.message }
        }
    }

    @Test fun `should test that any item has YearMonth value`() {
        val json = """["2023-09","2023-10","2023-11"]"""
        JSONExpect.expectJSON(json) {
            anyItem(YearMonth.of(2023, 9))
        }
    }

    @Test fun `should fail on incorrect test that any item has YearMonth value`() {
        val json = """["2023-09","2023-10","2023-11"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem(YearMonth.of(2023, 12))
            }
        }.let { expect("No JSON array item has value \"2023-12\"") { it.message } }
    }

}
