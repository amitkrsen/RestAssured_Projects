import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import org.testng.Assert;

import files.payloads;

public class AddApi {

	public static void main(String[] args) {

		//given - for all input details
		//when - Submit the API (POST, GET...) resource, http method
		//then - validate the response
		//E2E testing - Add Place > Update place with new address > 
		//> Get place to validate the new address is present in response
		
		//add place

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String addResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(payloads.add_place())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("status", equalTo("OK"))
		.extract().response().asString();
		
		System.out.println(addResponse);
		
		JsonPath js = new JsonPath(addResponse); //for parsing json to extract specific json body from response
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		//put place
		
		String newAddress = "70 Summer walk, California, USA";
		
		String putResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"))
		.extract().response().asString();
		
		System.out.println(putResponse);
		
		//get place
		
		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = new JsonPath(getResponse);
		String actualAddress = js1.getString("address");
		System.out.println(getResponse);
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
		
		

	}

}
