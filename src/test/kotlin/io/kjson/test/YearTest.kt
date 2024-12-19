/*
 * @(#) YearTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
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

import java.time.Year

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import net.pwall.util.MiniSet

class YearTest {

    @Test fun `should read nodeAsYear`() {
        val json = "\"2022\""
        JSONExpect.expectJSON(json) {
            nodeAsYear shouldBe Year.of(2022)
        }
    }

    @Test fun `should fail on invalid nodeAsYear`() {
        val json = "\"not a Year\""
        shouldThrow<AssertionError>("JSON string is not a Year - \"not a Year\"") {
            JSONExpect.expectJSON(json) {
                nodeAsYear
            }
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
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"2022\", was \"2021\"") {
            JSONExpect.expectJSON(json) {
                value(Year.of(2022))
            }
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
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"2022\", was \"2021\"") {
            JSONExpect.expectJSON(json) {
                property("abc", Year.of(2022))
            }
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
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"2022\", was \"2021\"") {
            JSONExpect.expectJSON(json) {
                item(0, Year.of(2022))
            }
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
        shouldThrow<AssertionError>("JSON value not in range \"2020\"..\"2021\" - \"2022\"") {
            JSONExpect.expectJSON(json) {
                value(Year.of(2020)..Year.of(2021))
            }
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
        shouldThrow<AssertionError>("/abc: JSON value not in range \"2020\"..\"2021\" - \"2022\"") {
            JSONExpect.expectJSON(json) {
                property("abc", Year.of(2020)..Year.of(2021))
            }
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
        shouldThrow<AssertionError>("/0: JSON value not in range \"2020\"..\"2021\" - \"2022\"") {
            JSONExpect.expectJSON(json) {
                item(0, Year.of(2020)..Year.of(2021))
            }
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
        shouldThrow<AssertionError>("JSON value not in collection - \"2022\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(Year.of(2021), Year.of(2023)))
            }
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
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"2022\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(Year.of(2021), Year.of(2023)))
            }
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
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"2022\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(Year.of(2021), Year.of(2023)))
            }
        }
    }

    @Test fun `should test that any item has Year value`() {
        val json = """["2023","2024","2025"]"""
        JSONExpect.expectJSON(json) {
            anyItem(Year.of(2024))
        }
    }

    @Test fun `should fail on incorrect test that any item has Year value`() {
        val json = """["2023","2024","2025"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"2026\"") {
            JSONExpect.expectJSON(json) {
                anyItem(Year.of(2026))
            }
        }
    }

}
