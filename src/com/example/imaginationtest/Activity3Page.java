package com.example.imaginationtest;

import java.util.ArrayList;

import com.threed.jpct.Logger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class Activity3Page extends Activity {

	enum EditStatus {
		Mode2D, Mode3D
	}

	private EditStatus editStatus = EditStatus.Mode2D;// 確認目前編輯狀態是在2D或3D
	private Button editChangeButton;
	private Button reducePaintButton;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;

	private float xpos = -1;
	private float ypos = -1;

	DrawPanel dp;
	private ArrayList<Path> pointsToDraw = new ArrayList<Path>();
	private Paint mPaint;
	Path path;

	private void Init() {

		// 設定"編輯切換"Button
		this.editChangeButton = (Button) findViewById(R.id.Act3_EditChangeButton);
		this.editChangeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editStatus == EditStatus.Mode2D)
					editStatus = EditStatus.Mode3D;
				else
					editStatus = EditStatus.Mode2D;
			}
		});

		// ----------------------

		// 設定"還原"Button
		this.reducePaintButton = (Button) findViewById(R.id.Act3_ReducePaintButton);
		this.reducePaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (pointsToDraw) {
					if (pointsToDraw.size() > 0) {
						pointsToDraw.remove(pointsToDraw.size() - 1);
						Logger.log("Remove:"
								+ String.valueOf(pointsToDraw.size()));
					}
				}
			}
		});

	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());

		renderer = new MyRenderer(getResources());
		mGLView.setRenderer(renderer);

		setContentView(R.layout.activity3_page);

		Init();

		FrameLayout frameLayout = (FrameLayout) this
				.findViewById(R.id.activity3_framelayout);

		frameLayout.addView(this.mGLView);

		dp = new DrawPanel(this);
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(30);

		frameLayout.addView(dp);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
		dp.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
		dp.resume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (editStatus == EditStatus.Mode3D) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xpos = event.getX();
				ypos = event.getY();
				return true;

			case MotionEvent.ACTION_UP:
				xpos = -1;
				ypos = -1;
				renderer.touchTurn = 0;
				renderer.touchTurnUp = 0;
				return true;

			case MotionEvent.ACTION_MOVE:
				float xd = event.getX() - xpos;
				float yd = event.getY() - ypos;

				xpos = event.getX();
				ypos = event.getY();

				renderer.touchTurn = xd / -100f;
				renderer.touchTurnUp = yd / -100f;
				return true;

			default:
				break;
			}
		}
		try {
			Thread.sleep(15);
		} catch (Exception e) {
			// No need for this...
		}

		return super.onTouchEvent(event);
	}

	protected boolean isFullscreenOpaque() {
		return true;
	}

	public class DrawPanel extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOk = false;

		public DrawPanel(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();
			setZOrderOnTop(true);
			holder.setFormat(PixelFormat.TRANSLUCENT);
		}

		public void run() {
			// TODO Auto-generated method stub
			while (isItOk == true) {

				if (!holder.getSurface().isValid()) {
					continue;
				}

				Canvas c = holder.lockCanvas();

				c.drawColor(0x00AAAAAA, Mode.CLEAR);
				// c.drawARGB(255, 0, 0, 0);
				onDraw(c);
				holder.unlockCanvasAndPost(c);
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			synchronized (pointsToDraw) {
				for (Path path : pointsToDraw) {
					canvas.drawPath(path, mPaint);
					Logger.log("Canvas===Draw:"
							+ String.valueOf(pointsToDraw.size()));
				}
			}
		}

		public void pause() {
			isItOk = false;
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			isItOk = true;
			t = new Thread(this);
			t.start();

		}

		@Override
		public boolean onTouchEvent(MotionEvent me) {
			// TODO Auto-generated method stub

			if (editStatus == EditStatus.Mode2D) {
				synchronized (pointsToDraw) {
					if (me.getAction() == MotionEvent.ACTION_DOWN) {

						path = new Path();
						path.moveTo(me.getX(), me.getY());
						// path.lineTo(me.getX(), me.getY());
						pointsToDraw.add(path);
						Logger.log("Draw:"
								+ String.valueOf(pointsToDraw.size()));
						return true;
					}

					if (me.getAction() == MotionEvent.ACTION_UP) {

						return true;
					}

					if (me.getAction() == MotionEvent.ACTION_MOVE) {

						path.lineTo(me.getX(), me.getY());
						return true;
					}
				}

				try {
					Thread.sleep(15);
				} catch (Exception e) {
					// No need for this...
				}

			}

			return super.onTouchEvent(me);
		}
	}
}
