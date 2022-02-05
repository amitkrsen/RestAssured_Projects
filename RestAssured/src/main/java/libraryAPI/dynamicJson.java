package libraryAPI;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payloads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class dynamicJson {

	@Test(dataProvider = "BookData", enabled = true)
	public void addBookAPI(String isbn, String aisle) {

		RestAssured.baseURI = "http://216.10.245.166";
		String addBookResponse = given().header("Content-Type", "application/json")
				.body(payloads.addBook(isbn, aisle))
				.when().post("/Library/Addbook.php")
				.then().assertThat().statusCode(200)
				.extract().response().asString();

		JsonPath js = new JsonPath(addBookResponse);
		String id = js.getString("ID");
		System.out.println(id);
	}
	
	@Test(dataProvider = "BookData", enabled = true)
	public void deleteBookAPI(String isbn, String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		String deleteBookResp = given().header("Content-Type", "application/json")
		.body(payloads.deleteBook(isbn, aisle))
		.when().post("Library/DeleteBook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = new JsonPath(deleteBookResp);
		String resp = js.getString("msg");
		System.out.println(resp);
	}
	
	@DataProvider(name="BookData")
	public Object[][] getData(){
		
		//array=collection of elements
		//multidimensional array= collection of arrays
		
		return new Object[][] {{"hgjk","3754"},{"hklt","6455"},{"xoid","8232"}};
	}

}

