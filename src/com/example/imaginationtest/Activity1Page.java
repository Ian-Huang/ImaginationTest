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
	private long Countdown_Time = 300; // �˼ƭp���`�ɶ� ( ���:��)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1_page);

		this.Init();
		this.StartCountDownTimer();

	}

	// ��l�Ƴ]�w
	void Init() {
		// Ū�Ҧ�answer��EditText ID
		for (int i = 0; i < answerEditText_Collection.length; i++) {
			this.answerEditText_Collection[i] = (EditText) findViewById(R.id.Act1answer_EditText01
					+ i);
		}
		// -----------------------------------
		this.timerTextView = (TextView) findViewById(R.id.Act1Timer_textView);
		this.nextActButton = (Button) findViewById(R.id.Act1_NextActivityButton);

		// �]�w"�U�@�B"Button Listener
		this.nextActButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// �p�ɾ��]�w
	void StartCountDownTimer() {

		new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// �ɶ���ᴣ�ܶi�J�U�@��
				timerTextView.setText("�Ѿl�ɶ�    00�G00");
				ShowMsgDialog();
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("�Ѿl�ɶ�    "
						+ String.format("%02d", totalSec / 60) + "�G"
						+ String.format("%02d", totalSec % 60));
			}
		}.start();
	}

	// �u�X�]���]�w:���ܬO�_�i�J�U�@��
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʤ@");
		// �]�w��ܮؤ��e
		MyAlertDialog.setMessage("�ɶ���  ����@���I�I\n�i�J�U�@����");

		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �T�wĲ�o��...
				Intent intent = new Intent();
				intent.setClass(Activity1Page.this,
						Activity2IntroductionPage.class);
				startActivity(intent);
				System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("�T�w", OkClick);

		MyAlertDialog.show();
	}
}
