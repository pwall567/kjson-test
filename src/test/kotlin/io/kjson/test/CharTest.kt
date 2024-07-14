package io.kjson.test

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class CharTest {

    @Test fun `should test Char value`() {
        val json = "\"A\""
        JSONExpect.expectJSON(json) {
            value('A')
        }
    }

    @Test fun `should fail on incorrect Char value`() {
        val json = "\"A\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value('B')
            }
        }.let {
            expect("JSON value doesn't match - expected \"B\", was \"A\"") { it.message }
        }
    }

    @Test fun `should fail when Char value not length 1 string`() {
        val json = "\"ABC\""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                value('A')
            }
        }.let {
            expect("JSON value doesn't match - expected \"A\", was \"ABC\"") { it.message }
        }
    }

    @Test fun `should test Char property`() {
        val json = """{"abc":"A"}"""
        JSONExpect.expectJSON(json) {
            property("abc", 'A')
        }
    }

    @Test fun `should fail on incorrect Char property`() {
        val json = """{"abc":"A"}"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                property("abc", 'B')
            }
        }.let {
            expect("/abc: JSON value doesn't match - expected \"B\", was \"A\"") { it.message }
        }
    }

    @Test fun `should test Char array item`() {
        val json = """["Q"]"""
        JSONExpect.expectJSON(json) {
            item(0, 'Q')
        }
    }

    @Test fun `should fail on incorrect Char array item`() {
        val json = """["X"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                item(0, 'Q')
            }
        }.let {
            expect("/0: JSON value doesn't match - expected \"Q\", was \"X\"") { it.message }
        }
    }

    @Test fun `should test that any item has Char value`() {
        val json = """["A", "B", "C"]"""
        JSONExpect.expectJSON(json) {
            anyItem('C')
        }
    }

    @Test fun `should fail on incorrect test that any item has Char value`() {
        val json = """["A", "B", "C"]"""
        assertFailsWith<AssertionError> {
            JSONExpect.expectJSON(json) {
                anyItem('Q')
            }
        }.let { expect("No JSON array item has value \"Q\"") { it.message } }
    }

}
