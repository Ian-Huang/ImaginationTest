package com.example.imaginationtest;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

public class MyRenderer implements Renderer {

	private Resources resource;

	public float touchTurnX = 0;
	public float touchTurnY = 0;
	public float deltaScale = 0;

	private long time = System.currentTimeMillis();

	private FrameBuffer frameBuffer = null;
	private World world = null;
	private RGBColor backgroundColor = new RGBColor(255, 255, 255);

	private Object3D arch = null;
	private Object3D box = null;
	private Object3D cylinder = null;
	private int fps = 0;

	private Light sun = null;

	public MyRenderer(Resources res) {
		this.resource = res;
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		if (frameBuffer != null)
			frameBuffer.dispose();

		frameBuffer = new FrameBuffer(gl, w, h);

		// 載入模型
		arch = loadModel("Arch.3DS", 15);
		arch.compile();
		box = loadModel("Box.3DS", 8);
		box.compile();
		cylinder = loadModel("Cylinder.3DS", 7);
		cylinder.compile();

		// 設定世界場景(加入3個模型)
		world = new World();
		world.setAmbientLight(75, 75, 75);
		world.addObject(arch);
		world.addObject(box);
		world.addObject(cylinder);

		box.translate(new SimpleVector(-15, -20, 5));
		arch.translate(new SimpleVector(-15, 5, -5));

		// 設定Camera
		Camera camera = world.getCamera();
		camera.moveCamera(Camera.CAMERA_MOVEOUT, 50);// 向後移50
		camera.lookAt(SimpleVector.ORIGIN); // 看向(0,0,0)

		// 設定Light
		sun = new Light(world);
		sun.setIntensity(175, 175, 175);
		sun.setPosition(camera.getPosition());

		MemoryHelper.compact();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void onDrawFrame(GL10 gl) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		if (cylinder.getScale() < 1.5f && cylinder.getScale() > 0.5f) {
			
			cylinder.setScale(cylinder.getScale() + deltaScale);
			Log.i("Render", String.valueOf(cylinder.getScale()));
		}
		// ---處理旋轉---begin
		if (touchTurnX != 0) {
			cylinder.rotateY(touchTurnX);
			touchTurnX = 0;
		}

		if (touchTurnY != 0) {
			cylinder.rotateX(touchTurnY);
			touchTurnY = 0;
		}
		// ---處理旋轉---end

		frameBuffer.clear(backgroundColor);
		world.renderScene(frameBuffer);
		world.draw(frameBuffer);
		frameBuffer.display();

		// if (System.currentTimeMillis() - time >= 1000) {
		// Logger.log(fps + "fps");
		// fps = 0;
		// time = System.currentTimeMillis();
		// }
		// fps++;
	}

	// 處理讀取Asset資料夾內的模型
	public Object3D loadModel(String filename, float scale) {
		Resources res = this.resource;
		AssetManager asManager = res.getAssets();
		Object3D obj3D = new Object3D(0);
		Object3D objTemp = null;

		try {
			Object3D[] model = Loader.load3DS(
					asManager.open(filename, AssetManager.ACCESS_UNKNOWN),
					scale);

			for (int i = 0; i < model.length; i++) {
				objTemp = model[i];
				objTemp.setCenter(SimpleVector.ORIGIN);
				objTemp.rotateX((float) (-0.5f * Math.PI));
				objTemp.rotateMesh();
				objTemp.setRotationMatrix(new Matrix());
				obj3D = Object3D.mergeObjects(obj3D, objTemp);
				obj3D.compile();
			}
			return obj3D;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}