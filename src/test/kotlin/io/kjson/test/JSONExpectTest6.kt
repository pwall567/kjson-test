/*
 * @(#) JSONExpectTest6.kt
 *
 * kjson-test Library for testing Kotlin JSON applications
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

import io.kstuff.test.shouldThrow

import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest6 {

    @Test fun `should check all object properties tested`() {
        val json = """{"abc":1,"def":2,"ghi":3}"""
        expectJSON(json) {
            exhaustive {
                property("abc", 1)
                property("def", 2)
                property("ghi", 3)
            }
        }
    }

    @Test fun `should allow exhaustive check on empty object`() {
        val json = "{}"
        expectJSON(json) {
            exhaustive {}
        }
    }

    @Test fun `should fail when not all object properties tested`() {
        val json = """{"abc":1,"def":2,"ghi":3}"""
        shouldThrow<AssertionError>("JSON object property not tested: ghi") {
            expectJSON(json) {
                exhaustive {
                    property("abc", 1)
                    property("def", 2)
                }
            }
        }
    }

    @Test fun `should fail when multiple object properties not tested`() {
        val json = """{"abc":1,"def":2,"ghi":3}"""
        shouldThrow<AssertionError>("JSON object properties not tested: def, ghi") {
            expectJSON(json) {
                exhaustive {
                    property("abc", 1)
                }
            }
        }
    }

    @Test fun `should check all array items tested`() {
        val json = """[111,222,333]"""
        expectJSON(json) {
            exhaustive {
                item(0, 111)
                item(1, 222)
                item(2, 333)
            }
        }
    }

    @Test fun `should allow exhaustive check on empty array`() {
        val json = "[]"
        expectJSON(json) {
            exhaustive {}
        }
    }

    @Test fun `should fail when not all array items tested`() {
        val json = """[111,222,333]"""
        shouldThrow<AssertionError>("JSON array item not tested: 2") {
            expectJSON(json) {
                exhaustive {
                    item(0, 111)
                    item(1, 222)
                }
            }
        }
    }

    @Test fun `should fail when multiple array items not tested`() {
        val json = """[111,222,333]"""
        shouldThrow<AssertionError>("JSON array items not tested: 1, 2") {
            expectJSON(json) {
                exhaustive {
                    item(0, 111)
                }
            }
        }
    }

    @Test fun `should fail when exhaustive check attempted on incorrect type`() {
        val json = """{"aaa":123}"""
        shouldThrow<AssertionError>("/aaa: JSON type doesn't match - expected object or array, was integer") {
            expectJSON(json) {
                property("aaa") {
                    exhaustive {}
                }
            }
        }
    }

    @Test fun `should test that any item of nested array has integer value`() {
        val json = """{"aaa":[7,8,9]}"""
        expectJSON(json) {
            property("aaa") {
                anyItem(8)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item of nested array has integer value`() {
        val json = """{"aaa":[7,8,9]}"""
        shouldThrow<AssertionError>("/aaa: No JSON array item has value 6") {
            expectJSON(json) {
                property("aaa") {
                    anyItem(6)
                }
            }
        }
    }

    @Test fun `should test that any item has boolean value`() {
        val json = "[false,false,true]"
        expectJSON(json) {
            anyItem(true)
        }
    }

    @Test fun `should fail on incorrect test that any item has boolean value`() {
        val json = "[false,false,false]"
        shouldThrow<AssertionError>("No JSON array item has value true") {
            expectJSON(json) {
                anyItem(true)
            }
        }
    }

    @Test fun `should test that any item has string value`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem("alpha")
        }
    }

    @Test fun `should fail on incorrect test that any item has string value`() {
        val json = """["alpha","beta","gamma"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"delta\"") {
            expectJSON(json) {
                anyItem("delta")
            }
        }
    }

    @Test fun `should test that any item matches a Regex`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem(Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test that any item matches a Regex`() {
        val json = """["alpha","beta","gamma"]"""
        shouldThrow<AssertionError>("No JSON array item has value matching given Regex - ^[0-9]+\$") {
            expectJSON(json) {
                anyItem(Regex("^[0-9]+$"))
            }
        }
    }

    @Test fun `should test that any item is in a Collection`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem(setOf("gamma", "mu", "omega"))
        }
    }

    @Test fun `should fail on incorrect test that any item is in a Collection`() {
        val json = """["alpha","beta","gamma"]"""
        shouldThrow<AssertionError>("No JSON array item has value in given collection") {
            expectJSON(json) {
                anyItem(setOf("delta", "mu", "omega"))
            }
        }
    }

    @Test fun `should test that any item has complex value`() {
        val json = """[{"a":1},{"b":2}]"""
        expectJSON(json) {
            anyItem {
                property("a", 1)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item has complex value`() {
        val json = """[{"a":1},{"b":2}]"""
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            expectJSON(json) {
                anyItem {
                    property("a", 2)
                }
            }
        }
    }

    @Test fun `should test that any item is an object with further tests`() {
        val json = """[{"a":1},{"b":2}]"""
        expectJSON(json) {
            anyItemIsObject {
                property("a", 1)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item is an object with further tests`() {
        val json = """[{"a":1},{"b":2}]"""
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            expectJSON(json) {
                anyItemIsObject {
                    property("a", 2)
                }
            }
        }
    }

    @Test fun `should test that any item is an array with further tests`() {
        val json = "[[1,2],[3,4]]"
        expectJSON(json) {
            anyItemIsArray {
                item(0, 1)
                item(1, 2)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item is an array with further tests`() {
        val json = "[[1,2],[3,4]]"
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            expectJSON(json) {
                anyItemIsArray {
                    item(0, 4)
                }
            }
        }
    }

    @Test fun `should test that any item is an array of a given size with further tests`() {
        val json = "[[1,2],[3,4]]"
        expectJSON(json) {
            anyItemIsArray(2) {
                item(0, 1)
                item(1, 2)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item is an array of a given size`() {
        val json = "[[1,2],[3,4]]"
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            expectJSON(json) {
                anyItemIsArray(3)
            }
        }
    }

}
