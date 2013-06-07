package com.example.imaginationtest;

import org.json.JSONException;

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

public class Activity2Page extends Activity {

	private EditText[] answerEditText_Collection = new EditText[42];

	private Button nextActButton;

	private TextView timerTextView;
	private long Countdown_Time = 300; // 倒數計時總時間 ( 單位:秒)

	private CountDownTimer timer;
	private boolean isTimeFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_page);

		this.Init();
		this.StartCountDownTimer();

	}

	// 初始化設定
	void Init() {
		// 讀所有answer的EditText ID
		for (int i = 0; i < answerEditText_Collection.length; i++) {
			this.answerEditText_Collection[i] = (EditText) findViewById(R.id.Act2answer_EditText01
					+ i);
		}
		// -----------------------------------
		this.timerTextView = (TextView) findViewById(R.id.Act2Timer_textView);
		this.nextActButton = (Button) findViewById(R.id.Act2_NextActivityButton);

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

		timer = new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// 時間到後提示進入下一頁
				if (!isTimeFinish) {
					timerTextView.setText("剩餘時間    00：00");
					ShowMsgDialog();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("剩餘時間    "
						+ String.format("%02d", totalSec / 60) + "："
						+ String.format("%02d", totalSec % 60));
			}
		};
		timer.start();
		isTimeFinish = false;
	}

	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動二");
		// 設定對話框內容
		MyAlertDialog.setMessage("時間到  停止作答！！\n進入下一活動");
		// 設定不能被取消
		MyAlertDialog.setCancelable(false);

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// ------PUT JSON DATA------
				// 活動二答案的key Ac2Q1~Ac2Q42
				int count = 0;
				try {
					for (int i = 0; i < answerEditText_Collection.length; i++) {
						if (answerEditText_Collection[i].getText().toString()
								.length() != 0) {
							count++;
							ParseJSON.PutJsonData(
									"Ac2Q" + String.valueOf(count),
									answerEditText_Collection[i].getText()
											.toString());
						}
					}

					if (count == 0)
						ParseJSON.PutJsonData("Ac2Q1", "無");
					ParseJSON.JsonOutput();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ------PUT JSON DATA------
				isTimeFinish = true;
				timer.cancel();
				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity2Page.this, Activity3Page.class);
				startActivity(intent);
				Activity2Page.this.finish();
				// System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("確定", OkClick);

		MyAlertDialog.show();
	}
}
