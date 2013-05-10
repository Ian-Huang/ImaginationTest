package com.example.imaginationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2IntroductionPage extends Activity {

	private Button startTestButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2introduction_page);

		this.Init();
	}

	// 初始化設定
	void Init() {

		this.startTestButton = (Button) findViewById(R.id.act2Introduction_StartTestButton);

		// 設定"開始測驗"Button Listener
		this.startTestButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動二");
		// 設定對話框內容
		MyAlertDialog.setMessage("五分鐘計時開始！！");

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity2IntroductionPage.this,
						Activity2Page.class);
				startActivity(intent);
				Activity2IntroductionPage.this.finish();
				//System.exit(0);
			}
		};
		DialogInterface.OnClickListener CancelClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 關閉對話方塊
			}
		};

		MyAlertDialog.setPositiveButton("確定", OkClick);
		MyAlertDialog.setNegativeButton("取消", CancelClick);

		MyAlertDialog.show();
	}
}
