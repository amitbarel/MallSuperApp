package superapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.data.CreatedBy;
import superapp.data.Location;
import superapp.data.ObjectId;
import superapp.data.UserId;
import superapp.restApi.boundaries.ObjectBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuperAppObjectOperationTest {

	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private String superapp;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.baseUrl = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}

	@Value("${spring.application.name:2023b.shir.zur}")
	public void setSuperapp(String name) {
		this.superapp = name;
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.baseUrl + "/superapp/admin/objects");
	}

	@Test
	public void putExistingChildToObject() throws Exception {

		// GIVEN the server is up
		// AND the database contains 4 messages, containing {"id":o1} and {"id":o2}
		String o1 = null, o2 = null;

		ObjectBoundary o1Boundary = new ObjectBoundary();
		o1Boundary.setAlias("Nissan");
		o1Boundary.setType("abc");
		o1Boundary.setActive(false);
		UserId userId1 = new UserId("nissan@gmail.com");
		CreatedBy createdBy1 = new CreatedBy();
		createdBy1.setUserId(userId1);
		o1Boundary.setCreatedBy(createdBy1);

		o1Boundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", o1Boundary, ObjectBoundary.class).getObjectId());
		o1 = o1Boundary.getObjectId().getInternalObjectId();

		ObjectBoundary o2Boundary = new ObjectBoundary();
		o2Boundary.setAlias("Yamin");
		o2Boundary.setType("efg");
		o2Boundary.setActive(true);
		o2Boundary.setLocation(new Location().setLat(0.03).setLng(3.26));
		UserId userId2 = new UserId("nissan@gmail.com");
		CreatedBy createdBy2 = new CreatedBy();
		createdBy2.setUserId(userId2);
		o2Boundary.setCreatedBy(createdBy2);

		o2Boundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", o2Boundary, ObjectBoundary.class).getObjectId());
		o2 = o2Boundary.getObjectId().getInternalObjectId();

		for (int i = 0; i < 2; i++) {
			ObjectBoundary ob = new ObjectBoundary();
			ob.setAlias(i + "a");
			ob.setType(i + "b");
			UserId userId = new UserId(i + "@gmail.com");
			CreatedBy createdBy = new CreatedBy();
			createdBy.setUserId(userId);
			ob.setCreatedBy(createdBy);

			this.restTemplate.postForObject(this.baseUrl + "/superapp/objects", ob, ObjectBoundary.class);
		}

		// WHEN I PUT /superapp/objects/{superapp}/{InternalObjectId}/children with
		// {"responseId":"o2"}
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children",
				new ObjectId(o2, superapp), superapp, o1);

		// THEN the database is updated with relation between o1 and o2 as its response
		ObjectBoundary[] actualChildersns = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children", ObjectBoundary[].class,
				superapp, o1);
		assertThat(actualChildersns).usingRecursiveFieldByFieldElementComparatorOnFields("ObjectId.internalObjectId")
				.contains(o2Boundary);
	}

	@Test
	public void testObjectCanBeRelatedToManyObjects() throws Exception {
		// GIVEN the server is up
		// AND the database contains 4 Objects
		// AND Objects ob1..ob3 are responses to ob0

		ObjectBoundary objectBoundary0 = new ObjectBoundary();
		objectBoundary0.setAlias("roni");
		objectBoundary0.setType("oz");
		UserId userId0 = new UserId();
		CreatedBy createdBy0 = new CreatedBy();
		createdBy0.setUserId(userId0.setEmail("zyq@gmail.com"));
		objectBoundary0.setCreatedBy(createdBy0);

		ObjectBoundary objectBoundary1 = new ObjectBoundary();
		objectBoundary1.setAlias("papa");
		objectBoundary1.setType("mama");
		UserId userId1 = new UserId();
		CreatedBy createdBy1 = new CreatedBy();
		createdBy1.setUserId(userId1.setEmail("mama@gmail.com"));
		objectBoundary1.setCreatedBy(createdBy1);

		ObjectBoundary objectBoundary2 = new ObjectBoundary();
		objectBoundary2.setAlias("eden");
		objectBoundary2.setType("nissan");
		UserId userId2 = new UserId();
		CreatedBy createdBy2 = new CreatedBy();
		createdBy2.setUserId(userId2.setEmail("abc@gmail.com"));
		objectBoundary2.setCreatedBy(createdBy2);

		ObjectBoundary objectBoundary3 = new ObjectBoundary();
		objectBoundary3.setAlias("a");
		objectBoundary3.setType("k");
		UserId userId3 = new UserId();
		CreatedBy createdBy3 = new CreatedBy();
		createdBy3.setUserId(userId3.setEmail("koko@gmail.com"));
		objectBoundary3.setCreatedBy(createdBy3);

		// WHEN I GET THIS POST // /superapp/objects
		objectBoundary0.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary0, ObjectBoundary.class)
				.getObjectId());
		String ob0 = objectBoundary0.getObjectId().getInternalObjectId();

		objectBoundary1.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary1, ObjectBoundary.class)
				.getObjectId());
		String ob1 = objectBoundary1.getObjectId().getInternalObjectId();
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children",
				new ObjectId(ob1, superapp), superapp, ob0);

		objectBoundary2.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary2, ObjectBoundary.class)
				.getObjectId());
		String ob2 = objectBoundary2.getObjectId().getInternalObjectId();
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children",
				new ObjectId(ob2, superapp), superapp, ob0);

		objectBoundary3.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", objectBoundary3, ObjectBoundary.class)
				.getObjectId());
		String ob3 = objectBoundary3.getObjectId().getInternalObjectId();
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children",
				new ObjectId(ob3, superapp), superapp, ob0);

		// WHEN I GET /superapp/objects/{superapp}/{InternalObjectId}/children
		ObjectBoundary[] actualchildrens = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children", ObjectBoundary[].class,
				superapp, ob0);
		// THEN the server returns objects of ob1, ob2, ob3
		assertThat(actualchildrens).usingRecursiveFieldByFieldElementComparatorOnFields("ObjectId.internalObjectId")
				.containsExactlyInAnyOrder(objectBoundary1, objectBoundary2, objectBoundary3);
	}

	@Test
	public void testSuccessfulGetParent() {
		// GIVEN the server is up
		// AND the database contains 4 objects, containing
		String o1 = null, o2 = null;

		ObjectBoundary o1Boundary = new ObjectBoundary();
		o1Boundary.setAlias("Nissan");
		o1Boundary.setType("abc");
		o1Boundary.setActive(false);
		o1Boundary.setLocation(new Location().setLat(10.123).setLng(5.26));
		UserId userId1 = new UserId("nissan@gmail.com");
		CreatedBy createdBy1 = new CreatedBy();
		createdBy1.setUserId(userId1);
		o1Boundary.setCreatedBy(createdBy1);

		o1Boundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", o1Boundary, ObjectBoundary.class).getObjectId());
		o1 = o1Boundary.getObjectId().getInternalObjectId();

		ObjectBoundary o2Boundary = new ObjectBoundary();
		o2Boundary.setAlias("Yamin");
		o2Boundary.setType("efg");
		o2Boundary.setActive(true);
		o2Boundary.setLocation(new Location().setLat(0.03).setLng(3.26));
		UserId userId2 = new UserId("nissan@gmail.com");
		CreatedBy createdBy2 = new CreatedBy();
		createdBy2.setUserId(userId2);
		o2Boundary.setCreatedBy(createdBy2);

		o2Boundary.setObjectId(this.restTemplate
				.postForObject(this.baseUrl + "/superapp/objects", o2Boundary, ObjectBoundary.class).getObjectId());
		o2 = o2Boundary.getObjectId().getInternalObjectId();

		for (int i = 0; i < 2; i++) {
			ObjectBoundary ob = new ObjectBoundary();
			ob.setAlias(i + "a");
			ob.setType(i + "b");
			UserId userId = new UserId(i + "@gmail.com");
			CreatedBy createdBy = new CreatedBy();
			createdBy.setUserId(userId);
			ob.setCreatedBy(createdBy);

			this.restTemplate.postForObject(this.baseUrl + "/superapp/objects", ob, ObjectBoundary.class);
		}

		// WHEN I PUT /superapp/objects/{superapp}/{InternalObjectId}/children with
		// {"responseId":"o2"}
		this.restTemplate.put(this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/children",
				new ObjectId(o2, superapp), superapp, o1);

		// THEN the database is updated with relation between o1 and o2 as its response
		ObjectBoundary actualOrigin = this.restTemplate.getForObject(
				this.baseUrl + "/superapp/objects/{superapp}/{InternalObjectId}/parents", ObjectBoundary[].class,
				superapp, o2)[0];
		assertThat(actualOrigin.getObjectId().getInternalObjectId()).isEqualTo(o1);
	}

}
