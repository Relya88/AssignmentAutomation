package StepDefs;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {

    private WebDriver driver;

    // Sring random för generering av slumpmail
    String generateRandomEmail() {
        long timestamp = System.currentTimeMillis();
        return "tester" + timestamp + "@example.com";
    }
    //Explicit wait
    private WebElement waitForElementAndReturn(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Öppnar länken i två browsrar - Chrome och Edge
    @Given("I navigate to page BasketballEngland using {string}")
    public void iNavigateToPageBasketballEnglandUsing(String browser) {

        if (browser.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        }
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    //Fyller i födelsedatum
    @When("I enter my {string}")
    public void iEnterMy(String dateOfBirth) {

        driver.findElement(By.id("dp")).click();
        driver.findElement(By.id("dp")).sendKeys(dateOfBirth);
    }

    //Fyller i För-och efternamn
    @And("I enter {string} and {string}")
    public void iEnterAnd(String FirstName, String LastName) {

        driver.findElement(By.id("member_firstname")).click();
        driver.findElement(By.id("member_firstname")).sendKeys(FirstName);
        driver.findElement(By.id("member_lastname")).click();
        driver.findElement(By.id("member_lastname")).sendKeys(LastName);
    }

    //Genererar och fyller i en unik email i bägge fältet varje gång
    @And("I enter my email and confirmed email in both fields")
    public void iEnterMyEmailAndConfirmedEmailInBothFields() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
    }

    //Generering av slumpmässigt lösenord
    @And("I enter my Password {string} and retype it {string}")
    public void iEnterMyPasswordAndRetypeIt(String Password, String RetypePassword) {

        if ("UseRandom".equals(Password) && "UseRandom".equals(RetypePassword)) {
            int random = new Random().nextInt(993458716);
            String randomPassword = "Pwd" + random;
            driver.findElement(By.id("signupunlicenced_password")).sendKeys(randomPassword);
            driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys(randomPassword);
        } else {
            driver.findElement(By.id("signupunlicenced_password")).sendKeys(Password);
            driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys(RetypePassword);
        }
    }

    //Bockar i boxen för användarvillkoren (men endast om den redan inte är ibockad)
    @And("I tick the {} checkbox is ticked")
    public void iTickTheCheckboxIsTicked(String TermsAndConditions) {

        if ("ticked".equals(TermsAndConditions)) {
            WebElement checkbox = driver.findElement(By.cssSelector(".md-checkbox > .md-checkbox:nth-child(1) .box"));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
    }

    //Bockar i övriga nödvändiga boxar
    @And("I tick boxes for PR and CoC are ticked")
    public void iTickBoxesForPRAndCoCAreTicked() {

        driver.findElement(By.cssSelector(".md-checkbox:nth-child(2) > label > .box")).click();
        driver.findElement(By.cssSelector(".md-checkbox:nth-child(7) .box")).click();
    }

    //Klickar på joinknapp
    @And("I click on the Confirm and join button")
    public void iClickOnTheConfirmAndJoinButton() {

        driver.findElement(By.name("join")).click();
    }

    //Verifierat text från registrerat konto
    @Then("my user account should be created giving the {string}")
    public void myUserAccountShouldBeCreatedGivingThe(String Result) {

        WebElement element;

        // Lyckad registrering - Expected: Text lyckad reg, Actual: Text lyckad reg
        if (!Result.startsWith("error:")) {
            element = waitForElementAndReturn(By.cssSelector(".bold:nth-child(1)"), Duration.ofSeconds(10));
            assertEquals(Result, element.getText());

            //Misslyckad registrering - Expected: "Feltext", Actual: Den hämtade feltexten
        } else {
            if (Result.contains("Last Name is required")) {
                element = waitForElementAndReturn(By.xpath("//form[@id='signup_form']/div[5]/div[2]/div/span/span"), Duration.ofSeconds(10));
                assertEquals("Last Name is required", element.getText());
            } else if (Result.contains("Password did not match")) {
                element = waitForElementAndReturn(By.xpath("//form[@id='signup_form']/div[8]/div/div[2]/div[2]/div/span/span"), Duration.ofSeconds(10));
                assertEquals("Password did not match", element.getText());
            } else if (Result.contains("You must confirm that you have read and accepted our Terms and Conditions")) {
                element = waitForElementAndReturn(By.xpath("//form[@id='signup_form']/div[11]/div/div[2]/div/span/span"), Duration.ofSeconds(10));
                assertEquals("You must confirm that you have read and accepted our Terms and Conditions", element.getText());
            }
        }
    }
    //Stänger varje testscenario/sida
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
