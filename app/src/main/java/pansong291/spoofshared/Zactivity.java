package pansong291.spoofshared;

import android.app.Activity;
import android.os.Bundle;
import pansong291.crash.ActivityControl;
import android.widget.Toast;
import android.content.SharedPreferences;

public class Zactivity extends Activity
{
 
 SharedPreferences Zsp;
 SharedPreferences.Editor Zspedt;
 
 @Override
 protected void onResume()
 {
  super.onResume();

 }

 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  ActivityControl.getActivityControl().addActivity(this);
  
  Zsp=getDefaultSharedPreferences();
  Zspedt=Zsp.edit();
  
 }
 
 protected boolean saveString(String s1,String s2)
 {
  return Zspedt.putString(s1,s2).commit();
 }
 
 protected String getSavedString(String s1,String s2)
 {
  return Zsp.getString(s1,s2);
 }
 
 protected boolean saveInt(String s,int i)
 {
  return Zspedt.putInt(s,i).commit();
 }

 protected int getSavedInt(String s,int i)
 {
  return Zsp.getInt(s,i);
 }
 
 private SharedPreferences getDefaultSharedPreferences()
 {
  return getSharedPreferences(getPackageName()+"_preferences",0);
 }
 
 @Override
 protected void onDestroy()
 {
  super.onDestroy();
  ActivityControl.getActivityControl().removeActivity(this);
 }

 public void toast(String s)
 {
  toast(s,0);
 }

 public void toast(String s,int i)
 {
  Toast.makeText(this,s,i).show();
 }

}

