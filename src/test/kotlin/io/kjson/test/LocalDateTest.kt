/*
 * @(#) LocalDateTest.kt
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

import java.time.LocalDate

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import net.pwall.util.MiniSet

class LocalDateTest {

    @Test fun `should read nodeAsLocalDate`() {
        val json = "\"2022-06-15\""
        JSONExpect.expectJSON(json) {
            nodeAsLocalDate shouldBe LocalDate.of(2022, 6, 15)
        }
    }

    @Test fun `should fail on invalid nodeAsLocalDate`() {
        val json = "\"not a LocalDate\""
        shouldThrow<AssertionError>("JSON string is not a LocalDate - \"not a LocalDate\"") {
            JSONExpect.expectJSON(json) {
                nodeAsLocalDate
            }
        }
    }

    @Test fun `should test LocalDate value`() {
        val json = "\"2022-06-15\""
        JSONExpect.expectJSON(json) {
            value(LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate value`() {
        val json = "\"2022-06-14\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"2022-06-15\", was \"2022-06-14\"") {
            JSONExpect.expectJSON(json) {
                value(LocalDate.of(2022, 6, 15))
            }
        }
    }

    @Test fun `should test LocalDate value in range`() {
        val json = "\"2022-06-15\""
        JSONExpect.expectJSON(json) {
            value(LocalDate.of(2022, 6, 14)..LocalDate.of(2022, 6, 16))
        }
    }

    @Test fun `should fail on incorrect LocalDate value in range`() {
        val json = "\"2022-06-15\""
        shouldThrow<AssertionError>("JSON value not in range \"2022-06-16\"..\"2022-06-18\" - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                value(LocalDate.of(2022, 6, 16)..LocalDate.of(2022, 6, 18))
            }
        }
    }

    @Test fun `should test LocalDate value in collection`() {
        val json = "\"2022-06-15\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(LocalDate.of(2022, 6, 15), LocalDate.of(2022, 6, 16)))
        }
    }

    @Test fun `should fail on incorrect LocalDate value in collection`() {
        val json = "\"2022-06-15\""
        shouldThrow<AssertionError>("JSON value not in collection - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(LocalDate.of(2022, 6, 14), LocalDate.of(2022, 6, 16)))
            }
        }
    }

    @Test fun `should test LocalDate property`() {
        val json = """{"abc":"2022-06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate property`() {
        val json = """{"abc":"2022-06-14"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected \"2022-06-15\", was \"2022-06-14\"") {
            JSONExpect.expectJSON(json) {
                property("abc", LocalDate.of(2022, 6, 15))
            }
        }
    }

    @Test fun `should test LocalDate property in range`() {
        val json = """{"abc":"2022-06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", LocalDate.of(2022, 6, 14)..LocalDate.of(2022, 6, 16))
        }
    }

    @Test fun `should fail on incorrect LocalDate property in range`() {
        val json = """{"abc":"2022-06-15"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"2022-06-16\"..\"2022-06-18\" - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                property("abc", LocalDate.of(2022, 6, 16)..LocalDate.of(2022, 6, 18))
            }
        }
    }

    @Test fun `should test LocalDate property in collection`() {
        val json = """{"abc":"2022-06-15"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(LocalDate.of(2022, 6, 15), LocalDate.of(2022, 6, 16)))
        }
    }

    @Test fun `should fail on incorrect LocalDate property in collection`() {
        val json = """{"abc":"2022-06-15"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(LocalDate.of(2022, 6, 14), LocalDate.of(2022, 6, 16)))
            }
        }
    }

    @Test fun `should test LocalDate array item`() {
        val json = """["2022-06-15"]"""
        JSONExpect.expectJSON(json) {
            item(0, LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate array item`() {
        val json = """["2022-06-14"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected \"2022-06-15\", was \"2022-06-14\"") {
            JSONExpect.expectJSON(json) {
                item(0, LocalDate.of(2022, 6, 15))
            }
        }
    }

    @Test fun `should test LocalDate array item in range`() {
        val json = "[\"2022-06-15\"]"
        JSONExpect.expectJSON(json) {
            item(0, LocalDate.of(2022, 6, 14)..LocalDate.of(2022, 6, 16))
        }
    }

    @Test fun `should fail on incorrect LocalDate array item in range`() {
        val json = "[\"2022-06-15\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"2022-06-16\"..\"2022-06-18\" - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                item(0, LocalDate.of(2022, 6, 16)..LocalDate.of(2022, 6, 18))
            }
        }
    }

    @Test fun `should test LocalDate array item in collection`() {
        val json = "[\"2022-06-15\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(LocalDate.of(2022, 6, 15), LocalDate.of(2022, 6, 16)))
        }
    }

    @Test fun `should fail on incorrect LocalDate array item in collection`() {
        val json = "[\"2022-06-15\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"2022-06-15\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(LocalDate.of(2022, 6, 14), LocalDate.of(2022, 6, 16)))
            }
        }
    }

    @Test fun `should test that any item has LocalDate value`() {
        val json = """["2023-09-18","2023-09-19","2023-09-20"]"""
        JSONExpect.expectJSON(json) {
            anyItem(LocalDate.of(2023, 9, 19))
        }
    }

    @Test fun `should fail on incorrect test that any item has LocalDate value`() {
        val json = """["2023-09-18","2023-09-19","2023-09-20"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"2023-09-21\"") {
            JSONExpect.expectJSON(json) {
                anyItem(LocalDate.of(2023, 9, 21))
            }
        }
    }

}
