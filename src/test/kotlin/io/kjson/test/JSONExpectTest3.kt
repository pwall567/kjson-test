/*
 * @(#) JSONExpectTest3.kt
 *
 * kjson-test  Library for testing Kotlin JSON applications
 * Copyright (c) 2020, 2021, 2022 Peter Wall
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

import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest3 {

    @Test fun `should test that value is a string`() {
        val json = "\"I am a string\""
        expectJSON(json) {
            value(isString)
        }
    }

    @Test fun `should fail on incorrect test that value is a string`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isString)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test that property is a string`() {
        val json = """{"abc":"I am a string"}"""
        expectJSON(json) {
            property("abc", isString)
        }
    }

    @Test fun `should fail on incorrect test that property is a string`() {
        val json = """{"abc":12345}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isString)
            }
        }
        expect("/abc: JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test that array item is a string`() {
        val json = """["I am a string"]"""
        expectJSON(json) {
            item(0, isString)
        }
    }

    @Test fun `should fail on incorrect test that array item is a string`() {
        val json = """[12345]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isString)
            }
        }
        expect("/0: JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test that value is an integer`() {
        val json = "12345"
        expectJSON(json) {
            value(isInteger)
        }
    }

    @Test fun `should fail on incorrect test that value is an integer`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isInteger)
            }
        }
        expect("JSON type doesn't match - expected integer, was string") { exception.message }
    }

    @Test fun `should test that property is an integer`() {
        val json = """{"abc":12345}"""
        expectJSON(json) {
            property("abc", isInteger)
        }
    }

    @Test fun `should fail on incorrect test that property is an integer`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isInteger)
            }
        }
        expect("/abc: JSON type doesn't match - expected integer, was string") { exception.message }
    }

    @Test fun `should test that array item is an integer`() {
        val json = """[12345]"""
        expectJSON(json) {
            item(0, isInteger)
        }
    }

    @Test fun `should fail on incorrect test that array item is an integer`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isInteger)
            }
        }
        expect("/0: JSON type doesn't match - expected integer, was string") { exception.message }
    }

    @Test fun `should test that value is a long integer`() {
        val json = "123456789123456789"
        expectJSON(json) {
            value(isLongInteger)
        }
    }

    @Test fun `should fail on incorrect test that value is a long integer`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLongInteger)
            }
        }
        expect("JSON type doesn't match - expected long integer, was string") { exception.message }
    }

    @Test fun `should test that property is a long integer`() {
        val json = """{"abc":123456789123456789}"""
        expectJSON(json) {
            property("abc", isLongInteger)
        }
    }

    @Test fun `should fail on incorrect test that property is a long integer`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isLongInteger)
            }
        }
        expect("/abc: JSON type doesn't match - expected long integer, was string") { exception.message }
    }

    @Test fun `should test that array item is a long integer`() {
        val json = """[123456789123456789]"""
        expectJSON(json) {
            item(0, isLongInteger)
        }
    }

    @Test fun `should fail on incorrect test that array item is a long integer`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isLongInteger)
            }
        }
        expect("/0: JSON type doesn't match - expected long integer, was string") { exception.message }
    }

    @Test fun `should test that value is a decimal`() {
        val json = "0.5"
        expectJSON(json) {
            value(isDecimal)
        }
    }

    @Test fun `should fail on incorrect test that value is a decimal`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isDecimal)
            }
        }
        expect("JSON type doesn't match - expected decimal, was string") { exception.message }
    }

    @Test fun `should test that property is a decimal`() {
        val json = """{"abc":0.5}"""
        expectJSON(json) {
            property("abc", isDecimal)
        }
    }

    @Test fun `should fail on incorrect test that property is a decimal`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isDecimal)
            }
        }
        expect("/abc: JSON type doesn't match - expected decimal, was string") { exception.message }
    }

    @Test fun `should test that array item is a decimal`() {
        val json = """[0.5]"""
        expectJSON(json) {
            item(0, isDecimal)
        }
    }

    @Test fun `should fail on incorrect test that array item is a decimal`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isDecimal)
            }
        }
        expect("/0: JSON type doesn't match - expected decimal, was string") { exception.message }
    }

    @Test fun `should test that value is a boolean`() {
        val json = "true"
        expectJSON(json) {
            value(isBoolean)
        }
    }

    @Test fun `should fail on incorrect test that value is a boolean`() {
        val json = "\"I am a string\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isBoolean)
            }
        }
        expect("JSON type doesn't match - expected boolean, was string") { exception.message }
    }

    @Test fun `should test that property is a boolean`() {
        val json = """{"abc":true}"""
        expectJSON(json) {
            property("abc", isBoolean)
        }
    }

    @Test fun `should fail on incorrect test that property is a boolean`() {
        val json = """{"abc":"I am a string"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isBoolean)
            }
        }
        expect("/abc: JSON type doesn't match - expected boolean, was string") { exception.message }
    }

    @Test fun `should test that array item is a boolean`() {
        val json = """[true]"""
        expectJSON(json) {
            item(0, isBoolean)
        }
    }

    @Test fun `should fail on incorrect test that array item is a boolean`() {
        val json = """["I am a string"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isBoolean)
            }
        }
        expect("/0: JSON type doesn't match - expected boolean, was string") { exception.message }
    }

    @Test fun `should test string value as UUID`() {
        val json = "\"f347ab96-7f62-11ea-ba4e-27278a06d491\""
        expectJSON(json) {
            value(isUUID)
        }
    }

    @Test fun `should fail on incorrect test of string value as UUID`() {
        val json = "\"not a UUID\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isUUID)
            }
        }
        expect("JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should fail on test of string value as UUID with shortened input`() {
        // There is a bug in some implementations of UUID.fromString()
        //   https://bugs.openjdk.java.net/browse/JDK-8216407
        // A shortened string would previously have been allowed;
        // validation changed to use JSONValidation.isUUID()
        val json = "\"f347ab96-7f62-11ea-ba4e-27278a06\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isUUID)
            }
        }
        expect("JSON string is not a UUID - \"f347ab96-7f62-11ea-ba4e-27278a06\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as UUID`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isUUID)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as UUID`() {
        val json = """{"abc":"f347ab96-7f62-11ea-ba4e-27278a06d491"}"""
        expectJSON(json) {
            property("abc", isUUID)
        }
    }

    @Test fun `should fail on incorrect test of string property as UUID`() {
        val json = """{"abc":"not a UUID"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isUUID)
            }
        }
        expect("/abc: JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should test string array item as UUID`() {
        val json = """["f347ab96-7f62-11ea-ba4e-27278a06d491"]"""
        expectJSON(json) {
            item(0, isUUID)
        }
    }

    @Test fun `should fail on incorrect test of string array item as UUID`() {
        val json = """["not a UUID"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isUUID)
            }
        }
        expect("/0: JSON string is not a UUID - \"not a UUID\"") { exception.message }
    }

    @Test fun `should test string value as LocalDate`() {
        val json = "\"2020-04-16\""
        expectJSON(json) {
            value(isLocalDate)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalDate`() {
        val json = "\"not a LocalDate\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalDate)
            }
        }
        expect("JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalDate`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalDate)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalDate`() {
        val json = """{"abc":"2020-04-16"}"""
        expectJSON(json) {
            property("abc", isLocalDate)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalDate`() {
        val json = """{"abc":"not a LocalDate"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isLocalDate)
            }
        }
        expect("/abc: JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should test string array item as LocalDate`() {
        val json = """["2020-04-16"]"""
        expectJSON(json) {
            item(0, isLocalDate)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalDate`() {
        val json = """["not a LocalDate"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isLocalDate)
            }
        }
        expect("/0: JSON string is not a LocalDate - \"not a LocalDate\"") { exception.message }
    }

    @Test fun `should test string value as LocalDateTime`() {
        val json = "\"2020-04-16T18:31:19.123\""
        expectJSON(json) {
            value(isLocalDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalDateTime`() {
        val json = "\"not a LocalDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalDateTime)
            }
        }
        expect("JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalDateTime)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123"}"""
        expectJSON(json) {
            property("abc", isLocalDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalDateTime`() {
        val json = """{"abc":"not a LocalDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isLocalDateTime)
            }
        }
        expect("/abc: JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as LocalDateTime`() {
        val json = """["2020-04-16T18:31:19.123"]"""
        expectJSON(json) {
            item(0, isLocalDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalDateTime`() {
        val json = """["not a LocalDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isLocalDateTime)
            }
        }
        expect("/0: JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { exception.message }
    }

    @Test fun `should test string value as LocalTime`() {
        val json = "\"18:31:19.123\""
        expectJSON(json) {
            value(isLocalTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as LocalTime`() {
        val json = "\"not a LocalTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalTime)
            }
        }
        expect("JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as LocalTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isLocalTime)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as LocalTime`() {
        val json = """{"abc":"18:31:19.123"}"""
        expectJSON(json) {
            property("abc", isLocalTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as LocalTime`() {
        val json = """{"abc":"not a LocalTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isLocalTime)
            }
        }
        expect("/abc: JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should test string array item as LocalTime`() {
        val json = """["18:31:19.123"]"""
        expectJSON(json) {
            item(0, isLocalTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as LocalTime`() {
        val json = """["not a LocalTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isLocalTime)
            }
        }
        expect("/0: JSON string is not a LocalTime - \"not a LocalTime\"") { exception.message }
    }

    @Test fun `should test string value as OffsetDateTime`() {
        val json = "\"2020-04-16T18:31:19.123+10:00\""
        expectJSON(json) {
            value(isOffsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as OffsetDateTime`() {
        val json = "\"not an OffsetDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isOffsetDateTime)
            }
        }
        expect("JSON string is not an OffsetDateTime - \"not an OffsetDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as OffsetDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isOffsetDateTime)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as OffsetDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123+10:00"}"""
        expectJSON(json) {
            property("abc", isOffsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as OffsetDateTime`() {
        val json = """{"abc":"not an OffsetDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isOffsetDateTime)
            }
        }
        expect("/abc: JSON string is not an OffsetDateTime - \"not an OffsetDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as OffsetDateTime`() {
        val json = """["2020-04-16T18:31:19.123+10:00"]"""
        expectJSON(json) {
            item(0, isOffsetDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as OffsetDateTime`() {
        val json = """["not an OffsetDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isOffsetDateTime)
            }
        }
        expect("/0: JSON string is not an OffsetDateTime - \"not an OffsetDateTime\"") { exception.message }
    }

    @Test fun `should test string value as OffsetTime`() {
        val json = "\"18:31:19.123+10:00\""
        expectJSON(json) {
            value(isOffsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as OffsetTime`() {
        val json = "\"not an OffsetTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isOffsetTime)
            }
        }
        expect("JSON string is not an OffsetTime - \"not an OffsetTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as OffsetTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isOffsetTime)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as OffsetTime`() {
        val json = """{"abc":"18:31:19.123+10:00"}"""
        expectJSON(json) {
            property("abc", isOffsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as OffsetTime`() {
        val json = """{"abc":"not an OffsetTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isOffsetTime)
            }
        }
        expect("/abc: JSON string is not an OffsetTime - \"not an OffsetTime\"") { exception.message }
    }

    @Test fun `should test string array item as OffsetTime`() {
        val json = """["18:31:19.123+10:00"]"""
        expectJSON(json) {
            item(0, isOffsetTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as OffsetTime`() {
        val json = """["not an OffsetTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isOffsetTime)
            }
        }
        expect("/0: JSON string is not an OffsetTime - \"not an OffsetTime\"") { exception.message }
    }

    @Test fun `should test string value as ZonedDateTime`() {
        val json = "\"2020-04-16T18:31:19.123+10:00[Australia/Sydney]\""
        expectJSON(json) {
            value(isZonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string value as ZonedDateTime`() {
        val json = "\"not a ZonedDateTime\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isZonedDateTime)
            }
        }
        expect("JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as ZonedDateTime`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isZonedDateTime)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as ZonedDateTime`() {
        val json = """{"abc":"2020-04-16T18:31:19.123+10:00[Australia/Sydney]"}"""
        expectJSON(json) {
            property("abc", isZonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string property as ZonedDateTime`() {
        val json = """{"abc":"not a ZonedDateTime"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isZonedDateTime)
            }
        }
        expect("/abc: JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should test string array item as ZonedDateTime`() {
        val json = """["2020-04-16T18:31:19.123+10:00[Australia/Sydney]"]"""
        expectJSON(json) {
            item(0, isZonedDateTime)
        }
    }

    @Test fun `should fail on incorrect test of string array item as ZonedDateTime`() {
        val json = """["not a ZonedDateTime"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isZonedDateTime)
            }
        }
        expect("/0: JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { exception.message }
    }

    @Test fun `should test string value as YearMonth`() {
        val json = "\"2020-04\""
        expectJSON(json) {
            value(isYearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string value as YearMonth`() {
        val json = "\"not a YearMonth\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isYearMonth)
            }
        }
        expect("JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as YearMonth`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isYearMonth)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as YearMonth`() {
        val json = """{"abc":"2020-04"}"""
        expectJSON(json) {
            property("abc", isYearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string property as YearMonth`() {
        val json = """{"abc":"not a YearMonth"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isYearMonth)
            }
        }
        expect("/abc: JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should test string array item as YearMonth`() {
        val json = """["2020-04"]"""
        expectJSON(json) {
            item(0, isYearMonth)
        }
    }

    @Test fun `should fail on incorrect test of string array item as YearMonth`() {
        val json = """["not a YearMonth"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isYearMonth)
            }
        }
        expect("/0: JSON string is not a YearMonth - \"not a YearMonth\"") { exception.message }
    }

    @Test fun `should test string value as MonthDay`() {
        val json = "\"--04-30\""
        expectJSON(json) {
            value(isMonthDay)
        }
    }

    @Test fun `should fail on incorrect test of string value as MonthDay`() {
        val json = "\"not a MonthDay\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isMonthDay)
            }
        }
        expect("JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as MonthDay`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isMonthDay)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as MonthDay`() {
        val json = """{"abc":"--04-30"}"""
        expectJSON(json) {
            property("abc", isMonthDay)
        }
    }

    @Test fun `should fail on incorrect test of string property as MonthDay`() {
        val json = """{"abc":"not a MonthDay"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isMonthDay)
            }
        }
        expect("/abc: JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should test string array item as MonthDay`() {
        val json = """["--04-30"]"""
        expectJSON(json) {
            item(0, isMonthDay)
        }
    }

    @Test fun `should fail on incorrect test of string array item as MonthDay`() {
        val json = """["not a MonthDay"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isMonthDay)
            }
        }
        expect("/0: JSON string is not a MonthDay - \"not a MonthDay\"") { exception.message }
    }

    @Test fun `should test string value as Year`() {
        val json = "\"2020\""
        expectJSON(json) {
            value(isYear)
        }
    }

    @Test fun `should fail on incorrect test of string value as Year`() {
        val json = "\"not a Year\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isYear)
            }
        }
        expect("JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Year`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isYear)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Year`() {
        val json = """{"abc":"2020"}"""
        expectJSON(json) {
            property("abc", isYear)
        }
    }

    @Test fun `should fail on incorrect test of string property as Year`() {
        val json = """{"abc":"not a Year"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isYear)
            }
        }
        expect("/abc: JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should test string array item as Year`() {
        val json = """["2020"]"""
        expectJSON(json) {
            item(0, isYear)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Year`() {
        val json = """["not a Year"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isYear)
            }
        }
        expect("/0: JSON string is not a Year - \"not a Year\"") { exception.message }
    }

    @Test fun `should test string value as Java Duration`() {
        val json = "\"PT2H\""
        expectJSON(json) {
            value(isJavaDuration)
        }
    }

    @Test fun `should fail on incorrect test of string value as Java Duration`() {
        val json = "\"not a Duration\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isJavaDuration)
            }
        }
        expect("JSON string is not a Java Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Java Duration`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isJavaDuration)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Java Duration`() {
        val json = """{"abc":"PT2H"}"""
        expectJSON(json) {
            property("abc", isJavaDuration)
        }
    }

    @Test fun `should fail on incorrect test of string property as Java Duration`() {
        val json = """{"abc":"not a Duration"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isJavaDuration)
            }
        }
        expect("/abc: JSON string is not a Java Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test string array item as Java Duration`() {
        val json = """["PT2H"]"""
        expectJSON(json) {
            item(0, isJavaDuration)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Java Duration`() {
        val json = """["not a Duration"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isJavaDuration)
            }
        }
        expect("/0: JSON string is not a Java Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test string value as Period`() {
        val json = "\"P3M\""
        expectJSON(json) {
            value(isPeriod)
        }
    }

    @Test fun `should fail on incorrect test of string value as Period`() {
        val json = "\"not a Period\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isPeriod)
            }
        }
        expect("JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Period`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isPeriod)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Period`() {
        val json = """{"abc":"P3M"}"""
        expectJSON(json) {
            property("abc", isPeriod)
        }
    }

    @Test fun `should fail on incorrect test of string property as Period`() {
        val json = """{"abc":"not a Period"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isPeriod)
            }
        }
        expect("/abc: JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should test string array item as Period`() {
        val json = """["P3M"]"""
        expectJSON(json) {
            item(0, isPeriod)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Period`() {
        val json = """["not a Period"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isPeriod)
            }
        }
        expect("/0: JSON string is not a Period - \"not a Period\"") { exception.message }
    }

    @Test fun `should test string value as Duration`() {
        val json = "\"2m 5s\""
        expectJSON(json) {
            value(isDuration)
        }
    }

    @Test fun `should fail on incorrect test of string value as Duration`() {
        val json = "\"not a Duration\""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isDuration)
            }
        }
        expect("JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should fail on test of non-string value as Duration`() {
        val json = "12345"
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isDuration)
            }
        }
        expect("JSON type doesn't match - expected string, was integer") { exception.message }
    }

    @Test fun `should test string property as Duration`() {
        val json = """{"abc":"2m 5s"}"""
        expectJSON(json) {
            property("abc", isDuration)
        }
    }

    @Test fun `should fail on incorrect test of string property as Duration`() {
        val json = """{"abc":"not a Duration"}"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isDuration)
            }
        }
        expect("/abc: JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test string array item as Duration`() {
        val json = """["2m 5s"]"""
        expectJSON(json) {
            item(0, isDuration)
        }
    }

    @Test fun `should fail on incorrect test of string array item as Duration`() {
        val json = """["not a Duration"]"""
        val exception = assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isDuration)
            }
        }
        expect("/0: JSON string is not a Duration - \"not a Duration\"") { exception.message }
    }

    @Test fun `should test that value is array`() {
        val json = "[]"
        expectJSON(json) {
            value(isArray)
        }
    }

    @Test fun `should fail on incorrect test of value as array`() {
        val json = "\"not an array\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isArray)
            }
        }.let {
            expect("JSON type doesn't match - expected array, was string") { it.message }
        }
    }

    @Test fun `should test that property is array`() {
        val json = """{"abc":[]}"""
        expectJSON(json) {
            property("abc", isArray)
        }
    }

    @Test fun `should fail on incorrect test of property as array`() {
        val json = """{"abc":"not an array"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isArray)
            }
        }.let {
            expect("/abc: JSON type doesn't match - expected array, was string") { it.message }
        }
    }

    @Test fun `should test that array item is array`() {
        val json = "[[]]"
        expectJSON(json) {
            item(0, isArray)
        }
    }

    @Test fun `should fail on incorrect test of array item as array`() {
        val json = "[\"not an array\"]"
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isArray)
            }
        }.let {
            expect("/0: JSON type doesn't match - expected array, was string") { it.message }
        }
    }

    @Test fun `should test that value is empty array`() {
        val json = "[]"
        expectJSON(json) {
            value(isEmptyArray)
        }
    }

    @Test fun `should fail on incorrect test of value as empty array`() {
        val json = "[2]"
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isEmptyArray)
            }
        }.let {
            expect("JSON array is not empty - size 1") { it.message }
        }
    }

    @Test fun `should test that property is empty array`() {
        val json = """{"abc":[]}"""
        expectJSON(json) {
            property("abc", isEmptyArray)
        }
    }

    @Test fun `should fail on incorrect test of property as empty array`() {
        val json = """{"abc":[3,4]}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isEmptyArray)
            }
        }.let {
            expect("/abc: JSON array is not empty - size 2") { it.message }
        }
    }

    @Test fun `should test that array item is empty array`() {
        val json = "[[]]"
        expectJSON(json) {
            item(0, isEmptyArray)
        }
    }

    @Test fun `should fail on incorrect test of array item as empty array`() {
        val json = "[[4,5,6,7]]"
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isEmptyArray)
            }
        }.let {
            expect("/0: JSON array is not empty - size 4") { it.message }
        }
    }

    @Test fun `should test that value is object`() {
        val json = "{}"
        expectJSON(json) {
            value(isObject)
        }
    }

    @Test fun `should fail on incorrect test of value as object`() {
        val json = "\"not an object\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isObject)
            }
        }.let {
            expect("JSON type doesn't match - expected object, was string") { it.message }
        }
    }

    @Test fun `should test that property is object`() {
        val json = """{"abc":{}}"""
        expectJSON(json) {
            property("abc", isObject)
        }
    }

    @Test fun `should fail on incorrect test of property as object`() {
        val json = """{"abc":"not an object"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isObject)
            }
        }.let {
            expect("/abc: JSON type doesn't match - expected object, was string") { it.message }
        }
    }

    @Test fun `should test that array item is object`() {
        val json = "[{}]"
        expectJSON(json) {
            item(0, isObject)
        }
    }

    @Test fun `should fail on incorrect test of array item as object`() {
        val json = "[\"not an object\"]"
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isObject)
            }
        }.let {
            expect("/0: JSON type doesn't match - expected object, was string") { it.message }
        }
    }

    @Test fun `should test that value is empty object`() {
        val json = "{}"
        expectJSON(json) {
            value(isEmptyObject)
        }
    }

    @Test fun `should fail on incorrect test of value as empty object`() {
        val json = """{"aaa":0}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(isEmptyObject)
            }
        }.let {
            expect("JSON object is not empty - size 1") { it.message }
        }
    }

    @Test fun `should test that property is empty object`() {
        val json = """{"abc":{}}"""
        expectJSON(json) {
            property("abc", isEmptyObject)
        }
    }

    @Test fun `should fail on incorrect test of property as empty object`() {
        val json = """{"abc":{"aaa":0,"bbb":1}}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", isEmptyObject)
            }
        }.let {
            expect("/abc: JSON object is not empty - size 2") { it.message }
        }
    }

    @Test fun `should test that array item is empty object`() {
        val json = "[{}]"
        expectJSON(json) {
            item(0, isEmptyObject)
        }
    }

    @Test fun `should fail on incorrect test of array item as empty object`() {
        val json = """[{"aaa":0,"bbb":1}]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, isEmptyObject)
            }
        }.let {
            expect("/0: JSON object is not empty - size 2") { it.message }
        }
    }

}
