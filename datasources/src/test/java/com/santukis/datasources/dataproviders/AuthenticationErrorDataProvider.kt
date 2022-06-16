package com.santukis.datasources.dataproviders

import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class AuthenticationErrorDataProvider {

    companion object {
        @JvmStatic
        fun provideErrorWhenAuthenticating(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    """
                        {
                            "error": "unsupported_grant_type",
                            "error_description": "Unsupported grant type: client_credential"
                        }
                    """.trimIndent(),
                    400,
                    "Unsupported grant type: client_credential"
                ),
                Arguments.of(
                    """
                        {
                            "error": "unauthorized",
                            "error_description": "Bad credentials"
                        }
                    """.trimIndent(),
                    401,
                    "Bad credentials"
                ),
                Arguments.of(
                    """
                        {
                            "error": "invalid_request",
                            "error_description": "Missing grant type"
                        }
                    """.trimIndent(),
                    400,
                    "Missing grant type"
                )
            )
    }
}