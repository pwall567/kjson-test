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

import java.math.BigDecimal

import io.jstuff.json.JSONSimple
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
    compareJSON(parseString(expected, "Expected string"), JSONExpect(parseString(this, "String")))
}

internal fun compareJSON(expected: Any?, actual: JSONExpect) {
    when (expected) {
        null -> actual.value(null)
        is Map<*, *> -> {
            val map = actual.nodeAsObject
            val missing = mutableListOf<Any?>()
            for ((key, value) in expected.entries) {
                if (map.containsKey(key))
                    compareJSON(value, JSONExpect(map[key], actual.propertyPointer(key.toString())))
                else
                    missing.add(key)
            }
            if (missing.isNotEmpty())
                actual.error("JSON ${propertiesText(missing.size)} missing - " + missing.joinToString())
            val extra = map.keys.filter { !expected.containsKey(it) }
            if (extra.isNotEmpty())
                actual.error("JSON ${propertiesText(extra.size)} unexpected - " + extra.joinToString())
        }
        is List<*> -> {
            val n = expected.size
            val array = actual.checkArray(n)
            for (i in 0 until n)
                compareJSON(expected[i], JSONExpect(array[i], actual.itemPointer(i)))
        }
        is String -> actual.value(expected)
        is Int -> actual.value(expected)
        is Long -> actual.value(expected)
        is BigDecimal -> actual.value(expected)
        is Boolean -> actual.value(expected)
    }
}

internal fun parseString(string: String, description: String): Any? = try {
    JSONSimple.parse(string)
} catch (e: Exception) {
    throw AssertionError("$description is not valid JSON: ${e.message}")
}
