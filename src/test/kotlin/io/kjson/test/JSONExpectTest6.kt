package io.kjson.test

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect
import kotlin.test.fail
import kotlin.time.Duration.Companion.seconds

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
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID

import io.kjson.test.JSONExpect.Companion.expectJSON

class JSONExpectTest6 {

    @Test fun `should read nodeAsLocalDate`() {
        val json = "\"2022-06-15\""
        expectJSON(json) {
            if (nodeAsLocalDate != LocalDate.of(2022, 6, 15))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsLocalDate`() {
        val json = "\"not a LocalDate\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsLocalDate
            }
        }.let {
            expect("JSON string is not a LocalDate - \"not a LocalDate\"") { it.message }
        }
    }

    @Test fun `should test LocalDate value`() {
        val json = "\"2022-06-15\""
        expectJSON(json) {
            value(LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate value`() {
        val json = "\"2022-06-14\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(LocalDate.of(2022, 6, 15))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022-06-15\", was \"2022-06-14\"") { it.message }
        }
    }

    @Test fun `should test LocalDate property`() {
        val json = """{"abc":"2022-06-15"}"""
        expectJSON(json) {
            property("abc", LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate property`() {
        val json = """{"abc":"2022-06-14"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", LocalDate.of(2022, 6, 15))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022-06-15\", was \"2022-06-14\"") { it.message }
        }
    }

    @Test fun `should test LocalDate array item`() {
        val json = """["2022-06-15"]"""
        expectJSON(json) {
            item(0, LocalDate.of(2022, 6, 15))
        }
    }

    @Test fun `should fail on incorrect LocalDate array item`() {
        val json = """["2022-06-14"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, LocalDate.of(2022, 6, 15))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022-06-15\", was \"2022-06-14\"") { it.message }
        }
    }

    @Test fun `should read nodeAsLocalDateTime`() {
        val json = "\"2022-06-15T17:05:09.123\""
        expectJSON(json) {
            if (nodeAsLocalDateTime != LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsLocalDateTime`() {
        val json = "\"not a LocalDateTime\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsLocalDateTime
            }
        }.let {
            expect("JSON string is not a LocalDateTime - \"not a LocalDateTime\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123\""
        expectJSON(json) {
            value(LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime value`() {
        val json = "\"2022-06-15T17:05:09.124\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022-06-15T17:05:09.123\", was \"2022-06-15T17:05:09.124\"") {
                it.message
            }
        }
    }

    @Test fun `should test LocalDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123"}"""
        expectJSON(json) {
            property("abc", LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.124"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022-06-15T17:05:09.123\"," +
                    " was \"2022-06-15T17:05:09.124\"") { it.message }
        }
    }

    @Test fun `should test LocalDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123"]"""
        expectJSON(json) {
            item(0, LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalDateTime array item`() {
        val json = """["2022-06-15T17:05:09.124"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, LocalDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022-06-15T17:05:09.123\"," +
                    " was \"2022-06-15T17:05:09.124\"") { it.message }
        }
    }

    @Test fun `should read nodeAsLocalTime`() {
        val json = "\"17:05:09.123\""
        expectJSON(json) {
            if (nodeAsLocalTime != LocalTime.of(17, 5, 9, 123_000_000))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsLocalTime`() {
        val json = "\"not a LocalTime\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsLocalTime
            }
        }.let {
            expect("JSON string is not a LocalTime - \"not a LocalTime\"") { it.message }
        }
    }

    @Test fun `should test LocalTime value`() {
        val json = "\"17:05:09.123\""
        expectJSON(json) {
            value(LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime value`() {
        val json = "\"17:05:09\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime property`() {
        val json = """{"abc":"17:05:09.123"}"""
        expectJSON(json) {
            property("abc", LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime property`() {
        val json = """{"abc":"17:05:09"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should test LocalTime array item`() {
        val json = """["17:05:09.123"]"""
        expectJSON(json) {
            item(0, LocalTime.of(17, 5, 9, 123_000_000))
        }
    }

    @Test fun `should fail on incorrect LocalTime array item`() {
        val json = """["17:05:09"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, LocalTime.of(17, 5, 9, 123_000_000))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"17:05:09.123\", was \"17:05:09\"") { it.message }
        }
    }

    @Test fun `should read nodeAsOffsetDateTime`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        expectJSON(json) {
            if (nodeAsOffsetDateTime != OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsOffsetDateTime`() {
        val json = "\"not an OffsetDateTime\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsOffsetDateTime
            }
        }.let {
            expect("JSON string is not an OffsetDateTime - \"not an OffsetDateTime\"") { it.message }
        }
    }

    @Test fun `should test OffsetDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        expectJSON(json) {
            value(OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneOffset.ofHours(11)))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+11:00\"," +
                    " was \"2022-06-15T17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        expectJSON(json) {
            property("abc", OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneOffset.ofHours(11)))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+11:00\"," +
                    " was \"2022-06-15T17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should test OffsetDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00"]"""
        expectJSON(json) {
            item(0, OffsetDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, OffsetDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneOffset.ofHours(11)))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+11:00\"," +
                    " was \"2022-06-15T17:05:09.123+10:00\"") { it.message }
        }
    }

    @Test fun `should read nodeAsOffsetTime`() {
        val json = "\"17:05:09.123+10:00\""
        expectJSON(json) {
            if (nodeAsOffsetTime != OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsOffsetTime`() {
        val json = "\"not an OffsetTime\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsOffsetTime
            }
        }.let {
            expect("JSON string is not an OffsetTime - \"not an OffsetTime\"") { it.message }
        }
    }

    @Test fun `should test OffsetTime value`() {
        val json = "\"17:05:09.123+10:00\""
        expectJSON(json) {
            value(OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime value`() {
        val json = "\"17:05:09.124+10:00\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should test OffsetTime property`() {
        val json = """{"abc":"17:05:09.123+10:00"}"""
        expectJSON(json) {
            property("abc", OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime property`() {
        val json = """{"abc":"17:05:09.124+10:00"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should test OffsetTime array item`() {
        val json = """["17:05:09.123+10:00"]"""
        expectJSON(json) {
            item(0, OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
        }
    }

    @Test fun `should fail on incorrect OffsetTime array item`() {
        val json = """["17:05:09.124+10:00"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, OffsetTime.of(17, 5, 9, 123_000_000, ZoneOffset.ofHours(10)))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"17:05:09.123+10:00\", was \"17:05:09.124+10:00\"") {
                it.message
            }
        }
    }

    @Test fun `should read nodeAsZonedDateTime`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        expectJSON(json) {
            if (nodeAsZonedDateTime != ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000,
                    ZoneId.of("Australia/Sydney")))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsZonedDateTime`() {
        val json = "\"not a ZonedDateTime\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsZonedDateTime
            }
        }.let {
            expect("JSON string is not a ZonedDateTime - \"not a ZonedDateTime\"") { it.message }
        }
    }

    @Test fun `should test ZonedDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        expectJSON(json) {
            value(ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime value`() {
        val json = "\"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                    " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
        }
    }

    @Test fun `should test ZonedDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        expectJSON(json) {
            property("abc", ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime property`() {
        val json = """{"abc":"2022-06-15T17:05:09.123+10:00[Australia/Sydney]"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                    " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
        }
    }

    @Test fun `should test ZonedDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00[Australia/Sydney]"]"""
        expectJSON(json) {
            item(0, ZonedDateTime.of(2022, 6, 15, 17, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
        }
    }

    @Test fun `should fail on incorrect ZonedDateTime array item`() {
        val json = """["2022-06-15T17:05:09.123+10:00[Australia/Sydney]"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, ZonedDateTime.of(2022, 6, 15, 18, 5, 9, 123_000_000, ZoneId.of("Australia/Sydney")))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022-06-15T18:05:09.123+10:00[Australia/Sydney]\"," +
                    " was \"2022-06-15T17:05:09.123+10:00[Australia/Sydney]\"") { it.message }
        }
    }

    @Test fun `should read nodeAsYearMonth`() {
        val json = "\"2022-06\""
        expectJSON(json) {
            if (nodeAsYearMonth != YearMonth.of(2022, 6))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsYearMonth`() {
        val json = "\"not a YearMonth\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsYearMonth
            }
        }.let {
            expect("JSON string is not a YearMonth - \"not a YearMonth\"") { it.message }
        }
    }

    @Test fun `should test YearMonth value`() {
        val json = "\"2022-06\""
        expectJSON(json) {
            value(YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth value`() {
        val json = "\"2022-05\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(YearMonth.of(2022, 6))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should test YearMonth property`() {
        val json = """{"abc":"2022-06"}"""
        expectJSON(json) {
            property("abc", YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth property`() {
        val json = """{"abc":"2022-05"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", YearMonth.of(2022, 6))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should test YearMonth array item`() {
        val json = """["2022-06"]"""
        expectJSON(json) {
            item(0, YearMonth.of(2022, 6))
        }
    }

    @Test fun `should fail on incorrect YearMonth array item`() {
        val json = """["2022-05"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, YearMonth.of(2022, 6))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022-06\", was \"2022-05\"") { it.message }
        }
    }

    @Test fun `should read nodeAsMonthDay`() {
        val json = "\"--06-15\""
        expectJSON(json) {
            if (nodeAsMonthDay != MonthDay.of(6, 15))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsMonthDay`() {
        val json = "\"not a MonthDay\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsMonthDay
            }
        }.let {
            expect("JSON string is not a MonthDay - \"not a MonthDay\"") { it.message }
        }
    }

    @Test fun `should test MonthDay value`() {
        val json = "\"--06-15\""
        expectJSON(json) {
            value(MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay value`() {
        val json = "\"--06-14\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(MonthDay.of(6, 15))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"--06-15\", was \"--06-14\"") { it.message }
        }
    }

    @Test fun `should test MonthDay property`() {
        val json = """{"abc":"--06-15"}"""
        expectJSON(json) {
            property("abc", MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay property`() {
        val json = """{"abc":"--06-14"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", MonthDay.of(6, 15))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"--06-15\", was \"--06-14\"") { it.message }
        }
    }

    @Test fun `should test MonthDay array item`() {
        val json = """["--06-15"]"""
        expectJSON(json) {
            item(0, MonthDay.of(6, 15))
        }
    }

    @Test fun `should fail on incorrect MonthDay array item`() {
        val json = """["--06-14"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, MonthDay.of(6, 15))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"--06-15\", was \"--06-14\"") { it.message }
        }
    }

    @Test fun `should read nodeAsYear`() {
        val json = "\"2022\""
        expectJSON(json) {
            if (nodeAsYear != Year.of(2022))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsYear`() {
        val json = "\"not a Year\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsYear
            }
        }.let {
            expect("JSON string is not a Year - \"not a Year\"") { it.message }
        }
    }

    @Test fun `should test Year value`() {
        val json = "\"2022\""
        expectJSON(json) {
            value(Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year value`() {
        val json = "\"2021\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(Year.of(2022))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should test Year property`() {
        val json = """{"abc":"2022"}"""
        expectJSON(json) {
            property("abc", Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year property`() {
        val json = """{"abc":"2021"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", Year.of(2022))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should test Year array item`() {
        val json = """["2022"]"""
        expectJSON(json) {
            item(0, Year.of(2022))
        }
    }

    @Test fun `should fail on incorrect Year array item`() {
        val json = """["2021"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, Year.of(2022))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"2022\", was \"2021\"") { it.message }
        }
    }

    @Test fun `should read nodeAsJavaDuration`() {
        val json = "\"PT34S\""
        expectJSON(json) {
            if (nodeAsJavaDuration != JavaDuration.ofSeconds(34))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsJavaDuration`() {
        val json = "\"not a Java Duration\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsJavaDuration
            }
        }.let {
            expect("JSON string is not a Java Duration - \"not a Java Duration\"") { it.message }
        }
    }

    @Test fun `should test Java Duration value`() {
        val json = "\"PT34S\""
        expectJSON(json) {
            value(JavaDuration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration value`() {
        val json = "\"PT33S\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(JavaDuration.ofSeconds(34))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration property`() {
        val json = """{"abc":"PT34S"}"""
        expectJSON(json) {
            property("abc", JavaDuration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration property`() {
        val json = """{"abc":"PT33S"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", JavaDuration.ofSeconds(34))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should test Java Duration array item`() {
        val json = """["PT34S"]"""
        expectJSON(json) {
            item(0, JavaDuration.ofSeconds(34))
        }
    }

    @Test fun `should fail on incorrect Java Duration array item`() {
        val json = """["PT33S"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, JavaDuration.ofSeconds(34))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"PT34S\", was \"PT33S\"") { it.message }
        }
    }

    @Test fun `should read nodeAsPeriod`() {
        val json = "\"P45D\""
        expectJSON(json) {
            if (nodeAsPeriod!= Period.ofDays(45))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsPeriod`() {
        val json = "\"not a Period\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsPeriod
            }
        }.let {
            expect("JSON string is not a Period - \"not a Period\"") { it.message }
        }
    }

    @Test fun `should test Period value`() {
        val json = "\"P45D\""
        expectJSON(json) {
            value(Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period value`() {
        val json = "\"P44D\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(Period.ofDays(45))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should test Period property`() {
        val json = """{"abc":"P45D"}"""
        expectJSON(json) {
            property("abc", Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period property`() {
        val json = """{"abc":"P44D"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", Period.ofDays(45))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should test Period array item`() {
        val json = """["P45D"]"""
        expectJSON(json) {
            item(0, Period.ofDays(45))
        }
    }

    @Test fun `should fail on incorrect Period array item`() {
        val json = """["P44D"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, Period.ofDays(45))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"P45D\", was \"P44D\"") { it.message }
        }
    }

    @Test fun `should read nodeAsDuration`() {
        val json = "\"34s\""
        expectJSON(json) {
            if (nodeAsDuration != 34.seconds)
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsDuration`() {
        val json = "\"not a Duration\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsDuration
            }
        }.let {
            expect("JSON string is not a Duration - \"not a Duration\"") { it.message }
        }
    }

    @Test fun `should test Duration value`() {
        val json = "\"34s\""
        expectJSON(json) {
            value(34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration value`() {
        val json = "\"33s\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(34.seconds)
            }
        }.let {
            expect("JSON value doesn't match - Expected \"34s\", was \"33s\"") { it.message }
        }
    }

    @Test fun `should test Duration property`() {
        val json = """{"abc":"34s"}"""
        expectJSON(json) {
            property("abc", 34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration property`() {
        val json = """{"abc":"33s"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", 34.seconds)
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"34s\", was \"33s\"") { it.message }
        }
    }

    @Test fun `should test Duration array item`() {
        val json = """["34s"]"""
        expectJSON(json) {
            item(0, 34.seconds)
        }
    }

    @Test fun `should fail on incorrect Duration array item`() {
        val json = """["33s"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, 34.seconds)
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"34s\", was \"33s\"") { it.message }
        }
    }

    @Test fun `should read nodeAsUUID`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        expectJSON(json) {
            if (nodeAsUUID != UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
                fail()
        }
    }

    @Test fun `should fail on invalid nodeAsUUID`() {
        val json = "\"not a UUID\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                nodeAsUUID
            }
        }.let {
            expect("JSON string is not a UUID - \"not a UUID\"") { it.message }
        }
    }

    @Test fun `should test UUID value`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265750\""
        expectJSON(json) {
            value(UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
        }
    }

    @Test fun `should fail on incorrect UUID value`() {
        val json = "\"ea986c80-ed1d-11ec-a20b-a7e136265751\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
            }
        }.let {
            expect("JSON value doesn't match - Expected \"ea986c80-ed1d-11ec-a20b-a7e136265750\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265751\"") { it.message }
        }
    }

    @Test fun `should test UUID property`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265750"}"""
        expectJSON(json) {
            property("abc", UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
        }
    }

    @Test fun `should fail on incorrect UUID property`() {
        val json = """{"abc":"ea986c80-ed1d-11ec-a20b-a7e136265751"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"ea986c80-ed1d-11ec-a20b-a7e136265750\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265751\"") { it.message }
        }
    }

    @Test fun `should test UUID array item`() {
        val json = """["ea986c80-ed1d-11ec-a20b-a7e136265750"]"""
        expectJSON(json) {
            item(0, UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
        }
    }

    @Test fun `should fail on incorrect UUID array item`() {
        val json = """["ea986c80-ed1d-11ec-a20b-a7e136265751"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, UUID.fromString("ea986c80-ed1d-11ec-a20b-a7e136265750"))
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"ea986c80-ed1d-11ec-a20b-a7e136265750\"," +
                    " was \"ea986c80-ed1d-11ec-a20b-a7e136265751\"") { it.message }
        }
    }

    @Test fun `should test Enum value`() {
        val json = "\"OPEN\""
        expectJSON(json) {
            value(Status.OPEN)
        }
    }

    @Test fun `should fail on incorrect Enum value`() {
        val json = "\"WRONG\""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                value(Status.OPEN)
            }
        }.let {
            expect("JSON value doesn't match - Expected \"OPEN\", was \"WRONG\"") { it.message }
        }
    }

    @Test fun `should test Enum property`() {
        val json = """{"abc":"CLOSED"}"""
        expectJSON(json) {
            property("abc", Status.CLOSED)
        }
    }

    @Test fun `should fail on incorrect Enum property`() {
        val json = """{"abc":"WRONG"}"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                property("abc", Status.CLOSED)
            }
        }.let {
            expect("/abc: JSON value doesn't match - Expected \"CLOSED\", was \"WRONG\"") { it.message }
        }
    }

    @Test fun `should test Enum array item`() {
        val json = """["OPEN"]"""
        expectJSON(json) {
            item(0, Status.OPEN)
        }
    }

    @Test fun `should fail on incorrect Enum array item`() {
        val json = """["BAD"]"""
        assertFailsWith<AssertionError> {
            expectJSON(json) {
                item(0, Status.OPEN)
            }
        }.let {
            expect("/0: JSON value doesn't match - Expected \"OPEN\", was \"BAD\"") { it.message }
        }
    }

    enum class Status { OPEN, CLOSED }

}
