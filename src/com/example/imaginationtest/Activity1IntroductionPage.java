package com.example.imaginationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity1IntroductionPage extends Activity {

	private Button startTestButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1introduction_page);

		this.Init();
	}

	// ��l�Ƴ]�w
	void Init() {

		this.startTestButton = (Button) findViewById(R.id.act1Introduction_StartTestButton);

		// �]�w"�}�l����"Button Listener
		this.startTestButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ShowMsgDialog();
				// �T�wĲ�o��...
				Intent intent = new Intent();
				intent.setClass(Activity1IntroductionPage.this,
						Activity1Page.class);
				startActivity(intent);
				Activity1IntroductionPage.this.finish();
				// System.exit(0);
			}
		});
	}

	// // �u�X�]���]�w:���ܬO�_�i�J�U�@��
	// private void ShowMsgDialog() {
	// Builder MyAlertDialog = new AlertDialog.Builder(this);
	//
	// // �]�w��ܮؼ��D
	// MyAlertDialog.setTitle("���ʤ@");
	// // �]�w��ܮؤ��e
	// MyAlertDialog.setMessage("�������p�ɶ}�l�I�I");
	//
	// // ButtonĲ�o�᪺�]�w
	// DialogInterface.OnClickListener OkClick = new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// // �T�wĲ�o��...
	// Intent intent = new Intent();
	// intent.setClass(Activity1IntroductionPage.this,
	// Activity1Page.class);
	// startActivity(intent);
	// Activity1IntroductionPage.this.finish();
	// //System.exit(0);
	// }
	// };
	// DialogInterface.OnClickListener CancelClick = new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// // ������ܤ��
	// }
	// };
	//
	// MyAlertDialog.setPositiveButton("�T�w", OkClick);
	// MyAlertDialog.setNegativeButton("����", CancelClick);
	//
	// MyAlertDialog.show();
	// }
}
