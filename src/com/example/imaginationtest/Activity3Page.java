package com.example.imaginationtest;

import com.example.imaginationtest.OpenGL.MySurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity3Page extends Activity {

	private MySurfaceView mSurfaceView;
	
	public static TextView TestPosX;
	public static TextView TestPosY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3_page);

		this.mSurfaceView = new MySurfaceView(this);
		this.mSurfaceView.requestFocus();
		this.mSurfaceView.setFocusableInTouchMode(true);
		LinearLayout ll = (LinearLayout) this
				.findViewById(R.id.activity3_linear);
		ll.addView(this.mSurfaceView);
		
		TestPosX = (TextView) findViewById(R.id.act3TextX);
		TestPosY = (TextView) findViewById(R.id.act3TextY);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.mSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.mSurfaceView.onResume();
	}

}
