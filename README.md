# KoAsync

KoAsync is a simple and easy to use library for asynchronous programming in Kotlin.

## Usage

```kotlin
fun main() = runBlocking {
    val results = parallelAsync(
        scope = this,
        tasks = arrayOf(
            async { 1 },
            async { 2 },
            async { 3 }
        ),
        timeoutMillis = 2000,
        onProgress = { completed, total -> println("Progress: $completed / $total") },
        onError = { exception, index -> println("Error at index $index: $exception") }
    )

    results.forEachIndexed { index, result -> println("Result at index $index: $result") }

}
```
