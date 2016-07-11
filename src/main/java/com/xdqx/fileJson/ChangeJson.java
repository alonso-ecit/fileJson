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

public class ChangeJson
{
  File file;

  public void Jsonfile(File file)
  {
    this.file = file;
    ReadLocalFile readLocalFile = new ReadLocalFile(file.getPath());
    JSONArray arr = new JSONArray();
    JSONArray arr_00 = new JSONArray();
    JSONArray arr_30 = new JSONArray();
    JSONArray arr_60 = new JSONArray();
    try
    {
      String content = readLocalFile.getLocalFileContent("GBK");

      String[] splitContent = content.split("DATA BEGIN");
      if (2 == splitContent.length) {
        String[] datas = splitContent[1].split("DATA END");
        if (2 == datas.length) {
          String data = datas[0];

          String[] codes = data.split("VALUE");
          int tag = 0;

          int i = 0; for (int length = codes.length; i < length; i++)
          {
            JSONObject featureObj = new JSONObject();
            featureObj.element("type", "Feature");
            JSONObject propertiesObj = new JSONObject();
            String code = codes[i];
            String[] split = code.split("\r\n");
            if (split.length > 1) {
              String[] properties = split[0].trim().split("\\s+");
              String type = split[1].trim().split("\\s+")[0];

              if (file.getName().substring(11, 22).equals("RADRWARNING")) {
                if ((properties.length == 1) && 
                  (!properties[0].equals("9999")))
                  propertiesObj.element("Grade", Integer.parseInt(properties[0]));
              }
              else
              {
                if ((type.equals("POLYGON")) && 
                  (!properties[0].equals("0")) && (!properties[0].equals("30")) && (!properties[0].equals("60")))
                {
                  continue;
                }

                if (properties.length == 7) {
                  if (!properties[0].equals("9999")) {
                    propertiesObj.element("F", Integer.parseInt(properties[0]));
                    tag = ((Integer)propertiesObj.get("F")).intValue();
                  }

                  if (!properties[1].equals("9999")) {
                    propertiesObj.element("X", properties[1]);
                  }

                  if (!properties[2].equals("9999")) {
                    propertiesObj.element("Y", properties[2]);
                  }

                  if (!properties[3].equals("9999")) {
                    propertiesObj.element("Z", properties[3]);
                  }

                  if (!properties[4].equals("9999")) {
                    propertiesObj.element("MR", properties[4]);
                  }

                  if (!properties[5].equals("9999")) {
                    propertiesObj.element("PA", properties[5]);
                  }

                  if (!properties[6].equals("9999")) {
                    propertiesObj.element("V", properties[6]);
                  }
                }
              }

              featureObj.element("properties", propertiesObj);
              JSONObject geometryObj = new JSONObject();
              JSONArray geoArr = new JSONArray();

              if (type.toLowerCase().equals("polygon")) {
                geometryObj.element("type", "Polygon");
                JSONArray tem = new JSONArray();

                int j = 2; for (int jLen = split.length; j < jLen; j++) {
                  if (!split[j].trim().equals(""))
                  {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };

                    tem.add(latlngs);
                  }
                }
                geoArr.add(tem);
                geometryObj.element("coordinates", geoArr);
              } else if (type.toLowerCase().equals("polyline")) {
                geometryObj.element("type", "LineString");

                int j = 2; for (int jLen = split.length; j < jLen; j++)
                  if (!split[j].trim().equals(""))
                  {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };
                    geoArr.add(latlngs);
                  }
                geometryObj.element("coordinates", geoArr);
              } else {
                geometryObj.element("type", "Point");

                int j = 2; for (int jLen = split.length; j < jLen; j++)
                  if (!split[j].trim().equals(""))
                  {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };
                    geometryObj.element("coordinates", latlngs);
                  }
              }
              featureObj.element("geometry", geometryObj);
              if (file.getName().substring(11, 16).equals("TITAN"))
              {
                if (tag == 0)
                  arr_00.add(featureObj);
                else if (tag == 30)
                  arr_30.add(featureObj);
                else
                  arr_60.add(featureObj);
              }
              else {
                arr.add(featureObj);
              }
            }
          }
        }

      }

      for (int index = 0; index < 3; index++) {
        JSONObject obj = new JSONObject();
        obj.element("type", "FeatureCollection");
        String fileName = "";

        if (index == 0) {
          obj.element("features", arr_00);
          fileName = "_F000M";
        } else if (index == 1) {
          obj.element("features", arr_30);
          fileName = "_F030M";
        } else {
          obj.element("features", arr_60);
          fileName = "_F060M";
        }
        if (!file.getName().substring(11, 16).equals("TITAN")) {
          obj.element("features", arr);
          fileName = "";
        }

        InputStream is = getClass().getResourceAsStream("/config");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        try
        {
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
          String a = "\\" + file.getName().toString();
          String b = file.toString();
          String c = b.replace(a, "");

          if (c.equals(jsonObject.get("input").toString())) {
            System.out.println("文件转换成功");
            String[] ff = file.getName().toString().split("_");
            String file1 = ff[0];
            String file2 = ff[2].substring(0, 4) + "-" + ff[2].substring(4, 6) + "-" + ff[2].substring(6, 8);
            String file4 = ff[1];

            contentToTxt(file, jsonObject.get("output").toString(), jsonObject.get("output").toString() + "\\" + file2 + "\\" + file1 + "\\" + file4 + "\\" + file.getName().replace(".txt", new StringBuilder(String.valueOf(fileName)).append(".json").toString()), obj.toString());
          }
        }
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void contentToTxt(File file3, String lujing, String filePath, String content)
  {
    try
    {
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
        if (file3.getName().substring(11, 16).equals("TITAN"))
          System.out.println("文件名已存在，无法保存，请重命名源文件！");
      }
      else {
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