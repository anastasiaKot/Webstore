package com.kohls.aqa.webstore.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DriverFactory {

	public static final String FIREFOX = "firefox";
	public static final String CHROME = "chrome";
	private static final int IMPLICIT_WAIT = 10;
	private static final int SCRIPT_LOAD_WAIT = 10;
	public static synchronized WebDriver getDriver(final String driverType) {
		WebDriver driver;
		switch (driverType) {
		case FIREFOX:
			driver = new FirefoxDriver();
			break;
		case CHROME:
			driver = new ChromeDriver();
			break;
		default:
			String msg = "Unknown driver type";
			throw new IllegalArgumentException(msg);
		}
		WebDriver.Timeouts timeouts = driver.manage().timeouts();
		timeouts.implicitlyWait(IMPLICIT_WAIT, SECONDS);
		timeouts.setScriptTimeout(SCRIPT_LOAD_WAIT, SECONDS);
		addShutDownHook(driver);
		return driver;
	}

	public static synchronized void close(WebDriver driver) {
		if (driver != null) {
			try {
				driver.getCurrentUrl();
				driver.close();
				driver.quit();
			} catch (Throwable e) {
			}
		}
	}

	private static synchronized void addShutDownHook(final WebDriver driver) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				close(driver);
			}
		});

	}
}
