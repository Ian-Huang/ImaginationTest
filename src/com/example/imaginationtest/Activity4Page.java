package com.example.imaginationtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity4Page extends Activity {

	enum PaintType {
		Eraser, Black
	}

	class PaintData {
		private Path paintPath;
		private PaintType paintType;

		PaintData(Path path, PaintType type) {
			this.paintPath = path;
			this.paintType = type;
		}
	}

	private PaintType currentPaintType = PaintType.Black;// 活動四僅有黑筆

	// ----路徑資訊----
	private ArrayList<PaintData> rePaintDataList = new ArrayList<PaintData>();
	private ArrayList<PaintData>[] drawPaintDataList = new ArrayList[picMaxCount
			* pageMaxCount];
	private Path currentPath;
	// ------------------------------------

	// ----畫筆初始化----
	private Paint BlackPaint;
	private Paint EraserPaint;
	// ------------------------------------

	// --------2D Canvas繪圖相關-------
	private DrawPanel[] drawPanel = new DrawPanel[picMaxCount * pageMaxCount];
	// ------------------------------------

	// --------按鈕相關--------------------
	private Button Act4_ClearButton;
	private Button Act4_EraserButton;
	private Button Act4_RedoButton;
	private Button Act4_UndoButton;
	private Button Act4_NextPageButton;
	private Button Act4_PreviousPageButton;
	private Button Act4_SaveFileButton;
	private Button Act4_NextActivity;
	// ------------------------------------

	// --------時間訊息-------------------
	private TextView Act4_Timer;
	// ------------------------------------

	// --------輸入字串-------------------
	private EditText Act4_EditText01;
	private EditText Act4_EditText02;
	// ---------------------------------------

	// --------顯示圖片訊息-------------------
	private ImageView Act4_ImageView01;
	private ImageView Act4_ImageView02;
	// ---------------------------------------

	// --------畫布-------------------
	private FrameLayout Act4_UpFrameLayout;
	private FrameLayout Act4_DownFrameLayout;
	// ---------------------------------------

	private long Countdown_Time = 600; // 倒數計時總時間 ( 單位:秒)

	// 總頁數
	private static int pageMaxCount = 28;
	// 每頁圖片數
	private static int picMaxCount = 2;
	// 每頁的文字資訊
	private String[] EditText_Collection = new String[picMaxCount
			* pageMaxCount];
	// 每頁的圖片資訊
	public static Bitmap[] Bitmap_Collection = new Bitmap[pageMaxCount];

	// 儲存畫面的Layout
	private View Layout;
	// 儲存畫面的Bitmap
	private Bitmap bitmapLayout;
	// 當前頁面編號
	private int CurrentPage = 1;
	// 當前畫布Panel編號
	private int CurrentPanel;
	// 當前按下的是上還是下Panel
	private int CurrentClickPanel;

	private boolean getpic = false;

	Bitmap bitmap[] = new Bitmap[2];

	DrawView drawView1;
	DrawView drawView2;

	private CountDownTimer timer;
	private boolean isTimeFinish = false;

	private void paintInit() {

		// 黑色筆初始化
		BlackPaint = new Paint();
		BlackPaint.setDither(true);
		BlackPaint.setColor(Color.BLACK);
		BlackPaint.setStyle(Paint.Style.STROKE);
		BlackPaint.setStrokeJoin(Paint.Join.ROUND);
		BlackPaint.setStrokeCap(Paint.Cap.ROUND);
		BlackPaint.setStrokeWidth(10);

		// 橡皮擦初始化
		EraserPaint = new Paint();
		EraserPaint.setDither(true);
		EraserPaint.setMaskFilter(null);
		EraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		EraserPaint.setARGB(1, 0, 0, 0);
		EraserPaint.setStyle(Paint.Style.STROKE);
		EraserPaint.setStrokeJoin(Paint.Join.ROUND);
		EraserPaint.setStrokeCap(Paint.Cap.ROUND);
		EraserPaint.setStrokeWidth(15);
	}

	private void drawPanelInit() {

		for (int i = 0; i < picMaxCount * pageMaxCount; i++)
			drawPaintDataList[i] = new ArrayList<PaintData>();

		drawPanel[0] = new DrawPanel(this, 0);
		drawPanel[1] = new DrawPanel(this, 1);
		Act4_UpFrameLayout.addView(drawPanel[0]);
		Act4_DownFrameLayout.addView(drawPanel[1]);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity4_page);

		this.Layout = (View) findViewById(R.id.Act4_Layout);
		this.Act4_Timer = (TextView) findViewById(R.id.Act4_Timer);
		this.Act4_EditText01 = (EditText) findViewById(R.id.Act4_editText01);
		this.Act4_EditText02 = (EditText) findViewById(R.id.Act4_editText02);
		this.Act4_ImageView01 = (ImageView) findViewById(R.id.Act4_ImageView01);
		this.Act4_ImageView02 = (ImageView) findViewById(R.id.Act4_ImageView02);
		this.Act4_UpFrameLayout = (FrameLayout) findViewById(R.id.activity4_Upframelayout);
		this.Act4_DownFrameLayout = (FrameLayout) findViewById(R.id.activity4_Downframelayout);

		// 初始化設定
		ButtonsInit();
		paintInit();
		drawPanelInit();

		Act4_PageUpdate();
		this.StartCountDownTimer();

		// 創建兩View的畫布將SurfaceView的筆畫重畫在View裡 並將其INVISIBLE(不被看到)等到要存檔時在顯示出來
		drawView1 = new DrawView(this, 0);
		drawView1.setVisibility(View.INVISIBLE);
		drawView2 = new DrawView(this, 1);
		drawView2.setVisibility(View.INVISIBLE);
		Act4_UpFrameLayout.addView(drawView1);
		Act4_DownFrameLayout.addView(drawView2);

	}

	// new!!
	public class DrawView extends View {

		// 各畫布獨立變數 用來區分上畫布與下畫布
		private int _panelCount;

		public DrawView(Context context, int count) {
			super(context);
			_panelCount = count;

		}

		@Override
		public void onDraw(Canvas canvas) {
			synchronized (drawPaintDataList) {
				CurrentPanel = (CurrentPage - 1) * 2 + _panelCount;
				for (PaintData data : drawPaintDataList[CurrentPanel]) {
					switch (data.paintType) {
					case Black:
						canvas.drawPath(data.paintPath, BlackPaint);
						break;
					case Eraser:
						canvas.drawPath(data.paintPath, EraserPaint);
						break;

					}
				}
			}
		}

	}

	void ButtonsInit() {

//		// 設定存檔Button回饋
//		this.Act4_SaveFileButton = (Button) findViewById(R.id.Act4_SaveFileButton);
//		this.Act4_SaveFileButton
//				.setOnClickListener(new Button.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// 重要備註：必須destroyDrawingCache();才能進行更新getDrawingCache();不然會抓不到新資料！
//						Layout.setDrawingCacheEnabled(true);
//						Layout.destroyDrawingCache();
//						bitmapLayout = Layout.getDrawingCache();
//						saveImage(bitmapLayout);
//					}
//				});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定上一頁Button回饋
		this.Act4_PreviousPageButton = (Button) findViewById(R.id.Act4_PreviousPageButton);
		this.Act4_PreviousPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 儲存圖片
						drawView1.setVisibility(View.VISIBLE);
						drawView2.setVisibility(View.VISIBLE);

						Layout.setDrawingCacheEnabled(true);
						Layout.destroyDrawingCache();
						bitmapLayout = Layout.getDrawingCache();
						saveImage(bitmapLayout);

						// 儲存完後隱藏
						drawView1.setVisibility(View.INVISIBLE);
						drawView2.setVisibility(View.INVISIBLE);

						// 儲存文字
						Act4_SaveEditText();
						if (CurrentPage > 1)
							CurrentPage--;
						// 更新畫面資料
						Act4_PageUpdate();

					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定下一頁Button回饋
		this.Act4_NextPageButton = (Button) findViewById(R.id.Act4_NextPageButton);
		this.Act4_NextPageButton
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {

						// 顯示兩View的畫布
						drawView1.setVisibility(View.VISIBLE);
						drawView2.setVisibility(View.VISIBLE);

						// 儲存圖片
						Layout.setDrawingCacheEnabled(true);
						Layout.destroyDrawingCache();
						bitmapLayout = Layout.getDrawingCache();
						saveImage(bitmapLayout);

						// 儲存完後隱藏
						drawView1.setVisibility(View.INVISIBLE);
						drawView2.setVisibility(View.INVISIBLE);

						// 儲存文字
						Act4_SaveEditText();
						if (CurrentPage < pageMaxCount)
							CurrentPage++;
						// 更新畫面資料
						Act4_PageUpdate();

					}
				});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定下一步Button回饋
		this.Act4_NextActivity = (Button) findViewById(R.id.Act4_NextActivity);
		this.Act4_NextActivity.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				drawView1.setVisibility(View.VISIBLE);
				drawView2.setVisibility(View.VISIBLE);
				// 儲存圖片
				Layout.setDrawingCacheEnabled(true);
				Layout.destroyDrawingCache();
				bitmapLayout = Layout.getDrawingCache();
				saveImage(bitmapLayout);

				// 出現訊息視窗
				ShowMsgDialog();
			}
		});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定清除Button回饋
		this.Act4_ClearButton = (Button) findViewById(R.id.Act4_ClearButton);
		this.Act4_ClearButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {

						drawPaintDataList[CurrentClickPanel].clear();
						rePaintDataList.clear();
						Act4_RedoButton.setEnabled(false);
						Act4_EraserButton.setEnabled(false);
						Act4_UndoButton.setEnabled(false);
						Act4_ClearButton.setEnabled(false);

						if (currentPaintType == PaintType.Eraser)
							currentPaintType = PaintType.Black;
					}
				}
			}
		});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定橡皮擦Button回饋
		this.Act4_EraserButton = (Button) findViewById(R.id.Act4_EraserButton);
		this.Act4_EraserButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (currentPaintType == PaintType.Eraser) {
					Act4_EraserButton.setText("橡皮擦");
					currentPaintType = PaintType.Black;
				} else if (currentPaintType == PaintType.Black) {
					Act4_EraserButton.setText("黑筆");
					currentPaintType = PaintType.Eraser;
				}
			}
		});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定重做Button回饋
		this.Act4_RedoButton = (Button) findViewById(R.id.Act4_RedoButton);
		this.Act4_RedoButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (rePaintDataList.size() > 0) {
							drawPaintDataList[CurrentClickPanel]
									.add(rePaintDataList.get(rePaintDataList
											.size() - 1));
							Act4_RedoButton.setEnabled(true);
							Act4_ClearButton.setEnabled(true);

							rePaintDataList.remove(rePaintDataList.size() - 1);
							if (rePaintDataList.size() == 0)
								Act4_RedoButton.setEnabled(false);

						}
					}
				}
			}
		});
		// ///////////////////////////////////////////////////////////////////////////

		// 設定還原Button回饋
		this.Act4_UndoButton = (Button) findViewById(R.id.Act4_UndoButton);
		this.Act4_UndoButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (drawPaintDataList) {
					synchronized (rePaintDataList) {
						if (drawPaintDataList[CurrentClickPanel].size() > 0) {
							rePaintDataList.add(drawPaintDataList[CurrentClickPanel]
									.get(drawPaintDataList[CurrentClickPanel]
											.size() - 1));
							Act4_RedoButton.setEnabled(true);

							drawPaintDataList[CurrentClickPanel]
									.remove(drawPaintDataList[CurrentClickPanel]
											.size() - 1);
							if (drawPaintDataList[CurrentClickPanel].size() == 0) {
								Act4_EraserButton.setEnabled(false);
								Act4_UndoButton.setEnabled(false);
								if (currentPaintType == PaintType.Eraser)
									currentPaintType = PaintType.Black;
							}
						}
					}
				}
			}
		});
		// ///////////////////////////////////////////////////////////////////////////

	}

	// 儲存圖片
	protected void saveImage(Bitmap bmScreen2) {
		// TODO Auto-generated method stub

		// 設定外部儲存位置
		File publicFolder = Environment
				.getExternalStoragePublicDirectory("ImaginationTest");
		if (!publicFolder.exists())
			publicFolder.mkdir();
		// 以使用者人名當作資料夾名子
		File userNameFolder = new File(publicFolder, "users");
		if (!userNameFolder.exists())
			userNameFolder.mkdir();
		// 設定檔案名子

		File fileName = new File(userNameFolder, "Act4_Page_"
				+ String.valueOf(CurrentPage) + ".png");

		if (fileName.exists())
			fileName.delete();

		try {
			OutputStream os = new FileOutputStream(fileName);
			bmScreen2.compress(Bitmap.CompressFormat.PNG, 80, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void Act4_SaveEditText() {
		EditText_Collection[(CurrentPage - 1) * 2 + 0] = Act4_EditText01
				.getText().toString();
		EditText_Collection[(CurrentPage - 1) * 2 + 1] = Act4_EditText02
				.getText().toString();
	}

	private void Act4_PageUpdate() {
		Act4_EditText01.setText(EditText_Collection[(CurrentPage - 1) * 2 + 0]);
		Act4_EditText02.setText(EditText_Collection[(CurrentPage - 1) * 2 + 1]);

		Act4_ImageView01.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 0);
		Act4_ImageView02.setImageResource(R.drawable.action01
				+ (CurrentPage - 1) * 2 + 1);

		if (CurrentPage == 1)
			this.Act4_PreviousPageButton.setEnabled(false);
		else
			this.Act4_PreviousPageButton.setEnabled(true);
		if (CurrentPage == 28)
			this.Act4_NextPageButton.setEnabled(false);
		else
			this.Act4_NextPageButton.setEnabled(true);

		Act4_ClearButton.setEnabled(true);

	}

	// 計時器設定
	void StartCountDownTimer() {

		timer = new CountDownTimer(Countdown_Time * 1000, 500) {

			@Override
			public void onFinish() {
				// 時間到後提示進入下一頁
				if (!isTimeFinish) {
					Act4_Timer.setText("剩餘時間    00：00");
					ShowMsgDialog();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {

				long totalSec = millisUntilFinished / 1000;
				Act4_Timer.setText("剩餘時間    "
						+ String.format("%02d", totalSec / 60) + "："
						+ String.format("%02d", totalSec % 60));
			}
		};
		timer.start();
		isTimeFinish = false;
	}

	// 彈出設窗設定:提示是否進入下一頁
	private void ShowMsgDialog() {
		Builder MyAlertDialog = new AlertDialog.Builder(this);

		// 設定對話框標題
		MyAlertDialog.setTitle("活動四");
		// 設定對話框內容
		MyAlertDialog.setMessage("時間到  停止作答！！");
		// 設定不能被取消
		MyAlertDialog.setCancelable(false);

		// Button觸發後的設定
		DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				isTimeFinish = true;
				timer.cancel();

				// 確定觸發後...
				Intent intent = new Intent();
				intent.setClass(Activity4Page.this, Activity5Page.class);
				startActivity(intent);
				// System.exit(0);
				Activity4Page.this.finish();
			}
		};

		MyAlertDialog.setNeutralButton("確定", OkClick);

		MyAlertDialog.show();
	}

	public class DrawPanel extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOk = false;
		// 各畫布獨立變數 用來區分上畫布與下畫布
		private int _panelCount;

		public DrawPanel(Context context, int panelCount) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();
			setZOrderOnTop(true);
			holder.setFormat(PixelFormat.TRANSPARENT);
			// bitmap = Bitmap.createBitmap(400,500, Bitmap.Config.ARGB_8888);
			_panelCount = panelCount;
			// set Drawing Cache Enabled
			this.setDrawingCacheEnabled(true);

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
			// http://stackoverflow.com/questions/2174875/android-canvas-to-jpg
			// http://stackoverflow.com/questions/2738834/combining-two-png-files-in-android
			super.onDraw(canvas);

			synchronized (drawPaintDataList) {
				CurrentPanel = (CurrentPage - 1) * 2 + _panelCount;
				for (PaintData data : drawPaintDataList[CurrentPanel]) {
					switch (data.paintType) {
					case Black:
						canvas.drawPath(data.paintPath, BlackPaint);
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

			synchronized (drawPaintDataList) {
				synchronized (rePaintDataList) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						// 設定當前手按下的Panel編號
						CurrentClickPanel = (CurrentPage - 1) * 2 + _panelCount;

						Act4_RedoButton.setEnabled(true);
						Act4_EraserButton.setEnabled(true);
						Act4_UndoButton.setEnabled(true);
						Act4_ClearButton.setEnabled(true);

						rePaintDataList.clear();// 清除重做(Redo)的所有紀錄

						currentPath = new Path();
						currentPath.moveTo(event.getX(), event.getY());

						PaintData pData = new PaintData(currentPath,
								currentPaintType);
						drawPaintDataList[CurrentClickPanel].add(pData);

						return true;

					case MotionEvent.ACTION_MOVE:
						currentPath.lineTo(event.getX(), event.getY());

						return true;

					default:
						break;
					}
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
		drawPanel[0].pause();
		drawPanel[1].pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawPanel[0].resume();
		drawPanel[1].resume();
	}
}
