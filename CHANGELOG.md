# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

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
