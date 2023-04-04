package com.mobile.vo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import io.appium.java_client.AppiumDriver;
public class VisualComparisionPage {

	private AppiumDriver<WebElement> driver = null;
	private String destFile1;
	private String destFile2;

	private static final Logger logger = Logger.getLogger(VisualComparisionPage.class.getName());
	private String destDir = (System.getProperty("user.dir") + "/screenshots");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

	public VisualComparisionPage(AppiumDriver<WebElement> driver) {
		this.driver = driver;
	}

	public synchronized void takeScreenShot(Enum<?> SCREEN_ID, String pDeviceName) {

		logger.info("Starting of takeScreenShot method");
		File compFile = null;
		File mkdir = new File(destDir);
		mkdir.mkdirs();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		destFile1 = SCREEN_ID.name() + ".png";
		logger.debug("destFile1 is :" + destFile1);
		try {
			File baseFile = new File(destDir + "/" + pDeviceName + "/" + destFile1);

			logger.debug("baseFile is : " + baseFile);
			Boolean override = false;
			if (!baseFile.exists() || override) {
				FileUtils.copyFile(scrFile, baseFile);
			}
			destFile2 = SCREEN_ID.name() + "_COMP.png";

			compFile = new File(destDir + "/" + pDeviceName + "/" + destFile2);
			override = true;
			if (!compFile.exists() || override) {
				FileUtils.copyFile(scrFile, compFile);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("Ending of takeScreenShot method");
	}

	public synchronized double visualDifference(Enum<?> SCREEN_ID, String pDeviceName) {

		logger.info("Starting of visualDifference method");
		BufferedImage imgA = null;
		BufferedImage imgB = null;
		double percentage = 0;
		try {
			File fileA = new File(destDir + "/" + pDeviceName + "/" + SCREEN_ID + "_COMP.png");
			File fileB = new File(destDir + "/" + pDeviceName + "/" + SCREEN_ID + ".png");
			imgA = ImageIO.read(fileA);
			imgB = ImageIO.read(fileB);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		int width1 = imgA.getWidth();
		int width2 = imgB.getWidth();
		int height1 = imgA.getHeight();
		int height2 = imgB.getHeight();

		if ((width1 != width2) || (height1 != height2))
			logger.debug("Error: Images dimensions" + " mismatch");
		else {
			long difference = 0;
			for (int y = 0; y < height1; y++) {
				for (int x = 0; x < width1; x++) {
					int rgbA = imgA.getRGB(x, y);
					int rgbB = imgB.getRGB(x, y);
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA) & 0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB) & 0xff;
					difference += Math.abs(redA - redB);
					difference += Math.abs(greenA - greenB);
					difference += Math.abs(blueA - blueB);
				}
			}

			double total_pixels = width1 * height1 * 3;
			double avg_different_pixels = difference / total_pixels;
			percentage = (avg_different_pixels / 255) * 100;
			logger.info("***********************************************************");
			logger.info("Difference Percentage-->" + SCREEN_ID + "-->" + percentage);
			logger.info("***********************************************************");
		}
		logger.info("Ending of visualDifference method");
		return percentage;
	}

	public synchronized void visualComparision(Enum<?> SCREEN_ID, String pDeviceName) {
		logger.info("Starting of visualComparision method");
		BufferedImage expectedImage = ImageComparisonUtil
				.readImageFromResources(destDir + "/" + pDeviceName + "/" + SCREEN_ID + ".png");
		BufferedImage actualImage = ImageComparisonUtil
				.readImageFromResources(destDir + "/" + pDeviceName + "/" + SCREEN_ID + "_COMP.png");
		File resultDestination = new File(destDir + "/" + pDeviceName + "/" + SCREEN_ID + "_RESULT.png");
		ImageComparison imageComparison = new ImageComparison(expectedImage, actualImage, resultDestination);

		/*
		 * Also can be configured BEFORE comparing next properties: Threshold - it's the
		 * max distance between non-equal pixels. By default it's 5.
		 */

		imageComparison.setThreshold(50);

		/*
		 * RectangleListWidth - Width of the line that is drawn in the rectangle. By
		 * default it's 1.
		 */

		imageComparison.setRectangleLineWidth(5);

		/*
		 * DifferenceRectangleFilling - Fill the inside the difference rectangles with a
		 * transparent fill. By default it's false and 20.0% opacity.
		 */

		imageComparison.setDifferenceRectangleFilling(true, 30.0);
		imageComparison.isFillDifferenceRectangles();
		imageComparison.getPercentOpacityDifferenceRectangles();

		/*
		 * ExcludedRectangleFilling - Fill the inside the excluded rectangles with a
		 * transparent fill. By default it's false and 20.0% opacity.
		 */

		imageComparison.setExcludedRectangleFilling(true, 30.0);
		imageComparison.isFillExcludedRectangles();
		imageComparison.getPercentOpacityExcludedRectangles();

		// Destination. Before comparing also can be added destination file for result
		// image.
		imageComparison.setDestination(resultDestination);
		imageComparison.getDestination();

		/*
		 * MaximalRectangleCount - It means that would get first x biggest rectangles
		 * for drawing. By default all the rectangles would be drawn.
		 */

		imageComparison.setMaximalRectangleCount(10);
		imageComparison.getMaximalRectangleCount();

		/*
		 * MinimalRectangleSize - The number of the minimal rectangle size. Count as
		 * (width x height). By default it's 1.
		 */

		imageComparison.setMinimalRectangleSize(100);
		imageComparison.getMinimalRectangleSize();

		// Change the level of the pixel tolerance:
		imageComparison.setPixelToleranceLevel(0.2);
		imageComparison.getPixelToleranceLevel();

		// After configuring the ImageComparison object, can be executed compare()
		// method:

		ImageComparisonResult imageComparisonResult = imageComparison.compareImages();

		// Can be found ComparisonState.
		// ImageComparisonState imageComparisonState =
		// imageComparisonResult.getImageComparisonState();

		// And Result Image
		BufferedImage resultImage = imageComparisonResult.getResult();

		// Image can be saved after comparison, using ImageComparisonUtil.
		ImageComparisonUtil.saveImage(resultDestination, resultImage);

		logger.info("Ending of visualComparision method");
	}

}
