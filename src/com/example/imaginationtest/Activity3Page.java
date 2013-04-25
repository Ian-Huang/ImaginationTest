package com.example.imaginationtest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.threed.jpct.Logger;

public class Activity3Page extends Activity {

	enum EditStatus {
		Mode2D, Mode3D
	}

	enum PaintType {
		Eraser, White, Black
	}

	class PaintData {
		private Path paintPath;
		private PaintType paintType;

		PaintData(Path path, PaintType type) {
			this.paintPath = path;
			this.paintType = type;
		}
	}

	private EditStatus currentEditStatus = EditStatus.Mode2D;// �T�{�ثe�s�説�A�O�b2D��3D
	private PaintType currentPaintType = PaintType.Black;// �T�{�ثe�e�����C��(�µ��B�յ��B�����)

	private Button editChangeButton;
	private Button undoPaintButton;
	private Button redoPaintButton;
	private Button eraserButton;
	private Button whitePaintButton;
	private Button blackPaintButton;
	private Button clearCanvasButton;

	// ----�e����l��----
	private Paint BlackPaint;
	private Paint WhitePaint;
	private Paint EraserPaint;
	// -------------

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;

	private float xpos = -1;
	private float ypos = -1;

	DrawPanel dp;
	private ArrayList<PaintData> drawPaintDataList = new ArrayList<PaintData>();
	private Path currentPath;

	private void ButtonInit() {

		// �]�w"�s�����"Button
		this.editChangeButton = (Button) findViewById(R.id.Act3_EditChangeButton);
		this.editChangeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentEditStatus == EditStatus.Mode2D)
					currentEditStatus = EditStatus.Mode3D;
				else
					currentEditStatus = EditStatus.Mode2D;
			}
		});
		// ----------------------

		// �]�w"�٭�"Button
		this.undoPaintButton = (Button) findViewById(R.id.Act3_UndoPaintButton);
		this.undoPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					if (drawPaintDataList.size() > 0) {
						drawPaintDataList.remove(drawPaintDataList.size() - 1);
						Logger.log("Remove:"
								+ String.valueOf(drawPaintDataList.size()));
					}
				}
			}
		});
		// ----------------------

		// �]�w"���e"Button
		this.redoPaintButton = (Button) findViewById(R.id.Act3_RedoPaintButton);
		this.redoPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// synchronized (drawPaintDataList) {
				// if (drawPaintDataList.size() > 0) {
				// drawPaintDataList.remove(drawPaintDataList.size() - 1);
				// Logger.log("Remove:"
				// + String.valueOf(drawPaintDataList.size()));
				// }
				// }
			}
		});
		// ----------------------

		// �]�w"�¦�"Button (�������µ�)
		this.blackPaintButton = (Button) findViewById(R.id.Act3_BlackPaintButton);
		this.blackPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Black;
			}
		});
		// ----------------------

		// �]�w"�զ�"Button (�������յ�)
		this.whitePaintButton = (Button) findViewById(R.id.Act3_WhitePaintButton);
		this.whitePaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.White;
			}
		});
		// ----------------------

		// �]�w"�����"Button (������������Ҧ��A�����e��)
		this.eraserButton = (Button) findViewById(R.id.Act3_EraserButton);
		this.eraserButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Eraser;
			}
		});
		// ----------------------

		// �]�w"�M��"Button (Canvas �Ҧ���Paint�����M��)
		this.clearCanvasButton = (Button) findViewById(R.id.Act3_ClearCanvasButton);
		this.clearCanvasButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					drawPaintDataList.clear();
				}
			}
		});
		// ----------------------

	}

	private void paintInit() {

		// �¦ⵧ��l��
		BlackPaint = new Paint();
		BlackPaint.setDither(true);
		BlackPaint.setColor(Color.BLACK);
		BlackPaint.setStyle(Paint.Style.STROKE);
		BlackPaint.setStrokeJoin(Paint.Join.ROUND);
		BlackPaint.setStrokeCap(Paint.Cap.ROUND);
		BlackPaint.setStrokeWidth(30);

		// �զⵧ��l��
		WhitePaint = new Paint();
		WhitePaint.setDither(true);
		WhitePaint.setColor(Color.WHITE);
		WhitePaint.setStyle(Paint.Style.STROKE);
		WhitePaint.setStrokeJoin(Paint.Join.ROUND);
		WhitePaint.setStrokeCap(Paint.Cap.ROUND);
		WhitePaint.setStrokeWidth(30);

		// �������l��
		EraserPaint = new Paint();
		EraserPaint.setDither(true);
		EraserPaint.setMaskFilter(null);
		EraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		EraserPaint.setARGB(1, 0, 0, 0);
		EraserPaint.setStyle(Paint.Style.STROKE);
		EraserPaint.setStrokeJoin(Paint.Join.ROUND);
		EraserPaint.setStrokeCap(Paint.Cap.ROUND);
		EraserPaint.setStrokeWidth(30);
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());

		renderer = new MyRenderer(getResources());
		mGLView.setRenderer(renderer);

		setContentView(R.layout.activity3_page);

		ButtonInit();
		paintInit();

		FrameLayout frameLayout = (FrameLayout) this
				.findViewById(R.id.activity3_framelayout);

		frameLayout.addView(this.mGLView);

		dp = new DrawPanel(this);

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

		if (currentEditStatus == EditStatus.Mode3D) {
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
			synchronized (drawPaintDataList) {
				for (PaintData data : drawPaintDataList) {
					switch (data.paintType) {
					case Black:
						canvas.drawPath(data.paintPath, BlackPaint);
						break;
					case White:
						canvas.drawPath(data.paintPath, WhitePaint);
						break;
					case Eraser:
						canvas.drawPath(data.paintPath, EraserPaint);
						break;
					}

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

			if (currentEditStatus == EditStatus.Mode2D) {
				synchronized (drawPaintDataList) {
					if (me.getAction() == MotionEvent.ACTION_DOWN) {

						currentPath = new Path();
						currentPath.moveTo(me.getX(), me.getY());

						PaintData pData = new PaintData(currentPath,
								currentPaintType);
						drawPaintDataList.add(pData);

						Logger.log("Draw:"
								+ String.valueOf(drawPaintDataList.size()));
						return true;
					}

					if (me.getAction() == MotionEvent.ACTION_UP) {

						return true;
					}

					if (me.getAction() == MotionEvent.ACTION_MOVE) {

						currentPath.lineTo(me.getX(), me.getY());
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
