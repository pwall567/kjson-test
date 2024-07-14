/*
 * @(#) JSONExpectTest6.kt
 *
 * kjson-test Library for testing Kotlin JSON applications
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

import java.math.BigDecimal

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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                exhaustive {
                    property("abc", 1)
                    property("def", 2)
                }
            }
        }.let { expect("JSON object property not tested: ghi") { it.message } }
    }

    @Test fun `should fail when multiple object properties not tested`() {
        val json = """{"abc":1,"def":2,"ghi":3}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                exhaustive {
                    property("abc", 1)
                }
            }
        }.let { expect("JSON object properties not tested: def, ghi") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                exhaustive {
                    item(0, 111)
                    item(1, 222)
                }
            }
        }.let { expect("JSON array item not tested: 2") { it.message } }
    }

    @Test fun `should fail when multiple array items not tested`() {
        val json = """[111,222,333]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                exhaustive {
                    item(0, 111)
                }
            }
        }.let { expect("JSON array items not tested: 1, 2") { it.message } }
    }

    @Test fun `should fail when exhaustive check attempted on incorrect type`() {
        val json = """{"aaa":123}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("aaa") {
                    exhaustive {}
                }
            }
        }.let { expect("/aaa: JSON type doesn't match - expected object or array, was integer") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("aaa") {
                    anyItem(6)
                }
            }
        }.let { expect("/aaa: No JSON array item has value 6") { it.message } }
    }

    @Test fun `should test that any item has boolean value`() {
        val json = "[false,false,true]"
        expectJSON(json) {
            anyItem(true)
        }
    }

    @Test fun `should fail on incorrect test that any item has boolean value`() {
        val json = "[false,false,false]"
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItem(true)
            }
        }.let { expect("No JSON array item has value true") { it.message } }
    }

    @Test fun `should test that any item has string value`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem("alpha")
        }
    }

    @Test fun `should fail on incorrect test that any item has string value`() {
        val json = """["alpha","beta","gamma"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItem("delta")
            }
        }.let { expect("No JSON array item has value \"delta\"") { it.message } }
    }

    @Test fun `should test that any item matches a Regex`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem(Regex("^[a-z]+$"))
        }
    }

    @Test fun `should fail on incorrect test that any item matches a Regex`() {
        val json = """["alpha","beta","gamma"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItem(Regex("^[0-9]+$"))
            }
        }.let { expect("No JSON array item has value matching given Regex - ^[0-9]+\$") { it.message } }
    }

    @Test fun `should test that any item is in a Collection`() {
        val json = """["alpha","beta","gamma"]"""
        expectJSON(json) {
            anyItem(setOf("gamma", "mu", "omega"))
        }
    }

    @Test fun `should fail on incorrect test that any item is in a Collection`() {
        val json = """["alpha","beta","gamma"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItem(setOf("delta", "mu", "omega"))
            }
        }.let { expect("No JSON array item has value in given collection") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItem {
                    property("a", 2)
                }
            }
        }.let { expect("No JSON array item has value matching given tests") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItemIsObject {
                    property("a", 2)
                }
            }
        }.let { expect("No JSON array item has value matching given tests") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItemIsArray {
                    item(0, 4)
                }
            }
        }.let { expect("No JSON array item has value matching given tests") { it.message } }
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
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                anyItemIsArray(3)
            }
        }.let { expect("No JSON array item has value matching given tests") { it.message } }
    }

}
