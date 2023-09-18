/*
 * @(#) UUIDTest.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
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
import kotlin.test.fail

import java.util.UUID

import net.pwall.util.MiniSet

class UUIDTest {

    @Test fun `should read nodeAsUUID`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        JSONExpect.expectJSON(json) {
            if (nodeAsUUID != uuid0)
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsUUID`() {
        val json = "\"not a UUID\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsUUID
            }
        }.let {
            expect("JSON string is not a UUID - \"not a UUID\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(uuid1)
            }
        }.let {
            expect("JSON value doesn't match - expected \"ea986c80-ed1d-11ec-a20b-a7e136265751\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", uuid1)
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"ea986c80-ed1d-11ec-a20b-a7e136265751\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, uuid1)
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"ea986c80-ed1d-11ec-a20b-a7e136265751\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(uuid1..uuid2)
            }
        }.let {
            expect("JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - " +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", uuid1..uuid2)
            }
        }.let {
            expect("/abc: JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - " +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, uuid1..uuid2)
            }
        }.let {
            expect("/0: JSON value not in range \"ea986c80-ed1d-11ec-a20b-a7e136265751\".." +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265752\" - " +
                    "\"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(uuid1, uuid2))
            }
        }.let {
            expect("JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(uuid1, uuid2))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(uuid1, uuid2))
            }
        }.let {
            expect("/0: JSON value not in collection - \"ea986c80-ed1d-11ec-a20b-a7e136265750\"") { it.message }
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
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem(uuid2)
            }
        }.let { expect("No JSON array item has value \"$uuid2\"") { it.message } }
    }

    companion object {
        val uuid0: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750")
        val uuid1: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265751")
        val uuid2: UUID = UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265752")
    }

}
