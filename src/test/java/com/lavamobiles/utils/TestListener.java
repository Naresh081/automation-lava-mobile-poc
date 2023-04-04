package com.lavamobiles.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.lavamobiles.test.BaseLavaMobilesAutomationTest;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

public class TestListener extends TestListenerAdapter {
	private static final Logger logger = Logger.getLogger(TestListener.class.getName());

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	// Text attachments for Allure
	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}

	// HTML attachments for Allure
	@Attachment(value = "{0}", type = "text/html")
	public static String attachHtml(String html) {
		return html;
	}

	public void saveScreenshotPNG(String screenshotType, WebDriver driver) {
		byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		Allure.getLifecycle().addAttachment(
				screenshotType + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")),
				"image/png", "png", screenshot);
	}

	@Override
	public void onStart(ITestContext iTestContext) {
		logger.info("In onStart method " + iTestContext.getName());
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		logger.info(getTestMethodName(iTestResult) + " test is starting.");
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		logger.info(getTestMethodName(iTestResult) + " test is succeed.");
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		logger.info(getTestMethodName(iTestResult) + " test is failed.");
		// Get driver from BaseClassplusAutomationTest and assign to local webdriver
		Object testClass = iTestResult.getInstance();
		WebDriver driver = ((BaseLavaMobilesAutomationTest) testClass).getChildWebDriver();

		// Allure ScreenShotRobot and SaveTestLog
		if (driver != null) {
			logger.info("Screenshot captured for test case:" + getTestMethodName(iTestResult));
			saveScreenshotPNG("Test_Failure_Screenshot_", driver);
		}
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		logger.info(getTestMethodName(iTestResult) + " test is skipped.");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		logger.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
	}

	public void onTestFailedWithTimeout(ITestResult iTestResult) {
		logger.info(getTestMethodName(iTestResult) + " test is failed with Timeout.");
		onTestFailure(iTestResult);
	}
}
