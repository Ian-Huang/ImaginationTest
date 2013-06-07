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
	private long Countdown_Time = 300; // �˼ƭp���`�ɶ� ( ���:��)

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

	// ��l�Ƴ]�w
	void Init() {
		// Ū�Ҧ�answer��EditText ID
		for (int i = 0; i < answerEditText_Collection.length; i++) {
			this.answerEditText_Collection[i] = (EditText) findViewById(R.id.Act2answer_EditText01
					+ i);
		}
		// -----------------------------------
		this.timerTextView = (TextView) findViewById(R.id.Act2Timer_textView);
		this.nextActButton = (Button) findViewById(R.id.Act2_NextActivityButton);

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

		timer = new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// �ɶ���ᴣ�ܶi�J�U�@��
				if (!isTimeFinish) {
					timerTextView.setText("�Ѿl�ɶ�    00�G00");
					ShowMsgDialog();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("�Ѿl�ɶ�    "
						+ String.format("%02d", totalSec / 60) + "�G"
						+ String.format("%02d", totalSec % 60));
			}
		};
		timer.start();
		isTimeFinish = false;
	}

	// �u�X�]���]�w:���ܬO�_�i�J�U�@��
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʤG");
		// �]�w��ܮؤ��e
		MyAlertDialog.setMessage("�ɶ���  ����@���I�I\n�i�J�U�@����");
		// �]�w����Q����
		MyAlertDialog.setCancelable(false);

		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// ------PUT JSON DATA------
				// ���ʤG���ת�key Ac2Q1~Ac2Q42
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
						ParseJSON.PutJsonData("Ac2Q1", "�L");
					ParseJSON.JsonOutput();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ------PUT JSON DATA------
				isTimeFinish = true;
				timer.cancel();
				// �T�wĲ�o��...
				Intent intent = new Intent();
				intent.setClass(Activity2Page.this, Activity3Page.class);
				startActivity(intent);
				Activity2Page.this.finish();
				// System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("�T�w", OkClick);

		MyAlertDialog.show();
	}
}
