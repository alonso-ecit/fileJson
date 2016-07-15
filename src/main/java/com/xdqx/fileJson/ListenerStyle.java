package com.xdqx.fileJson;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ListenerStyle implements FileAlterationListener {
  ListenerMean monitor = null;

  public void onStart(FileAlterationObserver observer) {
	  
  }

  public void onDirectoryCreate(File directory){
	  
  }

  public void onDirectoryChange(File directory){
	  
  }

  public void onDirectoryDelete(File directory) {
	  
  }

  public void onFileCreate(File file) {
    System.out.println("加入文件:" + file.getAbsolutePath());

    if (file.getName().trim().toLowerCase().endsWith(".txt")) {
      if (file.getName().contains("TRECINDEX")) {
        ChangeTR changetr = new ChangeTR();
        changetr.Jsonfile(file);
      } else {
        ChangeJson changejson = new ChangeJson();
        changejson.Jsonfile(file);
      }
    } else {
    	System.out.println("此文件格式不正确！！");
    } 
  }

  public void onFileChange(File file) {
	  
  }

  public void onFileDelete(File file) {
	  
  }

  public void onStop(FileAlterationObserver observer) {
	  
  }
}