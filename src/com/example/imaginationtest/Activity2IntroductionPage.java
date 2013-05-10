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

	// ��l�Ƴ]�w
	void Init() {

		this.startTestButton = (Button) findViewById(R.id.act2Introduction_StartTestButton);

		// �]�w"�}�l����"Button Listener
		this.startTestButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// �u�X�]���]�w:���ܬO�_�i�J�U�@��
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʤG");
		// �]�w��ܮؤ��e
		MyAlertDialog.setMessage("�������p�ɶ}�l�I�I");

		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �T�wĲ�o��...
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
				// ������ܤ��
			}
		};

		MyAlertDialog.setPositiveButton("�T�w", OkClick);
		MyAlertDialog.setNegativeButton("����", CancelClick);

		MyAlertDialog.show();
	}
}
