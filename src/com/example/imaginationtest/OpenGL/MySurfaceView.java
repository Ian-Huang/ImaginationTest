package com.example.imaginationtest.OpenGL;

import java.nio.ByteBuffer;

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
		gl.glEnable(GL10.GL_CULL_FACE); // 打開背面剪裁
		gl.glShadeModel(GL10.GL_SMOOTH); // 著色模型:平滑著色
		gl.glFrontFace(GL10.GL_CCW); // 捲繞順序:逆時針為正面
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // 清除暫存

		gl.glMatrixMode(GL10.GL_MODELVIEW); // 當前矩陣:模式矩陣(操作物體時使用模型矩陣，如平移、旋轉、縮放等。)
		gl.glLoadIdentity(); // 當前矩陣為單位矩陣

		gl.glTranslatef(0, 0, -2.0f); // 位移
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub

		gl.glViewport(0, 0, width, height); // 視窗大小和位置
		gl.glMatrixMode(GL10.GL_PROJECTION);// 投影矩陣(設置觀察物體的方式時，選擇投影矩陣)
		gl.glLoadIdentity(); // 當前矩陣為單位矩陣

		float ratio = width / height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);// 投影模式
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER); // 關閉抗抖動(非必要:若機器的分辨率已經相當高)
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_DONT_CARE);

		gl.glClearColor(0, 0, 0, 0); // 背景色
		gl.glEnable(GL10.GL_DEPTH_TEST); // 啟動深度檢測(沒有看到的面就不會被畫)
	}
}