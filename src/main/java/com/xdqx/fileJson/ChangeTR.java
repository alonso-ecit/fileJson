package com.xdqx.fileJson;

import java.io.File;

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
          featureObj.put("timeserial", shuju[1]);
          String[] filedescrible = properties[0].split("=");
          featureObj.put("filedescrible", filedescrible[1]);
          String[] contents = properties[8].split("=");
          featureObj.put("content", contents[1]);

          JSONArray tem = new JSONArray();
          String[] values = value[0].trim().split("\r\n");

          int i = 0; for (int length = (values.length + 1) / 2; i < length; i++) {
            JSONObject shujuzhi = new JSONObject();
            shujuzhi.put("value", values[(i * 2)]);
            tem.add(shujuzhi);
            featureObj.put("value", tem);
          }

          arr.add(featureObj);
          String rootOutPut = Utils.getPropertyByKey("trecindex.output");
          String outPutPath = rootOutPut + "/" + Utils.getRelativePath(file.getName());//此处根据文件名日期获取文件
          String fileName =  Utils.getOutputFileName(file.getName()) + ".json";
          ChangeJson.contentToTxt(file, outPutPath, fileName, arr.toString());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}