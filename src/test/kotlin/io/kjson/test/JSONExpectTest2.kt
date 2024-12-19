/*
 * @(#) JSONExpectTest2.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2022, 2023, 2024 Peter Wall
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

class JSONExpectTest2 {

    @Test
    fun `should fail on test of missing property`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property missing - ghi") {
            expectJSON(json) {
                property("abc", 1)
                property("ghi", 9)
            }
        }
    }

    @Test fun `should test for property absent`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyAbsent("ghi")
        }
    }

    @Test fun `should test for property absent or null 1`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyAbsentOrNull("ghi")
        }
    }

    @Test fun `should test for property absent or null 2`() {
        val json = """{"abc":1,"def":-8,"ghi":null}"""
        expectJSON(json) {
            propertyAbsentOrNull("ghi")
        }
    }

    @Test fun `should test for property present`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            propertyPresent("def")
        }
    }

    @Test fun `should test for property non-null`() {
        val json = """{"abc":1,"def":-8}"""
        expectJSON(json) {
            property("def", isNonNull)
        }
    }

    @Test fun `should test that property is null using lambda`() {
        val json = """{"abc":1,"def":null}"""
        expectJSON(json) {
            property("def", isNull)
        }
    }

    @Test fun `should fail on incorrect test for property absent`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property not absent - def") {
            expectJSON(json) {
                propertyAbsent("def")
            }
        }
    }

    @Test fun `should fail on incorrect test for property absent or null`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property not absent or null - def") {
            expectJSON(json) {
                propertyAbsentOrNull("def")
            }
        }
    }

    @Test fun `should fail on incorrect test for property present`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property not present - ghi") {
            expectJSON(json) {
                propertyPresent("ghi")
            }
        }
    }

    @Test fun `should fail on incorrect test for property non-null 1`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property missing - ghi") {
            expectJSON(json) {
                property("ghi", isNonNull)
            }
        }
    }

    @Test fun `should fail on incorrect test for property non-null 2`() {
        val json = """{"abc":1,"def":-8,"ghi":null}"""
        shouldThrow<AssertionError>("/ghi: JSON item is null") {
            expectJSON(json) {
                property("ghi", isNonNull)
            }
        }
    }

    @Test fun `should fail on incorrect test that property is null using lambda 1`() {
        val json = """{"abc":1,"def":-8}"""
        shouldThrow<AssertionError>("JSON property missing - ghi") {
            expectJSON(json) {
                property("ghi", isNull)
            }
        }
    }

    @Test fun `should fail on incorrect test that property is null using lambda 2`() {
        val json = """{"abc":1,"def":-8,"ghi":""}"""
        shouldThrow<AssertionError>("/ghi: JSON item is not null - \"\"") {
            expectJSON(json) {
                property("ghi", isNull)
            }
        }
    }

    @Test fun `should test for nested property absent`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyAbsent("missing")
            }
        }
    }

    @Test fun `should test for nested property absent or null`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyAbsentOrNull("missing")
            }
        }
    }

    @Test fun `should test for nested property present`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                propertyPresent("field")
            }
        }
    }

    @Test fun `should test for nested property non-null`() {
        val json = """{"outer":{"field":99}}"""
        expectJSON(json) {
            property("outer") {
                property("field", isNonNull)
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property absent`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON property not absent - field") {
            expectJSON(json) {
                property("outer") {
                    propertyAbsent("field")
                }
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property absent or null`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON property not absent or null - field") {
            expectJSON(json) {
                property("outer") {
                    propertyAbsentOrNull("field")
                }
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property present`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON property not present - missing") {
            expectJSON(json) {
                property("outer") {
                    propertyPresent("missing")
                }
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property non-null 1`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON property missing - other") {
            expectJSON(json) {
                property("outer") {
                    property("other", isNonNull)
                }
            }
        }
    }

    @Test fun `should fail on incorrect test for nested property non-null 2`() {
        val json = """{"outer":{"field":99,"other":null}}"""
        shouldThrow<AssertionError>("/outer/other: JSON item is null") {
            expectJSON(json) {
                property("outer") {
                    property("other", isNonNull)
                }
            }
        }
    }

    @Test fun `should fail when comparing object as value`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON type doesn't match - expected string, was object") {
            expectJSON(json) {
                property("outer", """{"field":99}""")
            }
        }
    }

    @Test fun `should quote strings in match error message`() {
        val json = """["un","deux","trois"]"""
        shouldThrow<AssertionError>("""/1: JSON value doesn't match - expected "un", was "deux"""") {
            expectJSON(json) {
                item(1, "un")
            }
        }
    }

    @Test fun `should fail when comparing null to object`() {
        val json = """{"outer":{"field":99}}"""
        shouldThrow<AssertionError>("/outer: JSON type doesn't match - expected null, was object") {
            expectJSON(json) {
                property("outer", null)
            }
        }
    }

    @Test fun `should fail when comparing null to array`() {
        val json = """{"outer":[1,2,3]}"""
        shouldThrow<AssertionError>("/outer: JSON type doesn't match - expected null, was array") {
            expectJSON(json) {
                property("outer", null)
            }
        }
    }

    @Test fun `should fail when comparing null to string`() {
        val json = """{"abc":"Hello"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected null, was string") {
            expectJSON(json) {
                property("abc", null)
            }
        }
    }

    @Test fun `should fail when comparing null to integer`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected null, was integer") {
            expectJSON(json) {
                property("abc", null)
            }
        }
    }

    @Test fun `should fail when comparing null to long`() {
        val json = """{"abc":123456789123456789}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected null, was long integer") {
            expectJSON(json) {
                property("abc", null)
            }
        }
    }

    @Test fun `should fail when comparing string to null`() {
        val json = """{"abc":null}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected string, was null") {
            expectJSON(json) {
                property("abc", "Hello")
            }
        }
    }

    @Test fun `should fail when comparing integer to null`() {
        val json = """{"abc":null}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected integer, was null") {
            expectJSON(json) {
                property("abc", 123)
            }
        }
    }

    @Test fun `should fail when comparing integer to string`() {
        val json = """{"abc":"Hello"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected integer, was string") {
            expectJSON(json) {
                property("abc", 123)
            }
        }
    }

    @Test fun `should fail when comparing string to integer`() {
        val json = """{"abc":123}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected string, was integer") {
            expectJSON(json) {
                property("abc", "Hello")
            }
        }
    }

    @Test fun `should test null as member of a collection of int`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of int`() {
        val json = "null"
        shouldThrow<AssertionError>("JSON value not in collection - null") {
            expectJSON(json) {
                value(setOf(123, 456, 789))
            }
        }
    }

    @Test fun `should test null as member of a collection of long`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(123456789123456789, 0L, null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of long`() {
        val json = "null"
        shouldThrow<AssertionError>("JSON value not in collection - null") {
            expectJSON(json) {
                value(setOf(123456789123456789, 0L))
            }
        }
    }

    @Test fun `should test null as member of a collection of decimal`() {
        val json = "null"
        expectJSON(json) {
            value(setOf(BigDecimal("9.99"), BigDecimal("19.99"), null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of decimal`() {
        val json = "null"
        shouldThrow<AssertionError>("JSON value not in collection - null") {
            expectJSON(json) {
                value(setOf(BigDecimal("9.99"), BigDecimal("19.99")))
            }
        }
    }

    @Test fun `should test string as member of a collection`() {
        val json = "\"abc\""
        expectJSON(json) {
            value(setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string as member of a collection`() {
        val json = "\"abcd\""
        shouldThrow<AssertionError>("JSON value not in collection - \"abcd\"") {
            expectJSON(json) {
                value(setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test null as member of a collection of string`() {
        val json = "null"
        expectJSON(json) {
            value(setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null as member of a collection of string`() {
        val json = "null"
        shouldThrow<AssertionError>("JSON value not in collection - null") {
            expectJSON(json) {
                value(setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test null property as member of a collection of int`() {
        val json = """{"abc":null}"""
        expectJSON(json) {
            property("abc", setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null property as member of a collection of int`() {
        val json = """{"abc":null}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - null") {
            expectJSON(json) {
                property("abc", setOf(123, 456, 789))
            }
        }
    }

    @Test fun `should test string property as member of a collection`() {
        val json = """{"prop":"abc"}"""
        expectJSON(json) {
            property("prop", setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string property as member of a collection`() {
        val json = """{"prop":"abcd"}"""
        shouldThrow<AssertionError>("/prop: JSON value not in collection - \"abcd\"") {
            expectJSON(json) {
                property("prop", setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test null property as member of a collection of string`() {
        val json = """{"prop":null}"""
        expectJSON(json) {
            property("prop", setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null property as member of a collection of string`() {
        val json = """{"prop":null}"""
        shouldThrow<AssertionError>("/prop: JSON value not in collection - null") {
            expectJSON(json) {
                property("prop", setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test null array item as member of a collection of int`() {
        val json = "[null]"
        expectJSON(json) {
            item(0, setOf(123, 456, 789, null))
        }
    }

    @Test fun `should fail on incorrect test of null array item as member of a collection of int`() {
        val json = "[null]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - null") {
            expectJSON(json) {
                item(0, setOf(123, 456, 789))
            }
        }
    }

    @Test fun `should test string array item as member of a collection`() {
        val json = """["abc"]"""
        expectJSON(json) {
            item(0, setOf("abc", "def", "ghi"))
        }
    }

    @Test fun `should fail on incorrect test of string array item as member of a collection`() {
        val json = """["abcd"]"""
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"abcd\"") {
            expectJSON(json) {
                item(0, setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test null array item as member of a collection of string`() {
        val json = "[null]"
        expectJSON(json) {
            item(0, setOf("abc", "def", "ghi", null))
        }
    }

    @Test fun `should fail on incorrect test of null array item as member of a collection of string`() {
        val json = "[null]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - null") {
            expectJSON(json) {
                item(0, setOf("abc", "def", "ghi"))
            }
        }
    }

    @Test fun `should test string value as member of a range`() {
        val json = "\"abcde\""
        expectJSON(json) {
            value("abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string value as member of a range`() {
        val json = "\"abcde\""
        shouldThrow<AssertionError>("JSON value not in range \"abcdg\"..\"abcdz\" - \"abcde\"") {
            expectJSON(json) {
                value("abcdg".."abcdz")
            }
        }
    }

    @Test fun `should test string property as member of a range`() {
        val json = """{"prop":"abcde"}"""
        expectJSON(json) {
            property("prop", "abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string property as member of a range`() {
        val json = """{"prop":"abcde"}"""
        shouldThrow<AssertionError>("/prop: JSON value not in range \"abcdg\"..\"abcdz\" - \"abcde\"") {
            expectJSON(json) {
                property("prop", "abcdg".."abcdz")
            }
        }
    }

    @Test fun `should test string array item as member of a range`() {
        val json = "[\"abcde\"]"
        expectJSON(json) {
            item(0, "abcda".."abcdz")
        }
    }

    @Test fun `should fail on incorrect test of string array item as member of a range`() {
        val json = """["abcde"]"""
        shouldThrow<AssertionError>("/0: JSON value not in range \"abcdg\"..\"abcdz\" - \"abcde\"") {
            expectJSON(json) {
                item(0, "abcdg".."abcdz")
            }
        }
    }

    @Test fun `should fail on test for value as member of a collection of other class`() {
        val json = "\"C\""
        shouldThrow<AssertionError>("Can't perform test using collection of Char") {
            expectJSON(json) {
                value(setOf('C'))
            }
        }
    }

    @Test fun `should fail on test for property as member of a collection of other class`() {
        val json = """{"abc":"C"}"""
        shouldThrow<AssertionError>("/abc: Can't perform test using collection of Char") {
            expectJSON(json) {
                property("abc", setOf('C'))
            }
        }
    }

    @Test fun `should fail on test for array item as member of a collection of other class`() {
        val json = """["C"]"""
        shouldThrow<AssertionError>("/0: Can't perform test using collection of Char") {
            expectJSON(json) {
                item(0, setOf('C'))
            }
        }
    }

}
