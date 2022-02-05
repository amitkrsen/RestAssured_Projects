import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class fromStaticJsonFile {

	@Test
	public void addPlaceAPI() throws IOException
	{
		String currDir = System.getProperty("user.dir");
		//content of the file to String -> first ot will convert into byte -> from byte to String 
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String addPlaceResp = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(new String(Files.readAllBytes(Paths.get(currDir+"\\jsonFiles\\addPlaceAPI.json"))))
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("status", equalTo("OK"))
		.extract().response().asString();
		
		System.out.println(addPlaceResp);
		
	}
	
}
