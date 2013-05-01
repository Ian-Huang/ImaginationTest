package com.example.imaginationtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Activity4Page extends Activity {

	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_RedoButton;
	private Button Act4_SaveFileButton;
	private Button Act4_LoadFileButton;
	private Button Act4_UndoButton;
	
	private EditText Act4_EditText01;
	private EditText Act4_EditText02;
	private ImageView Act4_ImageView01;
	private ImageView Act4_ImageView02;

	//總頁數
	private static int pageMaxCount = 14;
	//每頁的文字資訊
	private String[] EditText_Collection = new String[2*pageMaxCount];
	//每頁的圖片資訊
	public static Bitmap[] Bitmap_Collection = new Bitmap[pageMaxCount];
	
	
	//儲存畫面的Layout
	private View Layout;
	//儲存畫面的Bitmap
	private Bitmap bitmapLayout;
	//當前頁面編號
	private int CurrentPage = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);
		
		this.Layout = (View) findViewById(R.id.Act4_Layout);
		
		this.Act4_EditText01 =  (EditText) findViewById(R.id.Act4_editText01);
		this.Act4_EditText02 =  (EditText) findViewById(R.id.Act4_editText02);
		this.Act4_ImageView01 = (ImageView)findViewById(R.id.Act4_ImageView01);
		this.Act4_ImageView02 = (ImageView)findViewById(R.id.Act4_ImageView02);
		
		Init();
		Act4_PageUpdate();
	}
	
	
	void Init()
	{
		// 設定存檔Button回饋
		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
		this.Act4_SaveFileButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
	               Layout.setDrawingCacheEnabled(true);
	               bitmapLayout = Layout.getDrawingCache();
	               saveImage(bitmapLayout);
			}
		});
		/////////////////////////////////////////////////////////////////////////////
		
		// 設定讀檔Button回饋
		this.Act4_LoadFileButton = (Button) findViewById(R.id.Act4_StartButton);
		this.Act4_LoadFileButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		/////////////////////////////////////////////////////////////////////////////
		
		// 設定上一頁Button回饋
		this.Act4_PreviousPageButton = (Button) findViewById(R.id.Act4_PreviousPageButton);
		this.Act4_PreviousPageButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
					Act4_SaveEditText();
					if(CurrentPage>1)
						CurrentPage--;
					Act4_PageUpdate();
			}
		});
		/////////////////////////////////////////////////////////////////////////////
		
		// 設定下一頁Button回饋
		this.Act4_NextPageButton = (Button) findViewById(R.id.Act4_NextPageButton);
		this.Act4_NextPageButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Act4_SaveEditText();
				if(CurrentPage<pageMaxCount)
					CurrentPage++;
				Act4_PageUpdate();
			}
		});
		/////////////////////////////////////////////////////////////////////////////
		
		/*
		// 設定EditText01的回饋
		this.Act4_EditText01 = (EditText) findViewById(R.id.Act4_editText01);
		this.Act4_EditText01.addTextChangedListener(new  TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//當文字改變後儲存文字字串
			
				//EditText_Collection[(CurrentPage-1)*2 + 0] += Act4_EditText01.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

		});
		/////////////////////////////////////////////////////////////////////////////
		
		// 設定EditText02的回饋
		this.Act4_EditText02 = (EditText) findViewById(R.id.Act4_editText02);
		this.Act4_EditText02.addTextChangedListener(new  TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//當文字改變後儲存文字字串

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

		});
		/////////////////////////////////////////////////////////////////////////////
		 */
		 
	}
	
	
	//儲存圖片
	protected void saveImage(Bitmap bmScreen2) {
        // TODO Auto-generated method stub
		
		//設定外部儲存位置
        File publicFolder = Environment.getExternalStoragePublicDirectory("ImaginationTest");
        if(!publicFolder.exists())
        	publicFolder.mkdir();
        //以使用者人名當作資料夾名子
        File userNameFolder = new File(publicFolder, "神");
        if(!userNameFolder.exists())
        	userNameFolder.mkdir();
        //設定檔案名子
        File fileName = new File(userNameFolder, "DemoPicture.jpg");

        try {
        	OutputStream os = new FileOutputStream(fileName);
            bmScreen2.compress(Bitmap.CompressFormat.PNG,80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	
	private void Act4_SaveEditText()
	{
		EditText_Collection[(CurrentPage-1)*2 + 0] = Act4_EditText01.getText().toString();
		EditText_Collection[(CurrentPage-1)*2 + 1] = Act4_EditText02.getText().toString();
	}
	
	private void Act4_PageUpdate()
	{
		Act4_EditText01.setText(EditText_Collection[(CurrentPage-1)*2 + 0]);
		Act4_EditText02.setText(EditText_Collection[(CurrentPage-1)*2 + 1]);
		Act4_ImageView01.setImageResource(R.drawable.action401 +(CurrentPage-1) *2 + 0);
		Act4_ImageView02.setImageResource(R.drawable.action401 +(CurrentPage-1) *2 + 1);
	}
	
	

	
	/*
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
	
*/
}
