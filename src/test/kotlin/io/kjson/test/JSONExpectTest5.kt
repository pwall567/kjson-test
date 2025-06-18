/*
 * @(#) JSONExpectTest5.kt
 *
 * kjson-test Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2024 Peter Wall
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

import java.math.BigDecimal

import io.kstuff.test.shouldThrow

import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest5 {

    @Test fun `should test value as one of multiple possibilities`() {
        val json1 = "\"2020-05-12\""
        expectJSON(json1) {
            oneOf(isLocalDate, test(42), test("NEVER"))
        }
        val json2 = "42"
        expectJSON(json2) {
            oneOf(isLocalDate, test(42), test("NEVER"))
        }
        val json3 = "\"NEVER\""
        expectJSON(json3) {
            oneOf(isLocalDate, test(42), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of value as one of multiple possibilities`() {
        val json = "\"INCORRECT\""
        shouldThrow<AssertionError>("No successful test - value is \"INCORRECT\"") {
            expectJSON(json) {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
    }

    @Test fun `should test property as one of multiple possibilities`() {
        val json1 = """{"abc":"2020-05-12"}"""
        expectJSON(json1) {
            property("abc") {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
        val json2 = """{"abc":42}"""
        expectJSON(json2) {
            property("abc") {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
        val json3 = """{"abc":"NEVER"}"""
        expectJSON(json3) {
            property("abc") {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
    }

    @Test fun `should fail on incorrect test of property as one of multiple possibilities`() {
        val json = """{"abc":"INCORRECT"}"""
        shouldThrow<AssertionError>("/abc: No successful test - value is \"INCORRECT\"") {
            expectJSON(json) {
                property("abc") {
                    oneOf(isLocalDate, test(42), test("NEVER"))
                }
            }
        }
    }

    @Test fun `should test array item as one of multiple possibilities`() {
        val json1 = "[\"2020-05-12\"]"
        expectJSON(json1) {
            item(0) {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
        val json2 = "[42]"
        expectJSON(json2) {
            item(0) {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
        val json3 = "[\"NEVER\"]"
        expectJSON(json3) {
            item(0) {
                oneOf(isLocalDate, test(42), test("NEVER"))
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as one of multiple possibilities`() {
        val json = "[\"INCORRECT\"]"
        shouldThrow<AssertionError>("/0: No successful test - value is \"INCORRECT\"") {
            expectJSON(json) {
                item(0) {
                    oneOf(isLocalDate, test(42), test("NEVER"))
                }
            }
        }
    }

    @Test fun `should include long check as one of multiple possibilities`() {
        val json = "2233445566778899"
        expectJSON(json) {
            oneOf(isLocalDate, test(2233445566778899), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of long check as one of multiple possibilities`() {
        val json = "0"
        shouldThrow<AssertionError>("No successful test - value is 0") {
            expectJSON(json) {
                oneOf(isLocalDate, test(2233445566778899), test("NEVER"))
            }
        }
    }

    @Test fun `should include decimal check as one of multiple possibilities`() {
        val json = "1.5"
        expectJSON(json) {
            oneOf(isLocalDate, test(BigDecimal("1.5")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of decimal check as one of multiple possibilities`() {
        val json = "0.5"
        shouldThrow<AssertionError>("No successful test - value is 0.5") {
            expectJSON(json) {
                oneOf(isLocalDate, test(BigDecimal("1.5")), test("NEVER"))
            }
        }
    }

    @Test fun `should include boolean check as one of multiple possibilities`() {
        val json = "true"
        expectJSON(json) {
            oneOf(isLocalDate, test(true), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of boolean check as one of multiple possibilities`() {
        val json = "false"
        shouldThrow<AssertionError>("No successful test - value is false") {
            expectJSON(json) {
                oneOf(isLocalDate, test(true), test("NEVER"))
            }
        }
    }

    @Test fun `should include null check as one of multiple possibilities`() {
        val json = "null"
        expectJSON(json) {
            oneOf(isLocalDate, test(null), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of null check as one of multiple possibilities`() {
        val json = "0"
        shouldThrow<AssertionError>("No successful test - value is 0") {
            expectJSON(json) {
                oneOf(isLocalDate, test(null), test("NEVER"))
            }
        }
    }

    @Test fun `should include Regex check as one of multiple possibilities`() {
        val json = "\"abcdef\""
        expectJSON(json) {
            oneOf(isLocalDate, test(Regex("^[a-z]+$")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of Regex check as one of multiple possibilities`() {
        val json = "\"0\""
        shouldThrow<AssertionError>("No successful test - value is \"0\"") {
            expectJSON(json) {
                oneOf(isLocalDate, test(Regex("^[a-z]+$")), test("NEVER"))
            }
        }
    }

    @Test fun `should include int range check as one of multiple possibilities`() {
        val json = "42"
        expectJSON(json) {
            oneOf(isLocalDate, test(0..50), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of int range check as one of multiple possibilities`() {
        val json = "51"
        shouldThrow<AssertionError>("No successful test - value is 51") {
            expectJSON(json) {
                oneOf(isLocalDate, test(0..50), test("NEVER"))
            }
        }
    }

    @Test fun `should include long range check as one of multiple possibilities`() {
        val json = "1122334455667700"
        expectJSON(json) {
            oneOf(isLocalDate, test(0..1122334455667788), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of long range check as one of multiple possibilities`() {
        val json = "1122334455667799"
        shouldThrow<AssertionError>("No successful test - value is 1122334455667799") {
            expectJSON(json) {
                oneOf(isLocalDate, test(0..1122334455667788), test("NEVER"))
            }
        }
    }

    @Test fun `should include decimal range check as one of multiple possibilities`() {
        val json = "2.5"
        expectJSON(json) {
            oneOf(isLocalDate, test(BigDecimal.ZERO..BigDecimal("10.0")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of decimal range check as one of multiple`() {
        val json = "-2.5"
        shouldThrow<AssertionError>("No successful test - value is -2.5") {
            expectJSON(json) {
                oneOf(isLocalDate, test(BigDecimal.ZERO..BigDecimal("10.0")), test("NEVER"))
            }
        }
    }

    @Test fun `should include string range check as one of multiple possibilities`() {
        val json = "\"abc\""
        expectJSON(json) {
            oneOf(isLocalDate, test("aaa".."zzz"), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of string range check as one of multiple possibilities`() {
        val json = "\"AAA\""
        shouldThrow<AssertionError>("No successful test - value is \"AAA\"") {
            expectJSON(json) {
                oneOf(isLocalDate, test("aaa".."zzz"), test("NEVER"))
            }
        }
    }

    @Test fun `should include string collection check as one of multiple possibilities`() {
        val json = "\"alpha\""
        expectJSON(json) {
            oneOf(isLocalDate, test(setOf("alpha", "beta", "gamma")), test("NEVER"))
        }
    }

    @Test fun `should fail on incorrect test of string collection check as one of multiple`() {
        val json = "\"delta\""
        shouldThrow<AssertionError>("No successful test - value is \"delta\"") {
            expectJSON(json) {
                oneOf(isLocalDate, test(setOf("alpha", "beta", "gamma")), test("NEVER"))
            }
        }
    }

    @Test fun `should test complex combinations of multiple possibilities`() {
        val json1 = """{"data":27}"""
        expectJSON(json1) {
            oneOf({
                property("data", 27)
            },{
                property("error", 0..999)
            })
        }
        val json2 = """{"error":8}"""
        expectJSON(json2) {
            oneOf({
                property("data", 27)
            },{
                property("error", 0..999)
            })
        }
    }

    @Test fun `should fail on incorrect test of complex combinations of multiple possibilities`() {
        val json = """{"data":28}"""
        shouldThrow<AssertionError>("No successful test - value is { \"data\": 28 }") {
            expectJSON(json) {
                oneOf({
                    property("data", 27)
                },{
                    property("error", 0..999)
                })
            }
        }
    }

}
