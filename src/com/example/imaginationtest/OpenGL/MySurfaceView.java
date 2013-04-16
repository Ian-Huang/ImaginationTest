package com.example.imaginationtest.OpenGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.imaginationtest.Activity3Page;
import com.example.imaginationtest.R;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MySurfaceView extends GLSurfaceView implements Renderer {

	private float myPreviousY;
	private float myPreviousX;

	

	public MySurfaceView(Context context) {
		super(context);

		this.setRenderer(this);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		float x = event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			
			
			
			float deltaY = y - this.myPreviousY;
			float deltaX = x - this.myPreviousX;
			Activity3Page.TestPosX.setText(String.valueOf(deltaX));
			Activity3Page.TestPosY.setText(String.valueOf(deltaY));
			break;

		default:
			break;
		}
		this.myPreviousX = x;
		this.myPreviousY = y;
		return true;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		

	}

}
