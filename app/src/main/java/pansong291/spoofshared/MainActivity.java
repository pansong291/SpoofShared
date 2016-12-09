package pansong291.spoofshared;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.File;
import pansong291.spoofshared.BL;
import pansong291.spoofshared.R;

public class MainActivity extends Zactivity 
{
 //main界面视图
 EditText edt_title,edt_summary,edt_shared_url,edt_inter_pic;
 RadioGroup rdg_shared_pic;
 RadioButton rdbn_local_pic,rdbn_inter_pic;
 TextView txt_local_pic;
 
 //对话框里的视图
 TextView txt_current_path;
 View view_dialog,view_goBack,view_other;
 AlertDialog dialog_list;
 ListView list_path;
 ListFolderAdapter listAdapter;
 MyOnClickListener myOnClickListener;
 
 static String mAppid;
 static Tencent mTencent;
 @Override
 protected void onCreate(Bundle savedInstanceState)
 {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.main);
  
  init();
  if(TextUtils.isEmpty(mAppid))
  {
   mAppid="1105800191";
   mTencent=Tencent.createInstance(mAppid,this);
  }else if(mTencent==null)
  {
   mTencent=Tencent.createInstance(mAppid,this);
  }
 }
 
 private void init()
 {
  edt_title=(EditText)findViewById(R.id.edt_main_shared_title);
  edt_summary=(EditText)findViewById(R.id.edt_main_shared_summary);
  edt_shared_url=(EditText)findViewById(R.id.edt_main_shared_url);
  edt_inter_pic=(EditText)findViewById(R.id.edt_main_shared_inter_pic);
  rdg_shared_pic=(RadioGroup)findViewById(R.id.rdg_main_shared_pic);
  rdbn_local_pic=(RadioButton)findViewById(R.id.rdbn_main_local_pic);
  rdbn_inter_pic=(RadioButton)findViewById(R.id.rdbn_main_inter_pic);
  txt_local_pic=(TextView)findViewById(R.id.txt_main_shared_local_pic);
    
  rdg_shared_pic.setOnCheckedChangeListener(new OnCheckedChangeListener()
   {
    @Override
    public void onCheckedChanged(RadioGroup p1,int p2)
    {
     switch(p2)
     {
      case R.id.rdbn_main_local_pic:
       txt_local_pic.setVisibility(0);
       edt_inter_pic.setVisibility(8);
      break;
      case R.id.rdbn_main_inter_pic:
       txt_local_pic.setVisibility(8);
       edt_inter_pic.setVisibility(0);
      break;
     }
    }
  });

  edt_title.setText(getSavedString("strTitle",""));
  edt_summary.setText(getSavedString("strSummary",""));
  edt_shared_url.setText(getSavedString("sttUrl",""));
  BL.getBL().setCurrentPath(getSavedString("strCurrentPath",BL.getBL().getCurrentPath()));
  if(getSavedInt("rdbn",0)==0)
  {
   rdbn_local_pic.setChecked(true);
   txt_local_pic.setText(getSavedString("strLocalPic","点此选择本地图片"));
  }else
  {
   rdbn_inter_pic.setChecked(true);
   edt_inter_pic.setText(getSavedString("strInterPic",""));
  }
  
  view_dialog=LayoutInflater.from(getApplication()).inflate(R.layout.dialog_choose_folder,null);
  list_path=(ListView)view_dialog.findViewById(R.id.listview_dialog_folder);
  txt_current_path=(TextView)view_dialog.findViewById(R.id.textview_dialog_current_folder);
  view_goBack=view_dialog.findViewById(R.id.linearlayout_dialog_goback);
  view_other=view_dialog.findViewById(R.id.button_dialog_other);
  
  myOnClickListener=new MyOnClickListener(this);
  view_goBack.setOnClickListener(myOnClickListener);
  view_other.setOnClickListener(myOnClickListener);

  listAdapter=new ListFolderAdapter(this);
  list_path.setAdapter(listAdapter);
  list_path.setOnItemClickListener(new AdapterView.OnItemClickListener()
   {
    @Override
    public void onItemClick(AdapterView<?> p1,View p2,int p3,long p4)
    {
     String fn=listAdapter.getFolderInfo(p3);
     File f=new File(BL.getBL().getCurrentPath()+"/"+fn);
     if(f.isDirectory())
     {
      BL.getBL().addToCurrentPath(fn);
      setListDataChange();
     }else
     {
      txt_local_pic.setText(f.getAbsolutePath());
      dialog_list.dismiss();
     }
    }
   });

  dialog_list=new AlertDialog.Builder(this)
   .setTitle("选择图片")
   .setView(view_dialog)
   .setNegativeButton("取消",null)
   .create();
  
 }
 
 public void onShared2QQClick(View v)
 {
  String strTitle,strSummary,strUrl,strPic;
  strTitle=edt_title.getText().toString();
  strSummary=edt_summary.getText().toString();
  strUrl=edt_shared_url.getText().toString();
  strPic=txt_local_pic.getText().toString();
  strPic=rdbn_local_pic.isChecked()?
   strPic.endsWith("图片")?"":strPic:
   edt_inter_pic.getText().toString();
  if(TextUtils.isEmpty(strTitle)||TextUtils.isEmpty(strUrl))
  {
   toast("标题和跳转链接为必填");
   return;
  }
  
  saveString("strTitle",strTitle);
  saveString("strSummary",strSummary);
  saveString("sttUrl",strUrl);
  saveString("strCurrentPath",BL.getBL().getCurrentPath());
  if(rdbn_local_pic.isChecked())
  {
   saveInt("rdbn",0);
   saveString("strLocalPic",strPic);
  }else if(rdbn_inter_pic.isChecked())
  {
   saveInt("rdbn",1);
   saveString("strInterPic",strPic);
  }
  
  final Bundle params=new Bundle();
  params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
  params.putString(QQShare.SHARE_TO_QQ_TITLE,strTitle);
  params.putString(QQShare.SHARE_TO_QQ_SUMMARY,strSummary);
  params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,strUrl);
  params.putString(rdbn_local_pic.isChecked()?QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL:QQShare.SHARE_TO_QQ_IMAGE_URL,strPic);
  params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"应用名称222222");
  params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
  try{
   doShareToQQ(params);
  }catch(Exception e){edt_inter_pic.setText(e.toString());}
 }
 
 private void doShareToQQ(final Bundle params)
 {
  // QQ分享要在主线程做
  ThreadManager.getMainHandler().post(new Runnable()
  {
    @Override
    public void run()
    {
     if(null!=mTencent)
      {
      MainActivity.mTencent.shareToQQ(MainActivity.this,params,new IUiListener()
       {
        public void onComplete(Object p1){}
        public void onError(UiError p1){}
        public void onCancel(){}
       }); 
     }
    }
   });
 }
 
 public void onChooseLocalPicClick(View v)
 {
  dialog_list.show();
  setListDataChange();
 }
 
 //重写onActivityResult以获得你需要的信息
 @Override
 protected void onActivityResult(int requestCode,int resultCode,Intent data)
 {
  super.onActivityResult(requestCode,resultCode,data);
  //此处的requestCode用于判断接收的Activity是不是你想要的那个
  if(resultCode==RESULT_OK&&requestCode==0)
  {
   //toast("path="+data.getData().getPath()+"\ntoString="+data.getData().toString()+"\nEncodedPath="+data.getData().getEncodedPath(),1);
   //获得图片的路径
   txt_local_pic.setText(Utils.getAbsolutePath(this,data.getData(),MediaStore.Images.Media.DATA));
   dialog_list.dismiss();
  }else if(resultCode==RESULT_OK)
  {
   toast("请重新选择图片");
  }
 }
 
 public void setListDataChange()
 {
  listAdapter.setFolderInfo(Utils.getAllSonFolder(BL.getBL().getCurrentPath()));
  listAdapter.notifyDataSetChanged();
  txt_current_path.setText(BL.getBL().getCurrentPath());
 }
 
 protected void onDestroy()
 {
  super.onDestroy();
  if(mTencent!=null)
  {
   mTencent.releaseResource();
  }
 }
 
}
