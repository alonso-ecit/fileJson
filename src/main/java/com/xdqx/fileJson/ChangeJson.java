package com.xdqx.fileJson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChangeJson
{
  File file;

  public void Jsonfile(File file) {
    this.file = file;
    ReadLocalFile readLocalFile = new ReadLocalFile(file.getPath());
    JSONArray arr = new JSONArray();
    JSONArray arr_00 = new JSONArray();
    JSONArray arr_30 = new JSONArray();
    JSONArray arr_60 = new JSONArray();
    try {
      String content = readLocalFile.getLocalFileContent("GBK");

      String[] splitContent = content.split("DATA BEGIN");
      if (2 == splitContent.length) {
        String[] datas = splitContent[1].split("DATA END");
        if (2 == datas.length) {
          String data = datas[0];

          String[] codes = data.split("VALUE");
          int tag = 0;

          int i = 0; 
          for (int length = codes.length; i < length; i++) {
            JSONObject featureObj = new JSONObject();
            featureObj.put("type", "Feature");
            JSONObject propertiesObj = new JSONObject();
            String code = codes[i];
            String[] split = code.split("\r\n");
            if (split.length > 1) {
              String[] properties = split[0].trim().split("\\s+");
              String type = split[1].trim().split("\\s+")[0];

              if (file.getName().substring(11, 22).equals("RADRWARNING")) {
                if ((properties.length == 1) && (!properties[0].equals("9999"))){
                	propertiesObj.put("Grade", Integer.parseInt(properties[0]));
                }
              } else {
                if ((type.equals("POLYGON")) && (!properties[0].equals("0")) && (!properties[0].equals("30")) && (!properties[0].equals("60"))) {
                  continue;
                }

                if (properties.length == 7) {
                  if (!properties[0].equals("9999")) {
                    propertiesObj.put("F", Integer.parseInt(properties[0]));
                    tag = ((Integer)propertiesObj.get("F")).intValue();
                  }

                  if (!properties[1].equals("9999")) {
                    propertiesObj.put("X", properties[1]);
                  }

                  if (!properties[2].equals("9999")) {
                    propertiesObj.put("Y", properties[2]);
                  }

                  if (!properties[3].equals("9999")) {
                    propertiesObj.put("Z", properties[3]);
                  }

                  if (!properties[4].equals("9999")) {
                    propertiesObj.put("MR", properties[4]);
                  }

                  if (!properties[5].equals("9999")) {
                    propertiesObj.put("PA", properties[5]);
                  }

                  if (!properties[6].equals("9999")) {
                    propertiesObj.put("V", properties[6]);
                  }
                }
              }

              featureObj.put("properties", propertiesObj);
              JSONObject geometryObj = new JSONObject();
              JSONArray geoArr = new JSONArray();

              if (type.toLowerCase().equals("polygon")) {
                geometryObj.put("type", "Polygon");
                JSONArray tem = new JSONArray();

                int j = 2; for (int jLen = split.length; j < jLen; j++) {
                  if (!split[j].trim().equals("")) {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };

                    tem.add(latlngs);
                  }
                }
                
                geoArr.add(tem);
                geometryObj.put("coordinates", geoArr);
              } else if (type.toLowerCase().equals("polyline")) {
                geometryObj.put("type", "LineString");

                int j = 2; 
                for (int jLen = split.length; j < jLen; j++)
                  if (!split[j].trim().equals("")) {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };
                    geoArr.add(latlngs);
                  }
                geometryObj.put("coordinates", geoArr);
              } else {
                geometryObj.put("type", "Point");

                int j = 2; for (int jLen = split.length; j < jLen; j++)
                  if (!split[j].trim().equals("")) {
                    String[] latlngSplit = split[j].trim().split("\\s+");
                    double latlngSplitx = Double.parseDouble(latlngSplit[0]);
                    double latlngSplity = Double.parseDouble(latlngSplit[1]);
                    double[] latlngs = { latlngSplitx, latlngSplity };
                    geometryObj.put("coordinates", latlngs);
                  }
              }
              
              featureObj.put("geometry", geometryObj);
              if (file.getName().substring(11, 16).equals("TITAN")) {
                if (tag == 0) {
                	arr_00.add(featureObj);
                } else if (tag == 30) {
                	arr_30.add(featureObj);
                } else {
                	arr_60.add(featureObj);
                }
              } else {
                arr.add(featureObj);
              }
            }
          }
        }
      }

      for (int index = 0; index < 3; index++) {
        JSONObject obj = new JSONObject();
        obj.put("type", "FeatureCollection");
        String suffix = "";
        String rootOutPut = "";//输出文件根路径
        //文件为titan 将titan拆分为00时效 30分钟时效  1小时时效
        if (index == 0) {
          obj.put("features", arr_00);
          suffix = "_F000M";
          rootOutPut = Utils.getPropertyByKey("titan.output");
        } else if (index == 1) {
          obj.put("features", arr_30);
          suffix = "_F030M";
          rootOutPut = Utils.getPropertyByKey("titan.output");
        } else {
          obj.put("features", arr_60);
          suffix = "_F060M";
          rootOutPut = Utils.getPropertyByKey("titan.output");
        }
        
        //文件为radarwarning
        if (!file.getName().substring(11, 16).equals("TITAN")) {
          obj.put("features", arr);
          suffix = "";
          rootOutPut = Utils.getPropertyByKey("radarwaring.output");
        }
        
        String outPutPath = rootOutPut + "/" + Utils.getRelativePath(file.getName());//此处根据文件名日期获取文件
        String fileName = file.getName().replace(".txt", "") + suffix + ".json";
        System.out.println(obj.toString());
        //输出geojson文件
        contentToTxt(file, outPutPath, fileName, obj.toString());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void contentToTxt(File file, String outPutPath, String fileName, String content) {
	  try {
      Utils.createFoldByPath(outPutPath);//判断文件路径是否存在 不存在则创建路径
      fileName = outPutPath + fileName;
      BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
      output.write(content);
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}