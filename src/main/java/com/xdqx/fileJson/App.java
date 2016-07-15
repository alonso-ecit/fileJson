package com.xdqx.fileJson;

public class App {
	public static void main(String[] args){
		System.out.println("监听启动");
		
		FileListener filelistener = new FileListener();
	    filelistener.startMonitor();
	}
}
