package com.willcurrie.webdriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

@Ignore
public class DecodeFirefoxTest {
	
	private WebDriver driver;

	@Before
	public void setup() {
		driver = new FirefoxDriver();
	}
	
	@Test
	public void testValidTVR_AllBitsSet() throws Exception {
		assertDecodedTVR("FFFFFFFFFF",
				"8000000000", "Offline data authentication was not performed",
				"4000000000", "SDA failed",
				"2000000000", "ICC data missing",
				"1000000000", "Card appears on terminal exception file",
				"0800000000", "DDA failed",
				"0400000000", "CDA failed",
				"0080000000", "ICC and terminal have different application versions",
				"0040000000", "Expired application",
				"0020000000", "Application not yet effective",
				"0010000000", "Requested service not allowed for card product",
				"0008000000", "New card",
				"0000800000", "Cardholder verification was not successful",
				"0000400000", "Unrecognised CVM",
				"0000200000", "PIN try limit exceeded",
				"0000100000", "PIN entry required and PIN pad not present or not working",
				"0000080000", "PIN entry required, PIN pad present, but PIN was not entered",
				"0000040000", "Online PIN entered",
				"0000008000", "Transaction exceeds floor limit",
				"0000004000", "Lower consecutive offline limit exceeded",
				"0000002000", "Upper consecutive offline limit exceeded",
				"0000001000", "Transaction selected randomly for online processing",
				"0000000800", "Merchant forced transaction online",
				"0000000080", "Default TDOL used",
				"0000000040", "Issuer authentication failed",
				"0000000020", "Script processing failed before final GENERATE AC",
				"0000000010", "Script processing failed after final GENERATE AC");
	}

	@Test
	public void testValidTVR_3BitsSet() throws Exception {
		assertDecodedTVR("0000888000", 
				"0000800000", "Cardholder verification was not successful",
				"0000080000", "PIN entry required, PIN pad present, but PIN was not entered",
				"0000008000", "Transaction exceeds floor limit");
	}
	
	private void assertDecodedTVR(String valueToDecode,
			String... expectedResults) throws InterruptedException {
		driver.navigate().to("http://localhost:8080");
		assertEquals("TVR Decoder", driver.getTitle());
		WebElement valueField = driver.findElement(By.id("value_field"));
		valueField.clear();
		valueField.sendKeys(valueToDecode);
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		waitForResponse();
		List<String> actualResults = gatherDecodedResponse();
		assertEquals(Arrays.asList(expectedResults), actualResults);
		driver.close();
	}

	private List<String> gatherDecodedResponse() {
		List<WebElement> rawData = driver.findElements(By.xpath("//span[@class='rawData']"));
		List<WebElement> decodedData = driver.findElements(By.xpath("//span[@class='decodedData']"));
		assertEquals(rawData.size(), decodedData.size());
		Iterator<WebElement> rawItr = rawData.iterator();
		Iterator<WebElement> decodedItr = decodedData.iterator();
		List<String> actualResults = new ArrayList<String>();
		while (rawItr.hasNext() && decodedItr.hasNext()) {
			actualResults.add(rawItr.next().getText());
			actualResults.add(decodedItr.next().getText());
		}
		return actualResults;
	}

	private void waitForResponse() throws InterruptedException {
		for (int i = 1; i <= 10; i++) {
			WebElement display = driver.findElement(By.id("display"));
			if (!"".equals(display.getText())) {
				return;
			}
			Thread.sleep(1000);
		}
		fail("No response after 10 seconds");
	}
}
