/*
 * @(#) Lambda.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022 Peter Wall
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

import kotlin.time.Duration

import java.math.BigDecimal
import java.time.Duration as JavaDuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZonedDateTime
import java.util.UUID

import net.pwall.json.validation.JSONValidation

/** Check that a value is non-null. */
@Suppress("unused")
val JSONExpect.nonNull: JSONExpect.() -> Unit
    get() = {
        if (node == null)
            error("JSON item is null")
    }

/** Check that a value is a string. */
@Suppress("unused")
val JSONExpect.string: JSONExpect.() -> Unit
    get() = {
        if (node !is String)
            errorOnType("string")
    }

/** Check that a value is an integer. */
@Suppress("unused")
val JSONExpect.integer: JSONExpect.() -> Unit
    get() = {
        if (node !is Int)
            errorOnType("integer")
    }

/** Check that a value is a long integer. */
@Suppress("unused")
val JSONExpect.longInteger: JSONExpect.() -> Unit
    get() = {
        if (!(node is Long || node is Int))
            errorOnType("long integer")
    }

/** Check that a value is a decimal. */
@Suppress("unused")
val JSONExpect.decimal: JSONExpect.() -> Unit
    get() = {
        if (!(node is BigDecimal || node is Long || node is Int))
            errorOnType("decimal")
    }

/** Check that a value is a boolean. */
@Suppress("unused")
val JSONExpect.boolean: JSONExpect.() -> Unit
    get() = {
        if (node !is Boolean)
            errorOnType("boolean")
    }

/** Check that a string value is a valid [UUID]. */
@Suppress("unused")
val JSONExpect.uuid: JSONExpect.() -> Unit
    get() = {
        if (!JSONValidation.isUUID(nodeAsString))
            error("JSON string is not a UUID - ${showNode()}")
    }

/** Check that a string value is a valid [LocalDate]. */
@Suppress("unused")
val JSONExpect.localDate: JSONExpect.() -> Unit
    get() = { nodeAsLocalDate }

/** Check that a string value is a valid [LocalDateTime]. */
@Suppress("unused")
val JSONExpect.localDateTime: JSONExpect.() -> Unit
    get() = { nodeAsLocalDateTime }

/** Check that a string value is a valid [LocalTime]. */
@Suppress("unused")
val JSONExpect.localTime: JSONExpect.() -> Unit
    get() = { nodeAsLocalTime }

/** Check that a string value is a valid [OffsetDateTime]. */
@Suppress("unused")
val JSONExpect.offsetDateTime: JSONExpect.() -> Unit
    get() = { nodeAsOffsetDateTime }

/** Check that a string value is a valid [OffsetTime]. */
@Suppress("unused")
val JSONExpect.offsetTime: JSONExpect.() -> Unit
    get() = { nodeAsOffsetTime }

/** Check that a string value is a valid [ZonedDateTime]. */
@Suppress("unused")
val JSONExpect.zonedDateTime: JSONExpect.() -> Unit
    get() = { nodeAsZonedDateTime }

/** Check that a string value is a valid [YearMonth]. */
@Suppress("unused")
val JSONExpect.yearMonth: JSONExpect.() -> Unit
    get() = { nodeAsYearMonth }

/** Check that a string value is a valid [MonthDay]. */
@Suppress("unused")
val JSONExpect.monthDay: JSONExpect.() -> Unit
    get() = { nodeAsMonthDay }

/** Check that a string value is a valid [Year]. */
@Suppress("unused")
val JSONExpect.year: JSONExpect.() -> Unit
    get() = { nodeAsYear }

/** Check that a string value is a valid [JavaDuration]. */
@Suppress("unused")
val JSONExpect.javaDuration: JSONExpect.() -> Unit
    get() = { nodeAsJavaDuration }

/** Check that a string value is a valid [Period]. */
@Suppress("unused")
val JSONExpect.period: JSONExpect.() -> Unit
    get() = { nodeAsPeriod }

/** Check that a string value is a valid [Duration]. */
@Suppress("unused")
val JSONExpect.duration: JSONExpect.() -> Unit
    get() = { nodeAsDuration }

/** Check that a value is an array. */
@Suppress("unused")
val JSONExpect.isArray: JSONExpect.() -> Unit
    get() = { nodeAsArray }

/** Check that a value is an array. */
@Suppress("unused")
val JSONExpect.isObject: JSONExpect.() -> Unit
    get() = { nodeAsObject }
