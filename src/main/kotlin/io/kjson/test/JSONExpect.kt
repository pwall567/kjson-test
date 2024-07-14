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
     */
    fun value(expected: Int) {
        if (nodeAsInt != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Long].
     */
    fun value(expected: Long) {
        if (nodeAsLong != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [BigDecimal].
     */
    fun value(expected: BigDecimal) {
        if (nodeAsDecimal.compareTo(expected) != 0)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Boolean].
     */
    fun value(expected: Boolean) {
        if (nodeAsBoolean != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Char].
     */
    fun value(expected: Char) {
        nodeAsString.let {
            if (it.length != 1 || it[0] != expected)
                errorOnValue(expected)
        }
    }

    /**
     * Check the value as a [String] or `null`.
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
     */
    fun value(expected: LocalDate) {
        if (nodeAsLocalDate != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [LocalDateTime].
     */
    fun value(expected: LocalDateTime) {
        if (nodeAsLocalDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [LocalTime].
     */
    fun value(expected: LocalTime) {
        if (nodeAsLocalTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [OffsetDateTime].
     */
    fun value(expected: OffsetDateTime) {
        if (nodeAsOffsetDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [OffsetTime].
     */
    fun value(expected: OffsetTime) {
        if (nodeAsOffsetTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [ZonedDateTime].
     */
    fun value(expected: ZonedDateTime) {
        if (nodeAsZonedDateTime != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [YearMonth].
     */
    fun value(expected: YearMonth) {
        if (nodeAsYearMonth != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Year].
     */
    fun value(expected: Year) {
        if (nodeAsYear != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [MonthDay].
     */
    fun value(expected: MonthDay) {
        if (nodeAsMonthDay != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [JavaDuration].
     */
    fun value(expected: JavaDuration) {
        if (nodeAsJavaDuration != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Period].
     */
    fun value(expected: Period) {
        if (nodeAsPeriod != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [Duration].
     */
    fun value(expected: Duration) {
        if (nodeAsDuration != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [UUID].
     */
    fun value(expected: UUID) {
        if (nodeAsUUID != expected)
            errorOnValue(expected)
    }

    /**
     * Check the value as an [Enum] member.
     */
    fun value(expected: Enum<*>) {
        if (nodeAsString != expected.name)
            errorOnValue(expected)
    }

    /**
     * Check the value as a [String] against a [Regex].
     */
    fun value(expected: Regex) {
        if (!(expected matches nodeAsString))
            error("JSON string doesn't match regex - Expected $expected, was ${showNode()}")
    }

    /**
     * Check the value as a member of an [IntRange].
     */
    fun value(expected: IntRange) {
        if (nodeAsInt !in expected)
            errorInRange(expected.first, expected.last)
    }

    /**
     * Check the value as a member of a [LongRange].
     */
    fun value(expected: LongRange) {
        if (nodeAsLong !in expected)
            errorInRange(expected.first, expected.last)
    }

    /**
     * Check the value as a member of a [ClosedRange].  This will normally be invoked via the inline function.
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
     */
    inline fun <reified T : Comparable<T>> value(expected: ClosedRange<T>) {
        valueInRange(expected, T::class)
    }

    /**
     * Check the value as a member of a [Collection].  This will normally be invoked via the inline function.
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
     */
    inline fun <reified T : Any> value(expected: Collection<T?>) {
        valueInCollection(expected, T::class)
    }

    /**
     * Apply pre-configured tests to the value.
     */
    fun value(tests: JSONExpect.() -> Unit) {
        tests.invoke(this)
    }

    /**
     * Check that the value is an object and optionally apply pre-configured tests.
     */
    fun valueIsObject(tests: JSONExpect.() -> Unit = {}) {
        nodeAsObject
        tests.invoke(this)
    }

    /**
     * Check that the value is an array and optionally apply pre-configured tests.
     */
    fun valueIsArray(tests: JSONExpect.() -> Unit = {}) {
        nodeAsArray
        tests.invoke(this)
    }

    /**
     * Check that the value is an array of the specified size and optionally apply nested tests.
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
     */
    fun property(name: String, expected: Int) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Long].
     */
    fun property(name: String, expected: Long) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [BigDecimal].
     */
    fun property(name: String, expected: BigDecimal) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Boolean].
     */
    fun property(name: String, expected: Boolean) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Char].
     */
    fun property(name: String, expected: Char) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] or `null`.
     */
    fun property(name: String, expected: String?) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalDate].
     */
    fun property(name: String, expected: LocalDate) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalDateTime].
     */
    fun property(name: String, expected: LocalDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [LocalTime].
     */
    fun property(name: String, expected: LocalTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [OffsetDateTime].
     */
    fun property(name: String, expected: OffsetDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [OffsetTime].
     */
    fun property(name: String, expected: OffsetTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [ZonedDateTime].
     */
    fun property(name: String, expected: ZonedDateTime) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [YearMonth].
     */
    fun property(name: String, expected: YearMonth) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [MonthDay].
     */
    fun property(name: String, expected: MonthDay) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Year].
     */
    fun property(name: String, expected: Year) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [JavaDuration].
     */
    fun property(name: String, expected: JavaDuration) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Period].
     */
    fun property(name: String, expected: Period) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [Duration].
     */
    fun property(name: String, expected: Duration) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [UUID].
     */
    fun property(name: String, expected: UUID) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as an [Enum] member.
     */
    fun property(name: String, expected: Enum<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] against a [Regex].
     */
    fun property(name: String, expected: Regex) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of an [IntRange].
     */
    fun property(name: String, expected: IntRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [LongRange].
     */
    fun property(name: String, expected: LongRange) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     */
    fun <T : Comparable<T>> propertyInRange(name: String, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueInRange(expected, itemClass)
        }
    }

    /**
     * Check a property as a member of a [ClosedRange].
     */
    inline fun <reified T : Comparable<T>> property(name: String, expected: ClosedRange<T>) {
        propertyInRange(name, expected, T::class)
    }

    /**
     * Check a property as a member of a [Collection].  This will normally be invoked via the inline function.
     */
    fun <T : Any> propertyInCollection(name: String, expected: Collection<T?>, itemClass: KClass<*>) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueInCollection(expected, itemClass)
        }
    }

    /**
     * Check a property as a member of a [Collection].
     */
    inline fun <reified T : Any> property(name: String, expected: Collection<T?>) {
        propertyInCollection(name, expected, T::class)
    }

    /**
     * Select a property for nested tests.
     */
    fun property(name: String, tests: JSONExpect.() -> Unit) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).tests()
        }
    }

    /**
     * Check that a property is an object and optionally apply nested tests.
     */
    fun propertyIsObject(name: String, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsObject(tests)
        }
    }

    /**
     * Check that a property is an array and optionally apply nested tests.
     */
    fun propertyIsArray(name: String, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsArray(tests)
        }
    }

    /**
     * Check that a property is an array of the specified size and optionally apply nested tests.
     */
    fun propertyIsArray(name: String, size: Int, tests: JSONExpect.() -> Unit = {}) {
        checkName(name).let {
            JSONExpect(getProperty(it), propertyPointer(it)).valueIsArray(size, tests)
        }
    }

    /**
     * Check that a property is absent from an object.
     */
    fun propertyAbsent(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject.containsKey(name))
            error("JSON property not absent - $name")
        accessedProperties?.add(name)
    }

    /**
     * Check that a property is absent from an object, or if present, is `null`.
     */
    fun propertyAbsentOrNull(name: String) {
        require(name.isNotEmpty()) { "JSON property name must not be empty" }
        if (nodeAsObject[name] != null)
            error("JSON property not absent or null - $name")
        accessedProperties?.add(name)
    }

    /**
     * Check that a property is present in an object.
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
     */
    fun item(index: Int, expected: Int) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Long].
     */
    fun item(index: Int, expected: Long) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [BigDecimal].
     */
    fun item(index: Int, expected: BigDecimal) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Boolean].
     */
    fun item(index: Int, expected: Boolean) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Char].
     */
    fun item(index: Int, expected: Char) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [String] or `null`.
     */
    fun item(index: Int, expected: String?) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalDate].
     */
    fun item(index: Int, expected: LocalDate) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalDateTime].
     */
    fun item(index: Int, expected: LocalDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [LocalTime].
     */
    fun item(index: Int, expected: LocalTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [OffsetDateTime].
     */
    fun item(index: Int, expected: OffsetDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [OffsetTime].
     */
    fun item(index: Int, expected: OffsetTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [ZonedDateTime].
     */
    fun item(index: Int, expected: ZonedDateTime) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [YearMonth].
     */
    fun item(index: Int, expected: YearMonth) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [MonthDay].
     */
    fun item(index: Int, expected: MonthDay) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Year].
     */
    fun item(index: Int, expected: Year) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [JavaDuration].
     */
    fun item(index: Int, expected: JavaDuration) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Period].
     */
    fun item(index: Int, expected: Period) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [Duration].
     */
    fun item(index: Int, expected: Duration) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a [UUID].
     */
    fun item(index: Int, expected: UUID) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as an [Enum] member.
     */
    fun item(index: Int, expected: Enum<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check a property as a [String] against a [Regex].
     */
    fun item(index: Int, expected: Regex) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of an [IntRange].
     */
    fun item(index: Int, expected: IntRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [LongRange].
     */
    fun item(index: Int, expected: LongRange) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).value(expected)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].  This will normally be invoked via the inline function.
     */
    fun <T : Comparable<T>> itemInRange(index: Int, expected: ClosedRange<T>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueInRange(expected, itemClass)
        }
    }

    /**
     * Check an array item as a member of a [ClosedRange].
     */
    inline fun <reified T : Comparable<T>> item(index: Int, expected: ClosedRange<T>) {
        itemInRange(index, expected, T::class)
    }

    /**
     * Check an array item as a member of a [Collection].  This will normally be invoked via the inline function.
     */
    fun <T : Any> itemInCollection(index: Int, expected: Collection<T?>, itemClass: KClass<*>) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueInCollection(expected, itemClass)
        }
    }

    /**
     * Check an array item as a member of a [Collection].
     */
    inline fun <reified T : Any> item(index: Int, expected: Collection<T?>) {
        itemInCollection(index, expected, T::class)
    }

    /**
     * Select an array item for nested tests.
     */
    fun item(index: Int, tests: JSONExpect.() -> Unit) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).tests()
        }
    }

    /**
     * Check that an array item is an object and optionally apply nested tests.
     */
    fun itemIsObject(index: Int, tests: JSONExpect.() -> Unit = {}) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueIsObject(tests)
        }
    }

    /**
     * Check that an array item is an array and optionally apply nested tests.
     */
    fun itemIsArray(index: Int, tests: JSONExpect.() -> Unit = {}) {
        checkIndex(index).let {
            JSONExpect(getItem(it), itemPointer(it)).valueIsArray(tests)
        }
    }

    /**
     * Check that an array item is an array of the specified size and optionally apply nested tests.
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
     * Check that any item in array matches a [Char].
     */
    fun anyItem(expected: Char) {
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
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.MATCHING_REGEX, expected)
    }

    /**
     * Check that any item in array is a member of an [IntRange].
     */
    fun anyItem(expected: IntRange) {
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.IN_RANGE, expected)
    }

    /**
     * Check that any item in array is a member of a [LongRange].
     */
    fun anyItem(expected: LongRange) {
        anyItemCheck { value(expected) } || errorOnAnyItem(ErrorEnum.IN_RANGE, expected)
    }

    /**
     * Check that any item in array is a member of a [ClosedRange].  This will normally be invoked via the inline
     * function.
     */
    fun <T : Comparable<T>> anyItemInRange(expected: ClosedRange<T>, itemClass: KClass<*>) {
        anyItemCheck { valueInRange(expected, itemClass) } || errorOnAnyItem(ErrorEnum.IN_RANGE, expected)
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
     */
    fun test(expected: Int): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Long] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Long): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [BigDecimal] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: BigDecimal): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Boolean] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Boolean): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [String] or `null` equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: String?): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalDate] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: LocalDate): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalDateTime] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: LocalDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [LocalTime] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: LocalTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [OffsetDateTime] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: OffsetDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [OffsetTime] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: OffsetTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [ZonedDateTime] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: ZonedDateTime): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [YearMonth] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: YearMonth): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [MonthDay] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: MonthDay): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Year] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Year): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [JavaDuration] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: JavaDuration): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Period] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Period): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Duration] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Duration): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [UUID] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: UUID): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [Enum] equality check to a lambda for use in a multiple test check.
     */
    fun test(expected: Enum<*>): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [String] [Regex] check to a lambda for use in a multiple test check.
     */
    fun test(expected: Regex): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert an [Int] range check to a lambda for use in a multiple test check.
     */
    fun test(expected: IntRange): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Long] range check to a lambda for use in a multiple test check.
     */
    fun test(expected: LongRange): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [ClosedRange] check to a lambda for use in a multiple test check.
     */
    inline fun <reified T : Comparable<T>> test(expected: ClosedRange<T>): JSONExpect.() -> Unit = { value(expected) }

    /**
     * Convert a [Collection] check to a lambda for use in a multiple test check.
     */
    inline fun <reified T : Any> test(expected: Collection<T>): JSONExpect.() -> Unit = { value(expected) }

    /* ====================================== other functions ====================================== */

    /**
     * Check the contents of an object or array exhaustively, that is, perform all checks and then confirm that every
     * object property or array item has been tested.
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
     */
    fun count(expected: Int) {
        require(expected >= 0) { "JSON array or object count must not be negative" }
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length != expected)
            error("JSON count doesn't match - expected $expected, was $length")
    }

    /**
     * Check the count of array items or object properties as a range.
     */
    fun count(expected: IntRange) {
        require(expected.first >= 0) { "JSON array or object count must not be negative" }
        val length = when (node) {
            is List<*> -> node.size
            is Map<*, *> -> node.size
            else -> error("JSON count check not on array or object")
        }
        if (length !in expected)
            error("JSON count doesn't match - expected $expected, was $length")
    }

    /**
     * Check the length of a string value.
     */
    fun length(expected: Int): JSONExpect.() -> Unit = {
        nodeAsString.length.let {
            if (it != expected)
                error("JSON string length doesn't match - expected $expected, was $it")
        }
    }

    /**
     * Check the length of a string value as a range.
     */
    fun length(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsString.length.let {
            if (it !in expected)
                error("JSON string length doesn't match - expected $expected, was $it")
        }
    }

    /**
     * Check the scale of a decimal value.
     */
    fun scale(expected: Int): JSONExpect.() -> Unit = {
        nodeAsDecimal.scale().let {
            if (it != expected)
                error("JSON decimal scale doesn't match - expected $expected, was $it")
        }
    }

    /**
     * Check the scale of a decimal value as a range.
     */
    fun scale(expected: IntRange): JSONExpect.() -> Unit = {
        nodeAsDecimal.scale().let {
            if (it !in expected)
                error("JSON decimal scale doesn't match - expected $expected, was $it")
        }
    }

    /**
     * Report error, including context path.
     */
    fun error(message: String): Nothing {
        throw AssertionError(pointer?.let { "$it: $message" } ?: message)
    }

    /**
     * Create a display form of the current node, for use in error messages.
     */
    fun showNode() = showValue(node)

    /**
     * Create a display form of a value, for use in error messages.
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
        IN_RANGE("in given range"),
        IN_COLLECTION("in given collection"),
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

    private fun errorOnAnyItem(expected: Any?, additional: Any? = null): Nothing {
        val message = buildString {
            append(showValue(expected))
            if (additional != null) {
                val additionalString = additional.toString()
                if (additionalString.length in 1..40) {
                    append(" - ")
                    append(additionalString)
                }
            }
        }
        error("No JSON array item has value $message")
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

        @Suppress("ConstPropertyName")
        const val maxStringDisplayLength = 49

        /**
         * Check that a JSON string matches the defined expectations.
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
            get() {
                val fullName = qualifiedName ?: return "<unknown>"
                return if (fullName.startsWith("kotlin."))
                    fullName.substringAfterLast('.')
                else
                    fullName
            }

    }

}
