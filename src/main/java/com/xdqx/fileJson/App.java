package com.xdqx.fileJson;

public class App {
	public static void main(String[] args){
		System.out.println("监听启动");
		FileListener filelistener = new FileListener();
	    filelistener.startMonitor();
		
		//System.out.println(Utils.getRelativePath("Nowcasting_TITAN_20160715000600_Z0025.txt"));
	}
}
