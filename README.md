# kjson-test

[![Build Status](https://github.com/pwall567/kjson-test/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/kjson-test/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v2.0.21&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v2.0.21)
[![Maven Central](https://img.shields.io/maven-central/v/io.kjson/kjson-test?label=Maven%20Central)](https://central.sonatype.com/artifact/io.kjson/kjson-test)

Library for testing Kotlin JSON applications

This library provides a convenient means of testing applications that produce JSON results.
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

**New in version 4.4:** the `items` test allows for the checking of multiple array items with a single function call.

**New in version 4.5:** the `shouldMatchJSON` infix function allows
[comparison with a target JSON string](#simple-tests), for cases where the use of the DSL syntax might be considered
unnecessarily complex.

**New in version 5.0:** the JSON parser has been switched to the [`kjson-core`](https://github.com/pwall567/kjson-core)
library.
This change should be transparent to most users, but it gives access to a more feature-rich set of functions for
examining the JSON structures being tested.

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
This checks that the current node is an object with a property named &ldquo;`id`&rdquo; having the value 1234, and
another property &ldquo;`surname`&rdquo; with the value &ldquo;`Walker`&rdquo;.

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
- a JSON **value** is any of the possible JSON data types: a string, a number, a boolean, the value &ldquo;`null`&rdquo;
  or a nested object or array

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

Or, from version 4.1 onwards, the following syntax does exactly the same, but fits in better with the `shouldXXXX` idiom
of the [`should-test`](https://github.com/pwall567/should-test) library:
```kotlin
    stringToBeTested shouldMatchJSON {
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

Where the array contains only primitive values, a shorthand form is available:
```kotlin
        items("Tom", "Dick", "Harry")
```
This is equivalent to:
```kotlin
        count(3)
        item(0, "Tom")
        item(1, "Dick")
        item(2, "Harry")
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
In the above tests, the property &ldquo;`account`&rdquo; is checked to have an integer value, item 0 of an array
contains a string, property &ldquo;`id`&rdquo; contains a string formatted as a UUID and property
&ldquo;`created`&rdquo; contains a string following the pattern of the `OffsetDateTime` class.

See the [Reference](#reference) section for a full list of these [General Test Lambdas](#general-test-lambdas).

### Floating Point

Floating point numbers are those with a decimal point, or using the scientific notation (_e.g._ 1.5e20).
Many decimal floating point numbers can not be represented with complete accuracy in a binary floating point system, so
the `kjson-test` library converts all such numbers to `BigDecimal`.
This means that tests on floating point numbers must use `BigDecimal`, or `ClosedRange<BigDecimal>`, or
`Collection<BigDecimal>`.

If a comparison using a `BigDecimal` is performed against an `Int` or a `Long`, the value will be &ldquo;widened&rdquo;
to `BigDecimal` before the test is performed.

One unusual feature of the `BigDecimal` class is that the `equals` comparison requires that both the value and the scale
of the number must be equal, but this library uses `compareTo` to compare the values regardless of scale.
If it is important to confirm that a certain number of decimal digits are present (for example, in a money value), the
[`scale`](#scale) function may be used to test the number of digits after the decimal point.
An `Int` or a `Long` will always be considered as having zero decimal places.

### Custom Deserialization

This library looks only at the input JSON, and does not take into account any custom deserializations that may be
applied to the JSON when it is used in normal circumstances.

For example, custom name annotations are sometimes used to specify the name to be used as the JSON property name if it
differs from the internal field name; in such cases the external JSON name must be used in the tests, not the field
name.

### Check for `null`

Checking a value for `null`, _e.g._ `property("note", null)` will check that the named property **is present** in the
JSON string and has the value `null`.
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

### Simple Tests

When the set of tests to be applied to a JSON string is very simple, it may be more convenient to express them as a
target JSON string:
```kotlin
    result shouldMatchJSON """{"number":27,"name":"George"}"""
```
This is exactly equivalent to:
```kotlin
    result shouldMatchJSON {
        property("number", 27)
        property("name", "George")
        count(2)
    }
```
Both the string to be tested and the target string will be parsed into an internal form, so differences in whitespace or
property order will not be significant.
And while the DSL syntax has the advantage that errors will be reported against specific lines in the set of tests, the
target string form will still give detailed error messages including the location in the JSON structure.

### Custom Tests

In any of the tests that take a lambda parameter, the lambda is not restricted to the functions provided by the library;
the full power of Kotlin is available to create tests of any complexity.

The &ldquo;receiver&rdquo; for the lambda is an object describing the current node in the JSON structure, and the value
is available as a `val` named
[`node`](#node-nodeasstring-nodeasint-nodeaslong-nodeasdecimal-nodeasboolean-nodeasobject-nodeasarray).
The type of `node` is `JSONValue?`; [`JSONValue`](https://github.com/pwall567/kjson-core#jsonvalue) is a sealed
interface, and the implementing classes represent the JSON data types:

- [`JSONString`](https://github.com/pwall567/kjson-core#jsonstring) &ndash; a string value
- [`JSONInt`](https://github.com/pwall567/kjson-core#jsonint) &ndash; a number value that fits in a 32-bit signed
  integer
- [`JSONLong`](https://github.com/pwall567/kjson-core#jsonlong) &ndash; a number value that fits in a 64-bit signed
  integer
- [`JSONDecimal`](https://github.com/pwall567/kjson-core#jsondecimal) &ndash; any number value, including non-integer
  (uses `BigDecimal` internally)
- [`JSONBoolean`](https://github.com/pwall567/kjson-core#jsonboolean) &ndash; a boolean value
- [`JSONArray`](https://github.com/pwall567/kjson-core#jsonarray) &ndash; an array
- [`JSONObject`](https://github.com/pwall567/kjson-core#jsonobject) &ndash; an object

There are also conversion functions, each of which takes the form of a `val` with a custom accessor.
These accessors either return the node in the form requested, or throw an `AssertionError` with a detailed error
message.

| Accessor        | Type                                                |
|-----------------|-----------------------------------------------------|
| `nodeAsString`  | `String`                                            |
| `nodeAsInt`     | `Int`                                               |
| `nodeAsLong`    | `Long`                                              |
| `nodeAsDecimal` | `BigDecimal`                                        |
| `nodeAsBoolean` | `Boolean`                                           |
| `nodeAsArray`   | `JSONArray` (implements `List<JSONValue?>`)         |
| `nodeAsObject`  | `JSONObject` (implements `Map<String, JSONValue?>`) |

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

From version 4.1 onwards the `shouldMatchJSON` infix function offers a more attractive syntax, as well as a simpler
import:
```kotlin
import io.kjson.test.shouldMatchJSON
```

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

### `shouldMatchJSON`

The `shouldMatchJSON` infix function does exactly the same as `expectJSON`, but uses the more attractive `shouldXXXX`
syntax.
The LHS argument is the `String` containing the JSON to be examined, while the RHS is the lambda containing the tests.

Example:
```kotlin
    stringOfJSON shouldMatchJSON {
        property("data") {
            // tests ...
        }
    }
```

An alternate form of the `shouldMatchJSON` infix function takes as its parameter a target JSON string.
In this case, the target string is taken as a set of exact comparison tests to be applied to the string under
examination.
Complex tests such as ranges or regular expression matching are not available, but for simple cases this can be a more
convenient syntax:
```kotlin
    stringOfJSON shouldMatchJSON """{"data":{"name":"Fred","age":27}}"""
```
Because the tests are performed on a node-by-node basis, any errors will still be reported in full detail, including
expected and actual values, and the location in JSON Pointer form.

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
| `property(String, JSONValue)`             | ...is equal to a `JSONValue`                               |
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

When the test is against a `JSONValue` (from the [`kjson-core`](https://github.com/pwall567/kjson-core) library), the
comparison is performed on a node-by-node basis; if the `JSONValue` is a `JSONObject` or a `JSONArray`, error messages
will include a pointer to the specific node in error.

In the case of a `ClosedRange` or `Collection`, the parameter type must be `Int`, `Long`, `BigDecimal`, `String` or one
of the time classes, although in practice a range of `Int` or `Long` would be more likely to use `IntRange` or
`LongRange` respectively.

Only the test for `String` has a signature that allows `null` values; this is to avoid compile-time ambiguity on tests
against `null`.
This does not mean that only `String` properties can be tested for `null` &ndash; a `null` property in the JSON is
typeless so a test for `null` would work, regardless of the type that the property would otherwise hold.

One important consideration to keep in mind when using the comparisons involving standard Java classes such as `UUID`
and `LocalDate` &ndash; some mocking libraries allow the static functions of Java classes to be mocked; for example,
[MockK](https://mockk.io) has a function `mockkStatic()` which will intercept calls to the static functions of a
nominated class.
This type of interception, when used on the standard library classes, can cause comparisons involving those classes to
fail in unpredictable ways, and this technique should probably be avoided when using `kjson-test`.

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
In all cases, the first parameter is the index of the array item (zero-based, must be within array bounds).
The second parameter varies according to the test being performed.

| Signature                          | Check that the array item...                               |
|------------------------------------|------------------------------------------------------------|
| `item(Int, String?)`               | ...is equal to a `String` or `null`                        |
| `item(Int, Int)`                   | ...is equal to an `Int`                                    |
| `item(Int, Long)`                  | ...is equal to a `Long`                                    |
| `item(Int, BigDecimal)`            | ...is equal to a `BigDecimal`                              |
| `item(Int, Boolean)`               | ...is equal to a `Boolean`                                 |
| `item(Int, JSONValue)`             | ...is equal to a `JSONValue`                               |
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


### `items`

Tests all the items in an array with a single function.
The `items` function takes a `vararg` list of values, and the array is tested to contain exactly those values in the
same order.
The values must all be of the same type, and that type must be one of:
- `Int`
- `Lomg`
- `BigDecimal`
- `Boolean`
- `Char`
- `String`
- `LocalDate`
- `LocalDateTime`
- `LocalTime`
- `OffsetDateTime`
- `OffsetTime`
- `ZonedDateTime`
- `YearMonth`
- `MonthDay`
- `Year`
- `java.time.Duration`
- `Period`
- `UUID`
- `Enum<*>`

Note that `kotlin.time.Duration` is not on the list; it is implemented as a `value class`, and Kotlin does not
currently (version 2.0.21) allow such classes to be used in `vararg` lists.

The array is checked to be of the same size as the list of values.

Examples:
```kotlin
        items(1, 1, 2, 3, 5, 8, 13)
        items("alpha", "beta", "gamma")
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
| `anyItem(JSONValue)`             | ...is equal to a `JSONValue`                               |
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
| `value(JSONValue)`             | ...is equal to a `JSONValue`                               |
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

### `valueIsObject`

Tests the value as an object.
The parameter is an **optional** lambda of tests to be applied to the object.

Examples:
```kotlin
        valueIsObject {
            // tests on object
        }
        valueIsObject() // no tests on content, just confirm that it is an object
```
It is not usually necessary to perform this test; the first access to a property will raise an exception if the value is
not an object.

### `valueIsArray`

Tests the value as an array.
Two forms are available: one which takes an optional lambda of tests to be applied to it, and a second which includes a
`size` parameter to check the size of the array.

Examples:
```kotlin
        valueIsArray {
            // tests on array
        }
        valueIsArray() // no tests on content, just confirm that it is an array
        valueIsArray(size = 2) {
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
| `test(JSONValue)`            | ...value equal to a `JSONValue`                               |
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

| `val` name      | Type         | Notes                                                                |
|-----------------|--------------|----------------------------------------------------------------------|
| `node`          | `Any?`       |                                                                      |
| `nodeAsString`  | `String`     |                                                                      |
| `nodeAsInt`     | `Int`        |                                                                      |
| `nodeAsLong`    | `Long`       | An `Int` will be accepted and returned as a `Long`                   |
| `nodeAsDecimal` | `BigDecimal` | An `Int` or a `Long` will be accepted and returned as a `BigDecimal` |
| `nodeAsBoolean` | `Boolean`    |                                                                      |
| `nodeAsObject`  | `Map<*, *>`  |                                                                      |
| `nodeAsArray`   | `List<*>`    |                                                                      |

If the node as not of the required type, an `AssertionError` will be thrown.

### `error`

In custom tests the `error` function will output the message given, prepended with the JSON pointer location for the
current node.
It takes one parameter - the message (`String`).

### `showNode`

The `showNode` function may be used to format the node for inclusion in an error message.
It takes no parameters.

### General Test Lambdas

These are all functions with the signature `JSONExpect.() -> Unit`, and they may be used with the form of the
[`property`](#property), [`item`](#item) or [`value`](#value) functions that takes a lambda parameter.
They are useful when only the general nature of the value is to be tested, and the actual value is not important.

| Name               | Tests that the value is...                                                                    |
|--------------------|-----------------------------------------------------------------------------------------------|
| `isNull`           | ...null                                                                                       |
| `isNonNull`        | ...non-null                                                                                   |
| `isObject`         | ...an object                                                                                  |
| `isEmptyObject`    | ...an empty object (an object with no properties)                                             |
| `isNonEmptyObject` | ...a non-empty object (an object with at least one property)                                  |
| `isArray`          | ...an array                                                                                   |
| `isEmptyArray`     | ...an empty array                                                                             |
| `isNonEmptyArray`  | ...a non-empty array                                                                          |
| `isString`         | ...a `String`                                                                                 |
| `isInteger`        | ...an `Int`                                                                                   |
| `isLongInteger`    | ...a `Long` (see <sup>1</sup> below)                                                          |
| `isDecimal`        | ...a `BigDecimal` (a number with an optional decimal fraction &ndash; see <sup>1</sup> below) |
| `isUUID`           | ...a `String` containing a valid UUID (see <sup>2</sup> below)                                |
| `isLocalDate`      | ...a `String` containing a valid `LocalDate`                                                  |
| `isLocalDateTime`  | ...a `String` containing a valid `LocalDateTime`                                              |
| `isLocalTime`      | ...a `String` containing a valid `LocalTime`                                                  |
| `isOffsetDateTime` | ...a `String` containing a valid `OffsetDateTime`                                             |
| `isOffsetTime`     | ...a `String` containing a valid `OffsetTime`                                                 |
| `isZonedDateTime`  | ...a `String` containing a valid `ZonedDateTime`                                              |
| `isYear`           | ...a `String` containing a valid `Year`                                                       |
| `isYearMonth`      | ...a `String` containing a valid `YearMonth`                                                  |
| `isMonthDay`       | ...a `String` containing a valid `MonthDay`                                                   |
| `isJavaDuration`   | ...a `String` containing a valid `java.time.Duration`                                         |
| `isPeriod`         | ...a `String` containing a valid `Period`                                                     |
| `isDuration`       | ...a `String` containing a valid `kotlin.time.Duration`                                       |

<sup>1</sup> Consistent with the widening of numbers in the tests against `Long` and `BigDecimal` values, the
`isLongInteger` test passes if the value is `Int` or `Long`, and the `isDecimal` test passes if the value is `Int` or
`Long` or `BigDecimal`.

<sup>2</sup> The Java `UUID.fromString()` function does not check that a UUID is valid &ndash; it allows the five blocks
of hexadecimal digits to be shorter or longer than their expected lengths (8-4-4-4-12).
The `isUUID` test performs a strict check of the lengths of the five blocks, as well as the content.

## Dependency Specification

The latest version of the library is 5.0, and it may be obtained from the Maven Central repository.
(The following dependency declarations assume that the library will be included for test purposes; this is
expected to be its principal use.)

### Maven
```xml
    <dependency>
      <groupId>io.kjson</groupId>
      <artifactId>kjson-test</artifactId>
      <version>5.0</version>
      <scope>test</scope>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'io.kjson:kjson-test:5.0'
```
### Gradle (kts)
```kotlin
    testImplementation("io.kjson:kjson-test:5.0")
```

Peter Wall

2025-06-19
