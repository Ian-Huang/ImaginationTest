package com.example.imaginationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity1Page extends Activity {

	private EditText[] answerEditText_Collection = new EditText[45];

	private Button nextActButton;

	private TextView timerTextView;
	private long Countdown_Time = 300; // 倒數計時總時間 ( 單位:秒)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1_page);

		this.Init();
		this.StartCountDownTimer();

	}

	// 初始化設定
	void Init() {
		// 讀所有answer的EditText ID
		for (int i = 0; i < answerEditText_Collection.length; i++) {
			this.answerEditText_Collection[i] = (EditText) findViewById(R.id.Act1answer_EditText01
					+ i);
		}
		// -----------------------------------
		this.timerTextView = (TextView) findViewById(R.id.Act1Timer_textView);
		this.nextActButton = (Button) findViewById(R.id.Act1_NextActivityButton);

		// 設定"下一步"Button Listener
		this.nextActButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// 計時器設定
	void StartCountDownTimer() {

		new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// 時間到後提示進入下一頁
				timerTextView.setText("剩餘時間    00：00");
				ShowMsgDialog();
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("剩餘時間    "
						+ String.format("%02d", totalSec / 60) + "："
						+ String.format("%02d", totalSec % 60));
			}
		}.start();
	}

	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動一");
		// 設定對話框內容
		MyAlertDialog.setMessage("時間到  停止作答！！\n進入下一活動");

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity1Page.this,
						Activity2IntroductionPage.class);
				startActivity(intent);
				System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("確定", OkClick);

		MyAlertDialog.show();
	}
}
