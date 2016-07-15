package com.xdqx.fileJson;

import java.io.File;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ListenerMean
{
  FileAlterationMonitor monitor = null;

  public ListenerMean(long interval) throws Exception { 
	  this.monitor = new FileAlterationMonitor(interval);
  }

  public void monitor(String path, FileAlterationListener listener)
  {
    FileAlterationObserver observer = new FileAlterationObserver(new File(path));
    this.monitor.addObserver(observer);
    observer.addListener(listener);
  }

  public void stop() throws Exception {
    this.monitor.stop();
  }

  public void start() throws Exception {
    this.monitor.start();
  }
}