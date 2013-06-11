package com.example.imaginationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Activity3IntroductionPage extends Activity {

	private ImageView act3_ImageButton;
	private int actionIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3introduction_page);

		this.Init();
	}

	// 初始化設定
	void Init() {
		this.act3_ImageButton = (ImageView) findViewById(R.id.Act3_ImageButton);

		// 設定"開始測驗"Button Listener
		this.act3_ImageButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionIndex++;

				if (actionIndex == 2)
					act3_ImageButton.setImageResource(R.drawable.act3t1);
				if (actionIndex == 3)
					act3_ImageButton.setImageResource(R.drawable.act3t2);
				if (actionIndex == 4) {
					Intent intent = new Intent();
					intent.setClass(Activity3IntroductionPage.this,
							Activity3Page.class);
					startActivity(intent);
					Activity3IntroductionPage.this.finish();
				}
			}
		});
	}
}
