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

				// get 每一個RadioButton ID
				radioButtonCollection[currentGroup][currentIndex] = (RadioButton) findViewById(R.id.act5_Radiobtn_answer01_1
						+ count);

				// set 每一個RadioButton Listener
				radioButtonCollection[currentGroup][currentIndex]
						.setOnClickListener(new RadioButton.OnClickListener() {

							// 分別記下每一個RadioButton的Index
							public int group = currentGroup;
							public int index = currentIndex;

							@Override
							public void onClick(View v) {
								// 因為是單選題，所以必須將每一個Group的RadioButton設值
								for (int i = 0; i < radioButtonCollection[group].length; i++) {
									radioButtonCollection[group][i]
											.setChecked(false);
								}
								radioButtonCollection[group][index] // 當前被選的RadioButton
										.setChecked(true);

								// 分別記錄每一個Group被選擇的情況
								checkAnswer[group] = true;
							}
						});
				count++;
			}
		}

		// 設定"填寫完成"Button Listener
		this.finishButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowMsgDialog();
			}
		});
	}

	// 彈出設窗設定:提示資料是否輸入完整
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動五");
		// 設定不能被取消
		MyAlertDialog.setCancelable(false);
		
		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// ------PUT JSON DATA------
				// 活動五答案的key Ac5Q1~Ac5Q15 value是1~6

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
					// 上傳成功
					Log.i("JSON String", responseStr);
				} else {
					// 上傳失敗
				}

				//------FTP上傳圖片------
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
				//------FTP上傳圖片(End)------

				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity5Page.this, HomePage.class);
				startActivity(intent);
				System.exit(0);
			}
		};
		DialogInterface.OnClickListener CancelClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 關閉對話方塊
			}
		};

		// 檢查資料是否齊全
		if (this.CheckInformationComplete()) {
			MyAlertDialog.setMessage("確定完成，不再修改？");
			MyAlertDialog.setPositiveButton("確定", OkClick);
			MyAlertDialog.setNegativeButton("取消", CancelClick);
		} else {
			MyAlertDialog.setMessage("未填寫完成");
			MyAlertDialog.setNeutralButton("確定", CancelClick);
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