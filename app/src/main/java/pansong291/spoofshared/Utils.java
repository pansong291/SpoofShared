package pansong291.spoofshared;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;

public class Utils
{

 //获取所有子目录
 public static ArrayList<String> getAllSonFolder(String s)
 {
  File f=new File(s);
  //文件过滤器，只有当其不是文件夹或图片文件时才被过滤掉
  FileFilter myFileFliter=new FileFilter()
  {
   @Override
   public boolean accept(File p1)
   {
    if(p1.isDirectory())return true;
    return isPicFile(p1.getName());
   }
  };
  ArrayList<String>arrayFiles=new ArrayList<String>();
  File[]files=f.listFiles(myFileFliter);
  if(files!=null)
  {
   for(int d=0;d<files.length;d++)
   {
    arrayFiles.add(files[d].getName());
   }
   //排序
   Collections.sort(arrayFiles);
  }
  return arrayFiles;
 }

 public static boolean isPicFile(String fn)
 {
  boolean isPic=false;
  String fnes=fn.substring(fn.lastIndexOf(".")+1,fn.length());
  switch(fnes)
  {
   case "jpg":
   case "jpeg":
   case "png":
   case "gif":
    isPic=true;
   break;
  }
  return isPic;
 }

 //从Uri中获取文件路径
 public static String getAbsolutePath(Activity c,Uri uri,String s) 
 {
  try{
   String [] proj={s};
   Cursor cursor=c.managedQuery(uri,
                                proj,  // Which columns to return
                                null,  // WHERE clause; which rows to return (all rows)
                                null,  // WHERE clause selection arguments (none)
                                null); // Order-by clause (ascending by name)
   int column_index=cursor.getColumnIndexOrThrow(proj[0]);
   cursor.moveToFirst();
   return cursor.getString(column_index);
  }catch(Exception e)
  {
   return uri.getPath();
  }
 }



}

