package com.xdqx.fileJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadLocalFile
{
  private String localFile;

  public ReadLocalFile(String localFile)
  {
    this.localFile = localFile;
  }

  public String getLocalFileContent(String unicode)
    throws Exception
  {
    File file = new File(this.localFile);
    if (!file.exists()) return "";
    String s = null;
    BufferedReader reader = null;
    StringBuffer sb = new StringBuffer();
    try {
      InputStreamReader streamReader = new InputStreamReader(new FileInputStream(this.localFile), unicode);
      reader = new BufferedReader(streamReader);
      s = reader.readLine();

      while (s != null)
      {
        sb.append(s);
        sb.append("\r\n");
        s = reader.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      reader.close();
    }
    return sb.toString();
  }

  public String getLocalFileLastLineContent()
    throws Exception
  {
    String lineString = null;
    BufferedReader reader = null;
    try {
      InputStreamReader streamReader = new InputStreamReader(new FileInputStream(this.localFile));
      reader = new BufferedReader(streamReader);
      String temp = reader.readLine();
      while (temp != null) {
        lineString = temp;
        temp = reader.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      reader.close();
    }
    return lineString;
  }
}