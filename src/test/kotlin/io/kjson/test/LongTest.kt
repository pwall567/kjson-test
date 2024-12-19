/*
 * @(#) LongTest.kt
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

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import net.pwall.util.MiniSet

class LongTest {

    @Test fun `should read nodeAsLong`() {
        val json = "123456789012345"
        JSONExpect.expectJSON(json) {
            nodeAsLong shouldBe 123456789012345
        }
    }

    @Test fun `should read Int for nodeAsLong`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            nodeAsLong shouldBe 12345L
        }
    }

    @Test fun `should fail on invalid nodeAsLong`() {
        val json = "\"not a Long\""
        shouldThrow<AssertionError>("JSON type doesn't match - expected long integer, was string") {
            JSONExpect.expectJSON(json) {
                nodeAsLong
            }
        }
    }

    @Test fun `should test Long value`() {
        val json = "123456789012345"
        JSONExpect.expectJSON(json) {
            value(123456789012345)
        }
    }

    @Test fun `should fail on incorrect Long value`() {
        val json = "123456789012346"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 123456789012345, was 123456789012346") {
            JSONExpect.expectJSON(json) {
                value(123456789012345)
            }
        }
    }

    @Test fun `should test Int as Long value`() {
        val json = "12345"
        JSONExpect.expectJSON(json) {
            value(12345L)
        }
    }

    @Test fun `should fail on incorrect Int as Long value`() {
        val json = "12344"
        shouldThrow<AssertionError>("JSON value doesn't match - expected 12345, was 12344") {
            JSONExpect.expectJSON(json) {
                value(12345L)
            }
        }
    }

    @Test fun `should test Long value in range`() {
        val json = "123456789012345"
        JSONExpect.expectJSON(json) {
            value(123456789012340..123456789012349)
        }
    }

    @Test fun `should fail on incorrect Long value in range`() {
        val json = "123456789012350"
        shouldThrow<AssertionError>("JSON value not in range 123456789012340..123456789012349 - 123456789012350") {
            JSONExpect.expectJSON(json) {
                value(123456789012340..123456789012349)
            }
        }
    }

    @Test fun `should test Long value in collection`() {
        val json = "123456789012345"
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(123456789012344, 123456789012345))
        }
    }

    @Test fun `should fail on incorrect Long value in collection`() {
        val json = "123456789012345"
        shouldThrow<AssertionError>("JSON value not in collection - 123456789012345") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(123456789012344, 123456789012349))
            }
        }
    }

    @Test fun `should read property nodeAsLong`() {
        val json = """{"abc":123456789012345}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsLong shouldBe 123456789012345
            }
        }
    }

    @Test fun `should read Int for property nodeAsLong`() {
        val json = """{"abc":12345}"""
        JSONExpect.expectJSON(json) {
            property("abc") {
                nodeAsLong shouldBe 12345L
            }
        }
    }

    @Test fun `should fail on invalid property nodeAsLong`() {
        val json = """{"abc":"not a long"}"""
        shouldThrow<AssertionError>("/abc: JSON type doesn't match - expected long integer, was string") {
            JSONExpect.expectJSON(json) {
                property("abc") {
                    nodeAsLong
                }
            }
        }
    }

    @Test fun `should test Long property`() {
        val json = """{"abc":123456789012345}"""
        JSONExpect.expectJSON(json) {
            property("abc", 123456789012345)
        }
    }

    @Test fun `should fail on incorrect Long property`() {
        val json = """{"abc":123456789012346}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected 123456789012345, was 123456789012346") {
            JSONExpect.expectJSON(json) {
                property("abc", 123456789012345)
            }
        }
    }

    @Test fun `should test Long property in range`() {
        val json = """{"abc":123456789012345}"""
        JSONExpect.expectJSON(json) {
            property("abc", 123456789012344..123456789012348)
        }
    }

    @Test fun `should fail on incorrect Long property in range`() {
        val json = """{"abc":123456789012340}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range 123456789012344..123456789012348 -" +
                " 123456789012340") {
            JSONExpect.expectJSON(json) {
                property("abc", 123456789012344..123456789012348)
            }
        }
    }

    @Test fun `should test Long property in collection`() {
        val json = """{"abc":123456789012345}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(123456789012344, 123456789012345, 123456789012349))
        }
    }

    @Test fun `should fail on incorrect Long property in collection`() {
        val json = """{"abc":123456789012345}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - 123456789012345") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(123456789012344, 123456789012348, 123456789012340))
            }
        }
    }

    @Test fun `should read array item nodeAsLong`() {
        val json = "[123456789012345]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsLong shouldBe 123456789012345
            }
        }
    }

    @Test fun `should read Int for array item nodeAsLong`() {
        val json = "[12345]"
        JSONExpect.expectJSON(json) {
            item(0) {
                nodeAsLong shouldBe 12345L
            }
        }
    }

    @Test fun `should fail on invalid array item nodeAsLong`() {
        val json = """["not a long"]"""
        shouldThrow<AssertionError>("/0: JSON type doesn't match - expected long integer, was string") {
            JSONExpect.expectJSON(json) {
                item(0) {
                    nodeAsLong
                }
            }
        }
    }

    @Test fun `should test Long array item`() {
        val json = "[123456789012345]"
        JSONExpect.expectJSON(json) {
            item(0, 123456789012345)
        }
    }

    @Test fun `should fail on incorrect Long array item`() {
        val json = "[123456789012344]"
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected 123456789012345, was 123456789012344") {
            JSONExpect.expectJSON(json) {
                item(0, 123456789012345)
            }
        }
    }

    @Test fun `should test Long array item in range`() {
        val json = "[123456789012345]"
        JSONExpect.expectJSON(json) {
            item(0, 123456789012344..123456789012348)
        }
    }

    @Test fun `should fail on incorrect Long array item in range`() {
        val json = "[123456789012340]"
        shouldThrow<AssertionError>("/0: JSON value not in range 123456789012344..123456789012348 - 123456789012340") {
            JSONExpect.expectJSON(json) {
                item(0, 123456789012344..123456789012348)
            }
        }
    }

    @Test fun `should test Long array item in collection`() {
        val json = "[123456789012345]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(123456789012340, 123456789012345, 123456789012350, 123456789012355))
        }
    }

    @Test fun `should fail on incorrect Long array item in collection`() {
        val json = "[123456789012346]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - 123456789012346") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(123456789012340, 123456789012345, 123456789012350, 123456789012355))
            }
        }
    }

    @Test fun `should test that any item has Long value`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        JSONExpect.expectJSON(json) {
            anyItem(123456789012345)
        }
    }

    @Test fun `should fail on incorrect test that any item has Long value`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        shouldThrow<AssertionError>("No JSON array item has value 123456789012346") {
            JSONExpect.expectJSON(json) {
                anyItem(123456789012346)
            }
        }
    }

    @Test fun `should test that any item has Long value - exhaustive`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        JSONExpect.expectJSON(json) {
            exhaustive {
                anyItem(123456789012347)
                anyItem(123456789012349)
                anyItem(123456789012345)
            }
        }
    }

    @Test fun `should fail on incorrect test that any item has Long value - exhaustive`() {
        val json = "[123456789012345, 123456789012347, 123456789012349, 123456789012350]"
        shouldThrow<AssertionError>("JSON array items not tested: 1, 2") {
            JSONExpect.expectJSON(json) {
                exhaustive {
                    anyItem(123456789012350)
                    anyItem(123456789012345)
                }
            }
        }
    }

    @Test fun `should test that any item has Long value in range`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        JSONExpect.expectJSON(json) {
            anyItem(123456789012344..123456789012346)
        }
    }

    @Test fun `should fail on incorrect test that any item has Long value in range`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        shouldThrow<AssertionError>("No JSON array item has value in given range - 123456789012342..123456789012342") {
            JSONExpect.expectJSON(json) {
                anyItem(123456789012342..123456789012342)
            }
        }
    }

    @Test fun `should test that any item has Long value in collection`() {
        val json = "[123456789012345, 123456789012347, 123456789012349]"
        JSONExpect.expectJSON(json) {
            anyItem(MiniSet.of(123456789012340, 123456789012345, 123456789012350, 123456789012355))
        }
    }

    @Test fun `should fail on incorrect test that any item has Long value in collection`() {
        val json = "[123456789012344, 123456789012347, 123456789012349]"
        shouldThrow<AssertionError>("No JSON array item has value in given collection") {
            JSONExpect.expectJSON(json) {
                anyItem(MiniSet.of(123456789012340, 123456789012345, 123456789012350, 123456789012355))
            }
        }
    }

}
