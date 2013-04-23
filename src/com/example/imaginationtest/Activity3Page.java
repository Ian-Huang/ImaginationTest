package com.example.imaginationtest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class Activity3Page extends Activity {

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;

	private float xpos = -1;
	private float ypos = -1;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());

		renderer = new MyRenderer(getResources());
		mGLView.setRenderer(renderer);

		setContentView(R.layout.activity3_page);
		FrameLayout frameLayout = (FrameLayout) this
				.findViewById(R.id.activity3_framelayout);
		frameLayout.addView(this.mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public boolean onTouchEvent(MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			xpos = me.getX();
			ypos = me.getY();
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_UP) {
			xpos = -1;
			ypos = -1;
			renderer.touchTurn = 0;
			renderer.touchTurnUp = 0;
			return true;
		}

		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			float xd = me.getX() - xpos;
			float yd = me.getY() - ypos;

			xpos = me.getX();
			ypos = me.getY();

			renderer.touchTurn = xd / -100f;
			renderer.touchTurnUp = yd / -100f;
			return true;
		}

		try {
			Thread.sleep(15);
		} catch (Exception e) {
			// No need for this...
		}

		return super.onTouchEvent(me);
	}

	protected boolean isFullscreenOpaque() {
		return true;
	}
}


