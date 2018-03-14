
package views;

import java.io.File;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ActorEditTest {

	private WebDriver	browser;


	@Before
	public void setup() {
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\corchu\\Desktop\\chromedriver.exe");
		final File pathToBinary = new File("C:\\Documents and Settings\\Student\\Local Settings\\Application Data\\Mozilla Firefox\\firefox.exe");
		final FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		final FirefoxProfile firefoxProfile = new FirefoxProfile();
		this.browser = new FirefoxDriver(ffBinary, firefoxProfile);
	}

	@Test
	public void startTest() {
		this.browser.get("https://localhost:8443/Acme-Rendezvous/actor/register.do");

		// Will throw exception if elements not found
		this.browser.findElement(By.id("name")).sendKeys("jeje");

		Assert.assertEquals("jeje", this.browser.findElement(By.id("name")).getAttribute("value"));
	}

	@After
	public void tearDown() {
		this.browser.close();
	}
}
