/*
 * @(#) ShouldMatchJSON.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2025 Peter Wall
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

import io.kjson.JSONArray
import io.kjson.JSONBoolean
import io.kjson.JSONDecimal
import io.kjson.JSONInt
import io.kjson.JSONLong
import io.kjson.JSONObject
import io.kjson.JSONString
import io.kjson.JSONValue
import io.kjson.test.JSONExpect.Companion.parseString
import io.kjson.test.JSONExpect.Companion.propertiesText

/**
 * Test a JSON string against a set of validations.
 */
infix fun String.shouldMatchJSON(tests: JSONExpect.() -> Unit) {
    JSONExpect.expectJSON(this, tests)
}

/**
 * Test a JSON string against another string, parsing both into JSON structures and then performing a node-by-node
 * comparison.  Differences in whitespace or property order will be ignored.
 */
infix fun String.shouldMatchJSON(expected: String) {
    this shouldMatchJSON parseString(expected, "Expected string")
}

/**
 * Test a JSON string against a JSON structure in the form of a [JSONValue].
 */
infix fun String.shouldMatchJSON(expected: JSONValue?) {
    compareJSON(expected, JSONExpect(parseString(this, "String")))
}

internal fun compareJSON(expected: JSONValue?, actual: JSONExpect) {
    when (expected) {
        null -> actual.value(null)
        is JSONObject -> {
            val map = actual.nodeAsObject
            val missing = mutableListOf<Any?>()
            for ((key, value) in expected.entries) {
                if (map.containsKey(key))
                    compareJSON(value, JSONExpect(map[key], actual.propertyPointer(key)))
                else
                    missing.add(key)
            }
            if (missing.isNotEmpty())
                actual.error("JSON ${propertiesText(missing.size)} missing - " + missing.joinToString())
            val extra = map.keys.filter { !expected.containsKey(it) }
            if (extra.isNotEmpty())
                actual.error("JSON ${propertiesText(extra.size)} unexpected - " + extra.joinToString())
        }
        is JSONArray -> {
            val n = expected.size
            val array = actual.checkArray(n)
            for (i in 0 until n)
                compareJSON(expected[i], JSONExpect(array[i], actual.itemPointer(i)))
        }
        is JSONString -> actual.value(expected.value)
        is JSONInt -> actual.value(expected.value)
        is JSONLong -> actual.value(expected.value)
        is JSONDecimal -> actual.value(expected.value)
        is JSONBoolean -> actual.value(expected.value)
    }
}
