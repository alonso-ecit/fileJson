package com.xdqx.fileJson;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ListenerMean
{
  FileAlterationMonitor monitor = null;

  public ListenerMean(long interval, String path){ 
	  FileAlterationObserver  observer = new FileAlterationObserver(path);
	  observer.addListener(new ListenerStyle());
	  this.monitor = new FileAlterationMonitor(interval, observer); 
  }

  public void stop() throws Exception {
    this.monitor.stop();
  }

  public void start() throws Exception {
    this.monitor.start();
  }
}