/*
 * @(#) JSONExpectTest1.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2022, 2023, 2024, 2025 Peter Wall
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
import kotlin.test.fail

import java.math.BigDecimal

import io.kstuff.test.shouldBeNonNull
import io.kstuff.test.shouldStartWith
import io.kstuff.test.shouldThrow

import io.kjson.JSON
import io.kjson.JSONInt
import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest1 {

    @Test fun `should fail on invalid JSON`() {
        val json = "["
        shouldThrow<AssertionError> {
            expectJSON(json) {
                fail("Tests should not be executed")
            }
        }.let {
            it.message.shouldBeNonNull() shouldStartWith "String is not valid JSON: "
        }
    }

    @Test fun `should test string value`() {
        val json = "\"abc\""
        expectJSON(json) {
            value("abc")
        }
    }

    @Test fun `should test null value`() {
        val json = "null"
        expectJSON(json) {
            value(null)
        }
    }

    @Test fun `should test floating point value`() {
        val json = "0.01"
        expectJSON(json) {
            value(BigDecimal("0.01"))
        }
    }

    @Test fun `should test value is object`() {
        val json = "{}"
        expectJSON(json) {
            valueIsObject()
        }
    }

    @Test fun `should fail on incorrect test of value as object`() {
        val json = "[]"
        shouldThrow<AssertionError>("JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                valueIsObject()
            }
        }
    }

    @Test fun `should test value is object with nested tests`() {
        val json = """{"abc":1}"""
        expectJSON(json) {
            valueIsObject {
                property("abc", 1)
            }
        }
    }

    @Test fun `should fail on incorrect test of value as object with nested tests`() {
        val json = "[]"
        shouldThrow<AssertionError>("JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                valueIsObject {
                    property("abc", 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of value as object with failing nested tests`() {
        val json = """{"abc":1}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                valueIsObject {
                    property("abc", 2)
                }
            }
        }
    }

    @Test fun `should test value is array`() {
        val json = "[]"
        expectJSON(json) {
            valueIsArray()
        }
    }

    @Test fun `should fail on incorrect test of value as array`() {
        val json = "{}"
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                valueIsArray()
            }
        }
    }

    @Test fun `should test value is array with size`() {
        val json = "[]"
        expectJSON(json) {
            valueIsArray(size = 0)
        }
    }

    @Test fun `should fail on incorrect test of value as array with size`() {
        val json = "[0,0,0,0]"
        shouldThrow<AssertionError>("JSON array size doesn't match - Expected 3, was 4") {
            expectJSON(json) {
                valueIsArray(3)
            }
        }
    }

    @Test fun `should test value is array with nested tests`() {
        val json = "[99]"
        expectJSON(json) {
            valueIsArray(1) {
                item(0, 99)
            }
        }
    }

    @Test fun `should fail on incorrect test of value as array with nested tests`() {
        val json = "{}"
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                valueIsArray {
                    item(0, 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of value as array with failing nested tests`() {
        val json = "[1]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                valueIsArray {
                    item(0, 2)
                }
            }
        }
    }

    @Test fun `should test simple int property specified as separate value`() {
        val json = """{"abc":1}"""
        expectJSON(json) {
            property("abc") {
                value(1)
            }
        }
    }

    @Test fun `should fail on incorrect test of simple property specified as separate value`() {
        val json = """{"abc":1}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                property("abc") {
                    value(2)
                }
            }
        }
    }

    @Test fun `should test multiple properties`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should test number of properties`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            count(2)
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should fail on incorrect test of number of properties`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON count doesn't match - expected 3, was 2") {
            expectJSON(json) {
                count(3)
            }
        }
    }

    @Test fun `should test number of properties as range`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            count(1..2)
            property("abc", 1)
            property("def", -8)
        }
    }

    @Test fun `should fail on incorrect test of number of properties as range`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON count doesn't match - expected 3..5, was 2") {
            expectJSON(json) {
                count(3..5)
            }
        }
    }

    @Test fun `should test property is object`() {
        val json = """{"abc":{}}"""
        expectJSON(json) {
            propertyIsObject("abc")
        }
    }

    @Test fun `should fail on incorrect test of property as object`() {
        val json = """{"abc":[]}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                propertyIsObject("abc")
            }
        }
    }

    @Test fun `should test property is object with nested tests`() {
        val json = """{"abc":{"def":1}}"""
        expectJSON(json) {
            propertyIsObject("abc") {
                property("def", 1)
            }
        }
    }

    @Test fun `should fail on incorrect test of property as object with nested tests`() {
        val json = """{"abc":[]}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                propertyIsObject("abc") {
                    property("def", 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of property as object with failing nested tests`() {
        val json = """{"abc":{"def":1}}"""
        shouldThrow<AssertionError>("/abc/def: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                propertyIsObject("abc") {
                    property("def", 2)
                }
            }
        }
    }

    @Test fun `should test property is array`() {
        val json = """{"abc":[]}"""
        expectJSON(json) {
            propertyIsArray("abc")
        }
    }

    @Test fun `should fail on incorrect test of property as array`() {
        val json = """{"abc":{}}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                propertyIsArray("abc")
            }
        }
    }

    @Test fun `should test property is array with size`() {
        val json = """{"abc":[true]}"""
        expectJSON(json) {
            propertyIsArray("abc", 1)
        }
    }

    @Test fun `should fail on incorrect test of property as array with size`() {
        val json = """{"abc":[0]}"""
        shouldThrow<AssertionError>("/abc: JSON array size doesn't match - Expected 0, was 1") {
            expectJSON(json) {
                propertyIsArray("abc", 0)
            }
        }
    }

    @Test fun `should test property is array with nested tests`() {
        val json = """{"abc":[1]}"""
        expectJSON(json) {
            propertyIsArray("abc", 1) {
                item(0, 1)
            }
        }
    }

    @Test fun `should fail on incorrect test of property as array with nested tests`() {
        val json = """{"abc":{}}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                propertyIsArray("abc") {
                    item(0, 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of property as array with failing nested tests`() {
        val json = """{"abc":[1]}"""
        shouldThrow<AssertionError>("/abc/0: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                propertyIsArray("abc") {
                    item(0, 2)
                }
            }
        }
    }

    @Test fun `should test string array item`() {
        val json = """["Hello!"]"""
        expectJSON(json) {
            item(0, "Hello!")
        }
    }

    @Test fun `should test null array item`() {
        val json = "[null]"
        expectJSON(json) {
            item(0, null)
        }
    }

    @Test fun `should test multiple int array items`() {
        val json = "[12345,-27]"
        expectJSON(json) {
            item(0, 12345)
            item(1, -27)
        }
    }

    @Test fun `should fail on incorrect test of multiple int array items`() {
        val json = "[12345,-27]"
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected -28, was -27") {
            expectJSON(json) {
                item(0, 12345)
                item(1, -28)
            }
        }
    }

    @Test fun `should test nested int array items`() {
        val json = "[[12345,-27],[44,55]]"
        expectJSON(json) {
            item(0) {
                item(0, 12345)
                item(1, -27)
            }
            item(1) {
                item(0, 44)
                item(1, 55)
            }
        }
    }

    @Test fun `should fail on incorrect test of nested int array items`() {
        val json = "[[12345,-27],[44,55]]"
        shouldThrow<AssertionError>("/1/0: JSON value doesn't match - expected 45, was 44") {
            expectJSON(json) {
                item(0) {
                    item(0, 12345)
                    item(1, -27)
                }
                item(1) {
                    item(0, 45)
                    item(1, 55)
                }
            }
        }
    }

    @Test fun `should test doubly nested int array items`() {
        val json = "[[[12345,-27],[44,55]]]"
        expectJSON(json) {
            item(0) {
                item(0) {
                    item(0, 12345)
                    item(1, -27)
                }
                item(1) {
                    item(0, 44)
                    item(1, 55)
                }
            }
        }
    }

    @Test fun `should test array item is object`() {
        val json = "[{}]"
        expectJSON(json) {
            itemIsObject(0)
        }
    }

    @Test fun `should fail on incorrect test of array item as object`() {
        val json = "[[]]"
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                itemIsObject(0)
            }
        }
    }

    @Test fun `should test array item is object with nested tests`() {
        val json = """[{"def":1}]"""
        expectJSON(json) {
            itemIsObject(0) {
                property("def", 1)
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as object with nested tests`() {
        val json = "[[]]"
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected object, was array") {
            expectJSON(json) {
                itemIsObject(0) {
                    property("def", 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as object with failing nested tests`() {
        val json = """[{"def":1}]"""
        shouldThrow<AssertionError>("/0/def: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                itemIsObject(0) {
                    property("def", 2)
                }
            }
        }
    }

    @Test fun `should test array item is array`() {
        val json = "[[123]]"
        expectJSON(json) {
            itemIsArray(0, size = 1)
        }
    }

    @Test fun `should fail on incorrect test of array item as array`() {
        val json = "[{}]"
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                itemIsArray(0)
            }
        }
    }

    @Test fun `should test array item is array with size`() {
        val json = "[[]]"
        expectJSON(json) {
            itemIsArray(0, size = 0)
        }
    }

    @Test fun `should fail on incorrect test of array item as array with size`() {
        val json = "[[1,2,3]]"
        shouldThrow<AssertionError>("/0: JSON array size doesn't match - Expected 1, was 3") {
            expectJSON(json) {
                itemIsArray(0, size = 1)
            }
        }
    }

    @Test fun `should test array item is array with nested tests`() {
        val json = "[[1]]"
        expectJSON(json) {
            itemIsArray(0) {
                item(0, 1)
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as array with nested tests`() {
        val json = "[{}]"
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected array, was object") {
            expectJSON(json) {
                itemIsArray(0) {
                    item(0, 1)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of array item as array with failing nested tests`() {
        val json = "[[1]]"
        shouldThrow<AssertionError>("/0/0: JSON value doesn't match - expected 2, was 1") {
            expectJSON(json) {
                itemIsArray(0) {
                    item(0, 2)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of doubly nested int array items`() {
        val json = "[[[12345,-27],[44,55]]]"
        shouldThrow<AssertionError>("/0/1/0: JSON value doesn't match - expected 45, was 44") {
            expectJSON(json) {
                item(0) {
                    item(0) {
                        item(0, 12345)
                        item(1, -27)
                    }
                    item(1) {
                        item(0, 45)
                        item(1, 55)
                    }
                }
            }
        }
    }

    @Test fun `should test nested property`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                property("field", 99)
            }
        }
    }

    @Test fun `should fail on incorrect test of nested property`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer/field: JSON value doesn't match - expected 98, was 99") {
            expectJSON(json) {
                property("outer") {
                    property("field", 98)
                }
            }
        }
    }

    @Test fun `should test doubly nested property`() {
        val json = """{"outer":{"middle":{"field":99}}}"""
        expectJSON(json) {
            property("outer") {
                property("middle") {
                    property("field", 99)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test of doubly nested property`() {
        val json = """{"outer":{"middle":{"field":99}}}"""
        shouldThrow<AssertionError>("/outer/middle/field: JSON value doesn't match - expected 98, was 99") {
            expectJSON(json) {
                property("outer") {
                    property("middle") {
                        property("field", 98)
                    }
                }
            }
        }
    }

    @Test fun `should test property nested in array`() {
        val json = """[{"field":99}]"""
        expectJSON(json) {
            item(0) {
                property("field", 99)
            }
        }
    }

    @Test fun `should fail on incorrect test of property nested in array`() {
        val json = """[{"field":99}]"""
        shouldThrow<AssertionError>("/0/field: JSON value doesn't match - expected 98, was 99") {
            expectJSON(json) {
                item(0) {
                    property("field", 98)
                }
            }
        }
    }

    @Test fun `should test array item nested in property`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                item(0, 1)
                item(1, 2)
                item(2, 3)
                item(3, 5)
                item(4, 7)
                item(5, 11)
                item(6, 13)
                item(7, 17)
                item(8, 19)
                item(9, 23)
            }
        }
    }

    @Test fun `should test array item nested in property using items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                items(1, 2, 3, 5, 7, 11, 13, 17, 19, 23)
            }
        }
    }

    @Test fun `should fail on test of array item nested in property using items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        shouldThrow<AssertionError>("/primes/8: JSON value doesn't match - expected 23, was 19") {
            expectJSON(json) {
                property("primes") {
                    items(1, 2, 3, 5, 7, 11, 13, 17, 23, 29)
                }
            }
        }
    }

    @Test fun `should fail on test of array item nested in property`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        shouldThrow<AssertionError>("/primes/9: JSON value doesn't match - expected 27, was 23") {
            expectJSON(json) {
                property("primes") {
                    item(9, 27)
                }
            }
        }
    }

    @Test fun `should fail on array index out of bounds`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        shouldThrow<AssertionError>("/primes: JSON array index out of bounds - 10") {
            expectJSON(json) {
                property("primes") {
                    item(10, 27)
                }
            }
        }
    }

    @Test fun `should test count of array items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                count(10)
                item(9, 23)
            }
        }
    }

    @Test fun `should fail on test of count of array items`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        shouldThrow<AssertionError>("/primes: JSON count doesn't match - expected 9, was 10") {
            expectJSON(json) {
                property("primes") {
                    count(9)
                }
            }
        }
    }

    @Test fun `should test count of array items as range`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        expectJSON(json) {
            property("primes") {
                count(8..12)
                item(9, 23)
            }
        }
    }

    @Test fun `should fail on test of count of array items as range`() {
        val json = """{"primes":[1,2,3,5,7,11,13,17,19,23]}"""
        shouldThrow<AssertionError>("/primes: JSON count doesn't match - expected 2..9, was 10") {
            expectJSON(json) {
                property("primes") {
                    count(2..9)
                }
            }
        }
    }

    @Test fun `should test value using JSONInt`() {
        val json = "27"
        json shouldMatchJSON {
            value(JSONInt(27))
        }
    }

    @Test fun `should report error on test of value using JSONInt`() {
        val json = "26"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 27, was 26") {
            json shouldMatchJSON {
                value(JSONInt(27))
            }
        }
    }

    @Test fun `should test property using JSONInt`() {
        val json = """{"field1":27}"""
        json shouldMatchJSON {
            property("field1", JSONInt(27))
        }
    }

    @Test fun `should report error on test of property using JSONInt`() {
        val json = """{"field1":26}"""
        shouldThrow<AssertionError>("/field1: JSON value doesn't match - expected 27, was 26") {
            json shouldMatchJSON {
                property("field1", JSONInt(27))
            }
        }
    }

    @Test fun `should test array item using JSONInt`() {
        val json = "[1,2,27]"
        json shouldMatchJSON {
            item(2, JSONInt(27))
        }
    }

    @Test fun `should report error on test of array item using JSONInt`() {
        val json = "[1,2,26]"
        shouldThrow<AssertionError>("/2: JSON value doesn't match - expected 27, was 26") {
            json shouldMatchJSON {
                item(2, JSONInt(27))
            }
        }
    }

    @Test fun `should test any array item using JSONInt`() {
        val json = "[1,2,27,3]"
        json shouldMatchJSON {
            anyItem(JSONInt(27))
        }
    }

    @Test fun `should report error on test of any array item using JSONInt`() {
        val json = "[1,2,26]"
        shouldThrow<AssertionError>("No JSON array item has value 27") {
            json shouldMatchJSON {
                anyItem(JSONInt(27))
            }
        }
    }

    @Test fun `should test property using complex JSON`() {
        val json = """{"field1":{"alpha":111,"beta":222}}"""
        json shouldMatchJSON {
            property("field1", JSON.parseObject("""{"alpha":111,"beta":222}"""))
        }
    }

    @Test fun `should report error on test of property using complex JSON`() {
        val json = """{"field1":{"alpha":111,"beta":333}}"""
        shouldThrow<AssertionError>("/field1/beta: JSON value doesn't match - expected 222, was 333") {
            json shouldMatchJSON {
                property("field1", JSON.parseObject("""{"alpha":111,"beta":222}"""))
            }
        }
    }

}
