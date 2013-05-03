package com.example.imaginationtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity4Page extends Activity {

	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_RedoButton;
	private Button Act4_UndoButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_SaveFileButton;
	private Button Act4_NextActivity;
	
	private TextView Act4_Timer;
	private EditText Act4_EditText01;
	private EditText Act4_EditText02;
	private ImageView Act4_ImageView01;
	private ImageView Act4_ImageView02;
	
	private long Countdown_Time = 600; // �˼ƭp���`�ɶ� ( ���:��)

	// �`����
	private static int pageMaxCount = 28;
	// �C������r��T
	private String[] EditText_Collection = new String[2 * pageMaxCount];
	// �C�����Ϥ���T
	public static Bitmap[] Bitmap_Collection = new Bitmap[pageMaxCount];

	// �x�s�e����Layout
	private View Layout;
	// �x�s�e����Bitmap
	private Bitmap bitmapLayout;
	// ��e�����s��
	private int CurrentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);

		this.Layout = (View) findViewById(R.id.Act4_Layout);

		this.Act4_Timer = (TextView) findViewById(R.id.Act4_Timer);
		this.Act4_EditText01 = (EditText) findViewById(R.id.Act4_editText01);
		this.Act4_EditText02 = (EditText) findViewById(R.id.Act4_editText02);
		this.Act4_ImageView01 = (ImageView) findViewById(R.id.Act4_ImageView01);
		this.Act4_ImageView02 = (ImageView) findViewById(R.id.Act4_ImageView02);

		Init();
		Act4_PageUpdate();
		this.StartCountDownTimer();
	}

	void Init() {
		// �]�w�s��Button�^�X
		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
		this.Act4_SaveFileButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Layout.setDrawingCacheEnabled(true);
						bitmapLayout = Layout.getDrawingCache();
						saveImage(bitmapLayout);
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// �]�w�W�@��Button�^�X
		this.Act4_PreviousPageButton = (Button) findViewById(R.id.Act4_PreviousPageButton);
		this.Act4_PreviousPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Act4_SaveEditText();
						if (CurrentPage > 1)
							CurrentPage--;
						Act4_PageUpdate();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// �]�w�U�@��Button�^�X
		this.Act4_NextPageButton = (Button) findViewById(R.id.Act4_NextPageButton);
		this.Act4_NextPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						Act4_SaveEditText();
						if (CurrentPage < pageMaxCount)
							CurrentPage++;
						Act4_PageUpdate();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// �]�w�U�@�BButton�^�X
		this.Act4_NextActivity = (Button) findViewById(R.id.Act4_NextActivity);
		this.Act4_NextActivity
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowMsgDialog();
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// �]�w�M��Button�^�X
		this.Act4_ClearButton = (Button) findViewById(R.id.Act4_ClearButton);
		this.Act4_ClearButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// �]�w�����Button�^�X
		this.Act4_EraserButton = (Button) findViewById(R.id.Act4_EraserButton);
		this.Act4_EraserButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// �]�w����Button�^�X
		this.Act4_RedoButton = (Button) findViewById(R.id.Act4_RedoButton);
		this.Act4_RedoButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////
		
		// �]�w�٭�Button�^�X
		this.Act4_UndoButton = (Button) findViewById(R.id.Act4_UndoButton);
		this.Act4_UndoButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//DO SOMETHING
					}
				});
		// ///////////////////////////////////////////////////////////////////////////

	}

	// �x�s�Ϥ�
	protected void saveImage(Bitmap bmScreen2) {
		// TODO Auto-generated method stub

		// �]�w�~���x�s��m
		File publicFolder = Environment
				.getExternalStoragePublicDirectory("ImaginationTest");
		if (!publicFolder.exists())
			publicFolder.mkdir();
		// �H�ϥΪ̤H�W��@��Ƨ��W�l
		File userNameFolder = new File(publicFolder, "users");
		if (!userNameFolder.exists())
			userNameFolder.mkdir();
		// �]�w�ɮצW�l

		File fileName = new File(userNameFolder, "Act4_Page" + CurrentPage
				+ ".jpg");

		try {
			OutputStream os = new FileOutputStream(fileName);
			bmScreen2.compress(Bitmap.CompressFormat.PNG, 80, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void Act4_SaveEditText() {
		EditText_Collection[(CurrentPage - 1) * 2 + 0] = Act4_EditText01
				.getText().toString();
		EditText_Collection[(CurrentPage - 1) * 2 + 1] = Act4_EditText02
				.getText().toString();
	}

	private void Act4_PageUpdate() {
		Act4_EditText01.setText(EditText_Collection[(CurrentPage - 1) * 2 + 0]);
		Act4_EditText02.setText(EditText_Collection[(CurrentPage - 1) * 2 + 1]);
		Act4_ImageView01.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 0);
		Act4_ImageView02.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 1);
		
		if(CurrentPage == 1)
			this.Act4_PreviousPageButton.setEnabled(false);
		else
			this.Act4_PreviousPageButton.setEnabled(true);
		if(CurrentPage == 28)
			this.Act4_NextPageButton.setEnabled(false);
		else
			this.Act4_NextPageButton.setEnabled(true);
	}

	
	// �p�ɾ��]�w
	void StartCountDownTimer() {

		new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// �ɶ���ᴣ�ܶi�J�U�@��
				Act4_Timer.setText("�Ѿl�ɶ�    00�G00");
				ShowMsgDialog();
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				Act4_Timer.setText("�Ѿl�ɶ�    "
						+ String.format("%02d", totalSec / 60) + "�G"
						+ String.format("%02d", totalSec % 60));
			}
		}.start();
	}
	
	// �u�X�]���]�w:���ܬO�_�i�J�U�@��
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʥ|");
		// �]�w��ܮؤ��e
		MyAlertDialog.setMessage("�ɶ���  ����@���I�I");

		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �T�wĲ�o��...
				Intent intent = new Intent();
				intent.setClass(Activity4Page.this,
						Activity5Page.class);
				startActivity(intent);
				System.exit(0);
			}
		};

		MyAlertDialog.setNeutralButton("�T�w", OkClick);

		MyAlertDialog.show();
	}
}