package com.lavamobiles.test;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;

import com.mobile.vo.MobileConfigurationVO;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class BaseLavaMobilesAutomationTest {
	private static final Logger logger = Logger.getLogger(BaseLavaMobilesAutomationTest.class.getName());
	protected MobileConfigurationVO mobileConfigurationVO = null;

	protected static Map<String, AppiumDriver<WebElement>> driversMap = new HashMap<String, AppiumDriver<WebElement>>();

	protected static Properties testDataProp = null;
	protected static Properties expectedAssertionsProp = null;

	protected AppiumDriver<WebElement> driver = null;

	public enum SCREEN_ID {

	}

	@BeforeSuite
	public void initApplication() {
		logger.debug("Inside init Application");

		if (testDataProp == null) {
			FileReader testDataReader = null;
			FileReader assertionsReader = null;

			try {

				testDataReader = new FileReader("src/main/resources/testdata.properties");
				assertionsReader = new FileReader("src/main/resources/expectedassertions.properties");

				testDataProp = new Properties();
				testDataProp.load(testDataReader);

				expectedAssertionsProp = new Properties();
				expectedAssertionsProp.load(assertionsReader);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (testDataReader != null) {
						testDataReader.close();
					}
					assertionsReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	protected synchronized void initMobileDriver(MobileConfigurationVO mobileConfigurationVO, boolean reset)
			throws MalformedURLException {
		logger.info("Starting of method initMobileDriver in LogiqidsTest");

		// AppiumDriver<WebElement> driver = null;

		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability("deviceName", mobileConfigurationVO.getDeviceName());
		cap.setCapability("udid", mobileConfigurationVO.getUdId());
		cap.setCapability("platformName", mobileConfigurationVO.getPlatformName());
		cap.setCapability("platformVersion", mobileConfigurationVO.getPlatformVersion());

		// cap.setCapability("appPackage", "com.google.android.apps.docs");
		// cap.setCapability("appActivity",
		// "com.google.android.apps.docs.app.NewMainProxyActivity");
		cap.setCapability("noreset", reset);
		cap.setCapability("appPackage", "");
		cap.setCapability("appActivity", "");

		driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
		driversMap.put(mobileConfigurationVO.getUdId(), driver);

		logger.info("Ending of method initMobileDriver in LogiqidsTest");
	}

	protected synchronized AppiumDriver<WebElement> getMobileDriver(String driverKey) throws Exception {
		logger.info("Starting of method getMobileDriver");

		AppiumDriver<WebElement> driver = null;
		driver = (AppiumDriver<WebElement>) driversMap.get(driverKey);

		// Use existing driver
		if (driver == null) {
			logger.error("Driver not initialized, Please call initDriver Method. Before calling getDriver ");
			throw new Exception("Driver not initialized");
		}

		// Otherwise create new driver
		logger.info("Ending of method getMobileDriver");
		return driver;
	}

	protected synchronized void quitMobileDriver(String driverKey) {
		logger.info("Starting of method quitDriver in LogiqidsTest ");

		AppiumDriver<WebElement> driver = null;

		driver = (AppiumDriver<WebElement>) driversMap.get(driverKey);
		try {
			if (driver != null) {
				driver.quit();
				driver = null;

			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			driver = null;
		}
		logger.info("Ending of method quitDriver in LogiqidsTest");
	}

	public WebDriver getChildWebDriver() {
		return driver;
	}
}
