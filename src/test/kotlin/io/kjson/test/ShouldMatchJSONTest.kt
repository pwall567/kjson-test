/*
 * @(#) ShouldMatchJSONTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2025 Peter Wall
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

class ShouldMatchJSONTest {

    @Test fun `should perform tests using shouldMatchJSON syntax`() {
        val json = """{"abc":[1]}"""
        json shouldMatchJSON {
            propertyIsArray("abc", 1) {
                item(0, 1)
            }
        }
    }

    @Test fun `should match simple JSON`() {
        """{"def":"Hello!","abc":123}""" shouldMatchJSON expected
    }

    @Test fun `should report error on simple JSON`() {
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 123, was 456") {
            """{"def":"Hello!","abc":456}""" shouldMatchJSON expected
        }
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected integer, was string") {
            """{"def":"Hello!","abc":"wrong"}""" shouldMatchJSON expected
        }
        shouldThrow<AssertionError>("JSON type doesn't match - expected object, was array") {
            "[]" shouldMatchJSON expected
        }
    }

    @Test fun `should report missing properties`() {
        shouldThrow<AssertionError>("JSON object property missing - abc") {
            """{"def":"Hello!"}""" shouldMatchJSON expected
        }
        shouldThrow<AssertionError>("JSON object properties missing - abc, def") {
            "{}" shouldMatchJSON expected
        }
    }

    @Test fun `should report extra properties`() {
        shouldThrow<AssertionError>("JSON object property unexpected - wrong1") {
            """{"def":"Hello!","abc":123,"wrong1":"bad"}""" shouldMatchJSON expected
        }
        shouldThrow<AssertionError>("JSON object properties unexpected - wrong1, wrong2") {
            """{"def":"Hello!","abc":123,"wrong1":"bad","wrong2":"worse"}""" shouldMatchJSON expected
        }
    }

    @Test fun `should match JSON array`() {
        "[1,2,3,4,5]" shouldMatchJSON "[1, 2, 3, 4, 5]"
        "[true,false,true]" shouldMatchJSON "[ true, false, true ]"
        """["alpha","beta"]""" shouldMatchJSON """[ "alpha", "beta" ]"""
    }

    @Test fun `should report error on JSON array`() {
        shouldThrow<AssertionError>("/4: JSON value doesn't match - expected 6, was 5") {
            "[1,2,3,4,5]" shouldMatchJSON "[1, 2, 3, 4, 6]"
        }
        shouldThrow<AssertionError>("/2: JSON value doesn't match - expected false, was true") {
            "[true,false,true]" shouldMatchJSON "[ true, false, false ]"
        }
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected \"gamma\", was \"beta\"") {
            """["alpha","beta"]""" shouldMatchJSON """[ "alpha", "gamma" ]"""
        }
    }

    @Test fun `should report error on incorrect JSON array length`() {
        shouldThrow<AssertionError>("JSON array size not the same as number of values - expected 3, was 2") {
            """["alpha","beta"]""" shouldMatchJSON """[ "alpha", "beta", "gamma" ]"""
        }
    }

    @Test fun `should match more complex JSON`() {
        val json =
            """{"number":1001,"name":"Fred Smith","address":["55 Main St","Nowhere"],"balance":100.50,"active":true}"""
        json shouldMatchJSON expected2
    }

    @Test fun `should report error on more complex JSON`() {
        val json1 =
            """{"number":101,"name":"Fred Smith","address":["55 Main St","Nowhere"],"balance":100.50,"active":true}"""
        shouldThrow<AssertionError>("/number: JSON value doesn't match - expected 1001, was 101") {
            json1 shouldMatchJSON expected2
        }
        val json2 =
            """{"number":1001,"name":"Fred Jones","address":["55 Main St","Nowhere"],"balance":100.50,"active":true}"""
        shouldThrow<AssertionError>("/name: JSON value doesn't match - expected \"Fred Smith\", was \"Fred Jones\"") {
            json2 shouldMatchJSON expected2
        }
        val json3 =
            """{"number":1001,"name":"Fred Smith","address":["55 Main St","Here"],"balance":100.50,"active":true}"""
        shouldThrow<AssertionError>("/address/1: JSON value doesn't match - expected \"Nowhere\", was \"Here\"") {
            json3 shouldMatchJSON expected2
        }
        val json4 =
            """{"number":1001,"name":"Fred Smith","address":["55 Main St","Nowhere"],"balance":100.50,"active":false}"""
        shouldThrow<AssertionError>("/active: JSON value doesn't match - expected true, was false") {
            json4 shouldMatchJSON expected2
        }
        val json5 =
            """{"number":1001,"name":"Fred Smith","address":["55 Main St","Nowhere"],"balance":99.50,"active":true}"""
        shouldThrow<AssertionError>("/balance: JSON value doesn't match - expected 100.50, was 99.50") {
            json5 shouldMatchJSON expected2
        }
    }

    @Suppress("ConstPropertyName")
    companion object {

        const val expected = """
{
  "abc": 123,
  "def": "Hello!"
}
"""

        const val expected2 = """
{
  "number": 1001,
  "name": "Fred Smith",
  "address": [
    "55 Main St",
    "Nowhere"
  ],
  "active": true,
  "balance": 100.50
}
"""

    }

}
