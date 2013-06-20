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

		// �]�w"��ƤW�Ǻ���"Button Listener
		this.uploadButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadData();
			}
		});

		// �]�w"��������"Button Listener
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
		
		this.hintCurrentState.setText("��ƤW�Ǥ�...");
		String responseStr = ParseJSON
				.UploadData("http://irating.ntue.edu.tw:8080/mobile/UploadUser.php");
		if (responseStr != null) {
			// �W�Ǧ��\
			Log.i("JSON String", responseStr);
		} else {
			// �W�ǥ���
			this.hintCurrentState.setText("�������s���A�W�ǥ��ѡI");
			return;
		}

		// ------FTP�W�ǹϤ�------
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
			this.hintCurrentState.setText("�������s���A�W�ǥ��ѡI");
		}
		try {
			client.logout();
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ------FTP�W�ǹϤ�(End)------
		this.hintCurrentState.setText("�W�Ǧ��\�I�I");
	}
}