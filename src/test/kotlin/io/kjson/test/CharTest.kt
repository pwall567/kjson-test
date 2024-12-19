/*
 * @(#) CharTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2024 Peter Wall
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

class CharTest {

    @Test fun `should test Char value`() {
        val json = "\"A\""
        JSONExpect.expectJSON(json) {
            value('A')
        }
    }

    @Test fun `should fail on incorrect Char value`() {
        val json = "\"A\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"B\", was \"A\"") {
            JSONExpect.expectJSON(json) {
                value('B')
            }
        }
    }

    @Test fun `should fail when Char value not string of length 1`() {
        val json = "\"ABC\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"A\", was \"ABC\"") {
            JSONExpect.expectJSON(json) {
                value('A')
            }
        }
    }

    @Test fun `should test Char property`() {
        val json = """{"abc":"A"}"""
        JSONExpect.expectJSON(json) {
            property("abc", 'A')
        }
    }

    @Test fun `should fail on incorrect Char property`() {
        val json = """{"abc":"A"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"B\", was \"A\"") {
            JSONExpect.expectJSON(json) {
                property("abc", 'B')
            }
        }
    }

    @Test fun `should test Char array item`() {
        val json = """["Q"]"""
        JSONExpect.expectJSON(json) {
            item(0, 'Q')
        }
    }

    @Test fun `should fail on incorrect Char array item`() {
        val json = """["X"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"Q\", was \"X\"") {
            JSONExpect.expectJSON(json) {
                item(0, 'Q')
            }
        }
    }

    @Test fun `should test that any item has Char value`() {
        val json = """["A", "B", "C"]"""
        JSONExpect.expectJSON(json) {
            anyItem('C')
        }
    }

    @Test fun `should fail on incorrect test that any item has Char value`() {
        val json = """["A", "B", "C"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"Q\"") {
            JSONExpect.expectJSON(json) {
                anyItem('Q')
            }
        }
    }

}
