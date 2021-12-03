import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class readFileTest {
	static HashMap<String, Account> dataSet = new HashMap<String,Account>();
	@Test
	void test() {
		Account playerTest = new Account(12,"Kidus","1","lighblue","K");
		dataSet = SettingsScene.readFile("accounts.xml");
		
		assert playerTest.getID().equals(dataSet.get("12").getID());
		assert playerTest.getPassword().equals(dataSet.get("12").getPassword());
		assert playerTest.getUsername().equals(dataSet.get("12").getUsername());
		assert playerTest.getBackGroundColor().equals(dataSet.get("12").getBackGroundColor());
		assert playerTest.getNickName().equals(dataSet.get("12").getNickName());
	}

}
