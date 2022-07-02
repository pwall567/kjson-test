/*
 * @(#) YearTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022 Peter Wall
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

import java.time.Year

import net.pwall.util.MiniSet

class YearTest {

    @Test fun `should read nodeAsYear`() {
        val json = "\"2022\""
        JSONExpect.expectJSON(json) {
            if (nodeAsYear != Year.of(2022))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsYear`() {
        val json = "\"not a Year\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsYear
            }
        }.let {
            expect("JSON string is not a Year - \"not a Year\"") { it.message }
        }
    }

    @Test fun `should test Year value`() {
        val json = "\"2022\""
        JSONExpect.expectJSON(json) {
            value(Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year value`() {
        val json = "\"2021\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(Year.of(2022))
            }
        }.let {
            expect("JSON value doesn't match - expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should test Year property`() {
        val json = """{"abc":"2022"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year property`() {
        val json = """{"abc":"2021"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", Year.of(2022))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should test Year array item`() {
        val json = """["2022"]"""
        JSONExpect.expectJSON(json) {
            item(0, Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year array item`() {
        val json = """["2021"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, Year.of(2022))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should test Year value in range`() {
        val json = "\"2022\""
        JSONExpect.expectJSON(json) {
            value(Year.of(2021)..Year.of(2023))
        }
    }

    @Test fun `should fail on incorrect Year value in range`() {
        val json = "\"2022\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(Year.of(2020)..Year.of(2021))
            }
        }.let {
            expect("JSON value not in range \"2020\"..\"2021\" - \"2022\"") { it.message }
        }
    }

    @Test fun `should test Year property in range`() {
        val json = """{"abc":"2022"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Year.of(2021)..Year.of(2023))
        }
    }

    @Test fun `should fail on incorrect Year property in range`() {
        val json = """{"abc":"2022"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", Year.of(2020)..Year.of(2021))
            }
        }.let {
            expect("/abc: JSON value not in range \"2020\"..\"2021\" - \"2022\"") { it.message }
        }
    }

    @Test fun `should test Year array item in range`() {
        val json = "[\"2022\"]"
        JSONExpect.expectJSON(json) {
            item(0, Year.of(2021)..Year.of(2023))
        }
    }

    @Test fun `should fail on incorrect Year array item in range`() {
        val json = "[\"2022\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, Year.of(2020)..Year.of(2021))
            }
        }.let {
            expect("/0: JSON value not in range \"2020\"..\"2021\" - \"2022\"") { it.message }
        }
    }

    @Test fun `should test Year value in collection`() {
        val json = "\"2022\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(Year.of(2022), Year.of(2023)))
        }
    }

    @Test fun `should fail on incorrect Year value in collection`() {
        val json = "\"2022\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(Year.of(2021), Year.of(2023)))
            }
        }.let {
            expect("JSON value not in collection - \"2022\"") { it.message }
        }
    }

    @Test fun `should test Year property in collection`() {
        val json = """{"abc":"2022"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(Year.of(2022), Year.of(2023)))
        }
    }

    @Test fun `should fail on incorrect Year property in collection`() {
        val json = """{"abc":"2022"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(Year.of(2021), Year.of(2023)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"2022\"") { it.message }
        }
    }

    @Test fun `should test Year array item in collection`() {
        val json = "[\"2022\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(Year.of(2022), Year.of(2023)))
        }
    }

    @Test fun `should fail on incorrect Year array item in collection`() {
        val json = "[\"2022\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(Year.of(2021), Year.of(2023)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"2022\"") { it.message }
        }
    }

}
