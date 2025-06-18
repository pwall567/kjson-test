/*
 * @(#) JSONExpectTest4.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2022, 2024 Peter Wall
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

import io.kjson.JSONString
import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest4 {

    @Test fun `should test string value against regex`() {
        val json = "\"abc\""
        expectJSON(json) {
            value(Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string value against regex`() {
        val json = "\"abc1\""
        shouldThrow<AssertionError>("JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") {
            expectJSON(json) {
                value(Regex("^[a-z]+$"))
            }
        }
    }

    @Test fun `should fail on test of non-string value against regex`() {
        val json = "12345"
        shouldThrow<AssertionError>("JSON type doesn't match - expected string, was integer") {
            expectJSON(json) {
                value(Regex("^[a-z]+$"))
            }
        }
    }

    @Test fun `should test string property against regex`() {
        val json = """{"prop":"abc"}"""
        expectJSON(json) {
            property("prop", Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string property against regex`() {
        val json = """{"prop":"abc1"}"""
        shouldThrow<AssertionError>("/prop: JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") {
            expectJSON(json) {
                property("prop", Regex("^[a-z]+$"))
            }
        }
    }

    @Test fun `should test string array item against regex`() {
        val json = """["abc"]"""
        expectJSON(json) {
            item(0, Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test of string array item against regex`() {
        val json = """["abc1"]"""
        shouldThrow<AssertionError>("/0: JSON string doesn't match regex - Expected ^[a-z]+\$, was \"abc1\"") {
            expectJSON(json) {
                item(0, Regex("^[a-z]+$"))
            }
        }
    }

    @Test fun `should test string value length`() {
        val json = "\"Hello!\""
        expectJSON(json) {
            value(length(6))
        }
    }

    @Test fun `should fail on incorrect test of string value length`() {
        val json = "\"Hello!\""
        shouldThrow<AssertionError>("JSON string length doesn't match - expected 5, was 6") {
            expectJSON(json) {
                value(length(5))
            }
        }
    }

    @Test fun `should test string value length as a range`() {
        val json = "\"Hello!\""
        expectJSON(json) {
            value(length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string value length as a range`() {
        val json = "\"Hello!\""
        shouldThrow<AssertionError>("JSON string length doesn't match - expected 1..5, was 6") {
            expectJSON(json) {
                value(length(1..5))
            }
        }
    }

    @Test fun `should test string property length`() {
        val json = """{"abc":"Hello!"}"""
        expectJSON(json) {
            property("abc", length(6))
        }
    }

    @Test fun `should fail on incorrect test of string property length`() {
        val json = """{"abc":"Hello!"}"""
        shouldThrow<AssertionError>("/abc: JSON string length doesn't match - expected 5, was 6") {
            expectJSON(json) {
                property("abc", length(5))
            }
        }
    }

    @Test fun `should test string property length as a range`() {
        val json = """{"abc":"Hello!"}"""
        expectJSON(json) {
            property("abc", length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string property length as a range`() {
        val json = """{"abc":"Hello!"}"""
        shouldThrow<AssertionError>("/abc: JSON string length doesn't match - expected 1..4, was 6") {
            expectJSON(json) {
                property("abc", length(1..4))
            }
        }
    }

    @Test fun `should test string array item length`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, length(6))
        }
    }

    @Test fun `should fail on incorrect test of string array item length`() {
        val json = """["Hello!"]"""
        shouldThrow<AssertionError>("/0: JSON string length doesn't match - expected 5, was 6") {
            expectJSON(json) {
                item(0, length(5))
            }
        }
    }

    @Test fun `should test string array item length as a range`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, length(4..10))
        }
    }

    @Test fun `should fail on incorrect test of string array item length as a range`() {
        val json = """["Hello!"]"""
        shouldThrow<AssertionError>("/0: JSON string length doesn't match - expected 1..4, was 6") {
            expectJSON(json) {
                item(0, length(1..4))
            }
        }
    }

    @Test fun `should fail with custom error message`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: Custom error message 123 /abc = 123") {
            expectJSON(json) {
                property("abc") {
                    if (node !is JSONString)
                        error("Custom error message ${showValue(node)} $pointer = ${showNode()}")
                }
            }
        }
    }

    @Test fun `should correctly access boolean node`() {
        val json = """{"abc":true}"""
        expectJSON(json) {
            property("abc") {
                if (!nodeAsBoolean)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to boolean node`() {
        val json = """{"abc":"xyz"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected boolean, was string") {
            expectJSON(json) {
                property("abc") {
                    if (!nodeAsBoolean)
                        error("Should not happen")
                }
            }
        }
    }

    @Test fun `should correctly access string node`() {
        val json = """{"abc":"xyz"}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsString != "xyz")
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to string node`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected string, was integer") {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsString != "xyz")
                        error("Should not happen")
                }
            }
        }
    }

    @Test fun `should correctly access nested object node`() {
        val json = """{"abc":{"xyz":0}}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsObject.size != 1)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to nested object node`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected object, was integer") {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsObject.size != 1)
                        error("Should not happen")
                }
            }
        }
    }

    @Test fun `should correctly access nested array node`() {
        val json = """{"abc":[0]}"""
        expectJSON(json) {
            property("abc") {
                if (nodeAsArray.size != 1)
                    error("Should not happen")
            }
        }
    }

    @Test fun `should fail on incorrect access to nested array node`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected array, was integer") {
            expectJSON(json) {
                property("abc") {
                    if (nodeAsArray.size != 1)
                        error("Should not happen")
                }
            }
        }
    }

    @Test fun `should test integer property as long`() {
        val json = """{"a":0}"""
        expectJSON(json) {
            property("a", 0L)
        }
    }

    @Test fun `should fail on incorrect test of integer property as long`() {
        val json = """{"a":1}"""
        shouldThrow<AssertionError>("/a: JSON value doesn't match - expected 0, was 1") {
            expectJSON(json) {
                property("a", 0L)
            }
        }
    }

    @Test fun `should test integer array item as long`() {
        val json = "[0]"
        expectJSON(json) {
            item(0, 0L)
        }
    }

    @Test fun `should fail on incorrect test of integer array item as long`() {
        val json = "[1]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 0, was 1") {
            expectJSON(json) {
                item(0, 0L)
            }
        }
    }

    @Test fun `should test integer value in long range`() {
        val json = "0"
        expectJSON(json) {
            value(0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer value in long range`() {
        val json = "0"
        shouldThrow<AssertionError>("JSON value not in range 1..123456789999 - 0") {
            expectJSON(json) {
                value(1L..123456789999)
            }
        }
    }

    @Test fun `should test integer property in long range`() {
        val json = """{"abc":27}"""
        expectJSON(json) {
            property("abc", 0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer property in long range`() {
        val json = """{"abc":-1}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range 0..123456789999 - -1") {
            expectJSON(json) {
                property("abc", 0L..123456789999)
            }
        }
    }

    @Test fun `should test integer array item in long range`() {
        val json = "[27]"
        expectJSON(json) {
            item(0, 0L..123456789999)
        }
    }

    @Test fun `should fail on incorrect test of integer array item in long range`() {
        val json = "[-1]"
        shouldThrow<AssertionError>("/0: JSON value not in range 0..123456789999 - -1") {
            expectJSON(json) {
                item(0, 0L..123456789999)
            }
        }
    }

    @Test fun `should test integer property as decimal`() {
        val json = """{"aaa":0}"""
        expectJSON(json) {
            property("aaa", BigDecimal.ZERO)
        }
    }

    @Test fun `should fail on incorrect test of integer property as decimal`() {
        val json = """{"aaa":1}"""
        shouldThrow<AssertionError>("/aaa: JSON value doesn't match - expected 0, was 1") {
            expectJSON(json) {
                property("aaa", BigDecimal.ZERO)
            }
        }
    }

    @Test fun `should test integer array item as decimal`() {
        val json = "[0]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO)
        }
    }

    @Test fun `should fail on incorrect test of integer array item as decimal`() {
        val json = "[1]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 0, was 1") {
            expectJSON(json) {
                item(0, BigDecimal.ZERO)
            }
        }
    }

    @Test fun `should test integer value in decimal range`() {
        val json = "44"
        expectJSON(json) {
            value(BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer value in decimal range`() {
        val json = "-1"
        shouldThrow<AssertionError>("JSON value not in range 0..999 - -1") {
            expectJSON(json) {
                value(BigDecimal.ZERO..BigDecimal(999))
            }
        }
    }

    @Test fun `should test integer property in decimal range`() {
        val json = """{"abcde":44}"""
        expectJSON(json) {
            property("abcde", BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer property in decimal range`() {
        val json = """{"abcde":-2}"""
        shouldThrow<AssertionError>("/abcde: JSON value not in range 0..999 - -2") {
            expectJSON(json) {
                property("abcde", BigDecimal.ZERO..BigDecimal(999))
            }
        }
    }

    @Test fun `should test integer array item in decimal range`() {
        val json = "[578]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO..BigDecimal(999))
        }
    }

    @Test fun `should fail on incorrect test of integer array item in decimal range`() {
        val json = "[-3]"
        shouldThrow<AssertionError>("/0: JSON value not in range 0..999 - -3") {
            expectJSON(json) {
                item(0, BigDecimal.ZERO..BigDecimal(999))
            }
        }
    }

    @Test fun `should test long integer property as decimal`() {
        val json = """{"aaa":123123123123}"""
        expectJSON(json) {
            property("aaa", BigDecimal(123123123123))
        }
    }

    @Test fun `should fail on incorrect test of long integer property as decimal`() {
        val json = """{"aaa":123123123123}"""
        shouldThrow<AssertionError>("/aaa: JSON value doesn't match - expected 0, was 123123123123") {
            expectJSON(json) {
                property("aaa", BigDecimal.ZERO)
            }
        }
    }

    @Test fun `should test long integer array item as decimal`() {
        val json = "[1234567812345678]"
        expectJSON(json) {
            item(0, BigDecimal(1234567812345678))
        }
    }

    @Test fun `should fail on incorrect test of long integer array item as decimal`() {
        val json = "[1234567812345678]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 0, was 1234567812345678") {
            expectJSON(json) {
                item(0, BigDecimal.ZERO)
            }
        }
    }

    @Test fun `should test long integer value in decimal range`() {
        val json = "9876543298765432"
        expectJSON(json) {
            value(BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer value in decimal range`() {
        val json = "9876543298765432"
        shouldThrow<AssertionError>("JSON value not in range 0..999999999999999 - 9876543298765432") {
            expectJSON(json) {
                value(BigDecimal.ZERO..BigDecimal(999999999999999))
            }
        }
    }

    @Test fun `should test long integer property in decimal range`() {
        val json = """{"abcde":1234567812345678}"""
        expectJSON(json) {
            property("abcde", BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer property in decimal range`() {
        val json = """{"abcde":-1234567812345678}"""
        shouldThrow<AssertionError>("/abcde: JSON value not in range 0..9999999999999999 - -1234567812345678") {
            expectJSON(json) {
                property("abcde", BigDecimal.ZERO..BigDecimal(9999999999999999))
            }
        }
    }

    @Test fun `should test long integer array item in decimal range`() {
        val json = "[1122334455667788]"
        expectJSON(json) {
            item(0, BigDecimal.ZERO..BigDecimal(9999999999999999))
        }
    }

    @Test fun `should fail on incorrect test of long integer array item in decimal range`() {
        val json = "[-1122334455667788]"
        shouldThrow<AssertionError>("/0: JSON value not in range 0..9999999999999999 - -1122334455667788") {
            expectJSON(json) {
                item(0, BigDecimal.ZERO..BigDecimal(9999999999999999))
            }
        }
    }

    @Test fun `should test scale of integer value as 0`() {
        val json = "1"
        expectJSON(json) {
            value(scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer value as 0`() {
        val json = "2"
        shouldThrow<AssertionError>("JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                value(scale(2))
            }
        }
    }

    @Test fun `should test scale of integer property as 0`() {
        val json = """{"abc":33}"""
        expectJSON(json) {
            property("abc", scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer property as 0`() {
        val json = """{"abc":33}"""
        shouldThrow<AssertionError>("/abc: JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                property("abc", scale(2))
            }
        }
    }

    @Test fun `should test scale of integer array item as 0`() {
        val json = "[2]"
        expectJSON(json) {
            item(0, scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of integer array item as 0`() {
        val json = "[2]"
        shouldThrow<AssertionError>("/0: JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                item(0, scale(2))
            }
        }
    }

    @Test fun `should test scale of long integer value as 0`() {
        val json = "1122334455667788"
        expectJSON(json) {
            value(scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer value as 0`() {
        val json = "1122334455667788"
        shouldThrow<AssertionError>("JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                value(scale(2))
            }
        }
    }

    @Test fun `should test scale of long integer property as 0`() {
        val json = """{"abc":1122334455667788}"""
        expectJSON(json) {
            property("abc", scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer property as 0`() {
        val json = """{"abc":1122334455667788}"""
        shouldThrow<AssertionError>("/abc: JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                property("abc", scale(2))
            }
        }
    }

    @Test fun `should test scale of long integer array item as 0`() {
        val json = "[1122334455667788]"
        expectJSON(json) {
            item(0, scale(0))
        }
    }

    @Test fun `should fail on incorrect test of scale of long integer array item as 0`() {
        val json = "[1122334455667788]"
        shouldThrow<AssertionError>("/0: JSON decimal scale doesn't match - expected 2, was 0") {
            expectJSON(json) {
                item(0, scale(2))
            }
        }
    }

}
