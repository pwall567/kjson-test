/*
 * @(#) JSONExpect.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2022, 2023 Peter Wall
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

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
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
import java.util.BitSet
import java.util.UUID

import net.pwall.json.JSONFunctions
import net.pwall.json.JSONSimple
import net.pwall.util.DateOutput

/**
 * Implementation class for `expectJSON()` function.
 *
 * @author  Peter Wall
 */
class JSONExpect private constructor(
    /** The context node. */
    val node: Any?,
    /** The context node pointer. */
    val pointer: String? = null,
) {

    private var accessedProperties: MutableSet<String>? = null
    private var accessedItems: BitSet? = null

    /* ====================================== node type conversions ====================================== */

    /** The context node as [Int]. */
    val nodeAsInt: Int
        get() = if (node is Int) node else errorOnType("integer")

    /** The context node as [Long]. */
    val nodeAsLong: Long
        get() = when (node) {
            is Long -> node
            is Int -> node.toLong()
            else -> errorOnType("long integer")
        }

    /** The context node as [BigDecimal]. */
    val nodeAsDecimal: BigDecimal
        get() = when (node) {
            is BigDecimal -> node
            is Long -> BigDecimal(node)
            is Int -> BigDecimal(node)
            else -> errorOnType("decimal")
        }

    /** The context node as [Boolean]. */
    val nodeAsBoolean: Boolean
        get() = if (node is Boolean) node else errorOnType("boolean")

    /** The context node as [String]. */
    val nodeAsString: String
        get() = if (node is String) node else errorOnType("string")

    /** The context node as [Map]. */
    val nodeAsObject: Map<*, *>
        get() = if (node is Map<*, *>) node else errorOnType("object")

    /** The context node as [List]. */
    val nodeAsArray: List<*>
        get() = if (node is List<*>) node else errorOnType("array")

    /** The context node as [LocalDate]. */
    val nodeAsLocalDate: LocalDate
        get() = try {
            LocalDate.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a LocalDate - ${showNode()}")
        }

    /** The context node as [LocalDateTime]. */
    val nodeAsLocalDateTime: LocalDateTime
        get() = try {
            LocalDateTime.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a LocalDateTime - ${showNode()}")
        }

    /** The context node as [LocalTime]. */
    val nodeAsLocalTime: LocalTime
        get() = try {
            LocalTime.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a LocalTime - ${showNode()}")
        }

    /** The context node as [OffsetDateTime]. */
    val nodeAsOffsetDateTime: OffsetDateTime
        get() = try {
            OffsetDateTime.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not an OffsetDateTime - ${showNode()}")
        }

    /** The context node as [OffsetTime]. */
    val nodeAsOffsetTime: OffsetTime
        get() = try {
            OffsetTime.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not an OffsetTime - ${showNode()}")
        }

    /** The context node as [ZonedDateTime]. */
    val nodeAsZonedDateTime: ZonedDateTime
        get() = try {
            ZonedDateTime.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a ZonedDateTime - ${showNode()}")
        }

    /** The context node as [YearMonth]. */
    val nodeAsYearMonth: YearMonth
        get() = try {
            YearMonth.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a YearMonth - ${showNode()}")
        }

    /** The context node as [MonthDay]. */
    val nodeAsMonthDay: MonthDay
        get() = try {
            MonthDay.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a MonthDay - ${showNode()}")
        }

    /** The context node as [Year]. */
    val nodeAsYear: Year
        get() = try {
            Year.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a Year - ${showNode()}")
        }

    /** The context node as [JavaDuration]. */
    val nodeAsJavaDuration: JavaDuration
        get() = try {
            JavaDuration.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a Java Duration - ${showNode()}")
        }

    /** The context node as [Period]. */
    val nodeAsPeriod: Period
        get() = try {
            Period.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a Period - ${showNode()}")
        }

    /** The context node as [Duration]. */
    val nodeAsDuration: Duration
        get() = try {
            Duration.parse(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a Duration - ${showNode()}")
        }

    /** The context node as [UUID]. */
    val nodeAsUUID: UUID
        get() = try {
            UUID.fromString(nodeAsString)
        }
        catch (_: Exception) {
            error("JSON string is not a UUID - ${showNode()}")
        }

    /* ====================================== value tests ====================================== */

    /**
     * Check the value as an [Int].
     *
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Int) {
        if (nodeAsInt != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Long].
     *
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Long) {
        if (nodeAsLong != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [BigDecimal].
     *
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: BigDecimal) {
        if (nodeAsDecimal.compareTo(expected) != 0)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Boolean].
     *
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Boolean) {
        if (nodeAsBoolean != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [String] or `null`.
     *
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: String?) {
        when (expected) {
            null -> {
                if (node != null)
                    errorOnType("null")
            }
            else -> {
                if (nodeAsString != expected)
                    errorOnValue(expected)
            }
        }
    }

    /**
     * Check the value as a [LocalDate].
     *
     * @param   expected        the expected [LocalDate] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: LocalDate) {
        if (nodeAsLocalDate != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [LocalDateTime].
     *
     * @param   expected        the expected [LocalDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: LocalDateTime) {
        if (nodeAsLocalDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [LocalTime].
     *
     * @param   expected        the expected [LocalTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: LocalTime) {
        if (nodeAsLocalTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [OffsetDateTime].
     *
     * @param   expected        the expected [OffsetDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: OffsetDateTime) {
        if (nodeAsOffsetDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [OffsetTime].
     *
     * @param   expected        the expected [OffsetTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: OffsetTime) {
        if (nodeAsOffsetTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [ZonedDateTime].
     *
     * @param   expected        the expected [ZonedDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: ZonedDateTime) {
        if (nodeAsZonedDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [YearMonth].
     *
     * @param   expected        the expected [YearMonth] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: YearMonth) {
        if (nodeAsYearMonth != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Year].
     *
     * @param   expected        the expected [Year] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Year) {
        if (nodeAsYear != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [MonthDay].
     *
     * @param   expected        the expected [MonthDay] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: MonthDay) {
        if (nodeAsMonthDay != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [JavaDuration].
     *
     * @param   expected        the expected [JavaDuration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: JavaDuration) {
        if (nodeAsJavaDuration != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Period].
     *
     * @param   expected        the expected [Period] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Period) {
        if (nodeAsPeriod != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Duration].
     *
     * @param   expected        the expected [Duration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Duration) {
        if (nodeAsDuration != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [UUID].
     *
     * @param   expected        the expected [UUID] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: UUID) {
        if (nodeAsUUID != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [Enum] member.
     *
     * @param   expected        the expected [Enum] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Enum<*>) {
        if (nodeAsString != expected.name)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [String] against a [Regex].
     *
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun value(expected: Regex) {
        if (!(expected matches nodeAsString))
            error("JSON string doesn't match regex - Expected $expected, was ${showNode()}")
    }

    /**
     * Check the value as a member of an [IntRange].
     *
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun value(expected: IntRange) {
        if (nodeAsInt !in expected)
            errorInRange(expected.first, expected.last)
    }

    /**
     * Check the value as a member of a [LongRange].
     *
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun value(expected: LongRange) {
        if (nodeAsLong !in expected)
            errorInRange(expected.first, expected.last)
    }

    /**
     * Check the value as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [ClosedRange]
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> valueInRange(expected: ClosedRange<T>, itemClass: KClass<*>) {
        when (itemClass) {
            Int::class -> {
                if (nodeAsInt as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            Long::class -> {
                if (nodeAsLong as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            BigDecimal::class -> {
                if (nodeAsDecimal as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            String::class -> {
                if (nodeAsString as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            LocalDate::class -> {
                if (nodeAsLocalDate as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            LocalDateTime::class -> {
                if (nodeAsLocalDateTime as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            LocalTime::class -> {
                if (nodeAsLocalTime as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            OffsetDateTime::class -> {
                if (nodeAsOffsetDateTime as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            OffsetTime::class -> {
                if (nodeAsOffsetTime as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            ZonedDateTime::class -> {
                if (nodeAsZonedDateTime as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            YearMonth::class -> {
                if (nodeAsYearMonth as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            MonthDay::class -> {
                if (nodeAsMonthDay as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            Year::class -> {
                if (nodeAsYear as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            JavaDuration::class -> {
                if (nodeAsJavaDuration as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            Duration::class -> {
                if (nodeAsDuration as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            UUID::class -> {
                if (nodeAsUUID as T !in expected)
                    errorInRange(expected.start, expected.endInclusive)
            }
            else -> error("Can't perform test using range of ${itemClass.displayName}")
        }
    }

    /**
     * Check the value as a member of a [ClosedRange].
     *
     * @param   expected        the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T : Comparable<T>> value(expected: ClosedRange<T>) {
        valueInRange(expected, T::class)
    }

    /**
     * Check the value as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> valueInCollection(expected: Collection<T?>, itemClass: KClass<*>) {
        if (node == null) {
            if (!expected.contains(null))
                errorInCollection()
        }
        else {
            when (itemClass) {
                Int::class -> {
                    if (!expected.contains(nodeAsInt as T))
                        errorInCollection()
                }
                Long::class -> {
                    if (!expected.contains(nodeAsLong as T))
                        errorInCollection()
                }
                BigDecimal::class -> {
                    if (!expected.contains(nodeAsDecimal as T))
                        errorInCollection()
                }
                String::class -> {
                    if (!expected.contains(nodeAsString as T))
                        errorInCollection()
                }
                LocalDate::class -> {
                    if (!expected.contains(nodeAsLocalDate as T))
                        errorInCollection()
                }
                LocalDateTime::class -> {
                    if (!expected.contains(nodeAsLocalDateTime as T))
                        errorInCollection()
                }
                LocalTime::class -> {
                    if (!expected.contains(nodeAsLocalTime as T))
                        errorInCollection()
                }
                OffsetDateTime::class -> {
                    if (!expected.contains(nodeAsOffsetDateTime as T))
                        errorInCollection()
                }
                OffsetTime::class -> {
                    if (!expected.contains(nodeAsOffsetTime as T))
                        errorInCollection()
                }
                ZonedDateTime::class -> {
                    if (!expected.contains(nodeAsZonedDateTime as T))
                        errorInCollection()
                }
                YearMonth::class -> {
                    if (!expected.contains(nodeAsYearMonth as T))
                        errorInCollection()
                }
                MonthDay::class -> {
                    if (!expected.contains(nodeAsMonthDay as T))
                        errorInCollection()
                }
                Year::class -> {
                    if (!expected.contains(nodeAsYear as T))
                        errorInCollection()
                }
                JavaDuration::class -> {
                    if (!expected.contains(nodeAsJavaDuration as T))
                        errorInCollection()
                }
                Period::class -> {
                    if (!expected.contains(nodeAsPeriod as T))
                        errorInCollection()
                }
                Duration::class -> {
                    if (!expected.contains(nodeAsDuration as T))
                        errorInCollection()
                }
                UUID::class -> {
                    if (!expected.contains(nodeAsUUID as T))
                        errorInCollection()
                }
                else -> {
                    if (itemClass.isSubclassOf(Enum::class)) {
                        val stringValue = nodeAsString
                        if (!expected.any { (it as Enum<*>).name == stringValue })
                            errorInCollection()
                    }
                    else
                        error("Can't perform test using collection of ${itemClass.displayName}")
                }
            }
        }
    }

    /**
     * Check the value as a member of a [Collection].
     *
     * @param   expected        the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T : Any> value(expected: Collection<T?>) {
        valueInCollection(expected, T::class)
    }

    /**
     * Apply pre-configured tests to the value.
     *
     * @param   tests           the tests
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun value(tests: JSONExpect.() -> Unit) {
        tests.invoke(this)
    }

    /**
     * Check that the value is an object and optionally apply pre-configured tests.
     *
     * @param   tests           the tests
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun valueIsObject(tests: JSONExpect.() -> Unit = {}) {
        nodeAsObject
        tests.invoke(this)
    }

    /**
     * Check that the value is an array and optionally apply pre-configured tests.
     *
     * @param   tests           the tests
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun valueIsArray(tests: JSONExpect.() -> Unit = {}) {
        nodeAsArray
        tests.invoke(this)
    }

    /**
     * Check that the value is an array of the specified size and optionally apply nested tests.
     *
     * @param   size            the expected size of the array
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun valueIsArray(size: Int, tests: JSONExpect.() -> Unit = {}) {
        require(size >= 0) { "JSON array size must not be negative" }
        val arraySize = nodeAsArray.size
        if (arraySize != size)
            error("JSON array size doesn't match - Expected $size, was $arraySize")
        tests.invoke(this)
    }

    /* ====================================== property tests ====================================== */

    /**
     * Check a property as an [Int].
     *
     * @param   name            the property name
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Int) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Long].
     *
     * @param   name            the property name
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Long) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [BigDecimal].
     *
     * @param   name            the property name
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: BigDecimal) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Boolean].
     *
     * @param   name            the property name
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Boolean) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] or `null`.
     *
     * @param   name            the property name
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: String?) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalDate].
     *
     * @param   name            the property name
     * @param   expected        the expected [LocalDate] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: LocalDate) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalDateTime].
     *
     * @param   name            the property name
     * @param   expected        the expected [LocalDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: LocalDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalTime].
     *
     * @param   name            the property name
     * @param   expected        the expected [LocalTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: LocalTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [OffsetDateTime].
     *
     * @param   name            the property name
     * @param   expected        the expected [OffsetDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: OffsetDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [OffsetTime].
     *
     * @param   name            the property name
     * @param   expected        the expected [OffsetTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: OffsetTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [ZonedDateTime].
     *
     * @param   name            the property name
     * @param   expected        the expected [ZonedDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: ZonedDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [YearMonth].
     *
     * @param   name            the property name
     * @param   expected        the expected [YearMonth] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: YearMonth) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [MonthDay].
     *
     * @param   name            the property name
     * @param   expected        the expected [MonthDay] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: MonthDay) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Year].
     *
     * @param   name            the property name
     * @param   expected        the expected [Year] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Year) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [JavaDuration].
     *
     * @param   name            the property name
     * @param   expected        the expected [JavaDuration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: JavaDuration) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Period].
     *
     * @param   name            the property name
     * @param   expected        the expected [Period] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Period) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Duration].
     *
     * @param   name            the property name
     * @param   expected        the expected [Duration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Duration) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [UUID].
     *
     * @param   name            the property name
     * @param   expected        the expected [UUID] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: UUID) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [Enum] member.
     *
     * @param   name            the property name
     * @param   expected        the expected [Enum] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Enum<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] against a [Regex].
     *
     * @param   name            the property name
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun property(name: String, expected: Regex) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of an [IntRange].
     *
     * @param   name            the property name
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun property(name: String, expected: IntRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [LongRange].
     *
     * @param   name            the property name
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun property(name: String, expected: LongRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   name            the property name
     * @param   expected        the [ClosedRange]
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T : Comparable<T>> propertyInRange(name: String, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueInRange(expected, itemClass)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].
     *
     * @param   name            the property name
     * @param   expected        the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T : Comparable<T>> property(name: String, expected: ClosedRange<T>) {
        propertyInRange(name, expected, T::class)
    }

    /**
     * Check a property as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   name            the property name
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T : Any> propertyInCollection(name: String, expected: Collection<T?>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueInCollection(expected, itemClass)
        }
    }

    /**
     * Check a property as a member of a [Collection].
     *
     * @param   name            the property name
     * @param   expected        the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T : Any> property(name: String, expected: Collection<T?>) {
        propertyInCollection(name, expected, T::class)
    }

    /**
     * Select a property for nested tests.
     *
     * @param   name            the property name
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun property(name: String, tests: JSONExpect.() -> Unit) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).tests()
        }
    }

    /**
     * Check that a property is an object and optionally apply nested tests.
     *
     * @param   name            the property name
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun propertyIsObject(name: String, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsObject(tests)
        }
    }

    /**
     * Check that a property is an array and optionally apply nested tests.
     *
     * @param   name            the property name
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun propertyIsArray(name: String, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsArray(tests)
        }
    }

    /**
     * Check that a property is an array of the specified size and optionally apply nested tests.
     *
     * @param   name            the property name
     * @param   size            the expected size of the array
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun propertyIsArray(name: String, size: Int, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsArray(size, tests)
        }
    }

    /**
     * Check that a property is absent from an object.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the property is present
     */
    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject.containsKey(name))
            error("JSON property not absent - $name")
        accessedProperties?.add(name)
    }

    /**
     * Check that a property is absent from an object, or if present, is `null`.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the property is present and not `null`
     */
    fun propertyAbsentOrNull(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject[name] != null)
            error("JSON property not absent or null - $name")
        accessedProperties?.add(name)
    }

    /**
     * Check that a property is present in an object.
     *
     * @param   name            the property name
     * @throws  AssertionError  if the property is absent
     */
    fun propertyPresent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (!nodeAsObject.containsKey(name))
            error("JSON property not present - $name")
        accessedProperties?.add(name)
    }

    /* ====================================== item tests ====================================== */

    /**
     * Check an array item as an [Int].
     *
     * @param   index           the array index
     * @param   expected        the expected [Int] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Int) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Long].
     *
     * @param   index           the array index
     * @param   expected        the expected [Long] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Long) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [BigDecimal].
     *
     * @param   index           the array index
     * @param   expected        the expected [BigDecimal] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: BigDecimal) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Boolean].
     *
     * @param   index           the array index
     * @param   expected        the expected [Boolean] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Boolean) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [String] or `null`.
     *
     * @param   index           the array index
     * @param   expected        the expected [String] value (or `null`)
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: String?) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalDate].
     *
     * @param   index           the array index
     * @param   expected        the expected [LocalDate] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: LocalDate) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalDateTime].
     *
     * @param   index           the array index
     * @param   expected        the expected [LocalDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: LocalDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalTime].
     *
     * @param   index           the array index
     * @param   expected        the expected [LocalTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: LocalTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [OffsetDateTime].
     *
     * @param   index           the array index
     * @param   expected        the expected [OffsetDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: OffsetDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [OffsetTime].
     *
     * @param   index           the array index
     * @param   expected        the expected [OffsetTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: OffsetTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [ZonedDateTime].
     *
     * @param   index           the array index
     * @param   expected        the expected [ZonedDateTime] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: ZonedDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [YearMonth].
     *
     * @param   index           the array index
     * @param   expected        the expected [YearMonth] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: YearMonth) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [MonthDay].
     *
     * @param   index           the array index
     * @param   expected        the expected [MonthDay] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: MonthDay) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Year].
     *
     * @param   index           the array index
     * @param   expected        the expected [Year] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Year) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [JavaDuration].
     *
     * @param   index           the array index
     * @param   expected        the expected [JavaDuration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: JavaDuration) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Period].
     *
     * @param   index           the array index
     * @param   expected        the expected [Period] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Period) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Duration].
     *
     * @param   index           the array index
     * @param   expected        the expected [Duration] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Duration) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [UUID].
     *
     * @param   index           the array index
     * @param   expected        the expected [UUID] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: UUID) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [Enum] member.
     *
     * @param   index           the array index
     * @param   expected        the expected [Enum] value
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Enum<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] against a [Regex].
     *
     * @param   index           the array index
     * @param   expected        the [Regex]
     * @throws  AssertionError  if the value is incorrect
     */
    fun item(index: Int, expected: Regex) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of an [IntRange].
     *
     * @param   index           the array index
     * @param   expected        the [IntRange]
     * @throws  AssertionError  if the value is not within the [IntRange]
     */
    fun item(index: Int, expected: IntRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [LongRange].
     *
     * @param   index           the array index
     * @param   expected        the [LongRange]
     * @throws  AssertionError  if the value is not within the [LongRange]
     */
    fun item(index: Int, expected: LongRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     *
     * @param   index           the array index
     * @param   expected        the [ClosedRange]
     * @param   itemClass       the class of the elements of the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    fun <T : Comparable<T>> itemInRange(index: Int, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueInRange(expected, itemClass)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].
     *
     * @param   index           the array index
     * @param   expected        the [ClosedRange]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value is not within the [ClosedRange]
     */
    inline fun <reified T : Comparable<T>> item(index: Int, expected: ClosedRange<T>) {
        itemInRange(index, expected, T::class)
    }

    /**
     * Check an array item as a member of a [Collection].  This will normally be invoked via the inline function.
     *
     * @param   index           the array index
     * @param   expected        the [Collection]
     * @param   itemClass       the class of the elements of the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    fun <T : Any> itemInCollection(index: Int, expected: Collection<T?>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueInCollection(expected, itemClass)
        }
    }

    /**
     * Check an array item as a member of a [Collection].
     *
     * @param   index           the array index
     * @param   expected        the [Collection]
     * @param   T               the type of the value
     * @throws  AssertionError  if the value does not match any element of the [Collection]
     */
    inline fun <reified T : Any> item(index: Int, expected: Collection<T?>) {
        itemInCollection(index, expected, T::class)
    }

    /**
     * Select an array item for nested tests.
     *
     * @param   index           the array index
     * @param   tests           the tests to be performed on the item
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun item(index: Int, tests: JSONExpect.() -> Unit) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).tests()
        }
    }

    /**
     * Check that an array item is an object and optionally apply nested tests.
     *
     * @param   index           the array index
     * @param   tests           the tests to be performed on the item
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun itemIsObject(index: Int, tests: JSONExpect.() -> Unit = {}) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueIsObject(tests)
        }
    }

    /**
     * Check that an array item is an array and optionally apply nested tests.
     *
     * @param   index           the array index
     * @param   tests           the tests to be performed on the item
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun itemIsArray(index: Int, tests: JSONExpect.() -> Unit = {}) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueIsArray(tests)
        }
    }

    /**
     * Check that an array item is an array of the specified size and optionally apply nested tests.
     *
     * @param   index           the array index
     * @param   size            the expected size of the array
     * @param   tests           the tests to be performed on the property
     * @throws  AssertionError  if thrown by any of the tests
     */
    fun itemIsArray(index: Int, size: Int, tests: JSONExpect.() -> Unit = {}) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueIsArray(size, tests)
        }
    }

    /* ====================================== anyItem tests ====================================== */

    /**
     * Check that any item in array matches an [Int].
     */
    fun anyItem(expected: Int) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Long].
     */
    fun anyItem(expected: Long) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [BigDecimal].
     */
    fun anyItem(expected: BigDecimal) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Boolean].
     */
    fun anyItem(expected: Boolean) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [String] or `null`.
     */
    fun anyItem(expected: String?) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [LocalDate].
     */
    fun anyItem(expected: LocalDate) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [LocalDateTime].
     */
    fun anyItem(expected: LocalDateTime) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [LocalTime].
     */
    fun anyItem(expected: LocalTime) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches an [OffsetDateTime].
     */
    fun anyItem(expected: OffsetDateTime) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches an [OffsetTime].
     */
    fun anyItem(expected: OffsetTime) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [ZonedDateTime].
     */
    fun anyItem(expected: ZonedDateTime) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [YearMonth].
     */
    fun anyItem(expected: YearMonth) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [MonthDay].
     */
    fun anyItem(expected: MonthDay) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Year].
     */
    fun anyItem(expected: Year) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [JavaDuration].
     */
    fun anyItem(expected: JavaDuration) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Period].
     */
    fun anyItem(expected: Period) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Duration].
     */
    fun anyItem(expected: Duration) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [UUID].
     */
    fun anyItem(expected: UUID) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches an [Enum] member.
     */
    fun anyItem(expected: Enum<*>) {
        anyItemCheck { value(expected) } || errorOnAnyItem(expected)
    }

    /**
     * Check that any item in array matches a [Regex].
     */
    fun anyItem(expected: Regex) {
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.MATCHING_REGEX)
    }

    /**
     * Check that any item in array is a member of an [IntRange].
     */
    fun anyItem(expected: IntRange) {
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.IN_RANGE)
    }

    /**
     * Check that any item in array is a member of a [LongRange].
     */
    fun anyItem(expected: LongRange) {
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.IN_RANGE)
    }

    /**
     * Check that any item in array is a member of a [ClosedRange].  This will normally be invoked via the inline
     * function.
     */
    fun <T : Comparable<T>> anyItemInRange(expected: ClosedRange<T>, itemClass: KClass<*>) {
        anyItemCheck { valueInRange(expected, itemClass) } || errorOnAnyItem(ErrorEnum.IN_RANGE)
    }

    /**
     * Check that any item in array is a member of a [ClosedRange].
     */
    inline fun <reified T : Comparable<T>> anyItem(expected: ClosedRange<T>) {
        anyItemInRange(expected, T::class)
    }

    /**
     * Check that any item in array is a member of a [Collection].  This will normally be invoked via the inline
     * function.
     */
    fun <T : Any> anyItemInCollection(expected: Collection<T?>, itemClass: KClass<*>) {
        anyItemCheck { valueInCollection(expected, itemClass) } || errorOnAnyItem(ErrorEnum.IN_COLLECTION)
    }

    /**
     * Check that any item in array is a member of a [Collection].
     */
    inline fun <reified T : Any> anyItem(expected: Collection<T?>) {
        anyItemInCollection(expected, T::class)
    }

    /**
     * Check that any item in array matches the given tests.
     */
    fun anyItem(tests: JSONExpect.() -> Unit) {
        anyItemCheck { value(tests) } || errorOnAnyItem(ErrorEnum.VALUE_MATCHING)
    }

    /**
     * Check that any item in array is an object and apply optional tests.
     */
    fun anyItemIsObject(tests: JSONExpect.() -> Unit = {}) {
        anyItemCheck { valueIsObject(tests) } || errorOnAnyItem(ErrorEnum.VALUE_MATCHING)
    }

    /**
     * Check that any item in array is an array and apply optional tests.
     */
    fun anyItemIsArray(tests: JSONExpect.() -> Unit = {}) {
        anyItemCheck { valueIsArray(tests) } || errorOnAnyItem(ErrorEnum.VALUE_MATCHING)
    }

    /**
     * Check that any item in array is an array of the specified size and apply optional tests.
     */
    fun anyItemIsArray(size: Int, tests: JSONExpect.() -> Unit = {}) {
        anyItemCheck { valueIsArray(size, tests) } || errorOnAnyItem(ErrorEnum.VALUE_MATCHING)
    }

    private fun anyItemCheck(itemTest: JSONExpect.() -> Unit): Boolean {
        nodeAsArray.let {
            for (i in it.indices) {
                try {
                    JSONExpect(it[i], itemPointer(i)).itemTest()
                    accessedItems?.set(i)
                    return true
                }
                catch (_: AssertionError) {}
            }
            return false
        }
    }

    /* ====================================== "test" lambda functions ====================================== */

    /**
     * Convert an [Int] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Int] value
     * @return              the lambda
     */
    fun test(expected: Int): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Long] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Long] value
     * @return              the lambda
     */
    fun test(expected: Long): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [BigDecimal] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [BigDecimal] value
     * @return              the lambda
     */
    fun test(expected: BigDecimal): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Boolean] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Boolean] value
     * @return              the lambda
     */
    fun test(expected: Boolean): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [String] or `null` equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [String] value (or `null`)
     * @return              the lambda
     */
    fun test(expected: String?): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalDate] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [LocalDate] value
     * @return              the lambda
     */
    fun test(expected: LocalDate): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalDateTime] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [LocalDateTime] value
     * @return              the lambda
     */
    fun test(expected: LocalDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalTime] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [LocalTime] value
     * @return              the lambda
     */
    fun test(expected: LocalTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [OffsetDateTime] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [OffsetDateTime] value
     * @return              the lambda
     */
    fun test(expected: OffsetDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [OffsetTime] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [OffsetTime] value
     * @return              the lambda
     */
    fun test(expected: OffsetTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [ZonedDateTime] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [ZonedDateTime] value
     * @return              the lambda
     */
    fun test(expected: ZonedDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [YearMonth] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [YearMonth] value
     * @return              the lambda
     */
    fun test(expected: YearMonth): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [MonthDay] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [MonthDay] value
     * @return              the lambda
     */
    fun test(expected: MonthDay): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Year] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Year] value
     * @return              the lambda
     */
    fun test(expected: Year): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [JavaDuration] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [JavaDuration] value
     * @return              the lambda
     */
    fun test(expected: JavaDuration): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Period] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Period] value
     * @return              the lambda
     */
    fun test(expected: Period): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Duration] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Duration] value
     * @return              the lambda
     */
    fun test(expected: Duration): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [UUID] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [UUID] value
     * @return              the lambda
     */
    fun test(expected: UUID): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [Enum] equality check to a lambda for use in a multiple test check.
     *
     * @param   expected    the expected [Enum] value
     * @return              the lambda
     */
    fun test(expected: Enum<*>): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [String] [Regex] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [Regex]
     * @return              the lambda
     */
    fun test(expected: Regex): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [Int] range check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [IntRange]
     * @return              the lambda
     */
    fun test(expected: IntRange): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Long] range check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [LongRange]
     * @return              the lambda
     */
    fun test(expected: LongRange): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [ClosedRange] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [ClosedRange]
     * @param   T           the type of the value
     * @return              the lambda
     */
    inline fun <reified T : Comparable<T>> test(expected: ClosedRange<T>): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Collection] check to a lambda for use in a multiple test check.
     *
     * @param   expected    the [Collection]
     * @param   T           the type of the value
     * @return              the lambda
     */
    inline fun <reified T : Any> test(expected: Collection<T>): JSONExpect.() -> Unit = { value(expected) }

    /* ====================================== other functions ====================================== */

    /**
     * Check the contents of an object or array exhaustively, that is, perform all checks and then confirm that every
     * object property or array item has been tested.
     *
     * @param   tests           the tests to be performed on the item
     * @throws  AssertionError  if there are untested object properties or array items
     */
    fun exhaustive(tests: JSONExpect.() -> Unit) {
        when (node) {
            is Map<*, *> -> {
                val accessed = mutableSetOf<String>()
                accessedProperties = accessed
                tests.invoke(this)
                val notAccessed = node.keys.filter { it !in accessed }
                if (notAccessed.isNotEmpty())
                    error("JSON ${propertiesText(notAccessed.size)} not tested: ${notAccessed.joinToString(", ")}")
            }
            is List<*> -> {
                val accessed = BitSet()
                accessedItems = accessed
                tests.invoke(this)
                val notAccessed = (0 until node.size).filter { !accessed[it] }
                if (notAccessed.isNotEmpty())
                    error("JSON ${itemsText(notAccessed.size)} not tested: ${notAccessed.joinToString(", ")}")
            }
            else -> errorOnType("object or array")
        }
    }

    /**
     * Check that the value matches one of a number of tests.
     *
     * @param   tests           the tests (as lambdas)
     * @throws  AssertionError  if all of the tests fail
     */
    fun oneOf(vararg tests: JSONExpect.() -> Unit) {
        for (test in tests) {
            try {
                test.invoke(this)
                return
            }
            catch (_: AssertionError) {}
        }
        error("No successful test - value is ${showNode()}")
    }

    /**
     * Check the count of array items or object properties.
     *
     * @param   expected        the expected count
     * @throws  AssertionError  if the value is incorrect
     */
    fun count(expected: Int) {
        require(expected >= 0) { "JSON array or object count must not be negative" }
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length != expected)
            error("JSON count doesn't match - Expected $expected, was $length")
    }

    /**
     * Check the count of array items or object properties as a range.
     *
     * @param   expected        the expected range
     * @throws  AssertionError  if the value is incorrect
     */
    fun count(expected: IntRange) {
        require(expected.first >= 0) { "JSON array or object count must not be negative" }
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length !in expected)
            error("JSON count doesn't match - Expected $expected, was $length")
    }

    /**
     * Check the length of a string value.
     *
     * @param   expected        the expected length
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: Int): JSONExpect.() -> Unit = {
        nodeAsString.length.let {
            if (it != expected)
                error("JSON string length doesn't match - Expected $expected, was $it")
        }
    }

    /**
     * Check the length of a string value as a range.
     *
     * @param   expected        the expected length as a range
     * @throws  AssertionError  if the length is incorrect
     */
    fun length(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsString.length.let {
            if (it !in expected)
                error("JSON string length doesn't match - Expected $expected, was $it")
        }
    }

    /**
     * Check the scale of a decimal value.
     *
     * @param   expected        the expected scale
     * @throws  AssertionError  if the scale is incorrect
     */
    fun scale(expected: Int): JSONExpect.() -> Unit = {
        nodeAsDecimal.scale().let {
            if (it != expected)
                error("JSON decimal scale doesn't match - Expected $expected, was $it")
        }
    }

    /**
     * Check the scale of a decimal value as a range.
     *
     * @param   expected        the expected scale as a range
     * @throws  AssertionError  if the scale is incorrect
     */
    fun scale(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsDecimal.scale().let {
            if (it !in expected)
                error("JSON decimal scale doesn't match - Expected $expected, was $it")
        }
    }

    /**
     * Report error, including context path.
     *
     * @param   message     the error message
     */
    fun error(message: String): Nothing {
        throw AssertionError(pointer?.let { "$it: $message" } ?: message)
    }

    /**
     * Create a display form of the current node, for use in error messages.
     *
     * @return      a text string describing the node
     */
    fun showNode() = showValue(node)

    /**
     * Create a display form of a value, for use in error messages.
     *
     * @return      a text string describing the value
     */
    fun showValue(value: Any?): String = when (value) {
        null -> "null"
        is ErrorEnum -> value.text
        is Number,
        is Boolean -> value.toString()
        is LocalDate -> showFormatted(value, DateOutput::appendLocalDate)
        is LocalDateTime -> showFormatted(value, DateOutput::appendLocalDateTime)
        is LocalTime -> showFormatted(value, DateOutput::appendLocalTime)
        is OffsetDateTime -> showFormatted(value, DateOutput::appendOffsetDateTime)
        is OffsetTime-> showFormatted(value, DateOutput::appendOffsetTime)
        is YearMonth -> showFormatted(value, DateOutput::appendYearMonth)
        is MonthDay -> showFormatted(value, DateOutput::appendMonthDay)
        is Year -> showFormatted(value, DateOutput::appendYear)
        is List<*> -> "[...]"
        is Map<*, *> -> "{...}"
        else -> JSONFunctions.displayString(value.toString(), maxStringDisplayLength)
    }

    enum class ErrorEnum(val text: String) {
        VALUE_MATCHING("matching given tests"),
        MATCHING_REGEX("matching given Regex"),
        IN_RANGE("in range"),
        IN_COLLECTION("in collection"),
    }

    private fun <T : Any> showFormatted(value: T, outputFunction: (Appendable, T) -> Unit): String =
        StringBuilder().apply {
            append('"')
            outputFunction(this, value)
            append('"')
        }.toString()

    private fun errorOnValue(expected: Any?): Nothing {
        error("JSON value doesn't match - expected ${showValue(expected)}, was ${showNode()}")
    }

    private fun errorOnAnyItem(expected: Any?): Nothing {
        error("No JSON array item has value ${showValue(expected)}")
    }

    internal fun errorOnType(expected: String): Nothing {
        val type = when (node) {
            null -> "null"
            is Int -> "integer"
            is Long -> "long integer"
            is BigDecimal -> "decimal"
            is String -> "string"
            is Boolean -> "boolean"
            is List<*> -> "array"
            is Map<*, *> -> "object"
            else -> "unknown"
        }
        error("JSON type doesn't match - expected $expected, was $type")
    }

    private fun errorInCollection() {
        error("JSON value not in collection - ${showNode()}")
    }

    private fun errorInRange(lo: Any, hi: Any) {
        error("JSON value not in range ${showValue(lo)}..${showValue(hi)} - ${showNode()}")
    }

    private fun checkName(name: String): String = name.ifEmpty { error("JSON property name must not be empty") }

    private fun checkIndex(index: Int): Int =
            if (index >= 0) index else error("JSON array index must not be negative - $index")

    private fun getProperty(name: String): Any? = nodeAsObject.let {
        if (!it.containsKey(name))
            error("JSON property missing - $name")
        accessedProperties?.add(name)
        it[name]
    }

    private fun getItem(index: Int): Any? = nodeAsArray.let {
        if (index !in it.indices)
            error("JSON array index out of bounds - $index")
        accessedItems?.set(index)
        it[index]
    }

    private fun propertyPointer(name: String) = if (pointer != null) "$pointer/$name" else "/$name"

    private fun itemPointer(index: Int) = if (pointer != null) "$pointer/$index" else "/$index"

    private fun propertiesText(count: Int) = if (count == 1) "object property" else "object properties"

    private fun itemsText(count: Int) = if (count == 1) "array item" else "array items"

    companion object {

        const val maxStringDisplayLength = 49

        /**
         * Check that a JSON string matches the defined expectations.
         *
         * @param   json            the JSON string
         * @param   tests           the tests to be performed on the JSON
         * @throws  AssertionError  if any of the tests fail (including failure to parse the JSON)
         */
        fun expectJSON(json: String, tests: JSONExpect.() -> Unit) {
            val obj = try {
                JSONSimple.parse(json)
            }
            catch (e: Exception) {
                throw AssertionError("Unable to parse JSON - ${e.message}")
            }
            JSONExpect(obj).tests()
        }

        private val KClass<*>.displayName: String
            get() = when (val qName = qualifiedName) {
                null -> "<unknown>"
                else -> if (qName.startsWith("kotlin.") && qName.indexOf('.', 7) < 0) qName.drop(7) else qName
            }

    }

}
