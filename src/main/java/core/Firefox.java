package core;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;


import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;



public class Firefox {


	static WebDriver driver;

	static Cipher cipher;
	//	public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
	//	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	//	String encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
	//	return encryptedText;
 
//}


	public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
 
}


public static void main(String[] args) throws Exception {

String mac_address;

String cmd_mac = "ifconfig en0";
   String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";

   if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
		mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1];}
		else {mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];}
		mac_address = mac_address.toLowerCase().replaceAll("-", ":");
			
	
	
Logger logger = Logger.getLogger("");
logger.setLevel(Level.OFF);

String driverPath = "";

String url = "http://facebook.com/";
String email_address = "9164728144";
String encrypted_password = "pyB/l2aXBXkTgJOYIegyEA==";
	// from login.java
	String password = decrypt(encrypted_password, new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"));

		if (System.getProperty("os.name").toUpperCase().contains("MAC"))
			driverPath = "./resources/webdrivers/mac/geckodriver.sh";
		else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
			driverPath = "./resources/webdrivers/pc/geckodriver.exe";
		else
			throw new IllegalArgumentException("Unknown OS");
		System.setProperty("webdriver.gecko.driver", driverPath);
		WebDriver driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get(url);

        Thread.sleep(1000); // Pause in milleseconds (1000 – 1 sec)
		
		String title = driver.getTitle();
		String copyright = driver.findElement(By.xpath("//*[@id=\'pageFooter\']/div[3]/div/span")).getText();
		
		driver.findElement(By.id("email")).sendKeys(email_address);
		driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("u_0_2")).click();
        
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id='u_0_a']/div[1]/div[1]/div/a/span")).click();

        Thread.sleep(1000);
        String friends = driver.findElement(By.xpath("//div[2]/ul/li[3]/a/span[1]")).getText();
        
        Thread.sleep(1000);
        driver.findElement(By.id("userNavigationLabel")).click();
        driver.findElement(By.linkText("Log Out")).click();
      
        
		driver.quit();
        
		System.out.println("Browser is: Firefox 56.0 (64-bit)");
        System.out.println("Title of the page: " + title);
        System.out.println("Copyright: " + copyright);
        System.out.println("You have " + friends + " friends");
	}

}



