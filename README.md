# kjson-test

[![Build Status](https://travis-ci.com/pwall567/kjson-test.svg?branch=main)](https://app.travis-ci.com/github/pwall567/kjson-test)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.8.22&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.8.22)
[![Maven Central](https://img.shields.io/maven-central/v/io.kjson/kjson-test?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.kjson%22%20AND%20a:%22kjson-test%22)

Library for testing Kotlin JSON applications

This library provides a convenient way of testing applications that produce JSON results.
It uses a Kotlin DSL to describe the expected JSON values, and produces detailed error messages in the case of failure.

The library is an evolution of the [`json-kotlin-test`](https://github.com/pwall567/json-kotlin-test) project.
This library operates almost identically to the earlier library, and in most cases users will need to change only the
dependency coordinates and an import statement in order to convert to the new library.

One very small change that will affect only those who examine the content of the error messages, is that error locations
are now output in [JSON Pointer](https://tools.ietf.org/html/rfc6901) form instead of pseudo-JavaScript.
This is to bring the internal references within a JSON array or object into line with the approach taken by
[JSON Schema](https://json-schema.org/).

**New in version 2.1:** the `property`, `item` and `value` tests may now take any of the `java.time` classes or
`kotlin.time.Duration` or `UUID` as a parameter.
There are often multiple ways of representing these classes in string form (for example, a time may include or omit
trailing zeros on fractional seconds, or a UUID may use upper or lower case alphabetic characters), so using string
comparisons in tests could cause those tests to fail incorrectly.
The new tests should be used in preference to string tests from now on.

**New in version 2.2:** the `property`, `item` and `value` tests may now take an `enum` value as a parameter, and the
effect of the test is to do a string comparison on the `enum` name.
This is just a matter of convenience &ndash; the tests are simpler if they don't require a `.name` or `.toString()`.

**New in version 3.0:** the `integer`, `localDate` _etc._ named lambda tests are now named `isInteger`, `isLocalDate`
_etc._
This is for consistency with the newly-added `isObject` and `isArray` tests.
Also, the lambda functions have been moved to standalone functions, so they each require an `import` statement.

**New in version 3.9:** the `anyItem` test allows for the checking of array items in an unordered set.

## Contents

- [Quick Start](#quick-start)
- [User Guide](#user-guide)
- [Reference](#reference)
- [Dependency Specification](#dependency-specification)




## Quick Start

### Example

In a test class:
```kotlin
    @Test fun `should produce correct JSON name data`() {
        val json = functionUnderTest()
        // json is (for example):
        // {"data":[{"id":1234,"surname":"Walker","givenNames":["Richard"]}]}
        expectJSON(json) {
            property("data") {
                item(0) {
                    property("id", 1234)
                    property("surname", "Walker")
                    property("givenNames") {
                        count(1)
                        item(0, "Richard")
                    }
                    propertyAbsent("birthDate")
                }
            }
        }
    }
```

### The Problem

Many Kotlin applications return their result in the form of a JSON object, or in some cases, as an array of JSON
objects.
When we attempt to test these applications, we run into a simple problem &ndash; there may be many valid JSON
representations of the same object.
The properties of a JSON object do not have a pre-determined order, and given a valid set of properties, any sequence of
those properties is equally valid.

Also, the JSON specification allows for whitespace to be added at many points in the JSON string, without affecting the
meaning of the content.

All of this means that it is not possible to test the results of a function returning JSON simply by performing a string
comparison on the output.
We need a means of checking the data content of the JSON, regardless of the formatting.

Many developers avoid the problem by deserializing the JSON into the internal form of the object for comparison, but
that is not always a useful option.
And there are libraries that will check an arbitrary JSON string, but they are mostly written in and for Java, and they
do not take advantage of the capabilities of Kotlin.

### The Solution

The `kjson-test` library allows the expected content of a JSON object to be described in a simple, intuitive form,
allowing functions returning JSON to be tested easily.

In many cases, the expected values will be known precisely, and the basic forms of comparison make a good starting
point:
```kotlin
    property("id", 1234)
    property("surname", "Walker")
```
This checks that the current node is an object with a property named "id" having the value 1234, and another property
"surname" with the value "Walker".

Nested objects may be checked by:
```kotlin
    property("details") {
        // tests on the nested object
    }
```

And array items can similarly be checked, either as primitive values or as nested objects or arrays.

The `kjson-test` library makes testing simple cases easy and clear, and at the same time provides functionality to
meet very broad and complex testing requirements.






## User Guide

First, some terminology:
- a JSON **object** is the form of JSON structure enclosed in curly braces (` { } `) and containing a set of name-value
  pairs
- these pairs are called **properties**, and each has a property name,  and a value which may be a primitive value or a
  nested object or array
- a JSON **array** is an ordered collection of values (possibly nested objects or arrays) enclosed in square brackets
  (` [ ] `)
- an **array item** is a member of an array
- a JSON **value** is any of the possible JSON data types: a string, a number, a boolean, the value "null" or a nested
  object or array

A function or service returning a JSON result will usually return an object or an array of objects, but according to the
JSON specification the string representation of any JSON value is a valid JSON string.
The `kjson-test` library provides facilities for testing all forms of JSON results.

### Invoking the Tests

The set of tests on a JSON string is enclosed in a call to the [`expectJSON`](#expectjson) function.
This parses the JSON into an internal form and then performs the tests in the supplied lambda.
```kotlin
    expectJSON(stringToBeTested) {
        // tests...
    }
```

If any of the tests fail, an `AssertionError` will be thrown with a detailed error message, usually including expected
and actual values.
The message will in most cases be prefixed by the location of the node in error in
[JSON Pointer](https://tools.ietf.org/html/rfc6901) form

### Testing JSON Properties

The [`property`](#property) function declares a test (or a group of tests) to be performed on a property of an object.
There are several forms of the `property` function; they all take a property name (`String`) as the first parameter, and
an expected value or a lambda as the second.

Some examples:
```kotlin
        property("name", "William")
        property("id", 12345678)
        property("open", true)
        property("details") {
            // tests on nested object or array
        }
```

The last of these examples above may be made more specific:
```kotlin
        propertyIsObject("details") {
            // tests on nested object
        }
        propertyIsArray("list") {
            // tests on nested array
        }
```
These functions confirm that the property is of the correct type before applying the nested tests.

And if the property is an array, the array size may be checked in the same operation:
```kotlin
        propertyIsArray("list", size = 2) {
            // tests on nested array
        }
```

### Testing Array Items

The [`item`](#item) function declares a test (or a group of tests) to be performed on an item of an array.
As with `property`, there are several forms of the `item` function, each taking an index (`Int`) as the first parameter,
and an expected value or a lambda as the second.

Some examples:
```kotlin
        item(0, "bear")
        item(27, -1)
        item(1, true)
        item(4) {
            // tests on nested object or array
        }
```

As with property tests, if the array item is expected to be an object or an array, additional functions are available:
```kotlin
        itemIsObject(3) {
            // tests on nested object
        }
        itemIsArray(0) {
            // tests on nested array
        }
        itemIsArray(0, size = 2) {
            // tests on nested array
        }
```

In the case of a JSON array representing an unordered set, the [`anyItem`](#anyitem) function tests whether any item in
an array matches the specified test.
For example, an authorisation function may return a set of permissions, but the order of the entries is immaterial:
```kotlin
        property("permissions") {
            anyItem("customer:read")
            anyItem("product:read")
        }
```
(See also the section on [Exhaustive Checks](#exhaustive-checks) below.)

And again, if the array item is expected to be an object or an array, additional functions are available:
```kotlin
        anyItemIsObject {
            // tests on nested object
        }
        anyItemIsArray {
            // tests on nested array
        }
        anyItemIsArray(size = 2) {
            // tests on nested array
        }
```

### Testing Primitive JSON Values

In rare cases a JSON string consists of a primitive value (string, number, boolean or `null`).
The [`value`](#value) function allows tests to be applied to the single primitive value, and it takes a single
parameter, the expected value or lambda.

Some examples:
```kotlin
        value("success")
        value(0)
        value(false)
        value(null)
```

### Named Tests

The lambda parameter of the `property`, `item` or `value` tests normally takes the form of a set of tests applied to a
nested object or array, but it can also specify a named lambda, as in the following examples:
```kotlin
        property("account", isInteger)
        item(0, isString)
        property("id", isUUID)
        property("created", isOffsetDateTime)
```

These functions test that a property or item meets a particular requirement, without specifying the exact value.
In the above tests, the property "account" is checked to have an integer value, item 0 of an array contains a string,
property "id" contains a string formatted as a UUID and property "created" contains a string following the pattern of
the `OffsetDateTime` class.

See the [Reference](#reference) section for a full list of these [General Test Lambdas](#general-test-lambdas).

### Floating Point

Floating point numbers are those with a decimal point, or using the scientific notation (e.g. 1.5e20).
Many decimal floating point numbers can not be represented with complete accuracy in a binary floating point system, so
the `kjson-test` library converts all such numbers to `BigDecimal`.
This means that tests on floating point numbers must use `BigDecimal`, or `ClosedRange<BigDecimal>`, or
`Collection<BigDecimal>`.

If a comparison using a `BigDecimal` is performed against an `Int` or a `Long`, the value will be "widened" to
`BigDecimal` before the test is performed.

One unusual feature of the `BigDecimal` class is that the `equals` comparison requires that both the value and the scale
of the number must be equal, but this library uses `compareTo` to compare the values regardless of scale.
If it is important to confirm that a certain number of decimal digits are present (for example, in a money value), the
[`scale`](#scale) function may be used to test the number of digits after the decimal point.
An `Int` or a `Long` will always be considered as having zero decimal places.

### Custom Deserialization

This library looks only at the input JSON, and does not take into account any custom deserializations that may be
applied to the JSON when it is used in other situations.
Custom name annotations, and even the conversion of dates from strings to `LocalDate` (for example) are not applied.

### Check for `null`

Checking a value for `null`, e.g. `property("note", null)` will check that the named property **is present** in the JSON
string and has the value `null`.
Also, for convenience, a named lambda `isNull` will do the same thing.

In some cases, the fact that a property is not present in the JSON may be taken as being equivalent to the property
being present with a `null` value.
If it is important to distinguish between a `null` property and an omitted property, there are functions which test
specifically for the presence or absence of a property:

| To test that a property...                   | Use                                                     |
|----------------------------------------------|---------------------------------------------------------|
| ...is present and is not null                | `property("name", isNonNull)`                           |
| ...is present and is null                    | `property("name", isNull)`                              |
| ...is present whether null or not            | [`propertyPresent("name")`](#propertypresent)           |
| ...is not present                            | [`propertyAbsent("name")`](#propertyabsent)             |
| ...is not present, or is present and is null | [`propertyAbsentOrNull("name")`](#propertyabsentornull) |

Instead of checking all of the properties that are expected not to be present, it may often be simpler to use the
[`count`](#count) function to check the number of properties that **are** present.

### Check for Member of Collection

You can check a value as being a member of a `Collection`.
For example:
```kotlin
    property("quality", setOf("good", "bad"))
```
This test will succeed if the value of the property is one of the members of the set.

The `Collection` must be of the appropriate type for the value being checked, and each of the functions `property`,
`item` and `value` has an overloaded version that takes a `Collection`.

### Check for Value in Range

You can also check a value as being included in a range (`IntRange`, `LongRange` or `ClosedRange`).
For example:
```kotlin
    property("number", 1000..9999)
```

As with `Collection`, the range must be of the appropriate type, and each of the functions `property`, `item` and
`value` has an overloaded version that takes a range.

### Check for String Length

The [`length`](#length) function is available to check the length of a string.
If, for example, you expect a string to contain between 5 and 20 characters, but you don't need to check the exact
contents, you can specify:
```kotlin
    property("name", length(5..20))
```
The length may be specified as an integer value or an `IntRange`.

### Check against `Regex`

It is also possible to check a string against a `Regex`, for example:
```kotlin
    property("name", Regex("^[A-Za-z]+$"))
```

### Exhaustive Checks

To confirm that all properties of an object or all items in an array are tested, the tests may be enclosed in an
`exhaustive` block, for example:
```kotlin
    property("address") {
        exhaustive {
            property("number", 27)
            property("streetName", "23rd")
            property("streetType", "St")
        }
    }
```
This will cause an error to be reported if the object contains other properties in addition to those tested.

An `exhaustive` block can also check that all items in an array have been tested.
This can be particularly useful when paired with the [`anyItem`](#anyitem) test, for example:
```kotlin
    property("members") {
        exhaustive {
            anyItem("Tom")
            anyItem("Dick")
            anyItem("Harry")
        }
    }
```
This tests that the array at `members` contains the items `"Tom"`, `"Dick"` and `"Harry"` in any order, and no other
entries.

### Multiple Possibilities

You can also check for a value matching one of a number of possibilities.
The [`oneOf`](#oneof) function takes any number of lambdas (as `vararg` parameters) and executes them in turn until it
finds one that matches.
```kotlin
    property("elapsedTime") {
        oneOf(isDuration, isInteger)
    }
```

To simplify the use of lambdas in the `oneOf` list, the [`test`](#test) function creates a lambda representing any of
the available types of test:
```kotlin
    property("response") {
        oneOf(test(null), test(setOf("YES", "NO")))
    }
```

Of course, the full lambda syntax can be used to describe complex combinations.
In the following case, the JSON string may be either `{"data":27}` or `{"error":nnn}`, where _nnn_ is a number between
0 and 999.
```kotlin
    expectJSON(json) {
        oneOf({
            property("data", 27)
        },{
            property("error", 0..999)
        })
    }
```

### Custom Tests

In any of the tests that take a lambda parameter, the lambda is not restricted to the functions provided by the library;
the full power of Kotlin is available to create tests of any complexity.

The "receiver" for the lambda is an object describing the current node in the JSON structure, and the value is available
as a `val` named [`node`](#node-nodeasstring-nodeasint-nodeaslong-nodeasdecimal-nodeasboolean-nodeasobject-nodeasarray).
The type of `node` is `Any?`, but in practice it will be one of:

- `String`
- `Int`
- `Long`
- `BigDecimal`
- `Boolean`
- `List<Any?>`
- `Map<String, Any?>`
- `null`

(In the case of `Map` or `List`, the type parameter `Any?` will itself be one of the above.)

There are also conversion functions, each of which takes the form of a `val` with a custom accessor.
These accessors either return the node in the form requested, or throw an `AssertionError` with a detailed error
message.

| Accessor        | Type         |
|-----------------|--------------|
| `nodeAsString`  | `String`     |
| `nodeAsInt`     | `Int`        |
| `nodeAsLong`    | `Long`       |
| `nodeAsDecimal` | `BigDecimal` |
| `nodeAsBoolean` | `Boolean`    |
| `nodeAsArray`   | `List<*>`    |
| `nodeAsObject`  | `Map<*, *>`  |

To report errors, the [`error`](#error) function will create an `AssertionError` with the message provided, prepending
the JSON pointer location for the current node in the JSON.
The [`showNode`](#shownode) function can be used to display the actual node value in the error message.

Example:
```kotlin
    expectJSON(jsonString) {
        property("abc") {
            if (nodeAsInt.rem(3) != 0)
                error("Value not divisible by 3 - ${showNode()}")
        }
    }
```

### Spring Framework

Many users will wish to use `kjson-test` in conjunction with the
[Spring Framework](https://spring.io/projects/spring-framework).
A suggestion for simplifying some forms of Spring testing is included in the [Spring and `kjson-test`](SPRING.md) guide.


## Reference

### The `import` Statement

Perhaps the most complex part of `kjson-test` is the import statement:
```kotlin
import io.kjson.test.JSONExpect.Companion.expectJSON
```
If the IDE is configured to include the `kjson-test` library, it will often include the `import` automatically when you
enter the name of the test function.

### `expectJSON`

The `expectJSON` function introduces the set of tests to be performed on the JSON.
It takes two parameters:
- a `String` containing the JSON to be examined
- a lambda, specifying the tests to be performed on the JSON

Example:
```kotlin
    expectJSON(stringOfJSON) {
        property("data") {
            // tests ...
        }
    }
```

### `property`

Tests the value of a property of an object.
In all cases, the first parameter is the name of the property; the second parameter varies according to the test being
performed.

| Signature                                 | Check that the property...                                 |
|-------------------------------------------|------------------------------------------------------------|
| `property(String, String?)`               | ...is equal to a `String` or `null`                        |
| `property(String, Int)`                   | ...is equal to an `Int`                                    |
| `property(String, Long)`                  | ...is equal to a `Long`                                    |
| `property(String, BigDecimal)`            | ...is equal to a `BigDecimal`                              |
| `property(String, Boolean)`               | ...is equal to a `Boolean`                                 |
| `property(String, Regex)`                 | ...is a `String` matching the given `Regex`                |
| `property(String, LocalDate)`             | ...is a `String` matching the given `LocalDate`            |
| `property(String, LocalDateTime)`         | ...is a `String` matching the given `LocalDateTime`        |
| `property(String, LocalTime)`             | ...is a `String` matching the given `LocalTime`            |
| `property(String, OffsetDateTime)`        | ...is a `String` matching the given `OffsetDateTime`       |
| `property(String, OffsetTime)`            | ...is a `String` matching the given `OffsetTime`           |
| `property(String, ZonedDateTime)`         | ...is a `String` matching the given `ZonedDateTime`        |
| `property(String, YearMonth)`             | ...is a `String` matching the given `YearMonth`            |
| `property(String, MonthDay)`              | ...is a `String` matching the given `MonthDay`             |
| `property(String, Year)`                  | ...is a `String` matching the given `Year`                 |
| `property(String, java.time.Duration)`    | ...is a `String` matching the given `java.time.Duration`   |
| `property(String, Period)`                | ...is a `String` matching the given `Period`               |
| `property(String, kotlin.time.Duration)`  | ...is a `String` matching the given `kotlin.time.Duration` |
| `property(String, UUID)`                  | ...is a `String` matching the given `UUID`                 |
| `property(String, Enum<*>)`               | ...is a `String` matching the given `Enum` member          |
| `property(String, IntRange)`              | ...is in a given range                                     |
| `property(String, LongRange)`             | ...is in a given range                                     |
| `property(String, ClosedRange<*>)`        | ...is in a given range                                     |
| `property(String, Collection<*>)`         | ...is in a given collection                                |
| `property(String, JSONExpect.() -> Unit)` | ...satisfies the given lambda                              |

In the case of a `ClosedRange` or `Collection`, the parameter type must be `Int`, `Long`, `BigDecimal` or `String`,
although in practice a range of `Int` or `Long` would be more likely to use `IntRange` or `LongRange` respectively.

Only the test for `String` has a signature that allows `null` values; this is to avoid compile-time ambiguity on tests
against `null`.
This does not mean that only `String` properties can be tested for `null` - a `null` property in the JSON is typeless
so a test for `null` would work, regardless of the type that the property would otherwise hold.

The last function signature in the list is the one that specifies a lambda &ndash; as is usual in Kotlin, when the last
parameter is a lambda it is usually written outside the parentheses of the function call.
This is the pattern followed when the lambda is an inline set of tests to be applied to the property, but this function
signature is also used for the [general test lambdas](#general-test-lambdas), or the [`length`](#length) or
[`scale`](#scale) functions.

Examples:
```kotlin
        property("id", 12345)
        property("name", "William")
        property("dob", LocalDate.of(1996, 7, 4))
        property("count", 0..9999)
        property("amount", isDecimal)
        property("reference", isUUID)
        property("address", length(1..80))
        property("code", setOf("AAA", "PQR", "XYZ"))
        property("details") {
            // nested tests
        }
```


### `propertyIsObject`

Tests a property of an object as a nested object.
The first parameter is the name of the property in the outer object; the second is an **optional** lambda of tests to be
applied to the nested object.

Examples:
```kotlin
        propertyIsObject("address") {
            // tests on address object
        }
        propertyIsObject("details") // no tests on content, just confirm that it is an object
```


### `propertyIsArray`

Tests a property of an object as an array.
Two forms are available: one which takes the name of the array property, and an optional lambda of tests to be applied
to it, and a second which includes a `size` parameter to check the size of the array.

Examples:
```kotlin
        propertyIsArray("lines") {
            // tests on lines array
        }
        propertyIsArray("details") // no tests on content, just confirm that it is an array
        propertyIsArray("lines", size = 4) {
            // tests on lines array, after checking that there are 4 items in the array
        }
```


### `item`

Tests the value of an array item.
In all cases, the first parameter is the index of the array item (must be non-negative).
The second parameter varies according to the test being performed.

| Signature                          | Check that the array item...                               |
|------------------------------------|------------------------------------------------------------|
| `item(Int, String?)`               | ...is equal to a `String` or `null`                        |
| `item(Int, Int)`                   | ...is equal to an `Int`                                    |
| `item(Int, Long)`                  | ...is equal to a `Long`                                    |
| `item(Int, BigDecimal)`            | ...is equal to a `BigDecimal`                              |
| `item(Int, Boolean)`               | ...is equal to a `Boolean`                                 |
| `item(Int, Regex)`                 | ...is a `String` matching the given `Regex`                |
| `item(Int, LocalDate)`             | ...is a `String` matching the given `LocalDate`            |
| `item(Int, LocalDateTime)`         | ...is a `String` matching the given `LocalDateTime`        |
| `item(Int, LocalTime)`             | ...is a `String` matching the given `LocalTime`            |
| `item(Int, OffsetDateTime)`        | ...is a `String` matching the given `OffsetDateTime`       |
| `item(Int, OffsetTime)`            | ...is a `String` matching the given `OffsetTime`           |
| `item(Int, ZonedDateTime)`         | ...is a `String` matching the given `ZonedDateTime`        |
| `item(Int, YearMonth)`             | ...is a `String` matching the given `YearMonth`            |
| `item(Int, MonthDay)`              | ...is a `String` matching the given `MonthDay`             |
| `item(Int, Year)`                  | ...is a `String` matching the given `Year`                 |
| `item(Int, java.time.Duration)`    | ...is a `String` matching the given `java.time.Duration`   |
| `item(Int, Period)`                | ...is a `String` matching the given `Period`               |
| `item(Int, kotlin.time.Duration)`  | ...is a `String` matching the given `kotlin.time.Duration` |
| `item(Int, UUID)`                  | ...is a `String` matching the given `UUID`                 |
| `item(Int, Enum<*>)`               | ...is a `String` matching the given `Enum` member          |
| `item(Int, IntRange)`              | ...is in a given range                                     |
| `item(Int, LongRange)`             | ...is in a given range                                     |
| `item(Int, ClosedRange<*>)`        | ...is in a given range                                     |
| `item(Int, Collection<*>)`         | ...is in a given collection                                |
| `item(Int, JSONExpect.() -> Unit)` | ...satisfies the given lambda                              |

The notes following [`property`](#property) describing the options for the second parameter apply equally to `item`.

Examples:
```kotlin
        item(0, 22)
        item(5, "William")
        item(4, 2.hours)
        item(7, isDecimal)
        item(7, scale(0..2))
        item(1, isUUID)
        item(0) {
            // nested tests
        }
```


### `itemIsObject`

Tests an array item as an object.
The first parameter is the index of the array item; the second is an **optional** lambda of tests to be applied to the
object.

Examples:
```kotlin
        itemIsObject(0) {
            // tests on object
        }
        itemIsObject(1) // no tests on content, just confirm that it is an object
```


### `itemIsArray`

Tests an array item as a nested array.
Two forms are available: one which takes the index of the array item, and an optional lambda of tests to be applied
to it, and a second which includes a `size` parameter to check the size of the nested array.

Examples:
```kotlin
        itemIsArray(0) {
            // tests on nested array
        }
        itemIsArray(2) // no tests on content, just confirm that it is an array
        itemIsArray(5, size = 2) {
            // tests on nested array, after checking that there are 2 items in the array
        }
```


### `anyItem`

Tests whether an array contains an item matching one or more tests.
The parameter varies according to the test being performed.

| Signature                        | Check that an array item...                                |
|----------------------------------|------------------------------------------------------------|
| `anyItem(String?)`               | ...is equal to a `String` or `null`                        |
| `anyItem(Int)`                   | ...is equal to an `Int`                                    |
| `anyItem(Long)`                  | ...is equal to a `Long`                                    |
| `anyItem(BigDecimal)`            | ...is equal to a `BigDecimal`                              |
| `anyItem(Boolean)`               | ...is equal to a `Boolean`                                 |
| `anyItem(Regex)`                 | ...is a `String` matching the given `Regex`                |
| `anyItem(LocalDate)`             | ...is a `String` matching the given `LocalDate`            |
| `anyItem(LocalDateTime)`         | ...is a `String` matching the given `LocalDateTime`        |
| `anyItem(LocalTime)`             | ...is a `String` matching the given `LocalTime`            |
| `anyItem(OffsetDateTime)`        | ...is a `String` matching the given `OffsetDateTime`       |
| `anyItem(OffsetTime)`            | ...is a `String` matching the given `OffsetTime`           |
| `anyItem(ZonedDateTime)`         | ...is a `String` matching the given `ZonedDateTime`        |
| `anyItem(YearMonth)`             | ...is a `String` matching the given `YearMonth`            |
| `anyItem(MonthDay)`              | ...is a `String` matching the given `MonthDay`             |
| `anyItem(Year)`                  | ...is a `String` matching the given `Year`                 |
| `anyItem(java.time.Duration)`    | ...is a `String` matching the given `java.time.Duration`   |
| `anyItem(Period)`                | ...is a `String` matching the given `Period`               |
| `anyItem(kotlin.time.Duration)`  | ...is a `String` matching the given `kotlin.time.Duration` |
| `anyItem(UUID)`                  | ...is a `String` matching the given `UUID`                 |
| `anyItem(Enum<*>)`               | ...is a `String` matching the given `Enum` member          |
| `anyItem(IntRange)`              | ...is in a given range                                     |
| `anyItem(LongRange)`             | ...is in a given range                                     |
| `anyItem(ClosedRange<*>)`        | ...is in a given range                                     |
| `anyItem(Collection<*>)`         | ...is in a given collection                                |
| `anyItem(JSONExpect.() -> Unit)` | ...satisfies the given lambda                              |

The notes following [`property`](#property) describing the options for the second parameter apply equally to `anyItem`.

Examples:
```kotlin
        anyItem(22)
        anyItem("William")
        anyItem(2.hours)
        anyItem(isDecimal)
        anyItem(scale(0..2))
        anyItem(isUUID)
        anyItem {
            // nested tests
        }
```


### `anyItemIsObject`

Tests whether an array contains an item which is an object, optionally matching a set of tests.
The parameter is an **optional** lambda of tests to be applied to the object.

Examples:
```kotlin
        anyItemIsObject {
            // tests on object
        }
        anyItemIsObject() // no tests on content, just confirm that any item is an object
```


### `anyItemIsArray`

Tests whether an array contains an item which is a nested array, optionally matching a set of tests, and also optionally
checking the array size.
Two forms are available: one which takes an optional lambda of tests to be applied to it, and a second which includes a
`size` parameter to check the size of the nested array.

Examples:
```kotlin
        anyItemIsArray {
            // tests on nested array
        }
        anyItemIsArray() // no tests on content, just confirm that any item is a nested array
        anyItemIsArray(size = 2) {
            // tests on nested array, after checking that there are 2 items in the array
        }
```


### `value`

This function takes one parameter, which varies according to the test being performed.

| Signature                      | Check that the value...                                    |
|--------------------------------|------------------------------------------------------------|
| `value(String?)`               | ...is equal to a `String` or `null`                        |
| `value(Int)`                   | ...is equal to an `Int`                                    |
| `value(Long)`                  | ...is equal to a `Long`                                    |
| `value(BigDecimal)`            | ...is equal to a `BigDecimal`                              |
| `value(Boolean)`               | ...is equal to a `Boolean`                                 |
| `value(Regex)`                 | ...is a `String` matching the given `Regex`                |
| `value(LocalDate)`             | ...is a `String` matching the given `LocalDate`            |
| `value(LocalDateTime)`         | ...is a `String` matching the given `LocalDateTime`        |
| `value(LocalTime)`             | ...is a `String` matching the given `LocalTime`            |
| `value(OffsetDateTime)`        | ...is a `String` matching the given `OffsetDateTime`       |
| `value(OffsetTime)`            | ...is a `String` matching the given `OffsetTime`           |
| `value(ZonedDateTime)`         | ...is a `String` matching the given `ZonedDateTime`        |
| `value(YearMonth)`             | ...is a `String` matching the given `YearMonth`            |
| `value(MonthDay)`              | ...is a `String` matching the given `MonthDay`             |
| `value(Year)`                  | ...is a `String` matching the given `Year`                 |
| `value(java.time.Duration)`    | ...is a `String` matching the given `java.time.Duration`   |
| `value(Period)`                | ...is a `String` matching the given `Period`               |
| `value(kotlin.time.Duration)`  | ...is a `String` matching the given `kotlin.time.Duration` |
| `value(UUID)`                  | ...is a `String` matching the given `UUID`                 |
| `value(Enum<*>)`               | ...is a `String` matching the given `Enum` member          |
| `value(IntRange)`              | ...is in a given range                                     |
| `value(LongRange)`             | ...is in a given range                                     |
| `value(ClosedRange<*>)`        | ...is in a given range                                     |
| `value(Collection<*>)`         | ...is in a given collection                                |
| `value(JSONExpect.() -> Unit)` | ...satisfies the given lambda                              |

The notes following [`property`](#property) describing the options for the second parameter apply equally to the sole
parameter of `value`.

Examples:
```kotlin
        value(0..9999)
        value(isDecimal)
        value(scale(0..2))
        value(isLocalDate)
```

### `valueAsObject`

Tests the value as an object.
The parameter is an **optional** lambda of tests to be applied to the object.

Examples:
```kotlin
        valueAsObject {
            // tests on object
        }
        valueAsObject() // no tests on content, just confirm that it is an object
```
It is not usually necessary to perform this test; the first access to a property will raise an exception if the value is
not an object.

### `valueAsArray`

Tests the value as an array.
Two forms are available: one which takes an optional lambda of tests to be applied to it, and a second which includes a
`size` parameter to check the size of the array.

Examples:
```kotlin
        valueAsArray {
            // tests on array
        }
        valueAsArray() // no tests on content, just confirm that it is an array
        valueAsArray(size = 2) {
            // tests on array, after checking that there are 2 items in the array
        }
```
It is not usually necessary to perform this test; the first access to an array item will raise an exception if the value
is not an array.

### `length`

This is used to check the length of a `String` property, array item or value.
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("name", length(1..40))
        item(0, length(12))
```

### `scale`

The scale of a `BigDecimal` may be checked, if it is required that a decimal number have a specific scale.
If the number is parsed as an `Int` or a `Long`, the scale will be zero.
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("amount", scale(1..2))
        item(0, scale(0))
```

### `count`

Tests the count of properties or array items (the length of the array).
The parameter may be an `Int` or an `IntRange`.

Examples:
```kotlin
        property("options") {
            count(2)
            item(0, "A")
            item(1, "B")
        }
```

### `oneOf`

Test each of the parameters in turn, exiting when one of them matches the node successfully.
It takes a variable number of parameters, each of which is a lambda (the [`test`](#test) function may be used to create
lambdas for this purpose).

Examples:
```kotlin
        property("id") {
            oneOf(isInteger, isUUID)
        }
        property("result") {
            oneof(test(0..99999), test("ERROR"))
        }
```

### `test`

Creates a lambda, principally for use with the [`oneOf`](#oneof) function.
It takes one parameter, which varies according to the type of test.

| Signature                    | Create a test for...                                          |
|------------------------------|---------------------------------------------------------------|
| `test(String?)`              | ...value equal to a `String` or `null`                        |
| `test(Int)`                  | ...value equal to an `Int`                                    |
| `test(Long)`                 | ...value equal to a `Long`                                    |
| `test(BigDecimal)`           | ...value equal to a `BigDecimal`                              |
| `test(Boolean)`              | ...value equal to a `Boolean`                                 |
| `test(Regex)`                | ...value a `String` matching the given `Regex`                |
| `test(LocalDate)`            | ...value a `String` matching the given `LocalDate`            |
| `test(LocalDateTime)`        | ...value a `String` matching the given `LocalDateTime`        |
| `test(LocalTime)`            | ...value a `String` matching the given `LocalTime`            |
| `test(OffsetDateTime)`       | ...value a `String` matching the given `OffsetDateTime`       |
| `test(OffsetTime)`           | ...value a `String` matching the given `OffsetTime`           |
| `test(ZonedDateTime)`        | ...value a `String` matching the given `ZonedDateTime`        |
| `test(YearMonth)`            | ...value a `String` matching the given `YearMonth`            |
| `test(MonthDay)`             | ...value a `String` matching the given `MonthDay`             |
| `test(Year)`                 | ...value a `String` matching the given `Year`                 |
| `test(java.time.Duration)`   | ...value a `String` matching the given `java.time.Duration`   |
| `test(Period)`               | ...value a `String` matching the given `Period`               |
| `test(kotlin.time.Duration)` | ...value a `String` matching the given `kotlin.time.Duration` |
| `test(UUID)`                 | ...value a `String` matching the given `UUID`                 |
| `test(Enum<*>)`              | ...value a `String` matching the given `Enum` member          |
| `test(IntRange)`             | ...value in a given range                                     |
| `test(LongRange)`            | ...value in a given range                                     |
| `test(ClosedRange<*>)`       | ...value in a given range                                     |
| `test(Collection<*>)`        | ...value in a given collection                                |

The notes following [`property`](#property) describing the options for the second parameter apply equally to the sole
parameter of `test`.

For examples see the [`oneOf`](#oneof) function.

### `propertyAbsent`

Tests that no property with the specified name is present in the object.
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("controls") {
            property("openingDate", isLocalDate)
            propertyAbsent("closingDate")
        }
```

### `propertyAbsentOrNull`

Tests that no property with the specified name is present in the object, or if one is present, that it is `null`.
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("controls") {
            property("openingDate", isLocalDate)
            propertyAbsentOrNull("closingDate")
        }
```

### `propertyPresent`

Tests that a property with the specified name is present in the object, regardless of the value.
It takes one parameter - the property name (`String`).

Examples:
```kotlin
        property("book") {
            propertyPresent("author")
        }
```

### `node`, `nodeAsString`, `nodeAsInt`, `nodeAsLong`, `nodeAsDecimal`, `nodeAsBoolean`, `nodeAsObject`, `nodeAsArray`

In custom tests the current node can be accessed by one of the following:

| `val` name      | Type         |
|-----------------|--------------|
| `node`          | `Any?`       |
| `nodeAsString`  | `String`     |
| `nodeAsInt`     | `Int`        |
| `nodeAsLong`    | `Long`       |
| `nodeAsDecimal` | `BigDecimal` |
| `nodeAsBoolean` | `Boolean`    |
| `nodeAsObject`  | `Map<*, *>`  |
| `nodeAsArray`   | `List<*>`    |

If the node as not of the required type, an `AssertionError` will be thrown.

### `error`

In custom tests the `error` function will output the message given, prepended with the JSON pointer location for the
current node.
It takes one parameter - the message (`String`).

### `showNode`

The `showNode` function may be used to format the node for inclusion in an error message.
It takes no parameters.

### General Test Lambdas

These may be used with the form of [`property`](#property), [`item`](#item) or [`value`](#value) that takes a lambda
parameter.
They are useful when only the general nature of the value is to be tested, and the actual value is not important.

| Name               | Tests that the value is...                                     |
|--------------------|----------------------------------------------------------------|
| `isNull`           | ...null                                                        |
| `isNonNull`        | ...non-null                                                    |
| `isObject`         | ...an object                                                   |
| `isEmptyObject`    | ...an empty object (an object with no properties)              |
| `isArray`          | ...an array                                                    |
| `isEmptyArray`     | ...an empty array                                              |
| `isString`         | ...a `String`                                                  |
| `isInteger`        | ...an `Int`                                                    |
| `isLongInteger`    | ...a `Long`                                                    |
| `isDecimal`        | ...a `BigDecimal` (a number with an optional decimal fraction) |
| `isUUID`           | ...a `String` containing a valid UUID                          |
| `isLocalDate`      | ...a `String` containing a valid `LocalDate`                   |
| `isLocalDateTime`  | ...a `String` containing a valid `LocalDateTime`               |
| `isLocalTime`      | ...a `String` containing a valid `LocalTime`                   |
| `isOffsetDateTime` | ...a `String` containing a valid `OffsetDateTime`              |
| `isOffsetTime`     | ...a `String` containing a valid `OffsetTime`                  |
| `isZonedDateTime`  | ...a `String` containing a valid `ZonedDateTime`               |
| `isYear`           | ...a `String` containing a valid `Year`                        |
| `isYearMonth`      | ...a `String` containing a valid `YearMonth`                   |
| `isMonthDay`       | ...a `String` containing a valid `MonthDay`                    |
| `isJavaDuration`   | ...a `String` containing a valid `java.time.Duration`          |
| `isPeriod`         | ...a `String` containing a valid `Period`                      |
| `isDuration`       | ...a `String` containing a valid `kotlin.time.Duration`        |

Consistent with the widening of numbers in the tests against `Long` and `BigDecimal` values, the `isLongInteger` test
passes if the value is `Int` or `Long`, and the `isDecimal` test passes if the value is `Int` or `Long` or `BigDecimal`.


## Dependency Specification

The latest version of the library is 3.9, and it may be obtained from the Maven Central repository.
(The following dependency declarations assume that the library will be included for test purposes; this is
expected to be its principal use.)

### Maven
```xml
    <dependency>
      <groupId>io.kjson</groupId>
      <artifactId>kjson-test</artifactId>
      <version>3.9</version>
      <scope>test</scope>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'io.kjson:kjson-test:3.9'
```
### Gradle (kts)
```kotlin
    testImplementation("io.kjson:kjson-test:3.9")
```

Peter Wall

2023-09-18
