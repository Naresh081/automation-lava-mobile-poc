package com.lavamobiles.base.pages;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mobile.vo.VisualComparisionPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class BaseLavaMobileAutomationPage {

	protected AppiumDriver<WebElement> driver = null;
	protected VisualComparisionPage visualComparisionPage = null;
	protected TouchAction<?> touchActions = null;
	protected WebElement scrollEle = null;

	private static final Logger logger = Logger.getLogger(BaseLavaMobileAutomationPage.class.getName());

	public BaseLavaMobileAutomationPage(AppiumDriver<WebElement> driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		visualComparisionPage = new VisualComparisionPage(driver);
		touchActions = new TouchAction<>(driver);
	}

	public void implicitWait() {
		logger.info("Starting of implicitWait Method");

		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);

		logger.info("Ending of implicitWait Method");
	}

	public void scrollUsingText(String textName) {
		logger.info("Starting of scrollUsingText Method");

		try {
			scrollEle = driver.findElement(MobileBy.AndroidUIAutomator(
					"new UiScrollable(new UiSelector()).scrollIntoView(new UiSelector().text(\" + textName +\"));\""));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.info("Ending of scrollUsingText Method");
	}

	public void scroll() {
		logger.info("Starting of scroll Method");

		sleep(3000);
		Dimension size = driver.manage().window().getSize();
		int startX = size.width / 2;
		int endX = startX;
		int startY = (int) (size.height * 0.8);
		int endY = (int) (size.height * 0.4);
		touchActions.press(PointOption.point(startX, startY))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(endX, endY))
				.release().perform();

		logger.info("Ending of scroll Method");
	}

	public void verticalScrollUp() {
		logger.info("Starting of verticalScrollUp Method");

		Dimension dimension = driver.manage().window().getSize();
		int start_X = (int) (dimension.width * 0.5);
		int start_Y = (int) (dimension.height * 0.2);
		int end_X = (int) (dimension.width * 0.2);
		int end_Y = (int) (dimension.height * 0.8);
		touchActions.press(PointOption.point(start_X, start_Y))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(end_X, end_Y))
				.release().perform();

		logger.info("Starting of verticalScrollUp Method");
	}

	public void sleep(int sec) {
		logger.info("Starting of sleep Method");

		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			logger.error("Error occurred while using sleep method ", e);
		}

		logger.info("Ending of sleep Method");
	}

	public void verticalScrollDown() throws InterruptedException {
		logger.info("Starting of verticalScrollDown Method");			
					
		Dimension size = driver.manage().window().getSize();
		int startX = size.width / 2;
		int endX = startX;
		int startY = (int) (size.height * 0.3);
		int endY = (int) (size.height * 0.2);
		TouchAction t = new TouchAction(driver);
		t.press(PointOption.point(startX, startY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
				.moveTo(PointOption.point(endX,-endY)).release().perform();
		
		logger.info("Ending of verticalScrollDown Method");
	}

	public void scrollUp() {
		logger.info("Starting of scrollUp Method");

		Dimension size = driver.manage().window().getSize();
		int startX = size.width / 2;
		int endX = startX;
		int startY = (int) (size.height * 0.5);
		int endY = (int) (size.height * 0.8);
		for (int i = 0; i < 4; i++) {
			touchActions.press(PointOption.point(startX, startY))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(endX, endY))
					.release().perform();
		}

		logger.info("Ending of scrollUp Method");
	}

	public void hardBack() {
		logger.info("Starting of hardBack Method");

		driver.navigate().back();

		logger.info("Ending of hardBack Method");
	}

	public void explicitWait(WebElement webElement) {
		logger.info("Starting of explicitWait method");

		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(webElement));

		logger.info("Ending of explicitWait method");
	}

	public enum MOBILE_ACTIONS {
		CLICK, VISIBILE, TOAST
	}

	protected WebElement findElement(WebElement webelement, MOBILE_ACTIONS mobileActions) {
		logger.info("Starting of findElement Method");

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30))
					.pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
			switch (mobileActions) {
			case CLICK:
				wait.until(ExpectedConditions.elementToBeClickable(webelement));
				break;
			case VISIBILE:
				wait.until(ExpectedConditions.visibilityOf(webelement));
				break;
			default:
				wait.until(ExpectedConditions.visibilityOf(webelement));
			}
		} catch (TimeoutException | StaleElementReferenceException ex) {
			logger.info("Webelement exception{}" + webelement, ex);
		}

		logger.info("Ending of findElement Method");
		return webelement;
	}

	protected void clickOnElement(WebElement webelement) {
		findElement(webelement, MOBILE_ACTIONS.CLICK).click();
	}

	protected String getText(WebElement webelement) {
		return findElement(webelement, MOBILE_ACTIONS.VISIBILE).getText();
	}

	protected boolean checkElementPresence(WebElement webelement) {
		return findElement(webelement, MOBILE_ACTIONS.VISIBILE).isDisplayed();
	}

	protected String getToastText(WebElement webelement) {
		return findElement(webelement, MOBILE_ACTIONS.TOAST).getText();
	}

	protected void sendKeys(WebElement webelement, String keys) {
		findElement(webelement, MOBILE_ACTIONS.VISIBILE).sendKeys(keys);
	}

	protected void clear(WebElement webelement) {
		findElement(webelement, MOBILE_ACTIONS.VISIBILE).clear();
	}
	
	public void hardWait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	
	 protected void androidScrollUsingText(String text) {
	        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))" +
	                ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
	    }
}
