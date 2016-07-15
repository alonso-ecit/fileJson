package com.xdqx.fileJson;

import org.apache.commons.io.monitor.FileAlterationListener;

/*
 * 文件监听  分别监听config.properties配置文件中三种文件产品 input为监听路径  output为输出路径
 * */
public class FileListener{
  public void startMonitor() {
        ListenerMean titanMonitor;
		try {
			titanMonitor = new ListenerMean(60000L, Utils.getPropertyByKey("titan.input"));
			titanMonitor.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
          
        ListenerMean radarwaringMonitor;
		try {
			radarwaringMonitor = new ListenerMean(6000L, Utils.getPropertyByKey("radarwaring.input"));
			radarwaringMonitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        ListenerMean trecindexMonitor;
		try {
			trecindexMonitor = new ListenerMean(5000L, Utils.getPropertyByKey("trecindex.input"));
			trecindexMonitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}