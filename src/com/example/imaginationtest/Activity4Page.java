package com.example.imaginationtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity4Page extends Activity {

	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_RedoButton;
	private Button Act4_UndoButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_SaveFileButton;
	private Button Act4_NextActivity;
	
	private TextView Act4_Timer;
	private EditText Act4_EditText01;
	private EditText Act4_EditText02;
	private ImageView Act4_ImageView01;
	private ImageView Act4_ImageView02;
	
	private long Countdown_Time = 600; // 倒數計時總時間 ( 單位:秒)

	// 總頁數
	private static int pageMaxCount = 28;
	// 每頁的文字資訊
	private String[] EditText_Collection = new String[2 * pageMaxCount];
	// 每頁的圖片資訊
	public static Bitmap[] Bitmap_Collection = new Bitmap[pageMaxCount];

	// 儲存畫面的Layout
	private View Layout;
	// 儲存畫面的Bitmap
	private Bitmap bitmapLayout;
	// 當前頁面編號
	private int CurrentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);

		this.Layout = (View) findViewById(R.id.Act4_Layout);

		this.Act4_Timer = (TextView) findViewById(R.id.Act4_Timer);
		this.Act4_EditText01 = (EditText) findViewById(R.id.Act4_editText01);
		this.Act4_EditText02 = (EditText) findViewById(R.id.Act4_editText02);
		this.Act4_ImageView01 = (ImageView) findViewById(R.id.Act4_ImageView01);
		this.Act4_ImageView02 = (ImageView) findViewById(R.id.Act4_ImageView02);

		Init();
		Act4_PageUpdate();
		this.StartCountDownTimer();
	}

	void Init() {
		// 設定存檔Button回饋
		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
		this.Act4_SaveFileButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Layout.setDrawingCacheEnabled(true);
						bitmapLayout = Layout.getDrawingCache();
						saveImage(bitmapLayout);
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定上一頁Button回饋
		this.Act4_PreviousPageButton = (Button) findViewById(R.id.Act4_PreviousPageButton);
		this.Act4_PreviousPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Act4_SaveEditText();
						if (CurrentPage > 1)
							CurrentPage--;
						Act4_PageUpdate();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定下一頁Button回饋
		this.Act4_NextPageButton = (Button) findViewById(R.id.Act4_NextPageButton);
		this.Act4_NextPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Act4_SaveEditText();
						if (CurrentPage < pageMaxCount)
							CurrentPage++;
						Act4_PageUpdate();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// 設定下一步Button回饋
		this.Act4_NextActivity = (Button) findViewById(R.id.Act4_NextActivity);
		this.Act4_NextActivity
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowMsgDialog();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// 設定清除Button回饋
		this.Act4_ClearButton = (Button) findViewById(R.id.Act4_ClearButton);
		this.Act4_ClearButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// 設定橡皮擦Button回饋
		this.Act4_EraserButton = (Button) findViewById(R.id.Act4_EraserButton);
		this.Act4_EraserButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// 設定重做Button回饋
		this.Act4_RedoButton = (Button) findViewById(R.id.Act4_RedoButton);
		this.Act4_RedoButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// 設定還原Button回饋
		this.Act4_UndoButton = (Button) findViewById(R.id.Act4_UndoButton);
		this.Act4_UndoButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

	}

	// 儲存圖片
	protected void saveImage(Bitmap bmScreen2) {
		// TODO Auto-generated method stub

		// 設定外部儲存位置
		File publicFolder = Environment
				.getExternalStoragePublicDirectory("ImaginationTest");
		if (!publicFolder.exists())
			publicFolder.mkdir();
		// 以使用者人名當作資料夾名子
		File userNameFolder = new File(publicFolder, "users");
		if (!userNameFolder.exists())
			userNameFolder.mkdir();
		// 設定檔案名子

		File fileName = new File(userNameFolder, "Act4_Page" + CurrentPage
				+ ".jpg");

		try {
			OutputStream os = new FileOutputStream(fileName);
			bmScreen2.compress(Bitmap.CompressFormat.PNG, 80, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void Act4_SaveEditText() {
		EditText_Collection[(CurrentPage - 1) * 2 + 0] = Act4_EditText01
				.getText().toString();
		EditText_Collection[(CurrentPage - 1) * 2 + 1] = Act4_EditText02
				.getText().toString();
	}

	private void Act4_PageUpdate() {
		Act4_EditText01.setText(EditText_Collection[(CurrentPage - 1) * 2 + 0]);
		Act4_EditText02.setText(EditText_Collection[(CurrentPage - 1) * 2 + 1]);
		Act4_ImageView01.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 0);
		Act4_ImageView02.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 1);
		
		if(CurrentPage == 1)
			this.Act4_PreviousPageButton.setEnabled(false);
		else
			this.Act4_PreviousPageButton.setEnabled(true);
		if(CurrentPage == 28)
			this.Act4_NextPageButton.setEnabled(false);
		else
			this.Act4_NextPageButton.setEnabled(true);
	}

	
	// 計時器設定
	void StartCountDownTimer() {

		new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// 時間到後提示進入下一頁
				Act4_Timer.setText("剩餘時間    00：00");
				ShowMsgDialog();
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				Act4_Timer.setText("剩餘時間    "
						+ String.format("%02d", totalSec / 60) + "："
						+ String.format("%02d", totalSec % 60));
			}
		}.start();
	}
	
	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動四");
		// 設定對話框內容
		MyAlertDialog.setMessage("時間到  停止作答！！");

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity4Page.this,
						Activity5Page.class);
				startActivity(intent);
				System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("確定", OkClick);

		MyAlertDialog.show();
	}
}