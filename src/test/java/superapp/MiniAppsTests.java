package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import superapp.data.CommandId;
import superapp.data.ObjectId;
import superapp.data.UserId;
import superapp.restApi.boundaries.InvokedBy;
import superapp.restApi.boundaries.MiniAppCommandBoundary;
import superapp.restApi.boundaries.TargetObjectBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MiniAppsTests {

	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private String superapp = "2023b.shir.zur";

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.baseUrl = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.baseUrl + "/superapp/admin/miniapp");
	}

	// test post using get operation
	@Test
	public void testSuccessfulCreateUser() throws Exception {
		// GIVEN the server is up
		// AND the database is empty
		// WHEN I POST /superapp/miniapp/test using {
		// "command" : "test",
		// "targetObject":{
		// "objectId":{
		// "superapp":"2023b.shir.zur",
		// "internalObjectId": "123"
		// }
		// },
		// "invocationTimestamp":"2023-04-14T13:24:41.664+03:00",
		// "invokedBy":{
		// "userId":{
		// "superapp":"2023b.shir.zur",
		// "email":"daniel@mail.com"
		// }
		// },
		// "commandAttributes":{
		// "key1":{
		// "daniel":"hello"
		// }
		// }
		// }
		Map<String, Object> commandAttributes = new HashMap<>();
		commandAttributes.put("daniel", "hello");
		MiniAppCommandBoundary miniAppCommandBoundary = new MiniAppCommandBoundary("test",
				new TargetObjectBoundary(new ObjectId("123", this.superapp)),
				new InvokedBy(new UserId("daniel@mail.com", this.superapp)), commandAttributes);

		CommandId commandId = this.restTemplate.postForObject(this.baseUrl + "/superapp/miniapp/test",
				miniAppCommandBoundary, MiniAppCommandBoundary.class).getCommandId();
		miniAppCommandBoundary.setCommandId(commandId);

		// THEN the miniappCommand is stored to the database
		MiniAppCommandBoundary miniAppCommandFromDb = this.restTemplate
				.getForObject(this.baseUrl + "/superapp/admin/miniapp/test", MiniAppCommandBoundary[].class)[0];
		assertThat(miniAppCommandFromDb).isNotNull()
				.extracting("commandId.superapp", "commandId.miniapp", "commandId.internalCommandID")

				.containsExactly(commandId.getSuperapp(), commandId.getMiniApp(), commandId.getInternalCommandId());
	}
}
