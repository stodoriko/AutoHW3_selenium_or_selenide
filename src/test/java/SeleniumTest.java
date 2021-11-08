import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


class SeleniumTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSetPersonalInfoTest() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Фамилия и имя')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("Тодорико Сергей");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Мобильный телефон')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("+79258582575");
        driver.findElement(By.xpath(".//span[contains(@class, 'checkbox__box')]")).click();
        driver.findElement(By.xpath(".//span[contains(text(), 'Продолжить')]")).click();
        String actualMessage = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage.strip());
    }

    @Test
    void shouldFailIfNameIsNotRussian() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Фамилия и имя')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("Todoriko Sergei");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Мобильный телефон')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("+79258582575");
        driver.findElement(By.xpath(".//span[contains(@class, 'checkbox__box')]")).click();
        driver.findElement(By.xpath(".//span[contains(text(), 'Продолжить')]")).click();
        String actualMessage = driver.findElement(By.xpath(".//span[preceding-sibling::span//input[contains(@name, 'name')]]")).getText();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldFailIfWrongNumber() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Фамилия и имя')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("Тодорико Сергей");
        driver.findElement(By.xpath(".//span[preceding-sibling::span[contains(text(), 'Мобильный телефон')]]" +
                "//input[contains(@class, 'input__control')]")).sendKeys("+7925858257");
        driver.findElement(By.xpath(".//span[contains(@class, 'checkbox__box')]")).click();
        driver.findElement(By.xpath(".//span[contains(text(), 'Продолжить')]")).click();
        String actualMessage = driver.findElement(By.xpath(".//span[preceding-sibling::span//input[contains(@name, 'phone')]]")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
