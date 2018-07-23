package test.api.automation;

import org.testng.Assert;
import org.testng.annotations.Test;

import  static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

import test.automation.common.CreatePost;


public class TC001 {
	
	@Test(enabled=false)
	public void TC001_GetMethod() {
		
		Response response = get("http://services.groupkt.com/get/iso2codeHELLO/IN");
		//System.out.println(response.asString()); //http://www.omdbapi.com/?t=Spiderman&y=&plot=short&r=json
		System.out.println(response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(),200);
	}
	@Test(enabled=false)
	public void TC001_GetMethod_Validation() {
		Response response =get("http://services.groupkt.com/country/get/all");
		Assert.assertEquals(response.getContentType(), "application/json;charset=UTF-8");
	}
	
	@Test(enabled = false  , description =" uri with parmeter and BDD")
	public void TCwithBDD() {
		given().param("t", "Spiderman")
				.param("y", "")
				.param("plot", "short")
				.param("r", "json")
		.when().get("http://www.omdbapi.com/")	
		.then().statusCode(401);
	}
	
	@Test(enabled=false)
	public void Tc_Post() {
		
		  given().contentType(ContentType.JSON)
				.body("{\"id\": 4, \"title\": \"iron man\", \"author\": \"ashu\"  }")
		 .when()
				.post(" http://localhost:3000/posts")
		 .then().statusCode(201)
		 		.contentType(ContentType.JSON);
		// System.out.println(response.asString());
				
		
	}
	
	@Test(enabled= false)
	public void TcPostObject() {
		
		CreatePost cPost = new CreatePost();
		cPost.setId(7);
		cPost.setTitle("jonwick");
		cPost.setAuthor("xyzw");
		
		given().contentType(ContentType.JSON)
				.body(cPost)
		.when().post("http://localhost:3000/posts")
		.then().contentType(ContentType.JSON)
		       .statusCode(201);
	}
	@Test
	public void endToEnd() {
		int index = 29;
		CreatePost cpost = new CreatePost();
		cpost.setId(index);
		cpost.setAuthor("New author");
		cpost.setTitle("New title");
		
   ValidatableResponse response =	 given()
		.contentType(ContentType.JSON)
		.body(cpost)
		.when()
		.post("http://localhost:3000/posts")
		.then()
		.contentType(ContentType.JSON);

   int responseId = response.extract().response().path("id");
   int actualstatusCode = response.extract().response().getStatusCode();
   Assert.assertEquals(actualstatusCode, 201);
   
   
   //step 2
   ValidatableResponse response1 = when()
		   						  .get("http://localhost:3000/posts/"+responseId)
		   						  .then()
		   						  .contentType(ContentType.JSON);
   
   String actualAuthor = response1.extract().response().path("author");
   String actualTitle = response1.extract().response().path("title");
   
   Assert.assertEquals(actualAuthor, "New author");
   Assert.assertEquals(actualTitle, "New title");
   
   //step 3
   
   CreatePost cpost1 = new CreatePost();
   cpost1.setId(index);
   cpost1.setAuthor("updated author");
   cpost1.setTitle("updated title");
   
   given()
   .contentType(ContentType.JSON)
   .body(cpost1)
   .when()
   .put("http://localhost:3000/posts/"+index)
   .then()
   .contentType(ContentType.JSON);
   
   ValidatableResponse response2 = when()
		   						  .get("http://localhost:3000/posts/"+responseId)
			               .then()
			  .contentType(ContentType.JSON);

   String actualAuthor2 = response2.extract().response().path("author");
   String actualTitle2 = response2.extract().response().path("title");

   Assert.assertEquals(actualAuthor2, "updated author");
   Assert.assertEquals(actualTitle2, "updated title");
   
   when()
   .delete("http://localhost:3000/posts/"+responseId);

   ValidatableResponse response3 = when()
		   						  .get("http://localhost:3000/posts/"+responseId)
		   			            .then()
		   					  .contentType(ContentType.JSON);
Assert.assertNotEquals(response3.extract().response().statusCode(), 201);
   
   


   
		
	}

}
