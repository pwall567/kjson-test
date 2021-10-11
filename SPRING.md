# Spring and `kjson-test`

Many users will seek to use `kjson-test` in conjunction with the
[Spring Framework](https://spring.io/projects/spring-framework).
The following suggestion may assist with the use of `kjson-test` with Spring's `MockMvc`.

## Testing with `MockMvc`

Spring includes functions to simplify access to the `MockMvc` class, taking advantage of Kotlin's DSL capabilities.
These can be extended to make use of `kjson-test`.

A class similar to the following will allow `kjson-test` tests to be integrated easily into `MockMvc` testing:

```kotlin
package my.example

import io.kjson.test.JSONExpect
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.springframework.test.web.servlet.result.ContentResultMatchersDsl

class JSONMatcher(private val tests: JSONExpect.() -> Unit) : BaseMatcher<String>() {

    override fun matches(actual: Any?): Boolean {
        if (actual !is String)
            return false
        JSONExpect.expectJSON(actual, tests)
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("valid JSON")
    }

    companion object {
        fun ContentResultMatchersDsl.matchesJSON(tests: JSONExpect.() -> Unit) = string(JSONMatcher(tests))
    }

}
```

Now, a test can be coded as follows:
```kotlin
        mockMvc.get("/path/to/be/tested") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content {
                matchesJSON {
                    property("accountID", "ABC123")
                    property("accountName", "Test account")
                    // etc.
                }
            }
        }
```

This class is provided as documentation rather than as a source file because it would not be practical for this project
to include the dependencies required to resolve the Spring references.

2021-10-11
