/*
 * @(#) IntTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2021,  2024 Peter Wall
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

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import net.pwall.util.MiniSet

class IntTest {

    @Test fun `should read nodeAsInt`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            nodeAsInt shouldBe 12345
        }
    }

    @Test fun `should fail on invalid nodeAsInt`() {
        val json = "\"not an Int\""
        shouldThrow<AssertionError>("JSON type doesn't match - expected integer, was string") {
            JSONExpect.expectJSON(json) {
                nodeAsInt
            }
        }
    }

    @Test fun `should test Int value`() {
        val json = "123456"
        JSONExpect.expectJSON(json) {
            value(123456)
        }
    }

    @Test fun `should fail on incorrect Int value`() {
        val json = "123456"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 654321, was 123456") {
            JSONExpect.expectJSON(json) {
                value(654321)
            }
        }
    }

    @Test fun `should test Int value in range`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            value(12340..12350)
        }
    }

    @Test fun `should fail on incorrect Int value in range`() {
        val json = "12355"
        shouldThrow<AssertionError>("JSON value not in range 12340..12350 - 12355") {
            JSONExpect.expectJSON(json) {
                value(12340..12350)
            }
        }
    }

    @Test fun `should test Int value in collection`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(12344, 12345))
        }
    }

    @Test fun `should fail on incorrect Int value in collection`() {
        val json = "12345"
        shouldThrow<AssertionError>("JSON value not in collection - 12345") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(12344, 12366))
            }
        }
    }

    @Test fun `should read property nodeAsInt`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsInt shouldBe 12345
            }
        }
    }

    @Test fun `should fail on invalid property nodeAsInt`() {
        val json = """{"abc":"not an Int"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected integer, was string") {
            JSONExpect.expectJSON(json) {
                property("abc") {
                    nodeAsInt
                }
            }
        }
    }

    @Test fun `should test Int property`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc", 12345)
        }
    }

    @Test fun `should fail on incorrect Int property`() {
        val json = """{"abc":123456}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 12345, was 123456") {
            JSONExpect.expectJSON(json) {
                property("abc", 12345)
            }
        }
    }

    @Test fun `should test Int property in range`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc", 12340..12350)
        }
    }

    @Test fun `should fail on incorrect Int property in range`() {
        val json = """{"abc":12355}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range 12340..12350 - 12355") {
            JSONExpect.expectJSON(json) {
                property("abc", 12340..12350)
            }
        }
    }

    @Test fun `should test Int property in collection`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(12344, 12345, 12348))
        }
    }

    @Test fun `should fail on incorrect Int property in collection`() {
        val json = """{"abc":12345}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - 12345") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(12344, 12355, 12366))
            }
        }
    }

    @Test fun `should read array item nodeAsInt`() {
        val json = "[12345]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsInt shouldBe 12345
            }
        }
    }

    @Test fun `should fail on invalid array item nodeAsInt`() {
        val json = """["not an Int"]"""
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected integer, was string") {
            JSONExpect.expectJSON(json) {
                item(0) {
                    nodeAsInt
                }
            }
        }
    }

    @Test fun `should test Int array item`() {
        val json = """[9876]"""
        JSONExpect.expectJSON(json) {
            item(0, 9876)
        }
    }

    @Test fun `should fail on incorrect Int array item`() {
        val json = """[987654]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 98765, was 987654") {
            JSONExpect.expectJSON(json) {
                item(0, 98765)
            }
        }
    }

    @Test fun `should test Int array item in range`() {
        val json = "[12345]"
        JSONExpect.expectJSON(json) {
            item(0, 12340..12350)
        }
    }

    @Test fun `should fail on incorrect Int array item in range`() {
        val json = "[12355]"
        shouldThrow<AssertionError>("/0: JSON value not in range 12340..12350 - 12355") {
            JSONExpect.expectJSON(json) {
                item(0, 12340..12350)
            }
        }
    }

    @Test fun `should test Int array item in collection`() {
        val json = "[12345]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(12300, 12340, 12345, 12350))
        }
    }

    @Test fun `should fail on incorrect Int array item in collection`() {
        val json = "[12344]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - 12344") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(12300, 12340, 12345, 12350))
            }
        }
    }

    @Test fun `should test that any item has Int value`() {
        val json = "[12344, 12345, 12346]"
        JSONExpect.expectJSON(json) {
            anyItem(12345)
        }
    }

    @Test fun `should fail on incorrect test that any item has Int value`() {
        val json = "[12345, 12346, 12347]"
        shouldThrow<AssertionError>("No JSON array item has value 12350") {
            JSONExpect.expectJSON(json) {
                anyItem(12350)
            }
        }
    }

    @Test fun `should test that any item has Int value - exhaustive`() {
        val json = "[12345, 12346, 12347]"
        JSONExpect.expectJSON(json) {
            exhaustive {
                anyItem(12347)
                anyItem(12345)
                anyItem(12346)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item has Int value - exhaustive`() {
        val json = "[12345, 12346, 12347, 12348]"
        shouldThrow<AssertionError>("JSON array items not tested: 0, 3") {
            JSONExpect.expectJSON(json) {
                exhaustive {
                    anyItem(12347)
                    anyItem(12346)
                }
            }
        }
    }

    @Test fun `should test that any item has Int value in range`() {
        val json = "[12345, 12355, 12365]"
        JSONExpect.expectJSON(json) {
            anyItem(12350..12359)
        }
    }

    @Test fun `should fail on incorrect test that any item has Int value in range`() {
        val json = """[12345, 12346, 12347]"""
        shouldThrow<AssertionError>("No JSON array item has value in given range - 12340..12344") {
            JSONExpect.expectJSON(json) {
                anyItem(12340..12344)
            }
        }
    }

    @Test fun `should test that any item has Int value in collection`() {
        val json = """[12345, 12346, 12347]"""
        JSONExpect.expectJSON(json) {
            anyItem(MiniSet.of(12344, 12345, 12346, 12349))
        }
    }

    @Test fun `should fail on incorrect test that any item has Int value in collection`() {
        val json = """[12345, 12346, 12347]"""
        shouldThrow<AssertionError>("No JSON array item has value in given collection") {
            JSONExpect.expectJSON(json) {
                anyItem(MiniSet.of(12340, 12342, 12348, 12350))
            }
        }
    }

}
