package api.tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	@BeforeClass
	public void setupData() {
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
	logger = LogManager.getLogger(this.getClass());
	
	}
	
	
	@Test(priority = 1)
	public void testPostUser() {
		
		logger.info("Post User test case is started");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("Post User test case is passed");
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("Get User test case is passed");
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("Get User test case is passed");
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() {
		
		logger.info("Update User test case is started");
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),405);
		
		Response AfterUpdateresponse = UserEndPoints.readUser(this.userPayload.getUsername());
		AfterUpdateresponse.then().log().body().statusCode(200);
		
		Assert.assertEquals(AfterUpdateresponse.getStatusCode(),200);
		logger.info("Update User test case is passed");
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() {
		logger.info("Delete User test case is started");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("Delete User test case is passed");
	}
}
