package com.example.imaginationtest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threed.jpct.Logger;

public class Activity3Page2 extends Activity {

	enum EditStatus {
		Mode2D, Mode3D
	}

	enum ActionType {
		Move, Rotate, Depth
	}

	enum Action3DObject {
		NoAction, DetectObject, Arch, Box, Cylinder
	}

	enum PaintType {
		Eraser, Gray, Black
	}

	class PaintData {
		private Path paintPath;
		private PaintType paintType;

		PaintData(Path path, PaintType type) {
			this.paintPath = path;
			this.paintType = type;
		}
	}

	private PaintType currentPaintType = PaintType.Black;// 確認目前畫筆的顏色(黑筆、灰筆、橡皮擦)
	public static EditStatus currentEditStatus = EditStatus.Mode2D;// 確認目前編輯狀態是在2D或3D
	public static ActionType currentActionType = ActionType.Move;// 確認目前3D模式的動作
	public static Action3DObject currentAction3DObject = Action3DObject.NoAction;// 確認目前3D模式的動作

	// ----------Button----------
	private Button undoPaintButton;
	private Button redoPaintButton;
	private Button eraserButton;
	private Button grayPaintButton;
	private Button blackPaintButton;
	private Button clearCanvasButton;

	private Button editChangeButton;
	private Button moveButton;
	private Button rotateButton;
	private Button depthButton;

	private Button NextActivityButton;
	// -----------------------------

	// --------2D Canvas繪圖相關-------
	private DrawPanel drawPanel;

	// ----畫筆初始化----
	private Paint BlackPaint;
	private Paint GrayPaint;
	private Paint EraserPaint;

	// ----路徑資訊----
	private ArrayList<PaintData> drawPaintDataList = new ArrayList<PaintData>();
	private ArrayList<PaintData> rePaintDataList = new ArrayList<PaintData>();
	private Path currentPath;
	// ------------------------------------

	private GLSurfaceView mGLView;
	private Activity3JpctRenderer2 renderer = null;

	private float tempTouchPosX = -1;
	private float tempTouchPosY = -1;
	private float oldDistance = -1;
	private float newDistance = -1;

	DrawView drawView1;

	public static Bitmap Activity3BitmapPaint;
	public static LinearLayout linearLayout1, linearLayout2;

	private TextView timerTextView;
	private long Countdown_Time = 600; // 倒數計時總時間 ( 單位:秒)
	private CountDownTimer timer;
	private boolean isTimeFinish = false;

	public static EditText PictureName_editText;
	EditText pictureName;

