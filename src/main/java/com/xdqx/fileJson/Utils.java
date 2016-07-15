package com.xdqx.fileJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
  public static String ReadFile(String Path) {
    BufferedReader reader = null;
    String laststr = "";
    try {
      FileInputStream fileInputStream = new FileInputStream(Path);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
      reader = new BufferedReader(inputStreamReader);
      String tempString = null;

      while ((tempString = reader.readLine()) != null) {
        laststr = laststr + tempString + "\r\n";
      }

      reader.close();
    } catch (IOException e) {
      e.printStackTrace();

      if (reader != null)
        try {
          reader.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return laststr;
  }
  
  public static String getPropertyByKey(String key){
	  String configPath = getPropertyPath();
	  if(configPath != null){
		  String content = ReadFile(configPath);
		  String[] split = content.split("\r\n");//换行分隔
		  for(int i=0, len=split.length; i<len; i++){
			  if(split[i].contains("=") && split[i].contains(key)){
				  return split[i].split("=")[1];//根据key返回value
			  }
		  }
	  } 
	  
	  return null;
  }
  
  /*
   * 获取config.properties文件位置
   * */
  public static String getPropertyPath(){
	  String path = Utils.class.getResource("/").getFile();
	  String[] split = path.split("/");
	  String p = "";
		
	  for(int i=0, len=split.length; i<len-2; i++){
		  if(!split[i].equals("")){
			  p += split[i] + "/";
		  }
	  }
	
	  String configPath = p + "config.properties";
	  File file = new File(configPath);
	  if(file.exists()){
		  return configPath;
	  }else{
		  return null;
	  }
  }
  
  /*
   * Nowcasting_TITAN_20160707073600_Z9519.txt
   * 根据文件名拼接文件路径
   * */
  public static String getRelativePath(String fileName){
	  String[] split = fileName.replace(".txt", "").split("_");
	  String relativePath = "";
	  String mosaicSite = getPropertyByKey("mosaicSite");//拼图站点名称
	  for(int i=0, len=split.length; i<len; i++){
		  if(split[i].matches("^[0-9]*$")){//过滤文件名中全数字文件路径
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
				Date date = sdf.parse(split[i]);
				String mmdd = new SimpleDateFormat("MM-dd").format(date);
				relativePath = mmdd + "/" + relativePath;
				continue;
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		  }
		  
		  if(split[i].equals(mosaicSite)){//判断是否为单站还是主站，若为主站则不创建Z文件夹 否则创建Z文件夹
			//主站,不创建文件夹
			continue;
		  }
		  relativePath += split[i] + "/";
	  }
	  
	  return relativePath;
  }
  
  /*
   * 根据文件路径判断文件夹是否存在，不存在则创建文件夹
   * */
  public static void createFoldByPath(String path){
	  String[] split = path.split("/");
	  String _path = "";
	  for(int i=0, len=split.length; i<len; i++){
		  _path += split[i] + "/";
		  //判断_path是否存在，不存在则创建文件夹
		  File file = new File(_path);
		  if (!file.exists() && !file.isDirectory()){       
		      file.mkdir();
		  }
	  }
  }
}