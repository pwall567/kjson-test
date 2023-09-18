/*
 * @(#) JavaDurationTest.kt
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

import java.time.Duration

import net.pwall.util.MiniSet

class JavaDurationTest {

    @Test fun `should read nodeAsJavaDuration`() {
        val json = "\"PT34S\""
        JSONExpect.expectJSON(json) {
            if (nodeAsJavaDuration != Duration.ofSeconds(34))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsJavaDuration`() {
        val json = "\"not a Java Duration\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                nodeAsJavaDuration
            }
        }.let {
            expect("JSON string is not a Java Duration - \"not a Java Duration\"") { it.message }
        }
    }

    @Test fun `should test Java Duration value`() {
        val json = "\"PT34S\""
        JSONExpect.expectJSON(json) {
            value(Duration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration value`() {
        val json = "\"PT33S\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(Duration.ofSeconds(34))
            }
        }.let {
            expect("JSON value doesn't match - expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration property`() {
        val json = """{"abc":"PT34S"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Duration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration property`() {
        val json = """{"abc":"PT33S"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", Duration.ofSeconds(34))
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration array item`() {
        val json = """["PT34S"]"""
        JSONExpect.expectJSON(json) {
            item(0, Duration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration array item`() {
        val json = """["PT33S"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, Duration.ofSeconds(34))
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration value in range`() {
        val json = "\"PT34S\""
        JSONExpect.expectJSON(json) {
            value(Duration.ofSeconds(33)..Duration.ofSeconds(35))
        }
    }

    @Test fun `should fail on incorrect Java Duration value in range`() {
        val json = "\"PT34S\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(Duration.ofSeconds(35)..Duration.ofSeconds(36))
            }
        }.let {
            expect("JSON value not in range \"PT35S\"..\"PT36S\" - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration property in range`() {
        val json = """{"abc":"PT34S"}"""
        JSONExpect.expectJSON(json) {
            property("abc", Duration.ofSeconds(33)..Duration.ofSeconds(35))
        }
    }

    @Test fun `should fail on incorrect Java Duration property in range`() {
        val json = """{"abc":"PT34S"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", Duration.ofSeconds(35)..Duration.ofSeconds(36))
            }
        }.let {
            expect("/abc: JSON value not in range \"PT35S\"..\"PT36S\" - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration array item in range`() {
        val json = "[\"PT34S\"]"
        JSONExpect.expectJSON(json) {
            item(0, Duration.ofSeconds(33)..Duration.ofSeconds(35))
        }
    }

    @Test fun `should fail on incorrect Java Duration array item in range`() {
        val json = "[\"PT34S\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, Duration.ofSeconds(35)..Duration.ofSeconds(36))
            }
        }.let {
            expect("/0: JSON value not in range \"PT35S\"..\"PT36S\" - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration value in collection`() {
        val json = "\"PT34S\""
        JSONExpect.expectJSON(json) {
            value(MiniSet.of(Duration.ofSeconds(34), Duration.ofSeconds(35)))
        }
    }

    @Test fun `should fail on incorrect Java Duration value in collection`() {
        val json = "\"PT34S\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value(MiniSet.of(Duration.ofSeconds(33), Duration.ofSeconds(35)))
            }
        }.let {
            expect("JSON value not in collection - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration property in collection`() {
        val json = """{"abc":"PT34S"}"""
        JSONExpect.expectJSON(json) {
            property("abc", MiniSet.of(Duration.ofSeconds(34), Duration.ofSeconds(35)))
        }
    }

    @Test fun `should fail on incorrect Java Duration property in collection`() {
        val json = """{"abc":"PT34S"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", MiniSet.of(Duration.ofSeconds(33), Duration.ofSeconds(35)))
            }
        }.let {
            expect("/abc: JSON value not in collection - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration array item in collection`() {
        val json = "[\"PT34S\"]"
        JSONExpect.expectJSON(json) {
            item(0, MiniSet.of(Duration.ofSeconds(34), Duration.ofSeconds(35)))
        }
    }

    @Test fun `should fail on incorrect Java Duration array item in collection`() {
        val json = "[\"PT34S\"]"
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, MiniSet.of(Duration.ofSeconds(33), Duration.ofSeconds(35)))
            }
        }.let {
            expect("/0: JSON value not in collection - \"PT34S\"") { it.message }
        }
    }

    @Test fun `should test that any item has Java Duration value`() {
        val json = """["PT30S","PT35S","PT40S"]"""
        JSONExpect.expectJSON(json) {
            anyItem(Duration.ofSeconds(30))
        }
    }

    @Test fun `should fail on incorrect test that any item has Java Duration value`() {
        val json = """["PT30S","PT35S","PT40S"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem(Duration.ofSeconds(45))
            }
        }.let { expect("No JSON array item has value \"PT45S\"") { it.message } }
    }

}
