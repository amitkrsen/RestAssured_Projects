package TestCodes;

import org.testng.Assert;
import org.testng.annotations.Test;

import Utilities.payloads;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class e2e_1 extends reusable_sources {
	
	String currDir = System.getProperty("user.dir");
	
	
	@Test
	public void end2end_test() throws IOException {
		
		//create session cookie with login authentication...
		
		SessionFilter session = new SessionFilter();
		String cookie_response = given().header("Content-Type", "application/json")
		.body(new String(Files.readAllBytes(Paths.get(currDir+"\\JsonFiles\\jira_login_cred.json"))))
		.filter(session).when().post("/rest/auth/1/session")
		.then().assertThat().statusCode(200).extract().response().asString();
		System.out.println(cookie_response);
		
		
		//add jira issue through API...	
		
		String add_response = given().header("Content-Type", "application/json")
		.body(payloads.addIssue())
		.filter(session).when().post("/rest/api/2/issue")
		.then().assertThat().statusCode(201).extract().response().asString();
		System.out.println(add_response);
		
		JsonPath js2 = new JsonPath(add_response);
		String issueID = js2.get("id").toString();
		System.out.println("Issued Id is: "+issueID);
		
		//add comment on issue...
		
		String commentResp = given().pathParam("id", issueID).header("Content-Type","application/json")
		.body(payloads.addComment())
		.filter(session).when().post("/rest/api/2/issue/{id}/comment")
		.then().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(commentResp);
		String commentID = js.get("id").toString();
		String expectedComBody = js.getString("body");
		System.out.println(commentID+":"+expectedComBody);
		
		//attach file...
		
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", issueID)
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("test.txt"))
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().assertThat().statusCode(200);
		
		
		//get issue with specific fields (comments) and validate the same...
		
		String issueDetails = given().filter(session).pathParam("id", issueID).queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{id}")
		.then().assertThat().statusCode(200).extract().response().asString();
//		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentCount = js1.getInt("fields.comment.comments.size()");
		
		for(int i = 0; i < commentCount; i++)
		{
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentID))
			{
				String actualMsg = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println("Actual Comment: "+actualMsg+" || "+"Expected Comment: "+expectedComBody);
				Assert.assertEquals(actualMsg, expectedComBody);
			}
		}
		
	}

}
