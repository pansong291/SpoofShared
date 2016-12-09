package pansong291.spoofshared;

import android.content.Intent;
import android.view.View;
import pansong291.spoofshared.BL;
import pansong291.spoofshared.R;
import android.provider.MediaStore;

public class MyOnClickListener implements View.OnClickListener
{
 MainActivity m;
 public MyOnClickListener(MainActivity n)
 {
  m=n;
 }

 @Override
 public void onClick(View p1)
 {
  switch(p1.getId())
  {
   case R.id.button_dialog_other:
    //调用其它文件选择器选择图片
    //使用startActivityForResult是为了获取用户选择的文件
    Intent it=new Intent(Intent.ACTION_PICK, 
                         MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    it.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"image/*");
    m.startActivityForResult(Intent.createChooser(it,"选择图片"),0);
   break;
   case R.id.linearlayout_dialog_goback:
    BL.getBL().removeLastFromCurrentPath();
    m.setListDataChange();
   break;
  }
 }

}

