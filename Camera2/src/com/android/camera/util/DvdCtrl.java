package com.android.camera.util;

public class DvdCtrl {
	    static {
	        System.loadLibrary("dvd_ctrl");
	    }
	  static public native int StartPause();
	  static public native int Pre();
	  static public native int Next();
          static public native int Repeat();
          static public native int Menu();
	  static public native int SetVol(int vol);
}
