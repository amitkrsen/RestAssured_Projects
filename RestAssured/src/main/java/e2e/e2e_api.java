package e2e;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.AssertJUnit;

import files.payloads;

public class e2e_api {

	public static void main(String[] args) {
		
		//add api
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String addResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(payloads.add_place())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("status", equalTo("OK"))
		.extract().response().asString();
		
		System.out.println(addResponse);
		
		JsonPath js = new JsonPath(addResponse);
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		//update api
		
		String newAddress = "70 Summer walk, USA";
		
		String updateResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"))
		.extract().response().asString();
		
		System.out.println(updateResponse);
		
		//get api
		
		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = new JsonPath(getResponse);
		String actualAddress = js1.getString("address");
		System.out.println(getResponse);
		System.out.println(actualAddress);
		AssertJUnit.assertEquals(newAddress, actualAddress);
		

	}

}
