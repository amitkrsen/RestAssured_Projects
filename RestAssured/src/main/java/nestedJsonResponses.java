import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payloads;
import io.restassured.path.json.JsonPath;

public class nestedJsonResponses {

	JsonPath js = new JsonPath(payloads.courses());
	int courseCount = js.getInt("courses.size()");
	
	@DataProvider(name="courseTitle")
	public Object[][] getData(){
		return new Object[][] {{"RPA"},{"Appium"},{"Cypress"},{"Selenium Python"}};
	}
	
	@Test(priority = 1, enabled = true)
	public void numOfCourses() {

		System.out.println("Print No of courses returned by API:");
		int courseCount = js.getInt("courses.size()");
		System.out.println(courseCount);
	}
	
	@Test(priority = 2, enabled = true)
	public void purchaseAmt() {
		
		System.out.println("Print Purchase Amount");
		int purchaseAmt = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmt);
	}
	
	@Test(priority = 3, enabled = true)
	public void courseTitle() {
		
		System.out.println("Print Title of the first course");
		String courseTitle = js.getString("courses[0].title");
		System.out.println(courseTitle);
	}
	
	@Test(priority = 4, enabled = true)
	public void titlePrice() {
		
		System.out.println("Print All course titles and their respective Prices");
		for(int i = 0; i < courseCount; i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			System.out.println(courseTitle+" = "+coursePrice);
		}
	}
	
	@Test(dataProvider="courseTitle", priority = 5, enabled = true)
	public void copiesSold(String title) {
		
		System.out.println("Print no of copies sold by "+title+" Course");
		for(int i = 0; i < courseCount; i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			if(courseTitle.equalsIgnoreCase(title))
			{
				int copies = js.getInt("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void sumOfAllCourse() {
		
		int actualSum = 0;
		System.out.println("Verify if Sum of all Course prices matches with Purchase Amount");
		for(int i = 0; i < courseCount; i++)
		{
			int price = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			int totalPrice = price * copies;
			actualSum = actualSum + totalPrice;
		}
		int expectedSum = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(actualSum, expectedSum);
		System.out.println("Actual Total Price: "+actualSum);
		System.out.println("Expected Total Price: "+expectedSum);
	}


}
