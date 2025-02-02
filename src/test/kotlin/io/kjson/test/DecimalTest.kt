/*
 * @(#) DecimalTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2021, 2024 Peter Wall
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

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class DecimalTest {

    @Test fun `should read nodeAsDecimal`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            nodeAsDecimal shouldBe BigDecimal("12.5")
        }
    }

    @Test fun `should read Int for nodeAsDecimal`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            nodeAsDecimal shouldBe BigDecimal("12345")
        }
    }

    @Test fun `should read Long for nodeAsDecimal`() {
        val json = "12345678901234567890"
        JSONExpect.expectJSON(json) {
            nodeAsDecimal shouldBe BigDecimal("12345678901234567890")
        }
    }

    @Test fun `should fail on invalid nodeAsDecimal`() {
        val json = "\"not a decimal\""
        shouldThrow<AssertionError>("JSON type doesn't match - expected decimal, was string") {
            JSONExpect.expectJSON(json) {
                nodeAsDecimal
            }
        }
    }

    @Test fun `should test BigDecimal value`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            value(BigDecimal("12.5"))
        }
    }

    @Test fun `should fail on incorrect BigDecimal value`() {
        val json = "12.0"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 12.5, was 12.0") {
            JSONExpect.expectJSON(json) {
                value(BigDecimal("12.5"))
            }
        }
    }

    @Test fun `should test Int as BigDecimal value`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            value(BigDecimal("12345"))
        }
    }

    @Test fun `should fail on incorrect Int as BigDecimal value`() {
        val json = "12344"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 12345, was 12344") {
            JSONExpect.expectJSON(json) {
                value(BigDecimal("12345"))
            }
        }
    }

    @Test fun `should test Long as BigDecimal value`() {
        val json = "12345678901234567890"
        JSONExpect.expectJSON(json) {
            value(BigDecimal("12345678901234567890"))
        }
    }

    @Test fun `should fail on incorrect Long as BigDecimal value`() {
        val json = "12345678901234567891"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 12345678901234567890," +
                " was 12345678901234567891") {
            JSONExpect.expectJSON(json) {
                value(BigDecimal("12345678901234567890"))
            }
        }
    }

    @Test fun `should test BigDecimal value in range`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            value(BigDecimal(12.0)..BigDecimal(13.0))
        }
    }

    @Test fun `should fail on incorrect BigDecimal value in range`() {
        val json = "13.5"
        shouldThrow<AssertionError>("JSON value not in range 12..13 - 13.5") {
            JSONExpect.expectJSON(json) {
                value(BigDecimal(12.0)..BigDecimal(13.0))
            }
        }
    }

    @Test fun `should test BigDecimal value in collection`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(BigDecimal("12.0"), BigDecimal("12.5"), BigDecimal("13.0")))
        }
    }

    @Test fun `should fail on incorrect BigDecimal value in collection`() {
        val json = "13.5"
        shouldThrow<AssertionError>("JSON value not in collection - 13.5") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(BigDecimal("12.0"), BigDecimal("12.5"), BigDecimal("13.0")))
            }
        }
    }

    @Test fun `should test BigDecimal scale`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            value(scale(1))
        }
    }

    @Test fun `should fail on incorrect BigDecimal scale`() {
        val json = "12.0"
        shouldThrow<AssertionError>("JSON decimal scale doesn't match - expected 2, was 1") {
            JSONExpect.expectJSON(json) {
                value(scale(2))
            }
        }
    }

    @Test fun `should test BigDecimal scale in range`() {
        val json = "12.5"
        JSONExpect.expectJSON(json) {
            value(scale(1..2))
        }
    }

    @Test fun `should fail on incorrect BigDecimal scale in range`() {
        val json = "12.0"
        shouldThrow<AssertionError>("JSON decimal scale doesn't match - expected 2..4, was 1") {
            JSONExpect.expectJSON(json) {
                value(scale(2..4))
            }
        }
    }

    @Test fun `should read property nodeAsDecimal`() {
        val json = """{"abc":12.5}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsDecimal shouldBe BigDecimal("12.5")
            }
        }
    }

    @Test fun `should read Int for property nodeAsDecimal`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsDecimal shouldBe BigDecimal("12345")
            }
        }
    }

    @Test fun `should read Long for property nodeAsDecimal`() {
        val json = """{"abc":12345678901234567890}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsDecimal shouldBe BigDecimal("12345678901234567890")
            }
        }
    }

    @Test fun `should fail on invalid property nodeAsDecimal`() {
        val json = """{"abc":"not a decimal"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected decimal, was string") {
            JSONExpect.expectJSON(json) {
                property("abc") {
                    nodeAsDecimal
                }
            }
        }
    }

    @Test fun `should test BigDecimal property`() {
        val json = """{"abc":12.5}"""
        JSONExpect.expectJSON(json) {
            property("abc", BigDecimal("12.5"))
        }
    }

    @Test fun `should fail on incorrect BigDecimal property`() {
        val json = """{"abc":12.0}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 12.5, was 12.0") {
            JSONExpect.expectJSON(json) {
                property("abc", BigDecimal("12.5"))
            }
        }
    }

    @Test fun `should test BigDecimal property in range`() {
        val json = """{"abc":12.0}"""
        JSONExpect.expectJSON(json) {
            property("abc", BigDecimal("12.0")..BigDecimal("12.5"))
        }
    }

    @Test fun `should fail on incorrect BigDecimal property in range`() {
        val json = """{"abc":12.0}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range 12.5..13.5 - 12.0") {
            JSONExpect.expectJSON(json) {
                property("abc", BigDecimal("12.5")..BigDecimal("13.5"))
            }
        }
    }

    @Test fun `should test BigDecimal property in collection`() {
        val json = """{"abc":12.5}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(BigDecimal("12.0"), BigDecimal("12.5"), BigDecimal("13.0")))
        }
    }

    @Test fun `should fail on incorrect BigDecimal property in collection`() {
        val json = """{"abc":13.5}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - 13.5") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(BigDecimal("12.0"), BigDecimal("12.5"), BigDecimal("13.0")))
            }
        }
    }

    @Test fun `should test BigDecimal property scale`() {
        val json = """{"abc":12.5}"""
        JSONExpect.expectJSON(json) {
            property("abc", scale(1))
        }
    }

    @Test fun `should fail on incorrect BigDecimal property scale`() {
        val json = """{"abc":12.0}"""
        shouldThrow<AssertionError>("/abc: JSON decimal scale doesn't match - expected 2, was 1") {
            JSONExpect.expectJSON(json) {
                property("abc", scale(2))
            }
        }
    }

    @Test fun `should test BigDecimal property scale in range`() {
        val json = """{"abc":12.5}"""
        JSONExpect.expectJSON(json) {
            property("abc", scale(1..2))
        }
    }

    @Test fun `should fail on incorrect BigDecimal property scale in range`() {
        val json = """{"abc":12.0}"""
        shouldThrow<AssertionError>("/abc: JSON decimal scale doesn't match - expected 2..4, was 1") {
            JSONExpect.expectJSON(json) {
                property("abc", scale(2..4))
            }
        }
    }

    @Test fun `should read array item nodeAsDecimal`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsDecimal shouldBe BigDecimal("12.5")
            }
        }
    }

    @Test fun `should read Int for array item nodeAsDecimal`() {
        val json = "[12345]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsDecimal shouldBe BigDecimal("12345")
            }
        }
    }

    @Test fun `should read Long for array item nodeAsDecimal`() {
        val json = "[12345678901234567890]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsDecimal shouldBe BigDecimal("12345678901234567890")
            }
        }
    }

    @Test fun `should fail on invalid array item nodeAsDecimal`() {
        val json = """["not a decimal"]"""
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected decimal, was string") {
            JSONExpect.expectJSON(json) {
                item(0) {
                    nodeAsDecimal
                }
            }
        }
    }

    @Test fun `should test BigDecimal array item`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0, BigDecimal("12.5"))
        }
    }

    @Test fun `should fail on incorrect BigDecimal array item`() {
        val json = "[12.0]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 12.5, was 12.0") {
            JSONExpect.expectJSON(json) {
                item(0, BigDecimal("12.5"))
            }
        }
    }

    @Test fun `should test BigDecimal array item in range`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0, BigDecimal("12.5")..BigDecimal("13.0"))
        }
    }

    @Test fun `should fail on incorrect BigDecimal array item in range`() {
        val json = "[12.0]"
        shouldThrow<AssertionError>("/0: JSON value not in range 12.5..13.0 - 12.0") {
            JSONExpect.expectJSON(json) {
                item(0, BigDecimal("12.5")..BigDecimal("13.0"))
            }
        }
    }

    @Test fun `should test BigDecimal array item in collection`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(BigDecimal("12.5"), BigDecimal("13.0"), BigDecimal("13.5")))
        }
    }

    @Test fun `should fail on incorrect BigDecimal array item in collection`() {
        val json = "[12.0]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - 12.0") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(BigDecimal("12.5"), BigDecimal("13.0"), BigDecimal("13.5")))
            }
        }
    }

    @Test fun `should test BigDecimal array item scale`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0, scale(1))
        }
    }

    @Test fun `should fail on incorrect BigDecimal array item scale`() {
        val json = "[12.0]"
        shouldThrow<AssertionError>("/0: JSON decimal scale doesn't match - expected 2, was 1") {
            JSONExpect.expectJSON(json) {
                item(0, scale(2))
            }
        }
    }

    @Test fun `should test BigDecimal array item scale in range`() {
        val json = "[12.5]"
        JSONExpect.expectJSON(json) {
            item(0, scale(1..2))
        }
    }

    @Test fun `should fail on incorrect BigDecimal array item scale in range`() {
        val json = "[12.0]"
        shouldThrow<AssertionError>("/0: JSON decimal scale doesn't match - expected 2..4, was 1") {
            JSONExpect.expectJSON(json) {
                item(0, scale(2..4))
            }
        }
    }

    @Test fun `should test that any item has BigDecimal value`() {
        val json = "[12.0, 12.5, 13.0]"
        JSONExpect.expectJSON(json) {
            anyItem(BigDecimal("12.5"))
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal value`() {
        val json = "[12.0, 12.5, 13.0]"
        shouldThrow<AssertionError>("No JSON array item has value 13.5") {
            JSONExpect.expectJSON(json) {
                anyItem(BigDecimal("13.5"))
            }
        }
    }

    @Test fun `should test that any item has BigDecimal value - exhaustive`() {
        val json = "[12.0, 12.5, 13.0]"
        JSONExpect.expectJSON(json) {
            exhaustive {
                anyItem(BigDecimal("12.5"))
                anyItem(BigDecimal("12.0"))
                anyItem(BigDecimal("13.0"))
            }
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal value - exhaustive`() {
        val json = "[12.0, 12.5, 13.0, 13.5]"
        shouldThrow<AssertionError>("JSON array item not tested: 3") {
            JSONExpect.expectJSON(json) {
                exhaustive {
                    anyItem(BigDecimal("12.5"))
                    anyItem(BigDecimal("12.0"))
                    anyItem(BigDecimal("13.0"))
                }
            }
        }
    }

    @Test fun `should test that any item has BigDecimal value in range`() {
        val json = "[12.0, 12.5, 13.0]"
        JSONExpect.expectJSON(json) {
            anyItem(BigDecimal("12.5")..BigDecimal("13.5"))
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal value in range`() {
        val json = "[12.0, 14.0, 14.5]"
        shouldThrow<AssertionError>("No JSON array item has value in given range - 12.5..13.5") {
            JSONExpect.expectJSON(json) {
                anyItem(BigDecimal("12.5")..BigDecimal("13.5"))
            }
        }
    }

    @Test fun `should test that any item has BigDecimal value in collection`() {
        val json = "[12.0, 12.5, 13.5]"
        JSONExpect.expectJSON(json) {
            anyItem(MiniSet.of(BigDecimal("12.5"), BigDecimal("13.0"), BigDecimal("13.5")))
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal value in collection`() {
        val json = "[12.0, 12.5, 13.5]"
        shouldThrow<AssertionError>("No JSON array item has value in given collection") {
            JSONExpect.expectJSON(json) {
                anyItem(MiniSet.of(BigDecimal("11.5"), BigDecimal("13.0"), BigDecimal("14.5")))
            }
        }
    }

    @Test fun `should test that any item has BigDecimal scale`() {
        val json = "[12, 12.5, 13]"
        JSONExpect.expectJSON(json) {
            anyItem(scale(1))
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal scale`() {
        val json = "[12, 12.5, 13]"
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            JSONExpect.expectJSON(json) {
                anyItem(scale(2))
            }
        }
    }

    @Test fun `should test that any item has BigDecimal scale in range`() {
        val json = "[12.5, 13.0, 13.50]"
        JSONExpect.expectJSON(json) {
            anyItem(scale(1..4))
        }
    }

    @Test fun `should fail on incorrect test that any item has BigDecimal scale in range`() {
        val json = "[12.5, 13.0, 13.50]"
        shouldThrow<AssertionError>("No JSON array item has value matching given tests") {
            JSONExpect.expectJSON(json) {
                anyItem(scale(3..4))
            }
        }
    }

}
