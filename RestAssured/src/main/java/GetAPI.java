import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class GetAPI {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", "83711f1260f8fe4e5ad6e61818f41e20")
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200);

	}

}
