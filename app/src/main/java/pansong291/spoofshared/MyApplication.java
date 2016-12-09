package pansong291.spoofshared;

import android.util.Log;
import pansong291.spoofshared.ErrorActivity;
import pansong291.crash.CrashApplication;

public class MyApplication extends CrashApplication
{
 @Override
 public Class<?> getPackageActivity()
 {
  setShouldShowDeviceInfo(false);
  return ErrorActivity.class;
 }

}
