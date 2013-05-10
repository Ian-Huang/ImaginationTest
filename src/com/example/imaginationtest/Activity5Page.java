package com.example.imaginationtest;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
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
				// ------PUT JSON DATA------
				ParseJSON
						.PostData("http://irating.ntue.edu.tw:8080/mobile/UploadUser.php");
				// // ------�x�sJSON��------
				// // �]�w�~���x�s��m
				// File publicFolder = Environment
				// .getExternalStoragePublicDirectory("ImaginationTest");
				// if (!publicFolder.exists())
				// publicFolder.mkdir();
				// // �H�ϥΪ̤H�W��@��Ƨ��W�r
				// File userNameFolder = new File(publicFolder, "users");
				// if (!userNameFolder.exists())
				// userNameFolder.mkdir();
				// // �]�w�ɮצW�l
				//
				// File fileName = new File(userNameFolder,
				// PersonalInformationPage.TestDay
				// + PersonalInformationPage.StudendName + ".json");
				//
				// try {
				// BufferedWriter buf = new BufferedWriter(new FileWriter(
				// fileName, false));
				// buf.append(ParseJSON.GetJSONString());
				// buf.newLine();
				// buf.close();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// // ------�x�sJSON��------

				// ---
				// HttpURLConnection connection = null;
				// DataOutputStream outputStream = null;
				// DataInputStream inputStream = null;
				//
				// String pathToOurFile = "/data/file_to_send.mp3";
				// String urlServer = "http://192.168.1.1/handle_upload.php";
				// String lineEnd = "\r\n";
				// String twoHyphens = "--";
				// String boundary = "*****";
				//
				// int bytesRead, bytesAvailable, bufferSize;
				// byte[] buffer;
				// int maxBufferSize = 1*1024*1024;
				//
				// try
				// {
				// FileInputStream fileInputStream = new FileInputStream(new
				// File(pathToOurFile) );
				//
				// URL url = new URL(urlServer);
				// connection = (HttpURLConnection) url.openConnection();
				//
				// // Allow Inputs & Outputs
				// connection.setDoInput(true);
				// connection.setDoOutput(true);
				// connection.setUseCaches(false);
				//
				// // Enable POST method
				// connection.setRequestMethod("POST");
				//
				// connection.setRequestProperty("Connection", "Keep-Alive");
				// connection.setRequestProperty("Content-Type",
				// "multipart/form-data;boundary="+boundary);
				//
				// outputStream = new DataOutputStream(
				// connection.getOutputStream() );
				// //outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				// outputStream.writeBytes(pathToOurFile);
				// //outputStream.writeBytes(lineEnd);
				//
				// bytesAvailable = fileInputStream.available();
				// bufferSize = Math.min(bytesAvailable, maxBufferSize);
				// buffer = new byte[bufferSize];
				//
				// // Read file
				// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				//
				// while (bytesRead > 0)
				// {
				// //outputStream.write(buffer, 0, bufferSize);
				// bytesAvailable = fileInputStream.available();
				// bufferSize = Math.min(bytesAvailable, maxBufferSize);
				// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				// }
				//
				// //outputStream.writeBytes(lineEnd);
				// //outputStream.writeBytes(twoHyphens + boundary + twoHyphens
				// + lineEnd);
				//
				// // Responses from the server (code and message)
				// //serverResponseCode = connection.getResponseCode();
				// String serverResponseMessage =
				// connection.getResponseMessage();
				//
				// fileInputStream.close();
				// outputStream.flush();
				// outputStream.close();
				// }
				// catch (Exception ex)
				// {
				// //Exception handling
				// }
				// ---

				// �T�wĲ�o��...
				// Intent intent = new Intent();
				// intent.setClass(Activity5Page.this, HomePage.class);
				// startActivity(intent);
				// System.exit(0);
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