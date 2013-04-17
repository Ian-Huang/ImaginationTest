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
		gl.glEnable(GL10.GL_CULL_FACE); // ���}�I���ŵ�
		gl.glShadeModel(GL10.GL_SMOOTH); // �ۦ�ҫ�:���Ƶۦ�
		gl.glFrontFace(GL10.GL_CCW); // ��¶����:�f�ɰw������
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // �M���Ȧs

		gl.glMatrixMode(GL10.GL_MODELVIEW); // ��e�x�}:�Ҧ��x�}(�ާ@����ɨϥμҫ��x�}�A�p�����B����B�Y�񵥡C)
		gl.glLoadIdentity(); // ��e�x�}�����x�}

		gl.glTranslatef(0, 0, -2.0f); // �첾
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub

		gl.glViewport(0, 0, width, height); // �����j�p�M��m
		gl.glMatrixMode(GL10.GL_PROJECTION);// ��v�x�}(�]�m�[��骺�覡�ɡA��ܧ�v�x�})
		gl.glLoadIdentity(); // ��e�x�}�����x�}

		float ratio = width / height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);// ��v�Ҧ�
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER); // �����ܧݰ�(�D���n:�Y����������v�w�g�۷�)
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_DONT_CARE);

		gl.glClearColor(0, 0, 0, 0); // �I����
		gl.glEnable(GL10.GL_DEPTH_TEST); // �Ұʲ`���˴�(�S���ݨ쪺���N���|�Q�e)
	}
}