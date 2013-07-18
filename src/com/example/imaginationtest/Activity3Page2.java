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

	private PaintType currentPaintType = PaintType.Black;// �T�{�ثe�e�����C��(�µ��B�ǵ��B�����)
	public static EditStatus currentEditStatus = EditStatus.Mode2D;// �T�{�ثe�s�説�A�O�b2D��3D
	public static ActionType currentActionType = ActionType.Move;// �T�{�ثe3D�Ҧ����ʧ@
	public static Action3DObject currentAction3DObject = Action3DObject.NoAction;// �T�{�ثe3D�Ҧ����ʧ@

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

	// --------2D Canvasø�Ϭ���-------
	private DrawPanel drawPanel;

	// ----�e����l��----
	private Paint BlackPaint;
	private Paint GrayPaint;
	private Paint EraserPaint;

	// ----���|��T----
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
	private long Countdown_Time = 600; // �˼ƭp���`�ɶ� ( ���:��)
	private CountDownTimer timer;
	private boolean isTimeFinish = false;

	public static EditText PictureName_editText;
	EditText pictureName;

	private void ButtonInit() {

		// �]�w"�٭�"Button
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

		// �]�w"���e"Button
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

		// �]�w"�¦�"Button (�������µ�)
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

		// �]�w"�Ǧ�"Button (�������ǵ�)
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

		// �]�w"�����"Button (������������Ҧ��A�����e��)
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

		// �]�w"�M��"Button (Canvas �Ҧ���Paint�����M��)
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

		// �]�w"����"Button (3D���󱱨�)
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

		// �]�w"����"Button (3D���󱱨�)
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

		// �]�w"�`��"Button (3D���󱱨�)
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

		// �]�w"�s�����"Button
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

		// �]�w"�}�l����"Button
		this.NextActivityButton = (Button) findViewById(R.id.Act3_2_NextActivityButton);

		// �]�w"�U�@�B"Button Listener
		this.NextActivityButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowMsgDialog();
					}
				});
		// ----------------------
	}

	// �u�X�]���]�w:���ܬO�_�i�J�U�@��
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// �]�w��ܮؼ��D
		MyAlertDialog.setTitle("���ʤT");
		// �]�w��ܮؤ��e
		MyAlertDialog.setMessage("�ɶ���  ����@���I\n�i�J�U�@����");
		// �]�w����Q����
		MyAlertDialog.setCancelable(false);
		// ButtonĲ�o�᪺�]�w
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �T�wĲ�o��...

				if (PictureName_editText.getText().toString().isEmpty() == true)
					PictureName_editText.setText(pictureName.getText()
							.toString());

				// /////////////�u���P�x�s�Ϥ�
				drawView1.setVisibility(View.VISIBLE);
				// �x�s�Ϥ�
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Act3_TotalView);
				linearLayout.setDrawingCacheEnabled(true);
				linearLayout.destroyDrawingCache();
				Activity3BitmapPaint = linearLayout.getDrawingCache();

				drawView1.setVisibility(View.INVISIBLE);
				// /////////////////

				// [0620�W�[]���o�Ϥ��W����LinearLayout�����סA�n��JCPT���ϤU��
				linearLayout1 = (LinearLayout) findViewById(R.id.Act3_LinearLayout1);
				linearLayout2 = (LinearLayout) findViewById(R.id.Act3_LinearLayout2);

				// �I�sJCPT�h�X��JCPT���Ϥ��æs��(�bMyRenderer.java)
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

		MyAlertDialog.setNeutralButton("�T�w", OkClick);

		// �p�G�S���w�q���D�A�s���@�Ӷ�J��
		PictureName_editText = (EditText) findViewById(R.id.Act3_2PictureName_editText);
		if (PictureName_editText.getText().toString().isEmpty() == true) {
			MyAlertDialog.setMessage("�ɶ���  ����@���I\n�i�J�U�@���� \n �Цb�U���J���D");
			pictureName = new EditText(this);
			MyAlertDialog.setView(pictureName);
		}
		MyAlertDialog.show();
	}

	private void paintInit() {

		// �¦ⵧ��l��
		BlackPaint = new Paint();
		BlackPaint.setDither(true);
		BlackPaint.setColor(Color.BLACK);
		BlackPaint.setStyle(Paint.Style.STROKE);
		BlackPaint.setStrokeJoin(Paint.Join.ROUND);
		BlackPaint.setStrokeCap(Paint.Cap.ROUND);
		BlackPaint.setStrokeWidth(5);

		// �Ǧⵧ��l��
		GrayPaint = new Paint();
		GrayPaint.setDither(true);
		GrayPaint.setColor(Color.GRAY);
		GrayPaint.setStyle(Paint.Style.STROKE);
		GrayPaint.setStrokeJoin(Paint.Join.ROUND);
		GrayPaint.setStrokeCap(Paint.Cap.ROUND);
		GrayPaint.setStrokeWidth(5);

		// �������l��
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

	// �p�ɾ��]�w
	void StartCountDownTimer() {

		timer = new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {

				// ���P"�U�@�B"���欰
				// /////////////�u���P�x�s�Ϥ�
				drawView1.setVisibility(View.VISIBLE);
				// �x�s�Ϥ�
				LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Act3_TotalView);
				linearLayout.setDrawingCacheEnabled(true);
				linearLayout.destroyDrawingCache();
				Activity3BitmapPaint = linearLayout.getDrawingCache();

				drawView1.setVisibility(View.INVISIBLE);
				// /////////////////

				// [0620�W�[]���o�Ϥ��W����LinearLayout�����סA�n��JCPT���ϤU��
				linearLayout1 = (LinearLayout) findViewById(R.id.Act3_LinearLayout1);
				linearLayout2 = (LinearLayout) findViewById(R.id.Act3_LinearLayout2);

				// �I�sJCPT�h�X��JCPT���Ϥ��æs��(�bMyRenderer.java)
				renderer.combineImage = true;

				// �ɶ���ᴣ�ܶi�J�U�@��
				if (!isTimeFinish) {
					timerTextView.setText("�Ѿl�ɶ�    00�G00");
					ShowMsgDialog();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				timerTextView.setText("�Ѿl�ɶ�    "
						+ String.format("%02d", totalSec / 60) + "�G"
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

							rePaintDataList.clear();// �M������(Redo)���Ҧ�����
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

				int pointerCount = event.getPointerCount(); // �X��Ĳ���I
				switch (event.getAction() & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_DOWN:
					tempTouchPosX = event.getX();
					tempTouchPosY = event.getY();

					renderer.PointX = event.getX();
					renderer.PointY = event.getY();
					currentAction3DObject = Action3DObject.DetectObject;
					return true;

				case MotionEvent.ACTION_POINTER_DOWN: // �����I�H�WĲ�I��Ĳ�o���ʧ@
					// �O�����I���Z��
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
					if (pointerCount == 1) {// Ĳ�I�@�I�ɪ�����

						renderer.PointX = event.getX();
						renderer.PointY = event.getY();

						float x_delta = event.getX() - tempTouchPosX;
						float y_delta = event.getY() - tempTouchPosY;
						tempTouchPosX = event.getX();
						tempTouchPosY = event.getY();

						// �`��
						renderer.deltaTranslatePositionZ = y_delta
								/ renderer.frameBuffer.getHeight() * 25;

						// ����
						renderer.deltaTranslatePositionX = x_delta
								/ renderer.frameBuffer.getWidth() * 100;
						renderer.deltaTranslatePositionY = y_delta
								/ renderer.frameBuffer.getHeight() * 100;

						// ����
						renderer.deltaRotateAngleX = x_delta
								/ -renderer.frameBuffer.getWidth() * 25;
						renderer.deltaRotateAngleY = y_delta
								/ -renderer.frameBuffer.getHeight() * 25;

					} else if (pointerCount == 2) {// Ĳ�I���I�ɪ�����

						// �O�����I���Z��
						newDistance = (float) Math.sqrt(Math.pow(
								(event.getX(0) - event.getX(1)), 2)
								+ Math.pow((event.getY(0) - event.getY(1)), 2));

						float delta = newDistance - oldDistance;// �p��W���P�o�����I���Z�����t��
						oldDistance = newDistance;

						// �Y��
						renderer.deltaScale = delta
								/ renderer.frameBuffer.getWidth();
					}
					return true;

				case MotionEvent.ACTION_POINTER_UP: // �����I�H�W���}��Ĳ�o���ʧ@
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
