package com.example.imaginationtest;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.opengl.GLSurfaceView.Renderer;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

public class MyRenderer implements Renderer {

	private Resources resource;

	public float touchTurn = 0;
	public float touchTurnUp = 0;

	private long time = System.currentTimeMillis();

	private FrameBuffer fb = null;
	private World world = null;
	private RGBColor back = new RGBColor(255, 255, 255);

	private Object3D arch = null;
	private Object3D box = null;
	private Object3D cylinder = null;
	private int fps = 0;

	private Light sun = null;

	public MyRenderer(Resources res) {
		this.resource = res;
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		if (fb != null)
			fb.dispose();

		fb = new FrameBuffer(gl, w, h);

		world = new World();
		world.setAmbientLight(75, 75, 75);

		// arch = loadModel("Arch.3DS", 10);
		// arch.compile();
		// box = loadModel("Box.3DS", 10);
		// box.compile();
		cylinder = loadModel("Cylinder.3DS", 10);
		cylinder.compile();

		// world.addObject(arch);
		// world.addObject(box);
		world.addObject(cylinder);

		Camera cam = world.getCamera();
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
		cam.lookAt(SimpleVector.ORIGIN);

		sun = new Light(world);
		sun.setIntensity(175, 175, 175);
		sun.setPosition(cam.getPosition());
		MemoryHelper.compact();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void onDrawFrame(GL10 gl) {
		gl.glShadeModel(GL10.GL_SMOOTH);

		if (touchTurn != 0) {
			cylinder.rotateY(touchTurn);
			touchTurn = 0;
		}

		if (touchTurnUp != 0) {
			cylinder.rotateX(touchTurnUp);
			touchTurnUp = 0;
		}

		fb.clear(back);
		world.renderScene(fb);
		world.draw(fb);
		fb.display();

//		if (System.currentTimeMillis() - time >= 1000) {
//			Logger.log(fps + "fps");
//			fps = 0;
//			time = System.currentTimeMillis();
//		}
//		fps++;
	}

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