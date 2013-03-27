package com.example.imaginationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class Activity5Page extends Activity {

	private RadioButton[][] radioButtonCollection = new RadioButton[15][6];
	public static int currentGroup = 0;
	public static int currentIndex = 0;

	TextView testViewxstr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity5_page);

		this.Init();
	}

	void Init() {
		testViewxstr = (TextView) findViewById(R.id.act5_testI);
		int count = 0;
		for (RadioButton[] element : radioButtonCollection) {
			currentIndex = 0;
			for (RadioButton radioButton : element) {
				radioButton = (RadioButton) findViewById(R.id.act5_Radiobtn_answer01_1
						+ count);

				radioButton
						.setOnClickListener(new RadioButton.OnClickListener() {

							public int group = Activity5Page.currentGroup;
							public int index = Activity5Page.currentIndex;

							@Override
							public void onClick(View v) {
								 testViewxstr.setText(Integer
								 .toString(radioButtonCollection[group][index].getId()));
//								for (RadioButton radioButton : radioButtonCollection[group]) {
//									radioButton.setChecked(false);
//								}
//
//								radioButtonCollection[group][index]
//										.setChecked(false);
							}
						});

				count++;
				currentIndex++;
			}
			currentGroup++;
		}

	}

}
