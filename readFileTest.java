import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
//Atuhor: JAck
class readFileTest {
	static HashMap<String, Account> dataSet = new HashMap<String,Account>();
	@Test
	void test() {
		Account playerTest = new Account(12,"Kidus1","1","Yellow");
		dataSet = AccountsReader.readFile("accounts1.xml");
		assert playerTest.getID().equals(dataSet.get("12").getID());
		assert playerTest.getPassword().equals(dataSet.get("12").getPassword());
		assert playerTest.getUsername().equals(dataSet.get("12").getUsername());
		assert playerTest.getBackGroundColor().equals(dataSet.get("12").getBackGroundColor());
	}

}
