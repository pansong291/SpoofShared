package pansong291.spoofshared;

import java.util.ArrayList;

public class BL
{
 //单例模式
 private static BL bl=new BL();
 private BL()
 {}

 public static BL getBL()
 {
  return bl;
 }

 //当前目录
 private String strCurrentPath="/storage";//"/storage";
 public String getCurrentPath(){return strCurrentPath;}
 public void addToCurrentPath(String s)
 {
  strCurrentPath=strCurrentPath+"/"+s;
 }
 public void removeLastFromCurrentPath()
 {
  if(strCurrentPath.length()>9)
   strCurrentPath=strCurrentPath.substring(0,strCurrentPath.lastIndexOf("/"));
 }
 public void setCurrentPath(String s)
 {
  strCurrentPath=s;
 }

}

