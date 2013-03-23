package com.example.imaginationtest;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PersonalInformationPage extends Activity {

	private Calendar calendar;

	private RadioGroup sexRadioGroup;

	private Button setBirthdayButton;
	private Button startTestButton;

	private TextView birthdayTextView;
	private TextView testDayTextView;

	private EditText userNameEditText;
	private EditText schoolNameEditText;
	private EditText gradeYearEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalinformation_page);

		this.Init();
	}

	// 初始化設定
	void Init() {
		this.calendar = Calendar.getInstance();

		this.sexRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_sex);

		this.setBirthdayButton = (Button) findViewById(R.id.SetBirthday_button);
		this.startTestButton = (Button) findViewById(R.id.StartTest_button);

		this.birthdayTextView = (TextView) findViewById(R.id.Birthday_testView);
		this.testDayTextView = (TextView) findViewById(R.id.TestDay_testView);

		this.userNameEditText = (EditText) findViewById(R.id.UserName_editText);
		this.schoolNameEditText = (EditText) findViewById(R.id.SchoolName_editText);
		this.gradeYearEditText = (EditText) findViewById(R.id.GradeYear_editText);

		// 設定測驗日期
		this.testDayTextView.setText(Integer.toString(calendar
				.get(Calendar.YEAR))
				+ "-"
				+ Integer.toString(calendar.get(Calendar.MONTH) + 1)
				+ "-"
				+ Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));

		// 設定"生日"Button Listener
		this.setBirthdayButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(PersonalInformationPage.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								birthdayTextView.setText(Integer.toString(year)
										+ "-"
										+ Integer.toString(monthOfYear + 1)
										+ "-" + Integer.toString(dayOfMonth));
							}
						}, calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// 設定"開始測驗"Button Listener
		this.startTestButton.setOnClickListener(new Button.OnClickListener() {
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
		MyAlertDialog.setTitle("填寫個人資料");

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(PersonalInformationPage.this,
						Activity1IntroductionPage.class);
				startActivity(intent);
				// finish();
			}
		};
		DialogInterface.OnClickListener CancelClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 關閉對話方塊
			}
		};

		// 檢查資料是否齊全
		if (this.CheckInformationComplete()) {
			MyAlertDialog.setMessage("確定要下一頁？");
			MyAlertDialog.setPositiveButton("確定", OkClick);
			MyAlertDialog.setNegativeButton("取消", CancelClick);
		} else {
			MyAlertDialog.setMessage("未填寫完成");
			MyAlertDialog.setNeutralButton("確定", CancelClick);
		}

		MyAlertDialog.show();
	}

	private boolean CheckInformationComplete() {
		if (this.userNameEditText.getText().length() == 0
				| this.schoolNameEditText.getText().length() == 0
				| this.gradeYearEditText.getText().length() == 0
				| this.birthdayTextView.getText().length() == 0
				| this.sexRadioGroup.getCheckedRadioButtonId() == -1)
			return false;

		return true;
	}
}