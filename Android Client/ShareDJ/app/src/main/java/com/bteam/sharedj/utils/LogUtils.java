package com.bteam.sharedj.utils;

import android.util.Log;

public class LogUtils {
	private final static boolean isDebug = true;
	private final static String VANXUAN = "ShareDJ";

	public static void debug(String msg) {
		if (isDebug) {
			Log.d(VANXUAN, msg);
		}
	}

	public static void error(String msg) {
		if (isDebug) {
			Log.e(VANXUAN, msg);
		}
	}

	public static void infor(String msg) {
		if (isDebug) {
			Log.i(VANXUAN, msg);
		}
	}
}
