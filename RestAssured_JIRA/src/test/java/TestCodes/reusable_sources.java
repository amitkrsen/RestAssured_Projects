package TestCodes;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

public class reusable_sources {
	
	@BeforeClass
	public void call_baseURI() {
		
		RestAssured.baseURI = "http://localhost:8080";
	}
	
	

}
