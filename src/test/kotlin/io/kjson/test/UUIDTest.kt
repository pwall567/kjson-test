/*
 * @(#) UUIDTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022, 2023, 2024, 2025 Peter Wall
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

import java.util.UUID

import io.kstuff.test.shouldBe
import io.kstuff.test.shouldThrow

import io.jstuff.util.MiniSet

class UUIDTest {

    @Test fun `should read nodeAsUUID`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        JSONExpect.expectJSON(json) {
            nodeAsUUID shouldBe uuid0
        }
    }

    @Test fun `should fail on invalid nodeAsUUID`() {
        val json = "\"not a UUID\""
        shouldThrow<AssertionError>("JSON string is not a UUID - \"not a UUID\"") {
            JSONExpect.expectJSON(json) {
                nodeAsUUID
            }
        }
    }

    @Test fun `should fail on invalid nodeAsUUID - number too short`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e13626575\""
        shouldThrow<AssertionError>("JSON string is not a UUID - \"ea986c80-ed1d-11ec-a20b-a7e13626575\"") {
            JSONExpect.expectJSON(json) {
                nodeAsUUID
            }
        }
    }

    @Test fun `should test UUID value`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        JSONExpect.expectJSON(json) {
            value(uuid0)
        }
    }

    @Test fun `should fail on incorrect UUID value`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        shouldThrow<AssertionError>("JSON value doesn't match - expected \"ea986c80-ed1d-11ec-a20b-a7e136265751\"," +
                " was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                value(uuid1)
            }
        }
    }

    @Test fun `should test UUID property`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        JSONExpect.expectJSON(json) {
            property("abc", uuid0)
        }
    }

    @Test fun `should fail on incorrect UUID property`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        shouldThrow<AssertionError>("/abc: JSON value doesn't match - expected " +
                "\"ea986c80-ed1d-11ec-a20b-a7e136265751\", was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                property("abc", uuid1)
            }
        }
    }

    @Test fun `should test UUID array item`() {
        val json = """["ea986c80-ed1d-11ec-a20b-a7e136265750"]"""
        JSONExpect.expectJSON(json) {
            item(0, uuid0)
        }
    }

    @Test fun `should fail on incorrect UUID array item`() {
        val json = """["ea986c80-ed1d-11ec-a20b-a7e136265750"]"""
        shouldThrow<AssertionError>("/0: JSON value doesn't match - expected " +
                "\"ea986c80-ed1d-11ec-a20b-a7e136265751\", was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                item(0, uuid1)
            }
        }
    }

    @Test fun `should test UUID value in range`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        JSONExpect.expectJSON(json) {
            value(uuid0..uuid1)
        }
    }

    @Test fun `should fail on incorrect UUID value in range`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        shouldThrow<AssertionError>("JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                value(uuid1..uuid2)
            }
        }
    }

    @Test fun `should test UUID property in range`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        JSONExpect.expectJSON(json) {
            property("abc", uuid0..uuid1)
        }
    }

    @Test fun `should fail on incorrect UUID property in range`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                property("abc", uuid1..uuid2)
            }
        }
    }

    @Test fun `should test UUID array item in range`() {
        val json = "[\"ea986c80-ed1d-11ec-a20b-a7e136265750\"]"
        JSONExpect.expectJSON(json) {
            item(0, uuid0..uuid1)
        }
    }

    @Test fun `should fail on incorrect UUID array item in range`() {
        val json = "[\"ea986c80-ed1d-11ec-a20b-a7e136265750\"]"
        shouldThrow<AssertionError>("/0: JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                item(0, uuid1..uuid2)
            }
        }
    }

    @Test fun `should test UUID value in collection`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(uuid0, uuid1))
        }
    }

    @Test fun `should fail on incorrect UUID value in collection`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        shouldThrow<AssertionError>("JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(uuid1, uuid2))
            }
        }
    }

    @Test fun `should test UUID property in collection`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(uuid0, uuid1))
        }
    }

    @Test fun `should fail on incorrect UUID property in collection`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        shouldThrow<AssertionError>("/abc: JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(uuid1, uuid2))
            }
        }
    }

    @Test fun `should test UUID array item in collection`() {
        val json = "[\"ea986c80-ed1d-11ec-a20b-a7e136265750\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(uuid0, uuid1))
        }
    }

    @Test fun `should fail on incorrect UUID array item in collection`() {
        val json = "[\"ea986c80-ed1d-11ec-a20b-a7e136265750\"]"
        shouldThrow<AssertionError>("/0: JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(uuid1, uuid2))
            }
        }
    }

    @Test fun `should test that any item has UUID value`() {
        val json = """["$uuid0","$uuid1","$uuid2"]"""
        JSONExpect.expectJSON(json) {
            anyItem(uuid2)
        }
    }

    @Test fun `should fail on incorrect test that any item has UUID value`() {
        val json = """["$uuid0","$uuid1"]"""
        shouldThrow<AssertionError>("No JSON array item has value \"$uuid2\"") {
            JSONExpect.expectJSON(json) {
                anyItem(uuid2)
            }
        }
    }

    @Test fun `should test multiple UUID values`() {
        val json = """["$uuid0","$uuid1","$uuid2"]"""
        JSONExpect.expectJSON(json) {
            items(uuid0, uuid1, uuid2)
        }
    }

    @Test fun `should fail on incorrect multiple UUID values`() {
        val json = """["$uuid0","$uuid1","$uuid2"]"""
        shouldThrow<AssertionError>("/1: JSON value doesn't match - expected \"$uuid2\", was \"$uuid1\"") {
            JSONExpect.expectJSON(json) {
                items(uuid0, uuid2, uuid1)
            }
        }
    }

    @Test fun `should fail on incorrect number of multiple UUID values`() {
        val json = """["$uuid0","$uuid1","$uuid2"]"""
        shouldThrow<AssertionError>("JSON array size not the same as number of values - expected 2, was 3") {
            JSONExpect.expectJSON(json) {
                items(uuid0, uuid1)
            }
        }
    }

    @Test fun `should fail on multiple UUID values applied to an object`() {
        val json = "{}"
        shouldThrow<AssertionError>("JSON type doesn't match - expected array, was object") {
            JSONExpect.expectJSON(json) {
                items(uuid0, uuid1, uuid2)
            }
        }
    }

    companion object {
        val uuid0: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750")
        val uuid1: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265751")
        val uuid2: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265752")
    }

}
