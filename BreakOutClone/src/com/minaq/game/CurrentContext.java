package com.minaq.game;

public class CurrentContext {

	private static boolean _initialized = false;
	private static int _deviceHeight = 0;
	private static int _deviceWidth = 0;
	
	public static synchronized void setInitialized(boolean initialized){
		_initialized = initialized;
	}
	
	public static synchronized boolean getInitialized(){
		return _initialized;
	}
	
	public static synchronized int getDeviceHeight(){
		return _deviceHeight;	
	}
	public static synchronized void setDeviceHeight(int deviceHeight){
		_deviceHeight = deviceHeight;
		
	}
	
	public static synchronized int getDeviceWidth(){
		return _deviceWidth;	
	}
	public static synchronized void setDeviceWidth(int deviceWidth){
		_deviceWidth = deviceWidth;	
	}
	
	public static synchronized void setDeviceDimensions(int deviceWidth, int deviceHeight){
		_deviceWidth = deviceWidth;	
		_deviceHeight = deviceHeight;
	}
	
}


