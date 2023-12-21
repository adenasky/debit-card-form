package ru.netology.selenuim;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumTestPt2 {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldFailIfIncorrectNameInput() {
        driver.findElement(By.xpath("//span[@data-test-id='name']//input")).sendKeys("Anna");
        driver.findElement(By.xpath("//span[@data-test-id='phone']//input")).sendKeys("+79161234567");
        driver.findElement(By.xpath("//label[@data-test-id='agreement']")).click();
        driver.findElement(By.xpath("//button[contains(@class, 'button')]")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.xpath("//span[@data-test-id='name'][contains(@class,'input_invalid')]//span[@class='input__sub']")).getText().trim());
    }


    @Test
    public void shouldFailIfEmptyNameInput() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldFailIfIncorrectPhoneInput() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванова-Александрова Анна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("Phone");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldFailIfEmptyPhoneInput() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванова-Александрова Анна");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldFailIfCheckboxUnchecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванова-Александрова Анна");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("button.button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }
}