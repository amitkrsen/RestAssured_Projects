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

public class e2e_2 extends reusable_sources {
	
	String currDir = System.getProperty("user.dir");
	
	@Test
	public void ene2end_Test_2() throws IOException {
		
		//create session cookie for login authentication...
		
		SessionFilter session = new SessionFilter();
		String session_response = given().header("Content-Type", "application/json")
		.body(new String(Files.readAllBytes(Paths.get(currDir+"\\JsonFiles\\jira_login_cred.json"))))
		.filter(session).when().post("/rest/auth/1/session")
		.then().assertThat().statusCode(200).extract().response().asString();
		System.out.println(session_response);
		

		//create issue...
		
		String createIssueResp = given().header("Content-Type", "application/json")
		.body(payloads.addIssue()).filter(session)
		.when().post("/rest/api/2/issue")
		.then().assertThat().statusCode(201).extract().response().asString();
		System.out.println(createIssueResp);
		
		JsonPath js = new JsonPath(createIssueResp);
		int issueId = js.getInt("id");
		System.out.println(issueId);
		
		//add comments...
		
		String commnetResp = given().header("Content-Type", "application/json").pathParam("id", issueId)
		.body(payloads.addComment()).filter(session)
		.when().post("/rest/api/2/issue/{id}/comment")
		.then().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js1 = new JsonPath(commnetResp);
		int expectedcommentID = js1.getInt("id");
		String expectedComment = js1.getString("body");
		System.out.println(expectedcommentID+" : "+expectedComment);
		
		//attach file...
		
		try 
		{
		given().pathParam("id", issueId).header("X-Atlassian-Token", "no-check")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("test.txt")).filter(session)
		.when().post("rest/api/2/issue/{id}/attachments")
		.then().assertThat().statusCode(200);
		System.out.println("File Added...");
		}
		catch(Exception e)
		{
			System.out.println("File Not added due to: "+e);
			
		}
		
		//get issue with specific fields (comments) and validate the same...
		
		String getIssueResp = given().filter(session).pathParam("id", issueId).queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{id}")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = new JsonPath(getIssueResp);
		int commentCount = js2.getInt("fields.comment.comments.size()");
		
		for(int i = 0; i < commentCount; i++)
		{
			String actualCommentId = js2.get("fields.comment.comments["+i+"].id").toString();
			if(actualCommentId.equalsIgnoreCase(expectedComment))
			{
				String actualComment = js2.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(actualComment);
				Assert.assertEquals(actualComment, expectedComment);
			}
		}
		
	}
	

}
