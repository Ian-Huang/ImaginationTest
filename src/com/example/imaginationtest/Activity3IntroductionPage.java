package com.example.imaginationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Activity3IntroductionPage extends Activity {

	private ImageButton act3_ImageButton;

	private int ImageMax = 3;
	int i = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3introduction_page);

		this.Init();
	}

	// 初始化設定
	void Init() {
		this.act3_ImageButton = (ImageButton) findViewById(R.id.Act3_ImageButton);

		// 設定"開始測驗"Button Listener
		this.act3_ImageButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				i++;
				
				if(i==2)
					act3_ImageButton.setImageResource(R.drawable.action01);
				if(i==3)
					act3_ImageButton.setImageResource(R.drawable.action02);
				if(i==4)
				{
					Intent intent = new Intent();
					intent.setClass(Activity3IntroductionPage.this, Activity3Page.class);
					startActivity(intent);
					Activity3IntroductionPage.this.finish();
				}
			}
		});
	}
}
