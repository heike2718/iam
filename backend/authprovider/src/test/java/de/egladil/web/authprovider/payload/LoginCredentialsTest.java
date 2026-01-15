// =====================================================
// Project: authprovider
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.authprovider.payload;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * LoginCredentialsTest
 */
@QuarkusTest
public class LoginCredentialsTest {

    @Test
    void serialize() throws Exception {

        // Arrange
        LoginCredentials loginCredentials = new LoginCredentials();
        AuthorizationCredentials authCredentials = new AuthorizationCredentials();
        authCredentials.setLoginName("zeze@egladil.de");
        authCredentials.setPasswort("g3h1m!!");
        loginCredentials.setAuthorizationCredentials(authCredentials);
        loginCredentials
                .setClientCredentials(
                        ClientCredentials.createWithState("aJDGUQQhuHQUIWHDIQ", "localhost:4200", "guenni"));

        // Act + Assert
        System.out.println(new ObjectMapper().writeValueAsString(loginCredentials));
    }

}
