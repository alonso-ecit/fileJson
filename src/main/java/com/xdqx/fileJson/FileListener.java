package com.xdqx.fileJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileListener
{
  private String addss;
  private static String path;

  public void getResource()
  {
    InputStream is = getClass().getResourceAsStream("/config");
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String s = "";
    try {
      s = br.readLine();
    }
    catch (IOException e)
    {
      String shuju;
      e.printStackTrace();
    }

    JSONArray jsonArray = JSONArray.fromObject(s);
    int size = jsonArray.size();
    for (int i = 0; i < size; i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      this.addss = jsonObject.getString("input").toString();
      String type = jsonObject.getString("type").toString();
      try
      {
        if (type.equals("TITAN")) {
          ListenerMean m1 = new ListenerMean(60000L);
          m1.monitor(this.addss, new ListenerStyle());
          m1.start();
        } else if (type.equals("RADRWARNING")) {
          ListenerMean m2 = new ListenerMean(6000L);
          m2.monitor(this.addss, new ListenerStyle());
          m2.start();
        } else if (type.equals("TRECINDEX")) {
          ListenerMean m3 = new ListenerMean(5000L);
          m3.monitor(this.addss, new ListenerStyle());
          m3.start();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args)
  {
    FileListener filelistener = new FileListener();
    filelistener.getResource();
  }
}