# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [4.0] - 2024-07-15
### Changed
### Added
- `build.yml`, `deploy.yml`: converted project to GitHub Actions
### Changed
- `JSONExpect`: added `value(Char)`, `property(String, Char)`, `item(Int, Char)` and `anyItem(Char)`
- `JSONExpect`: improved error messages
- `pom.xml`: updated Kotlin version to 1.9.24, updated dependency versions
### Removed
- `.travis.yml`

## [3.12] - 2024-02-18
### Changed
- `pom.xml`: updated dependency versions

## [3.11] - 2023-12-02
### Changed
- `pom.xml`: updated dependency versions

## [3.10] - 2023-09-24
### Changed
- `pom.xml`: updated dependency versions

## [3.9] - 2023-09-18
### Changed
- `JSONExpect`: added `anyItem()`, `anyItemIsObject()`, `anyItemIsArray()`
- `JSONExpect`: simplified KDoc in line with Kotlin recommendations

## [3.8] - 2023-07-22
### Changed
- `pom.xml`: updated Kotlin version to 1.8.22

## [3.7] - 2023-07-07
### Changed
- `JSONExpect`: added `valueIsObject()`, `valueIsArray()`, `propertyIsObject()`, `propertyIsArray()`, `itemIsObject()`,
  `itemIsArray()`
- `JSONExpect`: improved error messages on tests for member of range or collection
- `pom.xml`: updated Kotlin version to 1.7.21

## [3.6] - 2023-01-06
### Changed
- `pom.xml`: updated dependency versions

## [3.5] - 2022-11-27
### Changed
- `Lambda`: added `isEmptyObject` (for consistency)
- `JSONExpect`: added `exhaustive`
- `pom.xml`: updated dependency versions

## [3.4] - 2022-11-20
### Changed
- `pom.xml`: updated dependency versions

## [3.3] - 2022-11-07
### Changed
- `pom.xml`: updated dependency versions
- `README.md`: minor corrections

## [3.2] - 2022-10-26
### Changed
- `JSONExpect`: removed dependency on `kotlin-test-junit` library
- `pom.xml`: changed scope of `kotlin-test-junit` dependency to `test`

## [3.1] - 2022-10-24
### Changed
- `Lambda`: switched to use file:Suppress
- `Lambda`: added `isEmptyArray`
- `pom.xml`: added properties to avoid IntelliJ problem
- `pom.xml`: made `kotlin-test-junit` optional

## [3.0] - 2022-07-04
### Changed
- `JSONExpect`: moved named lambda tests to separate file (and renamed to `isXxxx`)
- `JSONExpect`: made error messages for range tests more consistent
- `JSONExpect`: added range and collection tests for `java.time` classes _etc._
### Added
- `Lambda`: named lambda tests

## [2.2] - 2022-06-17
### Changed
- `JSONExpect`: added `value`, `property` and `item` tests for `Enum` values

## [2.1] - 2022-06-13
### Changed
- `JSONExpect`: added `value`, `property` and `item` tests for `java.time` classes, `Duration` and `UUID`

## [2.0.3] - 2022-06-07
### Changed
- `pom.xml`: bumped dependency version

## [2.0.2] - 2022-05-29
### Changed
- `pom.xml`: bumped dependency version

## [2.0.1] - 2022-05-01
### Changed
- `pom.xml`: bumped dependency version

## [2.0] - 2022-04-18
### Changed
- `JSONExpect`: minor optimisations
- `JSONExpect`: added test for `kotlin.time.Duration`, renamed test for `java.time.Duration` to `javaDuration`
  (breaking change)
- `pom.xml`: bumped dependency version

## [1.4] - 2022-01-31
### Changed
- `pom.xml`: bumped dependency version

## [1.3] - 2022-01-22
### Changed
- `pom.xml`: updated to Kotlin 1.6.10, pinned JUnit version

## [1.2.1] - 2021-10-11
### Changed
- `README.md`, `SPRING.md`: added documentation (use of this library with Spring)
- `pom.xml`: bumped dependency version

## [1.2] - 2021-08-29
### Changed
- `pom.xml`: bumped dependency version

## [1.1] - 2021-08-24
### Changed
- `pom.xml`: bumped dependency version

## [1.0] - 2021-08-14
### Added
- `JSONExpect`: initial version (copied, with minor changes, from `json-kotlin-test`)
