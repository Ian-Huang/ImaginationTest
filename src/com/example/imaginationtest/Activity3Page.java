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
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
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

	enum ActionType {
		Move, Rotate, Depth
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

	private EditStatus currentEditStatus = EditStatus.Mode2D;// 確認目前編輯狀態是在2D或3D
	private PaintType currentPaintType = PaintType.Black;// 確認目前畫筆的顏色(黑筆、白筆、橡皮擦)
	public static ActionType currentActionType = ActionType.Move;// 確認目前3D模式的動作

	// ----------Button----------
	private Button undoPaintButton;
	private Button redoPaintButton;
	private Button eraserButton;
	private Button whitePaintButton;
	private Button blackPaintButton;
	private Button clearCanvasButton;

	private Button editChangeButton;
	private Button moveButton;
	private Button rotateButton;
	private Button depthButton;
	// -----------------------------

	// --------2D Canvas繪圖相關-------
	private DrawPanel drawPanel;

	// ----畫筆初始化----
	private Paint BlackPaint;
	private Paint WhitePaint;
	private Paint EraserPaint;

	// ----路徑資訊----
	private ArrayList<PaintData> drawPaintDataList = new ArrayList<PaintData>();
	private ArrayList<PaintData> rePaintDataList = new ArrayList<PaintData>();
	private Path currentPath;
	// ------------------------------------

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;

	private float tempTouchPosX = -1;
	private float tempTouchPosY = -1;
	private float oldDistance = -1;
	private float newDistance = -1;

	private void ButtonInit() {

		// 設定"還原"Button
		undoPaintButton = (Button) findViewById(R.id.Act3_UndoPaintButton);
		undoPaintButton.setEnabled(false);
		undoPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (drawPaintDataList.size() > 0) {
							rePaintDataList.add(drawPaintDataList
									.get(drawPaintDataList.size() - 1));
							redoPaintButton.setEnabled(true);

							drawPaintDataList.remove(drawPaintDataList.size() - 1);
							if (drawPaintDataList.size() == 0) {
								eraserButton.setEnabled(false);
								undoPaintButton.setEnabled(false);
								clearCanvasButton.setEnabled(false);
								if (currentPaintType == PaintType.Eraser)
									currentPaintType = PaintType.Black;
							}
							Logger.log("Action Undo: current size = "
									+ String.valueOf(drawPaintDataList.size())
									+ " redoSize = "
									+ String.valueOf(rePaintDataList.size()));
						}
					}
				}
			}
		});
		// ----------------------

		// 設定"重畫"Button
		redoPaintButton = (Button) findViewById(R.id.Act3_RedoPaintButton);
		redoPaintButton.setEnabled(false);
		redoPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (rePaintDataList.size() > 0) {
							drawPaintDataList.add(rePaintDataList
									.get(rePaintDataList.size() - 1));
							undoPaintButton.setEnabled(true);
							clearCanvasButton.setEnabled(true);

							rePaintDataList.remove(rePaintDataList.size() - 1);
							if (rePaintDataList.size() == 0)
								redoPaintButton.setEnabled(false);

							Logger.log("Action Redo: current size = "
									+ String.valueOf(drawPaintDataList.size())
									+ " redoSize = "
									+ String.valueOf(rePaintDataList.size()));
						}
					}
				}
			}
		});
		// ----------------------

		// 設定"黑色"Button (切換為黑筆)
		blackPaintButton = (Button) findViewById(R.id.Act3_BlackPaintButton);
		blackPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Black;
			}
		});
		// ----------------------

		// 設定"白色"Button (切換為白筆)
		whitePaintButton = (Button) findViewById(R.id.Act3_WhitePaintButton);
		whitePaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.White;
			}
		});
		// ----------------------

		// 設定"橡皮擦"Button (切換為橡皮擦模式，擦除畫筆)
		eraserButton = (Button) findViewById(R.id.Act3_EraserButton);
		eraserButton.setEnabled(false);
		eraserButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Eraser;
			}
		});
		// ----------------------

		// 設定"清除"Button (Canvas 所有的Paint全部清除)
		clearCanvasButton = (Button) findViewById(R.id.Act3_ClearCanvasButton);
		clearCanvasButton.setEnabled(false);
		clearCanvasButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						drawPaintDataList.clear();
						rePaintDataList.clear();
						redoPaintButton.setEnabled(false);
						eraserButton.setEnabled(false);
						undoPaintButton.setEnabled(false);
						clearCanvasButton.setEnabled(false);
						if (currentPaintType == PaintType.Eraser)
							currentPaintType = PaintType.Black;
					}
				}
			}
		});
		// ----------------------

		// 設定"移動"Button (3D物件控制)
		moveButton = (Button) findViewById(R.id.Act3_MoveButton);
		moveButton.setEnabled(false);
		moveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentActionType = ActionType.Move;
				moveButton.setEnabled(false);
				rotateButton.setEnabled(true);
				depthButton.setEnabled(true);
			}
		});
		// ----------------------

		// 設定"旋轉"Button (3D物件控制)
		rotateButton = (Button) findViewById(R.id.Act3_RotateButton);
		rotateButton.setEnabled(false);
		rotateButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentActionType = ActionType.Rotate;
				moveButton.setEnabled(true);
				rotateButton.setEnabled(false);
				depthButton.setEnabled(true);
			}
		});
		// ----------------------

		// 設定"深度"Button (3D物件控制)
		depthButton = (Button) findViewById(R.id.Act3_DepthButton);
		depthButton.setEnabled(false);
		depthButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentActionType = ActionType.Depth;
				moveButton.setEnabled(true);
				rotateButton.setEnabled(true);
				depthButton.setEnabled(false);
			}
		});
		// ----------------------

		// 設定"編輯切換"Button
		editChangeButton = (Button) findViewById(R.id.Act3_EditChangeButton);
		editChangeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (currentEditStatus == EditStatus.Mode2D) {
							undoPaintButton.setEnabled(false);
							redoPaintButton.setEnabled(false);
							eraserButton.setEnabled(false);
							whitePaintButton.setEnabled(false);
							blackPaintButton.setEnabled(false);
							clearCanvasButton.setEnabled(false);

							switch (currentActionType) {
							case Move:
								moveButton.setEnabled(false);
								rotateButton.setEnabled(true);
								depthButton.setEnabled(true);
								break;
							case Rotate:
								moveButton.setEnabled(true);
								rotateButton.setEnabled(false);
								depthButton.setEnabled(true);
								break;
							case Depth:
								moveButton.setEnabled(true);
								rotateButton.setEnabled(true);
								depthButton.setEnabled(false);
								break;
							}

							currentEditStatus = EditStatus.Mode3D;
						} else {
							moveButton.setEnabled(false);
							rotateButton.setEnabled(false);
							depthButton.setEnabled(false);

							whitePaintButton.setEnabled(true);
							blackPaintButton.setEnabled(true);
							if (drawPaintDataList.size() != 0) {
								undoPaintButton.setEnabled(true);
								eraserButton.setEnabled(true);
								clearCanvasButton.setEnabled(true);
							}
							if (rePaintDataList.size() != 0)
								redoPaintButton.setEnabled(true);
							if (currentPaintType == PaintType.Eraser)
								currentPaintType = PaintType.Black;
							currentEditStatus = EditStatus.Mode2D;
						}
					}
				}
			}
		});
		// ----------------------
	}

	private void paintInit() {

		// 黑色筆初始化
		BlackPaint = new Paint();
		BlackPaint.setDither(true);
		BlackPaint.setColor(Color.BLACK);
		BlackPaint.setStyle(Paint.Style.STROKE);
		BlackPaint.setStrokeJoin(Paint.Join.ROUND);
		BlackPaint.setStrokeCap(Paint.Cap.ROUND);
		BlackPaint.setStrokeWidth(30);

		// 白色筆初始化
		WhitePaint = new Paint();
		WhitePaint.setDither(true);
		WhitePaint.setColor(Color.WHITE);
		WhitePaint.setStyle(Paint.Style.STROKE);
		WhitePaint.setStrokeJoin(Paint.Join.ROUND);
		WhitePaint.setStrokeCap(Paint.Cap.ROUND);
		WhitePaint.setStrokeWidth(30);

		// 橡皮擦初始化
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
		setContentView(R.layout.activity3_page);

		ButtonInit();
		paintInit();

		FrameLayout frameLayout = (FrameLayout) this
				.findViewById(R.id.activity3_framelayout);

		mGLView = new GLSurfaceView(getApplication());
		renderer = new MyRenderer(getResources());
		mGLView.setRenderer(renderer);
		frameLayout.addView(mGLView);

		drawPanel = new DrawPanel(this);
		frameLayout.addView(drawPanel);
	}

	/*
	 * public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * if (currentEditStatus == EditStatus.Mode3D) { int pointerCount =
	 * event.getPointerCount(); // 幾個觸控點 switch (event.getAction() &
	 * MotionEvent.ACTION_MASK) {
	 * 
	 * case MotionEvent.ACTION_DOWN: tempTouchPosX = event.getX(); tempTouchPosY
	 * = event.getY(); return true;
	 * 
	 * case MotionEvent.ACTION_POINTER_DOWN: // 當兩個點以上觸碰時觸發的動作 // 記錄兩點間距離
	 * oldDistance = (float) Math.sqrt(Math.pow( (event.getX(0) -
	 * event.getX(1)), 2) + Math.pow((event.getY(0) - event.getY(1)), 2));
	 * return true;
	 * 
	 * case MotionEvent.ACTION_MOVE: if (pointerCount == 1) {// 觸碰一點時的控制
	 * 
	 * float x_delta = event.getX() - tempTouchPosX; float y_delta =
	 * event.getY() - tempTouchPosY; tempTouchPosX = event.getX(); tempTouchPosY
	 * = event.getY();
	 * 
	 * // Log.i("Position", // " event.getX: " + String.valueOf(event.getX()) //
	 * + " event.getY: " // + String.valueOf(event.getY()));
	 * 
	 * renderer.deltaTranslatePositionX = x_delta / 100.0f;
	 * renderer.deltaTranslatePositionY = y_delta / 100.0f;
	 * 
	 * renderer.deltaRotateAngleX = x_delta / -100f; renderer.deltaRotateAngleY
	 * = y_delta / -100f;
	 * 
	 * } else if (pointerCount == 2) {// 觸碰兩點時的控制
	 * 
	 * // 記錄兩點間距離 newDistance = (float) Math.sqrt(Math.pow( (event.getX(0) -
	 * event.getX(1)), 2) + Math.pow((event.getY(0) - event.getY(1)), 2));
	 * 
	 * float delta = newDistance - oldDistance;// 計算上次與這次兩點間距離的差值 //
	 * Log.i("delta", "delta" + String.valueOf(delta)); oldDistance =
	 * newDistance;
	 * 
	 * renderer.deltaScale = delta / 1000.0f; } return true;
	 * 
	 * case MotionEvent.ACTION_POINTER_UP: // 當兩個點以上離開時觸發的動作 renderer.deltaScale
	 * = 0; return true;
	 * 
	 * case MotionEvent.ACTION_UP: renderer.deltaTranslatePositionX = 0;
	 * renderer.deltaTranslatePositionY = 0;
	 * 
	 * renderer.deltaRotateAngleX = 0; renderer.deltaRotateAngleY = 0; return
	 * true;
	 * 
	 * default: break; } } try { Thread.sleep(15); } catch (Exception e) { // No
	 * need for this... } return super.onTouchEvent(event); }
	 */

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
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub

			if (currentEditStatus == EditStatus.Mode2D) {

				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						switch (event.getAction()) {

						case MotionEvent.ACTION_DOWN:

							rePaintDataList.clear();// 清除重做(Redo)的所有紀錄
							redoPaintButton.setEnabled(false);
							eraserButton.setEnabled(true);
							undoPaintButton.setEnabled(true);
							clearCanvasButton.setEnabled(true);

							currentPath = new Path();
							currentPath.moveTo(event.getX(), event.getY());

							PaintData pData = new PaintData(currentPath,
									currentPaintType);
							drawPaintDataList.add(pData);

							Logger.log("Action Draw: cuttentSize = "
									+ String.valueOf(drawPaintDataList.size()));
							return true;

						case MotionEvent.ACTION_MOVE:
							currentPath.lineTo(event.getX(), event.getY());
							Log.i("Position",
									" event.getX: "
											+ String.valueOf(event.getX())
											+ " event.getY: "
											+ String.valueOf(event.getY()));
							return true;

						default:
							break;
						}
					}
				}
			} else if (currentEditStatus == EditStatus.Mode3D) {

				int pointerCount = event.getPointerCount(); // 幾個觸控點
				switch (event.getAction() & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_DOWN:
					tempTouchPosX = event.getX();
					tempTouchPosY = event.getY();
					return true;

				case MotionEvent.ACTION_POINTER_DOWN: // 當兩個點以上觸碰時觸發的動作
					// 記錄兩點間距離
					oldDistance = (float) Math.sqrt(Math.pow(
							(event.getX(0) - event.getX(1)), 2)
							+ Math.pow((event.getY(0) - event.getY(1)), 2));

					renderer.deltaTranslatePositionX = 0;
					renderer.deltaTranslatePositionY = 0;
					renderer.deltaTranslatePositionZ = 0;

					renderer.deltaRotateAngleX = 0;
					renderer.deltaRotateAngleY = 0;
					return true;

				case MotionEvent.ACTION_MOVE:
					if (pointerCount == 1) {// 觸碰一點時的控制

						float x_delta = event.getX() - tempTouchPosX;
						float y_delta = event.getY() - tempTouchPosY;
						tempTouchPosX = event.getX();
						tempTouchPosY = event.getY();

						// 深度
						renderer.deltaTranslatePositionZ = y_delta / -100.0f;

						// 移動
						renderer.deltaTranslatePositionX = x_delta / 10.0f;
						renderer.deltaTranslatePositionY = y_delta / 10.0f;

						// 旋轉
						renderer.deltaRotateAngleX = x_delta / -100f;
						renderer.deltaRotateAngleY = y_delta / -100f;

					} else if (pointerCount == 2) {// 觸碰兩點時的控制

						// 記錄兩點間距離
						newDistance = (float) Math.sqrt(Math.pow(
								(event.getX(0) - event.getX(1)), 2)
								+ Math.pow((event.getY(0) - event.getY(1)), 2));

						float delta = newDistance - oldDistance;// 計算上次與這次兩點間距離的差值
						oldDistance = newDistance;

						// 縮放
						renderer.deltaScale = delta / 1000.0f;
					}
					return true;

				case MotionEvent.ACTION_POINTER_UP: // 當兩個點以上離開時觸發的動作
					if (event.getActionIndex() == 0) {
						tempTouchPosX = event.getX(1);
						tempTouchPosY = event.getY(1);
					} else if (event.getActionIndex() == 1) {
						tempTouchPosX = event.getX(0);
						tempTouchPosY = event.getY(0);
					}
				case MotionEvent.ACTION_UP:

					renderer.deltaTranslatePositionX = 0;
					renderer.deltaTranslatePositionY = 0;
					renderer.deltaTranslatePositionZ = 0;

					renderer.deltaRotateAngleX = 0;
					renderer.deltaRotateAngleY = 0;

					renderer.deltaScale = 0;
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
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
		drawPanel.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
		drawPanel.resume();
	}
}
