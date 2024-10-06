package WeBookMobileTask;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;

public class BaseTest {

    public weatherAppHelper WeatherAppHelper;
    private AndroidDriver driver;
    private AppiumDriverLocalService appiumServiceBuilder;

    public AndroidDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver has not been initialized. Call configureAppium() first.");
        }
        return driver;
    }

    public void configureAppium() throws MalformedURLException {
        // Start Appium server with specific configuration
        appiumServiceBuilder = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\USER\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1")
                .usingAnyFreePort() // Automatically find a free port
                .withTimeout(Duration.ofMinutes(2)) // Increase server startup timeout
                .build();

        // Start the Appium server
        appiumServiceBuilder.start();

        if (!appiumServiceBuilder.isRunning()) {
            throw new IllegalStateException("Appium server failed to start.");
        }

        DesiredCapabilities caps = new DesiredCapabilities();

        // Set W3C compliant capabilities with 'appium:' prefix for non-standard ones
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app", "C:\\Users\\user\\Documents\\WebookMobile\\src\\test\\java\\WebookMobileTask\\resources\\Weather Forecast.apk");
        caps.setCapability("appium:deviceName", "emulator-5554");

        // Initialize AndroidDriver with Appium server URL and capabilities
        driver = new AndroidDriver(appiumServiceBuilder.getUrl(), caps);
    }

    @AfterTest
    public void tearDown() {
        // Stop the Appium driver
        if (driver != null) {
            driver.quit();
        }
        // Stop the Appium server and release the port
        if (appiumServiceBuilder != null && appiumServiceBuilder.isRunning()) {
            appiumServiceBuilder.stop();
        }
    }

    @BeforeTest
    public void setup() throws MalformedURLException {
        configureAppium();
        WeatherAppHelper = new weatherAppHelper(getDriver());
    }
}
