package com.xdqx.fileJson;

/*
 * 文件监听  分别监听config.properties配置文件中三种文件产品 input为监听路径  output为输出路径
 * */
public class FileListener {
  public void startMonitor() {
        ListenerMean titanMonitor;
		try {
			titanMonitor = new ListenerMean(60000L);
			titanMonitor.monitor(Utils.getPropertyByKey("titan.input"), new ListenerStyle());
			titanMonitor.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
          
        ListenerMean radarwaringMonitor;
		try {
			radarwaringMonitor = new ListenerMean(6000L);
			radarwaringMonitor.monitor(Utils.getPropertyByKey("radarwaring.input"), new ListenerStyle());
			radarwaringMonitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        ListenerMean trecindexMonitor;
		try {
			trecindexMonitor = new ListenerMean(5000L);
			trecindexMonitor.monitor(Utils.getPropertyByKey("trecindex.input"), new ListenerStyle());
			trecindexMonitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}