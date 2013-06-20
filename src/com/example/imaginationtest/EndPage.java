package com.example.imaginationtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndPage extends Activity {

	private Button uploadButton;
	private Button ExitButton;
	private TextView hintCurrentState;

	void Init() {
		this.uploadButton = (Button) findViewById(R.id.endPage_UploadDataButton);
		this.ExitButton = (Button) findViewById(R.id.endPage_ExitTestButton);
		this.hintCurrentState = (TextView) findViewById(R.id.endPage_HintCurrentState);

		// 設定"資料上傳網路"Button Listener
		this.uploadButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadData();
			}
		});

		// 設定"結束測驗"Button Listener
		this.ExitButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_page);
		this.Init();
	}

	void UploadData() {
		
		this.hintCurrentState.setText("資料上傳中...");
		String responseStr = ParseJSON
				.UploadData("http://irating.ntue.edu.tw:8080/mobile/UploadUser.php");
		if (responseStr != null) {
			// 上傳成功
			Log.i("JSON String", responseStr);
		} else {
			// 上傳失敗
			this.hintCurrentState.setText("網路未連接，上傳失敗！");
			return;
		}

		// ------FTP上傳圖片------
		FTPClient client = new FTPClient();
		try {
			client.connect("irating.ntue.edu.tw");
			if (client.login("weiwen", "0988523032")) {

				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
				client.enterLocalPassiveMode();

				File[] userNameFolder = Environment
						.getExternalStoragePublicDirectory(
								"ImaginationTest/"
										+ String.valueOf(PersonalInformationPage.StudendName
												.hashCode())).listFiles();

				for (int i = 0; i < userNameFolder.length; i++) {

					FileInputStream fileStream = new FileInputStream(
							userNameFolder[i]);

					String.valueOf(client.changeWorkingDirectory("/image"));

					boolean result = client.storeFile(responseStr.toString()
							+ userNameFolder[i].getName(), fileStream);

					fileStream.close();
					if (result)
						Log.i("JSON String", "Success");
				}
			}
		} catch (Exception e) {
			this.hintCurrentState.setText("網路未連接，上傳失敗！");
		}
		try {
			client.logout();
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ------FTP上傳圖片(End)------
		this.hintCurrentState.setText("上傳成功！！");
	}
}