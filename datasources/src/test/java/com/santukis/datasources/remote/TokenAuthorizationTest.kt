package com.santukis.datasources.remote

import com.santukis.datasources.authentication.AuthenticationDataSource
import com.santukis.entities.authentication.Token
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class TokenAuthorizationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteAuthenticationDataSource: AuthenticationDataSource
    private lateinit var localAuthenticationDataSource: AuthenticationDataSource

    private lateinit var client: OkHttpClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        remoteAuthenticationDataSource = mockk()
        localAuthenticationDataSource = mockk()

        val tokenAuthorization = TokenAuthorization(
            remoteAuthenticationDataSource,
            localAuthenticationDataSource
        )

        client = OkHttpClient.Builder()
            .addInterceptor(tokenAuthorization)
            .authenticator(tokenAuthorization)
            .build()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `tokenAuthorization should add bearer token to request when stored in localAuthenticationDataStore`() {
        every { localAuthenticationDataSource.getToken() } returns Result.success(Token(accessToken = "expected Token"))
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        client.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val request = mockWebServer.takeRequest()

        assertEquals("Bearer expected Token", request.getHeader(HttpClient.AUTHORIZATION))
    }

    @Test
    fun `tokenAuthorization should add bearer token after refreshing it from remoteAuthenticationDataSource`() {
        every { localAuthenticationDataSource.getToken() } returnsMany listOf(
            Result.success(Token(accessToken = "time out Token")),
            Result.success(Token(accessToken = "expected Token"))
        )

        every { remoteAuthenticationDataSource.getToken() } returns Result.success(Token(accessToken = "updated Token"))

        every { localAuthenticationDataSource.saveToken(any()) } returns Unit

        mockWebServer.enqueue(MockResponse().setResponseCode(401))
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        client.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        val failRequest = mockWebServer.takeRequest()

        assertEquals("Bearer time out Token", failRequest.getHeader(HttpClient.AUTHORIZATION))

        val successRequest = mockWebServer.takeRequest()

        assertEquals("Bearer expected Token", successRequest.getHeader(HttpClient.AUTHORIZATION))
    }
}