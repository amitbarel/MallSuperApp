package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import superapp.data.CreatedBy;
import superapp.data.Location;
import superapp.data.UserId;
import superapp.restApi.boundaries.ObjectBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuperAppObjectTest {

	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.baseUrl = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.baseUrl + "/superapp/admin/objects");
	}

	@Test
	public void testSuccessfulPosingOfObject() {
//		// GIVEN the server is up
//		// AND the database is empty
//				
//		// WHEN I POST /superapp/objects using {
//										//	    
//										//	    "type": "abc",
//										//	    "alias": "nissan",
//										//	    "active": false,
//										//	    "location": {
//										//	        "lat": 4520,
//										//	        "lng": 0.0
//										//	    },
//										//	    "createdBy": {
//										//	        "userId": {
//										//	            "superapp": "2023b.shir.zur",
//										//	            "email": "nissanyam1@gmail.com"
//										//	        }
//										//	    },
//										//	    "objectDetails": {
//										//	    }
//										//		}
		ObjectBoundary newObjectBoundary = new ObjectBoundary();
		newObjectBoundary.setAlias("nissan");
		newObjectBoundary.setType("abc");
		newObjectBoundary.setActive(false);
		newObjectBoundary.setLocation(new Location().setLat(10.123).setLng(5.26));
		UserId userId = new UserId("nissan@gmail.com");
		CreatedBy createdBy = new CreatedBy();
		createdBy.setUserId(userId);
		newObjectBoundary.setCreatedBy(createdBy);

		newObjectBoundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", newObjectBoundary, ObjectBoundary.class)
				.getObjectId());

		// THEN the Object is stored to the database
		ObjectBoundary ObjectFromDatabase = this.restTemplate.getForObject(this.baseUrl + "/superapp/objects",
				ObjectBoundary[].class)[0]; // get message using GET HTTP METHOD

		assertThat(ObjectFromDatabase).isNotNull().extracting("ObjectId.internalObjectId", "type", "alias", "active",
				"location.lat", "location.lng", "createdBy.userId.email")
//				.isEqualTo(newMessage.getMessage());
				.containsExactly(newObjectBoundary.getObjectId().getInternalObjectId(), newObjectBoundary.getType(),
						newObjectBoundary.getAlias(), newObjectBoundary.getActive(),
						newObjectBoundary.getLocation().getLat(), newObjectBoundary.getLocation().getLng(),
						newObjectBoundary.getCreatedBy().getUserId().getEmail());
	}

	@Test
	public void testSuccessfulPutOfObject() {
//		// GIVEN the server is up
//		// AND the database is empty
		ObjectBoundary objectBoundary = new ObjectBoundary();
		objectBoundary.setAlias("rabea");
		objectBoundary.setType("aaa");
		UserId userId = new UserId("rabea@gmail.com");
		CreatedBy createdBy = new CreatedBy();
		createdBy.setUserId(userId);
		objectBoundary.setCreatedBy(createdBy);

		// post to get the id
		objectBoundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary, ObjectBoundary.class).getObjectId());

		objectBoundary.setAlias("braik");
		// WHEN I PUT /superapp/objects with a new objectBoundary
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superApp}/{id}", objectBoundary, "2023b.shir.zur",
				objectBoundary.getObjectId().getInternalObjectId());

		// THEN the database is updated
		ObjectBoundary actualOrigin = this.restTemplate.getForObject(this.baseUrl + "/superapp/objects",
				ObjectBoundary[].class)[0];
		assertThat(actualOrigin).isNotNull().extracting("ObjectId.internalObjectId", "alias")
				.containsExactly(objectBoundary.getObjectId().getInternalObjectId(), objectBoundary.getAlias());

	}

	@Test
	public void testSuccessfulGetSpecipicObject() {
		// GIVEN the server is up
		// AND the database have one object
		ObjectBoundary objectBoundary = new ObjectBoundary();
		objectBoundary.setAlias("eden");
		objectBoundary.setType("nissan");
		UserId userId = new UserId();
		CreatedBy createdBy = new CreatedBy();
		createdBy.setUserId(userId.setEmail("abc@gmail.com"));
		objectBoundary.setCreatedBy(createdBy);

		// WHEN I GET THIS POST // /superapp/objects
		objectBoundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary, ObjectBoundary.class).getObjectId());

		// THEN I GET THIS OBJECT // /superapp/objects/{superApp}/{id}
		ObjectBoundary objectBoundary1 = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/objects/{superApp}/{id}", ObjectBoundary.class, "2023b.shir.zur",
				objectBoundary.getObjectId().getInternalObjectId());

		assertThat(objectBoundary1).isNotNull().extracting("ObjectId.internalObjectId", "type", "alias")
				.containsExactly(objectBoundary.getObjectId().getInternalObjectId(), objectBoundary.getType(),
						objectBoundary.getAlias());

	}

	@Test
	public void testSuccessfulGetAllObject() {
		// GIVEN the server is up
		// AND the database have one object
		ObjectBoundary objectBoundary = new ObjectBoundary();
		objectBoundary.setAlias("eden");
		objectBoundary.setType("nissan");
		UserId userId = new UserId();
		CreatedBy createdBy = new CreatedBy();
		createdBy.setUserId(userId.setEmail("abc@gmail.com"));
		objectBoundary.setCreatedBy(createdBy);

		ObjectBoundary objectBoundary0 = new ObjectBoundary();
		objectBoundary0.setAlias("roni");
		objectBoundary0.setType("oz");
		UserId userId0 = new UserId();
		CreatedBy createdBy0 = new CreatedBy();
		createdBy0.setUserId(userId0.setEmail("zyq@gmail.com"));
		objectBoundary0.setCreatedBy(createdBy0);

		// WHEN I GET THIS POST // /superapp/objects
		objectBoundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary, ObjectBoundary.class).getObjectId());
		objectBoundary0.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary0, ObjectBoundary.class)
				.getObjectId());

		// THEN I GET THIS ALL OBJECTS // /superapp/objects
		ObjectBoundary[] objectBoundaryFromDB = this.restTemplate.getForObject(this.baseUrl + "/superapp/objects",
				ObjectBoundary[].class);
		assertThat(objectBoundaryFromDB).isNotNull();
		assertThat(objectBoundaryFromDB[0]).isNotNull().extracting("ObjectId.internalObjectId", "type", "alias")
				.containsExactly(objectBoundary.getObjectId().getInternalObjectId(), objectBoundary.getType(),
						objectBoundary.getAlias());
		assertThat(objectBoundaryFromDB[1]).isNotNull().extracting("ObjectId.internalObjectId", "type", "alias")
				.containsExactly(objectBoundary0.getObjectId().getInternalObjectId(), objectBoundary0.getType(),
						objectBoundary0.getAlias());

	}
}
