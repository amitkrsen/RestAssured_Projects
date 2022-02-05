package Utilities;

public class payloads {
	
	public static String addIssue() {
		
		return "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": \r\n"
				+ "        {\r\n"
				+ "            \"key\": \"RES\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"Ene2End 2 Test Issue\",\r\n"
				+ "        \"description\": \"Ene2End 2 test Bug created from RestAssured static json file, adding a comment, attaching a file and validate the comment...\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        },\r\n"
				+ "                \"priority\": {\r\n"
				+ "            \"name\": \"Highest\"\r\n"
				+ "        },\r\n"
				+ "        \"labels\": [\r\n"
				+ "            \"bugfix\",\r\n"
				+ "            \"end2end_@\"\r\n"
				+ "        ],\r\n"
				+ "        \"environment\": \"QA Environment\"\r\n"
				+ "    }\r\n"
				+ "}";
		
	}
	
	public static String addComment() {
		
		String comments = "End 2 End Automation Test Comments for validaion...";
		return "{\r\n"
				+ "    \"body\": \""+comments+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}";
		
	}

}
