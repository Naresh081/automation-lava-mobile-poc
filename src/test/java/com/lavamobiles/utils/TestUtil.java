package com.lavamobiles.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TestUtil {
	
	 public static String randomNumberWithLength(int digits) {
	        return RandomStringUtils.randomNumeric(digits);
	    }

	    public static String getCurrentDate() {
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDateTime now = LocalDateTime.now();
	        return dtf.format(now);
	    }

	    public static String getRandomString(final int length) {
	        final StringBuilder buffer = new StringBuilder();
	        final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        final int charactersLength = chars.length();

	        for (int i = 0; i < length; i++) {
	            final double index = Math.random() * charactersLength;
	            buffer.append(chars.charAt((int) index));
	        }
	        return buffer.toString();
	    }

	    public static double convertDouble(String value) {
	        return NumberUtils.toDouble(value);
	    }

	    public static int convertInteger(String value) {
	        return NumberUtils.toInt(value);
	    }

	    public static boolean convertBoolean(String value) {
	        return Boolean.parseBoolean(value);
	    }

	    public static String getRandomName() {
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMM");
	        return "" + dtf.format(java.time.LocalDateTime.now()) + "_" + getRandomString(8);
	    }

	    public static String getNumberFromString(String msg) {
	        return msg.replaceAll("[^0-9]", "");
	    }




}