	private void ButtonInit() {

		// 設定"還原"Button
		undoPaintButton = (Button) findViewById(R.id.Act3_2_UndoPaintButton);
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
		redoPaintButton = (Button) findViewById(R.id.Act3_2_RedoPaintButton);
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
		blackPaintButton = (Button) findViewById(R.id.Act3_2_BlackPaintButton);
		blackPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Black;
				
				blackPaintButton.setEnabled(false);
				grayPaintButton.setEnabled(true);
				eraserButton.setEnabled(true);
				
			}
		});
		// ----------------------

		// 設定"灰色"Button (切換為灰筆)
		grayPaintButton = (Button) findViewById(R.id.Act3_2_GrayPaintButton);
		grayPaintButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Gray;
				
				blackPaintButton.setEnabled(true);
				grayPaintButton.setEnabled(false);
				eraserButton.setEnabled(true);
			}
		});
		// ----------------------

		// 設定"橡皮擦"Button (切換為橡皮擦模式，擦除畫筆)
		eraserButton = (Button) findViewById(R.id.Act3_2_EraserButton);
		eraserButton.setEnabled(false);
		eraserButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPaintType = PaintType.Eraser;
				
				blackPaintButton.setEnabled(true);
				grayPaintButton.setEnabled(true);
				eraserButton.setEnabled(false);
			}
		});
		// ----------------------

		// 設定"清除"Button (Canvas 所有的Paint全部清除)
		clearCanvasButton = (Button) findViewById(R.id.Act3_2_ClearCanvasButton);
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
		moveButton = (Button) findViewById(R.id.Act3_2_MoveButton);
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
		rotateButton = (Button) findViewById(R.id.Act3_2_RotateButton);
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
		depthButton = (Button) findViewById(R.id.Act3_2_DepthButton);
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
		editChangeButton = (Button) findViewById(R.id.Act3_2_EditChangeButton);
		editChangeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (currentEditStatus == EditStatus.Mode2D) {
							undoPaintButton.setEnabled(false);
							redoPaintButton.setEnabled(false);
							eraserButton.setEnabled(false);
							grayPaintButton.setEnabled(false);
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

							grayPaintButton.setEnabled(true);
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

		// 設定"開始測驗"Button
		this.NextActivityButton = (Button) findViewById(R.id.Act3_2_NextActivityButton);

		// 設定"下一步"Button Listener
		this.NextActivityButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowMsgDialog();
					}
				});
		// ----------------------
	}

	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動三");
		// 設定對話框內容
		MyAlertDialog.setMessage("時間到  停止作答！\n進入下一活動");
		// 設定不能被取消
		MyAlertDialog.setCancelable(false);
		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 確定觸發後...

				if (PictureName_editText.getText().toString().isEmpty() == true)
					PictureName_editText.setText(pictureName.getText()
							.toString());

				// /////////////線條與儲存圖片
				drawView1.setVisibility(View.VISIBLE);
				// 儲存圖片
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Act3_TotalView);
				linearLayout.setDrawingCacheEnabled(true);
				linearLayout.destroyDrawingCache();
				Activity3BitmapPaint = linearLayout.getDrawingCache();

				drawView1.setVisibility(View.INVISIBLE);
				// /////////////////

				// [0620增加]取得圖片上方兩個LinearLayout的高度，要把JCPT的圖下移
				linearLayout1 = (LinearLayout) findViewById(R.id.Act3_LinearLayout1);
				linearLayout2 = (LinearLayout) findViewById(R.id.Act3_LinearLayout2);

				// 呼叫JCPT去合併JCPT的圖片並存圖(在MyRenderer.java)
				renderer.combineImage = true;

				isTimeFinish = true;
				timer.cancel();
				Intent intent = new Intent();
				intent.setClass(Activity3Page2.this,
						Activity4IntroductionPage.class);
				startActivity(intent);
				Activity3Page2.this.finish();
				// System.exit(0);

			}
		};

		MyAlertDialog.setNeutralButton("確定", OkClick);

		// 如果沒有定義標題，新給一個填入框
		PictureName_editText = (EditText) findViewById(R.id.Act3_2PictureName_editText);
		if (PictureName_editText.getText().toString().isEmpty() == true) {
			MyAlertDialog.setMessage("時間到  停止作答！\n進入下一活動 \n 請在下方輸入標題");
			pictureName = new EditText(this);
			MyAlertDialog.setView(pictureName);
		}
		MyAlertDialog.show();
	}

	private void paintInit() {

		// 黑色筆初始化
		BlackPaint = new Paint();
		BlackPaint.setDither(true);
		BlackPaint.setColor(Color.BLACK);
		BlackPaint.setStyle(Paint.Style.STROKE);
		BlackPaint.setStrokeJoin(Paint.Join.ROUND);
		BlackPaint.setStrokeCap(Paint.Cap.ROUND);
		BlackPaint.setStrokeWidth(5);

		// 灰色筆初始化
		GrayPaint = new Paint();
		GrayPaint.setDither(true);
		GrayPaint.setColor(Color.GRAY);
		GrayPaint.setStyle(Paint.Style.STROKE);
		GrayPaint.setStrokeJoin(Paint.Join.ROUND);
		GrayPaint.setStrokeCap(Paint.Cap.ROUND);
		GrayPaint.setStrokeWidth(5);

		// 橡皮擦初始化
		EraserPaint = new Paint();
		EraserPaint.setDither(true);
		EraserPaint.setMaskFilter(null);
		EraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		EraserPaint.setARGB(1, 0, 0, 0);
		EraserPaint.setStyle(Paint.Style.STROKE);
		EraserPaint.setStrokeJoin(Paint.Join.ROUND);
		EraserPaint.setStrokeCap(Paint.Cap.ROUND);
		EraserPaint.setStrokeWidth(20);
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3_page2);

		this.timerTextView = (TextView) findViewById(R.id.Act3_2Timer_textView);

		ButtonInit();
		paintInit();

		this.StartCountDownTimer();

		FrameLayout frameLayout = (FrameLayout) this
				.findViewById(R.id.activity3_2_framelayout);

		mGLView = new GLSurfaceView(getApplication());
		renderer = new Activity3JpctRenderer2(getResources());
		mGLView.setRenderer(renderer);
		frameLayout.addView(mGLView);
		drawPanel = new DrawPanel(this);
		frameLayout.addView(drawPanel);

		// new!!//INVISIBLE
		drawView1 = new DrawView(this);
		drawView1.setVisibility(View.INVISIBLE);
		frameLayout.addView(drawView1);
	}

	// 計時器設定
	void StartCountDownTimer() {

		timer = new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {

				// 等同"下一步"的行為
				// /////////////線條與儲存圖片
				drawView1.setVisibility(View.VISIBLE);
				// 儲存圖片
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Act3_TotalView);
				linearLayout.setDrawingCacheEnabled(true);
				linearLayout.destroyDrawingCache();
				Activity3BitmapPaint = linearLayout.getDrawingCache();

				drawView1.setVisibility(View.INVISIBLE);
				// /////////////////

				// [0620增加]取得圖片上方兩個LinearLayout的高度，要把JCPT的圖下移
				linearLayout1 = (LinearLayout) findViewById(R.id.Act3_LinearLayout1);
				linearLayout2 = (LinearLayout) findViewById(R.id.Act3_LinearLayout2);

				// 呼叫JCPT去合併JCPT的圖片並存圖(在MyRenderer.java)
				renderer.combineImage = true;

				// 時間到後提示進入下一頁
				if (!isTimeFinish) {
					timerTextView.setText("剩餘時間    00：00");
					ShowMsgDialog();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("剩餘時間    "
						+ String.format("%02d", totalSec / 60) + "："
						+ String.format("%02d", totalSec % 60));
			}
		};
		timer.start();
		isTimeFinish = false;
	}

	// new!!
	public class DrawView extends View {

		public DrawView(Context context) {
			super(context);
		}

		@Override
		public void onDraw(Canvas canvas) {
			synchronized (drawPaintDataList) {
				for (PaintData data : drawPaintDataList) {
					switch (data.paintType) {
					case Black:
						canvas.drawPath(data.paintPath, BlackPaint);
						break;
					case Gray:
						canvas.drawPath(data.paintPath, GrayPaint);
						break;
					case Eraser:
						canvas.drawPath(data.paintPath, EraserPaint);
						break;
					}
				}

			}
		}

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
					case Gray:
						canvas.drawPath(data.paintPath, GrayPaint);
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

					renderer.PointX = event.getX();
					renderer.PointY = event.getY();
					currentAction3DObject = Action3DObject.DetectObject;
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

						renderer.PointX = event.getX();
						renderer.PointY = event.getY();

						float x_delta = event.getX() - tempTouchPosX;
						float y_delta = event.getY() - tempTouchPosY;
						tempTouchPosX = event.getX();
						tempTouchPosY = event.getY();

						// 深度
						renderer.deltaTranslatePositionZ = y_delta
								/ renderer.frameBuffer.getHeight() * 25;

						// 移動
						renderer.deltaTranslatePositionX = x_delta
								/ renderer.frameBuffer.getWidth() * 100;
						renderer.deltaTranslatePositionY = y_delta
								/ renderer.frameBuffer.getHeight() * 100;

						// 旋轉
						renderer.deltaRotateAngleX = x_delta
								/ -renderer.frameBuffer.getWidth() * 25;
						renderer.deltaRotateAngleY = y_delta
								/ -renderer.frameBuffer.getHeight() * 25;

					} else if (pointerCount == 2) {// 觸碰兩點時的控制

						// 記錄兩點間距離
						newDistance = (float) Math.sqrt(Math.pow(
								(event.getX(0) - event.getX(1)), 2)
								+ Math.pow((event.getY(0) - event.getY(1)), 2));

						float delta = newDistance - oldDistance;// 計算上次與這次兩點間距離的差值
						oldDistance = newDistance;

						// 縮放
						renderer.deltaScale = delta
								/ renderer.frameBuffer.getWidth();
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
					renderer.deltaTranslatePositionX = 0;
					renderer.deltaTranslatePositionY = 0;
					renderer.deltaTranslatePositionZ = 0;

					renderer.deltaRotateAngleX = 0;
					renderer.deltaRotateAngleY = 0;

					renderer.deltaScale = 0;
					return true;

				case MotionEvent.ACTION_UP:

					renderer.deltaTranslatePositionX = 0;
					renderer.deltaTranslatePositionY = 0;
					renderer.deltaTranslatePositionZ = 0;

					renderer.deltaRotateAngleX = 0;
					renderer.deltaRotateAngleY = 0;

					renderer.deltaScale = 0;

					// renderer.PointX = -999;
					// renderer.PointY = -999;
					currentAction3DObject = Action3DObject.NoAction;
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
