/*
 * @(#) BooleanTest.kt
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

class BooleanTest {

    @Test fun `should test boolean value`() {
        val json = "true"
        JSONExpect.expectJSON(json) {
            value(true)
        }
    }

    @Test fun `should fail on incorrect test of boolean value`() {
        val json = "true"
        shouldThrow<AssertionError>("JSON value doesn't match - expected false, was true") {
            JSONExpect.expectJSON(json) {
                value(false)
            }
        }
    }

    @Test fun `should test simple boolean property`() {
        val json = """{"abc":true}"""
        JSONExpect.expectJSON(json) {
            property("abc", true)
        }
    }

    @Test fun `should fail on incorrect test of simple boolean property`() {
        val json = """{"abc":true}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected false, was true") {
            JSONExpect.expectJSON(json) {
                property("abc", false)
            }
        }
    }

    @Test fun `should test boolean array item`() {
        val json = "[true]"
        JSONExpect.expectJSON(json) {
            item(0, true)
        }
    }

    @Test fun `should fail on incorrect test of boolean array item`() {
        val json = "[true]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected false, was true") {
            JSONExpect.expectJSON(json) {
                item(0, false)
            }
        }
    }

    @Test fun `should test that any item has boolean value`() {
        val json = "[false,false,true]"
        JSONExpect.expectJSON(json) {
            anyItem(true)
        }
    }

    @Test fun `should fail on incorrect test that any item has boolean value`() {
        val json = "[false,false,false]"
        shouldThrow<AssertionError>("No JSON array item has value true") {
            JSONExpect.expectJSON(json) {
                anyItem(true)
            }
        }
    }

    @Test fun `should test multiple boolean values`() {
        val json = "[true,false,true]"
        JSONExpect.expectJSON(json) {
            items(true, false, true)
        }
    }

    @Test fun `should fail on incorrect multiple boolean values`() {
        val json = "[true,false,true]"
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected true, was false") {
            JSONExpect.expectJSON(json) {
                items(true, true, false)
            }
        }
    }

    @Test fun `should fail on incorrect number of multiple boolean values`() {
        val json = "[true,false,true]"
        shouldThrow<AssertionError>("JSON array size not the same as number of values - expected 2, was 3") {
            JSONExpect.expectJSON(json) {
                items(true, false)
            }
        }
    }

    @Test fun `should fail on multiple boolean values applied to string`() {
        val json = "\"abc\""
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was string") {
            JSONExpect.expectJSON(json) {
                items(true, false, true)
            }
        }
    }

}
