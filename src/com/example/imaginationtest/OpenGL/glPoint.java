package com.example.imaginationtest.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class glPoint {

	private IntBuffer myVertexBuffer;
	private IntBuffer myColorBuffer;
	private ByteBuffer myIndexBuffer;

	public int vCount = 0;
	public int iCount = 0;
	public float yAngle = 0;
	public float zAngle = 0;

	public glPoint() {
		vCount = 4;

		// 頂點Buffer設定
		final int UNIT_SIZE = 0x10000;
		int[] vertices = new int[] { -2 * UNIT_SIZE, 3 * UNIT_SIZE, 0,// 各頂點座標
				1 * UNIT_SIZE, 1 * UNIT_SIZE, 0,//
				-1 * UNIT_SIZE, -2 * UNIT_SIZE, 0,//
				2 * UNIT_SIZE, -3 * UNIT_SIZE, 0 };

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);// Buffer
		vbb.order(ByteOrder.nativeOrder());
		myVertexBuffer = vbb.asIntBuffer();
		myVertexBuffer.put(vertices);
		myVertexBuffer.position(0);

		// 顏色Buffer設定
		final int one = 65535;
		int[] colors = new int[] { one, one, one, one, // 各頂點RGBA
				one, one, one, one, //
				one, one, one, one, //
				one, one, one, one };

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);// Buffer
		cbb.order(ByteOrder.nativeOrder());
		myColorBuffer = cbb.asIntBuffer();
		myColorBuffer.put(colors);
		myColorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// 轉動
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 1, 0, 0);

		// 第一個參數:頂點類型(Vector2,Vector3,Vector4)
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, myVertexBuffer);
		
		// 第一個參數:顏色類型(RGB,RGBA)
		gl.glColorPointer(4, GL10.GL_FIXED, 0, myColorBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
	}
}
