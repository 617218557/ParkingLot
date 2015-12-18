package com.fyf.parkinglot.common;

public class ScreenInfo {

	private static int screenWidth;
	private static int screenHeight;
	private static float de;
	private static int statusBarHeight;

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		ScreenInfo.screenWidth = screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		ScreenInfo.screenHeight = screenHeight;
	}

	public static float getDe() {
		return de;
	}

	public static void setDe(float de) {
		ScreenInfo.de = de;
	}

	public static int getStatusBarHeight() {
		return statusBarHeight;
	}

	public static void setStatusBarHeight(int statusBarHeight) {
		ScreenInfo.statusBarHeight = statusBarHeight;
	}

}
