package com.santukis.datasources.authentication

import com.santukis.datasources.dataproviders.loadData
import com.santukis.datasources.remote.Environment
import com.santukis.datasources.remote.HttpClient
import com.santukis.entities.authentication.Token
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class BattlenetAuthenticationDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authenticationDataSource: AuthenticationDataSource

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()

        val client = HttpClient(
            environment = Environment.Testing(mockWebServer.url("").toString()),
            client = OkHttpClient()
        )

        authenticationDataSource = BattlenetAuthenticationDataSource(client)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getToken should build the Request as expected`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        authenticationDataSource.getToken()

        val request = mockWebServer.takeRequest()
        assertEquals("/oauth/token", request.path)
    }

    @Test
    fun `getToken should return Token when server response is success`() {
        val expectedToken = Token(
            accessToken = "USQSolEliW4G7WHs4IoB7DZglnf0fsqXdy",
            expires = 86399
        )
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(loadData("/token_success_response.json")))

        authenticationDataSource.getToken()
            .onSuccess {
                assertEquals(expectedToken, it)
            }
            .onFailure {
                fail("Success should be called")
            }
    }

    @ParameterizedTest
    @MethodSource("com.santukis.datasources.dataproviders.AuthenticationErrorDataProvider#provideErrorWhenAuthenticating")
    fun `getToken should return Error when server response is error`(
        serverResponse: String,
        serverResponseCode: Int,
        expectedErrorMessage: String
    ) {
        mockWebServer.enqueue(MockResponse().setResponseCode(serverResponseCode).setBody(serverResponse))

        authenticationDataSource.getToken()
            .onSuccess {
                fail("Error should be called")
            }
            .onFailure {
                assertEquals(expectedErrorMessage, it.message)
            }
    }
}