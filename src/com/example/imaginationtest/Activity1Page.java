package com.example.imaginationtest;

import android.app.Activity;
import android.os.Bundle;

public class Activity1Page extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1_page);

		// this.Init();
		//
		// new CountDownTimer(30000,1000){
		//
		// @Override
		// public void onFinish() {
		// // TODO Auto-generated method stub
		// mTextView.setText("Done!");
		// }
		//
		// @Override
		// public void onTick(long millisUntilFinished) {
		// // TODO Auto-generated method stub
		// mTextView.setText("seconds remaining:"+millisUntilFinished/1000);
		// }
		//
		// }.start();
	}

	// void Init() {
	// this.testTimer = (Chronometer) findViewById(R.id.ActivityTimer);
	// this.testTimer.setFormat("³Ñ¾l®É¶¡¡G%s");
	// this.testTimer.setBase(1);
	// this.testTimer.start();
	//
	// }
}
