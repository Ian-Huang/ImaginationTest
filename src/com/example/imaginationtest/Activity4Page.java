package com.example.imaginationtest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Activity4Page extends Activity {

	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_RedoButton;
	private Button Act4_SaveFileButton;
	private Button Act4_UndoButton;
	
	private String _filePath;
	protected boolean _taken;
	protected ImageView _image;
	protected static final String PHOTO_TAKEN	= "photo_taken";
	private Activity act4;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);
		
		Init();
	
	}
	
	//網址:http://stackoverflow.com/questions/10296711/androidtake-screenshot-and-share-it/10296881#10296881
	void Init()
	{
		act4 = this;
		
		// 設定存檔路徑
		//_filePath = Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg";
		_filePath = Environment.getDataDirectory().toString();
		
		//_filePath = Environment.getExternalStorageDirectory().toString() + "/" + ACCUWX.IMAGE_APPEND; 
		// 存檔Button
		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
		this.Act4_SaveFileButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				savePic(takeScreenShot(act4),_filePath);
				
			}
		});
	}
	
	
	private static Bitmap takeScreenShot(Activity activity)
	{
	    View view = activity.getWindow().getDecorView();
	    view.setDrawingCacheEnabled(true);
	    view.buildDrawingCache();
	    Bitmap b1 = view.getDrawingCache();
	    Rect frame = new Rect();
	    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	    int statusBarHeight = frame.top;
	    int width = activity.getWindowManager().getDefaultDisplay().getWidth();
	    int height = activity.getWindowManager().getDefaultDisplay().getHeight();

	    Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
	    view.destroyDrawingCache();
	    return b;
	}
	private static void savePic(Bitmap b, String strFileName)
	{
	    FileOutputStream fos = null;
	    try
	    {
	        fos = new FileOutputStream(strFileName);
	        if (null != fos)
	        {
	            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
	            fos.flush();
	            fos.close();
	        }
	    }
	    catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	

}
