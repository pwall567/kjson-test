/*
 * @(#) Lambda.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2022, 2024, 2025 Peter Wall
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

@file:Suppress("UnusedReceiverParameter")

package io.kjson.test

import kotlin.time.Duration

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

/** Check that a value is null. */
val JSONExpect.isNull: JSONExpect.() -> Unit
    get() = {
        if (node != null)
            error("JSON item is not null - ${showNode()}")
    }

/** Check that a value is non-null. */
val JSONExpect.isNonNull: JSONExpect.() -> Unit
    get() = {
        if (node == null)
            error("JSON item is null")
    }

/** Check that a value is a string. */
val JSONExpect.isString: JSONExpect.() -> Unit
    get() = { nodeAsString }

/** Check that a value is an integer. */
val JSONExpect.isInteger: JSONExpect.() -> Unit
    get() = { nodeAsInt }

/** Check that a value is a long integer. */
val JSONExpect.isLongInteger: JSONExpect.() -> Unit
    get() = { nodeAsLong }

/** Check that a value is a decimal. */
val JSONExpect.isDecimal: JSONExpect.() -> Unit
    get() = {nodeAsDecimal }

/** Check that a value is a boolean. */
val JSONExpect.isBoolean: JSONExpect.() -> Unit
    get() = { nodeAsBoolean }

/** Check that a string value is a valid [UUID]. */
val JSONExpect.isUUID: JSONExpect.() -> Unit
    get() = { nodeAsUUID }

/** Check that a string value is a valid [LocalDate]. */
val JSONExpect.isLocalDate: JSONExpect.() -> Unit
    get() = { nodeAsLocalDate }

/** Check that a string value is a valid [LocalDateTime]. */
val JSONExpect.isLocalDateTime: JSONExpect.() -> Unit
    get() = { nodeAsLocalDateTime }

/** Check that a string value is a valid [LocalTime]. */
val JSONExpect.isLocalTime: JSONExpect.() -> Unit
    get() = { nodeAsLocalTime }

/** Check that a string value is a valid [OffsetDateTime]. */
val JSONExpect.isOffsetDateTime: JSONExpect.() -> Unit
    get() = { nodeAsOffsetDateTime }

/** Check that a string value is a valid [OffsetTime]. */
val JSONExpect.isOffsetTime: JSONExpect.() -> Unit
    get() = { nodeAsOffsetTime }

/** Check that a string value is a valid [ZonedDateTime]. */
val JSONExpect.isZonedDateTime: JSONExpect.() -> Unit
    get() = { nodeAsZonedDateTime }

/** Check that a string value is a valid [YearMonth]. */
val JSONExpect.isYearMonth: JSONExpect.() -> Unit
    get() = { nodeAsYearMonth }

/** Check that a string value is a valid [MonthDay]. */
val JSONExpect.isMonthDay: JSONExpect.() -> Unit
    get() = { nodeAsMonthDay }

/** Check that a string value is a valid [Year]. */
val JSONExpect.isYear: JSONExpect.() -> Unit
    get() = { nodeAsYear }

/** Check that a string value is a valid [JavaDuration]. */
val JSONExpect.isJavaDuration: JSONExpect.() -> Unit
    get() = { nodeAsJavaDuration }

/** Check that a string value is a valid [Period]. */
val JSONExpect.isPeriod: JSONExpect.() -> Unit
    get() = { nodeAsPeriod }

/** Check that a string value is a valid [Duration]. */
val JSONExpect.isDuration: JSONExpect.() -> Unit
    get() = { nodeAsDuration }

/** Check that a value is an array. */
val JSONExpect.isArray: JSONExpect.() -> Unit
    get() = { nodeAsArray }

/** Check that a value is an array. */
val JSONExpect.isObject: JSONExpect.() -> Unit
    get() = { nodeAsObject }

/** Check that a value is an empty array. */
val JSONExpect.isEmptyArray: JSONExpect.() -> Unit
    get() = {
        nodeAsArray.let {
            if (it.isNotEmpty())
                error("JSON array is not empty - size ${it.size}")
        }
    }

/** Check that a value is a non-empty array. */
val JSONExpect.isNonEmptyArray: JSONExpect.() -> Unit
    get() = {
        nodeAsArray.let {
            if (it.isEmpty())
                error("JSON array is empty")
        }
    }

/** Check that a value is an empty object. */
val JSONExpect.isEmptyObject: JSONExpect.() -> Unit
    get() = {
        nodeAsObject.let {
            if (it.isNotEmpty())
                error("JSON object is not empty - size ${it.size}")
        }
    }

/** Check that a value is a non-empty object. */
val JSONExpect.isNonEmptyObject: JSONExpect.() -> Unit
    get() = {
        nodeAsObject.let {
            if (it.isEmpty())
                error("JSON object is empty")
        }
    }
