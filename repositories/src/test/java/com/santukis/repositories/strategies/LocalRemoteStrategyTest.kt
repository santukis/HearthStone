package com.santukis.repositories.strategies

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class LocalRemoteStrategyTest {

    @Test
    fun executeShouldReturnLocalResult() {
        runBlocking {
            val expectedValue = "Expected Value"
            val strategy = object : LocalRemoteStrategy<String, String>() {
                override suspend fun shouldLoadFromLocal(input: String): Boolean = true

                override suspend fun loadFromLocal(input: String): Result<String> {
                    return Result.success(expectedValue)
                }

                override suspend fun loadFromRemote(input: String, localResult: String?): Result<String> {
                    fail("loadFromRemote should not be call")
                }

                override suspend fun shouldUpdateFromRemote(input: String, localOutput: String): Boolean {
                    return false
                }

                override suspend fun saveIntoLocal(output: String) {
                    fail("saveIntoLocal should not be call")
                }
            }

            assertEquals(expectedValue, strategy.execute("").getOrNull())
        }
    }

    @Test
    fun executeShouldReturnRemoteResultAfterSavingIntoLocal() {
        runBlocking {
            val expectedValue = "Expected Value"
            val strategy = object : LocalRemoteStrategy<String, String>() {
                override suspend fun shouldLoadFromLocal(input: String): Boolean = false

                override suspend fun loadFromLocal(input: String): Result<String> {
                    return Result.success(expectedValue)
                }

                override suspend fun loadFromRemote(input: String, localResult: String?): Result<String> {
                    return Result.success(expectedValue)
                }

                override suspend fun shouldUpdateFromRemote(input: String, localOutput: String): Boolean {
                    return false
                }

                override suspend fun saveIntoLocal(output: String) {

                }
            }

            assertEquals(expectedValue, strategy.execute("").getOrNull())
        }
    }
    @Test
    fun executeShouldReturnRemoteResultWhenShouldUpdateFromRemoteReturnsTrue() {
        runBlocking {
            val expectedValue = "Expected Value"
            val strategy = object : LocalRemoteStrategy<String, String>() {
                override suspend fun shouldLoadFromLocal(input: String): Boolean = false

                override suspend fun loadFromLocal(input: String): Result<String> {
                    return Result.success(expectedValue)
                }

                override suspend fun loadFromRemote(input: String, localResult: String?): Result<String> {
                    return Result.success(expectedValue)
                }

                override suspend fun shouldUpdateFromRemote(input: String, localOutput: String): Boolean {
                    return true
                }

                override suspend fun saveIntoLocal(output: String) {
                    return
                }
            }

            assertEquals(expectedValue, strategy.execute("").getOrNull())
        }
    }

    @Test
    fun executeShouldReturnRemoteResultWhenIsFailure() {
        runBlocking {
            val expectedValue = Exception("Item not found")
            val strategy = object : LocalRemoteStrategy<String, String>() {
                override suspend fun shouldLoadFromLocal(input: String): Boolean = true

                override suspend fun loadFromLocal(input: String): Result<String> {
                    return Result.failure(Exception("Any local exception"))
                }

                override suspend fun loadFromRemote(input: String, localResult: String?): Result<String> {
                    return Result.failure(expectedValue)
                }

                override suspend fun shouldUpdateFromRemote(input: String, localOutput: String): Boolean {
                    return false
                }

                override suspend fun saveIntoLocal(output: String) {
                    fail("saveIntoLocal should not be call")
                }
            }

            assertEquals(expectedValue, strategy.execute("").exceptionOrNull())
        }
    }

    @Test
    fun executeShouldReturnFailureWhenErrorSavingIntoLocal() {
        runBlocking {
            val expectedValue = Exception("Any local exception")
            val strategy = object : LocalRemoteStrategy<String, String>() {
                override suspend fun shouldLoadFromLocal(input: String): Boolean = true

                override suspend fun loadFromLocal(input: String): Result<String> {
                    return Result.failure(expectedValue)
                }

                override suspend fun loadFromRemote(input: String, localResult: String?): Result<String> {
                    return Result.success("Any String")
                }

                override suspend fun shouldUpdateFromRemote(input: String, localOutput: String): Boolean {
                    return false
                }

                override suspend fun saveIntoLocal(output: String) {

                }
            }

            assertEquals(expectedValue, strategy.execute("").exceptionOrNull())
        }
    }
}