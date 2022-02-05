import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import files.payloads;
import io.restassured.path.json.JsonPath;

public class sumValidation {

	//Verify if Sum of all Course prices matches with Purchase Amount
	JsonPath js = new JsonPath(payloads.courses());
	
	@Test
	public void sumCourses() {
		
		int sum = 0;
		int courseCount = js.getInt("courses.size()");
		for (int i = 0; i < courseCount; i++)
		{
			int price = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			int amount = price * copies;
//			System.out.println(amount);
			sum = sum + amount;
		}
//		System.out.println(sum);
		System.out.println("Verify if Sum of all Course prices matches with Purchase Amount:");
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		AssertJUnit.assertEquals(purchaseAmount, sum);
		System.out.println("Expected: "+purchaseAmount);
		System.out.println("Actual: "+sum);
		
	}
	
}
