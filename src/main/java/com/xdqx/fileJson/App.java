package com.xdqx.fileJson;

public class App {
	public static void main(String[] args){
		System.out.println("监听启动");
		FileListener filelistener = new FileListener();
	    filelistener.startMonitor();
		
		//System.out.println(Utils.getRelativePath("Nowcasting_TRECINDEX_20160714232400_Z0025.txt"));
	}
}
