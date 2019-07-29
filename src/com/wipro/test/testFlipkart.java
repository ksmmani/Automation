package com.wipro.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

@Test
public class testFlipkart {
	public static WebDriver driver;

	public void LaunchBrowser() throws InterruptedException, IOException {

		// chrome browser
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Drivers/chromedriver.exe");
		driver = new ChromeDriver();

		// IE browser

		// System.setProperty("webdriver.ie.driver", "C://Program Files//internet
		// explorer//iexplore.exe");
		// driver = new InternetExplorerDriver();

		// Firefox

		// System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
		// driver = new FirefoxDriver();
		// Launch the URL
		driver.get("https://www.flipkart.com");
		driver.manage().window().maximize();
		ArrayList<String> emailid = readExcelData(0);
		ArrayList<String> password = readExcelData(1);
		for (int i = 0; i < emailid.size(); i++) {
			driver.findElement(By.xpath("//input[@class='_2zrpKA _1dBPDZ']")).sendKeys(emailid.get(i));
			driver.findElement(By.xpath("//input[@class='_2zrpKA _3v41xv _1dBPDZ']")).sendKeys(password.get(i));
			driver.findElement(By.xpath("//*[@class='_2AkmmA _1LctnI _7UHT_c']")).click();

		}

	}

	// Read the data from excel sheet
	@Test
	public ArrayList<String> readExcelData(int colNo) throws IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/Drivers/Login.xlsx");

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet s = wb.getSheet("Sheet1");
		Iterator<Row> rowIterator = s.iterator();
		rowIterator.next();
		ArrayList<String> list = new ArrayList<String>();
		while (rowIterator.hasNext()) {
			list.add(rowIterator.next().getCell(colNo).getStringCellValue());

		}
		System.out.println("List" + list);
		return list;

	}

	@Test
	public static void main() throws IOException {
		testFlipkart data = new testFlipkart();
		data.readExcelData(1);
	}

	// Search Item
	@Test
	public static void searchItem() throws IOException, InterruptedException {
		Thread.sleep(2000);
		// Search Item action
		driver.findElement(By.xpath("//input[@title='Search for products, brands and more']")).click();
		Thread.sleep(2000);
		WebElement searchItem = driver.findElement(By.xpath("//input[@title='Search for products, brands and more']"));
		searchItem.sendKeys("Camara");
		searchItem.sendKeys(Keys.RETURN);
		Thread.sleep(3000);

		// Scroll bar to search the item
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,1000)");
		Thread.sleep(2000);
		// Handle multiple window Tab
		String Parent = driver.getWindowHandle();
		System.out.println("parent window id:" + Parent);
		driver.findElement(By.xpath("//*[contains(text(),'Fujifilm Instax Mini8 Wander Lust Series Instant Camera')]"))
				.click();
		Thread.sleep(2000);
		Set<String> allwindow = driver.getWindowHandles();
		for (String child : allwindow) {
			if (!Parent.equalsIgnoreCase(child)) {
				driver.switchTo().window(child);
				driver.findElement(By.xpath("//*[@class='_2AkmmA _2Npkh4 _2MWPVK']")).click();
				Thread.sleep(2000);
				// Place Order
				driver.findElement(By.xpath("//*[@class='_2AkmmA iwYpF9 _7UHT_c']")).click();
				System.out.println("cliked on place order");
				Thread.sleep(5000);
				driver.navigate().back();
				Thread.sleep(2000);
				// Logout from user account
				WebElement eal = driver.findElement(By.xpath("//*[@class='_2aUbKa']"));
				Actions act = new Actions(driver);
				act.moveToElement(eal).perform();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[contains(text(),'Logout')]")).click();

			}
		}

	}

	@AfterTest
	public static void close() {
		driver.quit();
	}

}
