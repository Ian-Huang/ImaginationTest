package com.example.imaginationtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class Activity5Page extends Activity {

	private Button finishButton;

	public RadioButton[][] radioButtonCollection = new RadioButton[15][6];

	public boolean[] checkAnswer;
	public int currentGroup = 0;
	public int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity5_page);

		this.Init();
	}

	void Init() {
		finishButton = (Button) findViewById(R.id.act5_finish_button);
		checkAnswer = new boolean[radioButtonCollection.length];

		int count = 0;
		for (currentGroup = 0; currentGroup < radioButtonCollection.length; currentGroup++) {
			for (currentIndex = 0; currentIndex < radioButtonCollection[currentGroup].length; currentIndex++) {

				// get �C�@��RadioButton ID
				radioButtonCollection[currentGroup][currentIndex] = (RadioButton) findViewById(R.id.act5_Radiobtn_answer01_1
						+ count);

				// set �C�@��RadioButton Listener
				radioButtonCollection[currentGroup][currentIndex]
						.setOnClickListener(new RadioButton.OnClickListener() {

							// ���O�O�U�C�@��RadioButton��Index
							public int group = currentGroup;
							public int index = currentIndex;

							@Override
							public void onClick(View v) {
								// �]���O����D�A�ҥH�����N�C�@��Group��RadioButton�]��
								for (int i = 0; i < radioButtonCollection[group].length; i++) {
									radioButtonCollection[group][i]
											.setChecked(false);
								}
								radioButtonCollection[group][index] // ��e�Q�諸RadioButton
										.setChecked(true);

								// ���O�O���C�@��Group�Q��ܪ����p
								checkAnswer[group] = true;
							}
						});
				count++;
			}
		}

		// �]�w"��g����"Button Listener
		this.finishButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// �u�X�]���]�w:���ܸ�ƬO�_��J����
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʤ�");
		// �]�w����Q����
		MyAlertDialog.setCancelable(false);
		
		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// ------PUT JSON DATA------
				// ���ʤ����ת�key Ac5Q1~Ac5Q15 value�O1~6

				try {
					int i = 1;
					for (RadioButton[] group : radioButtonCollection) {
						int j = 1;
						for (RadioButton button : group) {
							if (button.isChecked()) {
								break;
							}
							j++;
						}
						ParseJSON.PutJsonData("Ac5Q" + String.valueOf(i),
								String.valueOf(j));
						i++;
					}
					ParseJSON.JsonOutput();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String responseStr = ParseJSON
						.UploadData("http://irating.ntue.edu.tw:8080/mobile/UploadUser.php");
				if (responseStr != null) {
					// �W�Ǧ��\
					Log.i("JSON String", responseStr);
				} else {
					// �W�ǥ���
				}

				//------FTP�W�ǹϤ�------
				FTPClient client = new FTPClient();
				try {
					client.connect("irating.ntue.edu.tw");
					if (client.login("weiwen", "0988523032")) {

						client.setFileType(FTP.BINARY_FILE_TYPE);
						client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
						client.enterLocalPassiveMode();

						File[] userNameFolder = Environment
								.getExternalStoragePublicDirectory(
										"ImaginationTest/users").listFiles();

						for (int i = 0; i < userNameFolder.length; i++) {

							FileInputStream fileStream = new FileInputStream(
									userNameFolder[i]);

							String.valueOf(client.changeWorkingDirectory("/image"));

							boolean result = client.storeFile(
									responseStr.toString()
											+ userNameFolder[i].getName(),
									fileStream);

							fileStream.close();
							if (result)
								Log.i("JSON String", "Success");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					client.logout();
					client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//------FTP�W�ǹϤ�(End)------

				// �T�wĲ�o��...
				Intent intent = new Intent();
				intent.setClass(Activity5Page.this, HomePage.class);
				startActivity(intent);
				System.exit(0);
			}
		};
		DialogInterface.OnClickListener CancelClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// ������ܤ��
			}
		};

		// �ˬd��ƬO�_����
		if (this.CheckInformationComplete()) {
			MyAlertDialog.setMessage("�T�w�����A���A�ק�H");
			MyAlertDialog.setPositiveButton("�T�w", OkClick);
			MyAlertDialog.setNegativeButton("����", CancelClick);
		} else {
			MyAlertDialog.setMessage("����g����");
			MyAlertDialog.setNeutralButton("�T�w", CancelClick);
		}

		MyAlertDialog.show();
	}

	boolean CheckInformationComplete() {
		for (int i = 0; i < checkAnswer.length; i++) {
			if (!checkAnswer[i])
				return false;
		}
		return true;
	}
}