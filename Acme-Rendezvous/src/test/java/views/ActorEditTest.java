
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
	public void registerUserEnTest() throws InterruptedException {
		this.browser.get("https://localhost:8443/Acme-Rendezvous/actor/register.do");

		Assert.assertTrue(!this.browser.findElement(By.name("save")).isEnabled());

		// Will throw exception if elements not found
		this.browser.findElement(By.id("check")).click();

		Assert.assertTrue(this.browser.findElement(By.name("save")).isEnabled());
		this.browser.findElement(By.name("cancel")).click();
		this.browser.get("https://localhost:8443/Acme-Rendezvous/actor/register.do?language=en");

		this.browser.findElement(By.id("check")).click();
		this.browser.findElement(By.name("save")).click();

		try {
			Thread.sleep(1 * 1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("Error in parameters", this.browser.findElement(By.className("message")).getText());
		Assert.assertEquals("Must not be blank", this.browser.findElement(By.id("name.errors")).getText());
		Assert.assertEquals("Must not be blank", this.browser.findElement(By.id("surname.errors")).getText());
		Assert.assertEquals("Must not be blank", this.browser.findElement(By.id("email.errors")).getText());
		Assert.assertEquals("Cannot be null", this.browser.findElement(By.id("birthDate.errors")).getText());
		Assert.assertEquals("Size must be from 5 to 32", this.browser.findElement(By.id("userAccount.username.errors")).getText());
		Assert.assertEquals("Size must be from 5 to 32", this.browser.findElement(By.id("userAccount.password.errors")).getText());

		this.browser.findElement(By.id("name")).sendKeys("New User");
		this.browser.findElement(By.id("surname")).sendKeys("Surname");
		this.browser.findElement(By.id("email")).sendKeys("abc");
		this.browser.findElement(By.id("birthDate")).sendKeys("15/03/2028");
		this.browser.findElement(By.id("userAccount.username")).sendKeys("NewUser");
		this.browser.findElement(By.id("userAccount.password")).sendKeys("NewUser");
		this.browser.findElement(By.id("confirmPassword")).sendKeys("NewUser");

		this.browser.findElement(By.id("check")).click();
		this.browser.findElement(By.name("save")).click();

		try {
			Thread.sleep(1 * 1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("Invalid email", this.browser.findElement(By.id("email.errors")).getText());
		Assert.assertEquals("Must be in the past", this.browser.findElement(By.id("birthDate.errors")).getText());
	}

	@Test
	public void registerUserEsTest() throws InterruptedException {
		this.browser.get("https://localhost:8443/Acme-Rendezvous/actor/register.do");

		Assert.assertTrue(!this.browser.findElement(By.name("save")).isEnabled());

		// Will throw exception if elements not found

		this.browser.findElement(By.id("check")).click();

		Assert.assertTrue(this.browser.findElement(By.name("save")).isEnabled());
		this.browser.findElement(By.name("cancel")).click();
		this.browser.get("https://localhost:8443/Acme-Rendezvous/actor/register.do?language=es");

		this.browser.findElement(By.id("check")).click();
		this.browser.findElement(By.name("save")).click();

		try {
			Thread.sleep(1 * 1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("Error en los parámetros", this.browser.findElement(By.className("message")).getText());
		Assert.assertEquals("No debe estar en blanco", this.browser.findElement(By.id("name.errors")).getText());
		Assert.assertEquals("No debe estar en blanco", this.browser.findElement(By.id("surname.errors")).getText());
		Assert.assertEquals("No debe estar en blanco", this.browser.findElement(By.id("email.errors")).getText());
		Assert.assertEquals("No puede ser null", this.browser.findElement(By.id("birthDate.errors")).getText());
		Assert.assertEquals("El tamaño debe estar entre 5 y 32", this.browser.findElement(By.id("userAccount.username.errors")).getText());
		Assert.assertEquals("El tamaño debe estar entre 5 y 32", this.browser.findElement(By.id("userAccount.password.errors")).getText());

		this.browser.findElement(By.id("name")).sendKeys("New User");
		this.browser.findElement(By.id("surname")).sendKeys("Surname");
		this.browser.findElement(By.id("email")).sendKeys("abc");
		this.browser.findElement(By.id("birthDate")).sendKeys("12/03/2020");
		this.browser.findElement(By.id("userAccount.username")).sendKeys("NewUser");
		this.browser.findElement(By.id("userAccount.password")).sendKeys("NewUser");
		this.browser.findElement(By.id("confirmPassword")).sendKeys("NewUser");

		this.browser.findElement(By.id("check")).click();
		this.browser.findElement(By.name("save")).click();

		try {
			Thread.sleep(1 * 1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertEquals("E-mail incorrecto", this.browser.findElement(By.id("email.errors")).getText());
		Assert.assertEquals("Debe ser en el pasado", this.browser.findElement(By.id("birthDate.errors")).getText());
	}
	@After
	public void tearDown() {
		this.browser.close();
	}
}
