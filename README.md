# KoAsync

KoAsync is a simple and easy to use library for asynchronous programming in Kotlin.

## Usage

```kotlin
fun main() = runBlocking {
    val results = parallelAsync(
        scope = this,
        tasks = arrayOf(
            { delay(300); "Task1" },
            { delay(500); "Task2" },
            { delay(700); "Task3" },
        ),
        timeoutMillis = 2000,
        onProgress = { completed, total ->
            println("Completed $completed of $total tasks")
        },
        onError = { e, index ->
            println("Error at index $index: $e")
        }
    )
    results.forEachIndexed { index, result ->
        println("Result at index $index: $result")
    }
}
```
