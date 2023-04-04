package com.mobile.vo;

import org.apache.log4j.Logger;

public class MobileConfigurationVO {

		public static final Logger logger = Logger.getLogger(MobileConfigurationVO.class.getName());

		private String deviceName = null;
		private String udId = null;
		private String platformName = null;
		private String platformVersion = null;

		public MobileConfigurationVO(String deviceName, String udid, String platformName, String platformVersion) {
			logger.info("Starting of MobileConfigurationVO method");
			this.deviceName = deviceName;
			this.udId = udid;
			this.platformName = platformName;
			this.platformVersion = platformVersion;
			logger.info("Ending of MobileConfigurationVO method");
		}

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}

		public String getUdId() {
			return udId;
		}

		public void setUdid(String udId) {
			this.udId = udId;
		}

		public String getPlatformName() {
			return platformName;
		}

		public void setPlatformName(String platformName) {
			this.platformName = platformName;
		}

		public String getPlatformVersion() {
			return platformVersion;
		}

		public void setPlatformVersion(String platformVersion) {
			this.platformVersion = platformVersion;
		}

	}



