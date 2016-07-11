package com.xdqx.fileJson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChangeTR
{
  File file;

  public void Jsonfile(File file)
  {
    this.file = file;
    ReadLocalFile readLocalFile = new ReadLocalFile(file.getPath());
    JSONArray arr = new JSONArray();
    try {
      String content = readLocalFile.getLocalFileContent("GBK");
      String[] splitContent = content.split("DATA BEGIN");
      if (2 == splitContent.length) {
        String[] top = splitContent[0].split("TAB");
        String[] value = splitContent[1].split("DATA END");
        if (2 == top.length) {
          String[] metadata = top[0].split("METADATA");
          String[] metadatalist = metadata[1].split("\r\n");

          String[] properties = metadatalist[1].trim().split(",");

          JSONObject featureObj = new JSONObject();
          String[] shuju = properties[9].split("=");
          featureObj.element("timeserial", shuju[1]);
          String[] filedescrible = properties[0].split("=");
          featureObj.element("filedescrible", filedescrible[1]);
          String[] contents = properties[8].split("=");
          featureObj.element("content", contents[1]);

          JSONArray tem = new JSONArray();
          String[] values = value[0].trim().split("\r\n");

          int i = 0; for (int length = (values.length + 1) / 2; i < length; i++) {
            JSONObject shujuzhi = new JSONObject();
            shujuzhi.element("value", values[(i * 2)]);
            tem.add(shujuzhi);
            featureObj.element("value", tem);
          }

          arr.add(featureObj);

          InputStream is = getClass().getResourceAsStream("/config");
          BufferedReader br = new BufferedReader(new InputStreamReader(is));
          String s = "";
          try
          {
            s = br.readLine();
          }
          catch (IOException e)
          {
            String shujus;
            e.printStackTrace();
          }

          JSONArray jsonArray = JSONArray.fromObject(s);
          int size = jsonArray.size();
          for (int i1 = 0; i1 < size; i1++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i1);
            String a = "\\" + file.getName().toString();
            String b = file.toString();
            String c = b.replace(a, "");

            if (c.equals(jsonObject.get("input").toString())) {
              System.out.println("文件转换成功");
              String[] ff = file.getName().toString().split("_");
              String file1 = ff[0];
              String file2 = ff[2].substring(0, 4) + "-" + ff[2].substring(4, 6) + "-" + ff[2].substring(6, 8);
              String file4 = ff[1];
              contentToTxt(file, jsonObject.get("output").toString(), jsonObject.get("output").toString() + "\\" + file2 + "\\" + file1 + "\\" + file4 + "\\" + file.getName().replace(".txt", ".json"), arr.toString());
            }
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void contentToTxt(File file3, String lujing, String filePath, String content) {
    try {
      File f = new File(filePath);
      String[] ff = file3.getName().toString().split("_");
      String file1 = ff[0];
      String file2 = ff[2].substring(0, 4) + "-" + ff[2].substring(4, 6) + "-" + ff[2].substring(6, 8);
      String file4 = ff[1];
      File swf2 = new File(lujing + "\\" + file2);
      File swf3 = new File(lujing + "\\" + file2 + "\\" + file1);
      File swf4 = new File(lujing + "\\" + file2 + "\\" + file1 + "\\" + file4);
      if ((!swf2.exists()) && (!swf2.isDirectory())) {
        swf2.mkdir();
      }

      if ((!swf3.exists()) && (!swf3.isDirectory())) {
        swf3.mkdir();
      }

      if ((!swf4.exists()) && (!swf4.isDirectory())) {
        swf4.mkdir();
      }

      if (f.exists()) {
        System.out.println("文件名已存在，无法保存，请重命名源文件！");
      } else {
        System.out.println("文件已保存！");
        f.createNewFile();
      }
      BufferedWriter output = new BufferedWriter(new FileWriter(f));

      output.write(content);
      output.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}