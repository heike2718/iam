//=====================================================
// Project: authprovider
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.web.authprovider.payload;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * EmailPayloadTest
 */
public class OrderTempPasswordPayloadTest {

	@Test
	void serialize() throws JsonProcessingException {
		// Arrange
		OrderTempPasswordPayload payload = new OrderTempPasswordPayload();
		payload.setEmail("zeze@egladil.de");
		payload
			.setClientCredentials(
				ClientCredentials.createWithState("WLJLH4vsldWapZrMZi2U5HKRBVpgyUiRTWwX7aiJd8nX", "localhost:4200", "horst"));

		// Act + Assert
		System.out.println(new ObjectMapper().writeValueAsString(payload));
	}

}
