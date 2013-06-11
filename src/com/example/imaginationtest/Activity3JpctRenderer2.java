package com.example.imaginationtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Environment;

import com.example.imaginationtest.Activity3Page2.Action3DObject;
import com.example.imaginationtest.Activity3Page2.EditStatus;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

public class Activity3JpctRenderer2 implements Renderer {

	private Resources resource;

	public float deltaTranslatePositionX = 0;
	public float deltaTranslatePositionY = 0;
	public float deltaTranslatePositionZ = 0;
	public float deltaRotateAngleX = 0;
	public float deltaRotateAngleY = 0;
	public float deltaScale = 0;

	public float PointX;
	public float PointY;

	private long currentSystemTime = System.currentTimeMillis();

	public FrameBuffer frameBuffer = null;
	private World world = null;
	private RGBColor backgroundColor = new RGBColor(255, 255, 255);

	private Object3D arch = null;
	private Object3D box = null;
	private Object3D cylinder = null;
	private SimpleVector archOriginPosition;
	private SimpleVector boxOriginPosition;
	private SimpleVector cylinderOriginPosition;
	private float ZaxisCanMoveDistance = 10;

	private float colorValue = 0;
	private float colorChangeSpeed = 255;

	private Light sun = null;

	public boolean combineImage = false;

	public Bitmap GLBitmap;

	public Activity3JpctRenderer2(Resources res) {
		this.resource = res;
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) {
		if (frameBuffer != null)
			frameBuffer.dispose();

		frameBuffer = new FrameBuffer(gl, w, h);

		// ���J�ҫ�
		arch = loadModel("Arch.3DS", 15);
		arch.compile();
		box = loadModel("Box.3DS", 8);
		box.compile();
		cylinder = loadModel("Cylinder.3DS", 7);
		cylinder.compile();

		arch.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		box.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		cylinder.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);

		arch.setName("Arch");
		box.setName("Box");
		cylinder.setName("Cylinder");

		// �]�w�@�ɳ���(�[�J3�Ӽҫ�)
		world = new World();
		world.setAmbientLight(75, 75, 75);
		world.addObject(arch);
		world.addObject(box);
		world.addObject(cylinder);

		box.translate(new SimpleVector(-15, -20, 5));
		arch.translate(new SimpleVector(-15, 5, -5));

		// �O����l��m
		archOriginPosition = arch.getTranslation();
		boxOriginPosition = box.getTranslation();
		cylinderOriginPosition = cylinder.getTranslation();

		// �]�wCamera
		Camera camera = world.getCamera();
		camera.moveCamera(Camera.CAMERA_MOVEOUT, 50);// �V�Ჾ50
		camera.lookAt(SimpleVector.ORIGIN); // �ݦV(0,0,0)

		// �]�wLight
		sun = new Light(world);
		sun.setIntensity(175, 175, 175);
		sun.setPosition(camera.getPosition());

		MemoryHelper.compact();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void onDrawFrame(GL10 gl) {

		// �{���w��²�L�A�Ƶ��G�����������g�bGL�ݡAgetPixel�������GL�������
		// ���~���ecall to OpenGL ES API with no current context (logged once per
		// thread)

		if (combineImage) {
			FrameBuffer fb = frameBuffer;
			GLBitmap = Bitmap.createBitmap(fb.getPixels(), fb.getWidth(),
					fb.getHeight(), Config.ARGB_8888);

			Bitmap combineBitmap;
			combineBitmap = Bitmap.createBitmap(GLBitmap.getWidth(),
					GLBitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas comboImage = new Canvas(combineBitmap);
			comboImage.drawBitmap(GLBitmap, 0f, 0f, null);
			comboImage.drawBitmap(Activity3Page2.Activity3BitmapPaint, 0f, 0f,
					null);
			saveImage(combineBitmap);

			combineImage = false;
		}

		if (Activity3Page2.currentEditStatus == EditStatus.Mode3D) {

			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST); // �Ұʲ`���˴�(�S���ݨ쪺���N���|�Q�e)

			float deltaTime = (System.currentTimeMillis() - currentSystemTime) / 1000.0f;
			currentSystemTime = System.currentTimeMillis();
			if (colorValue >= 255) {
				colorValue = 255;
				colorChangeSpeed = -colorChangeSpeed;
			} else if (colorValue <= 0) {
				colorValue = 0;
				colorChangeSpeed = -colorChangeSpeed;
			}
			colorValue += (deltaTime * colorChangeSpeed);

			switch (Activity3Page2.currentAction3DObject) {

			case DetectObject:
				SimpleVector ray = Interact2D.reproject2D3D(world.getCamera(),
						frameBuffer, (int) PointX, (int) PointY).normalize();
				Object[] res = world.calcMinDistanceAndObject3D(world
						.getCamera().getPosition(), ray, 1000);
				if (res[1] != null) {
					String name = null;
					name = ((Object3D) res[1]).getName();
					if (name == "Arch") {
						Activity3Page2.currentAction3DObject = Action3DObject.Arch;
					} else if (name == "Box") {
						Activity3Page2.currentAction3DObject = Action3DObject.Box;
					} else if (name == "Cylinder") {
						Activity3Page2.currentAction3DObject = Action3DObject.Cylinder;
					}
				}
			case NoAction:
				arch.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				box.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				cylinder.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				break;
			case Arch:
				arch.setAdditionalColor(new RGBColor((int) colorValue, 0, 0,
						255));// ���ܦ�
				box.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				cylinder.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				Handle3DControl(arch, archOriginPosition.z);
				break;

			case Box:
				arch.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				box.setAdditionalColor(new RGBColor((int) colorValue, 0, 0, 255));// ���ܦ�
				cylinder.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				Handle3DControl(box, boxOriginPosition.z);
				break;

			case Cylinder:
				arch.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				box.setAdditionalColor(new RGBColor(0, 0, 0, 0));// ���
				cylinder.setAdditionalColor(new RGBColor((int) colorValue, 0,
						0, 255));// ���ܦ�
				Handle3DControl(cylinder, cylinderOriginPosition.z);
				break;
			}

		}
		frameBuffer.clear(backgroundColor);

		world.renderScene(frameBuffer);
		world.draw(frameBuffer);
		frameBuffer.display();

	}

	private void Handle3DControl(Object3D obj, float originZ) {
		switch (Activity3Page2.currentActionType) {
		case Move:
			// ---�B�z����(Translate Position: X,Y)---begin
			SimpleVector transPos = new SimpleVector(obj.getTranslation().x
					+ deltaTranslatePositionX, obj.getTranslation().y
					+ deltaTranslatePositionY, obj.getTranslation().z);

			SimpleVector projectScreenV3 = Interact2D.project3D2D(
					world.getCamera(), frameBuffer, transPos); // (���n!!!)�o�i�H���D3D����b2D�e������v

			if (projectScreenV3.x >= 0 && projectScreenV3.y >= 0
					&& projectScreenV3.x <= frameBuffer.getWidth()
					&& projectScreenV3.y <= frameBuffer.getHeight()) {
				obj.translate(deltaTranslatePositionX, deltaTranslatePositionY,
						0);
			}
			deltaTranslatePositionX = 0;
			deltaTranslatePositionY = 0;
			// ---�B�z����(Translate Position: X,Y)---end
			break;

		case Rotate:
			// ---�B�z����(Rotate)---begin
			if (deltaRotateAngleX != 0) {
				obj.rotateY(deltaRotateAngleX);
				deltaRotateAngleX = 0;
			}
			if (deltaRotateAngleY != 0) {
				obj.rotateX(deltaRotateAngleY);
				deltaRotateAngleY = 0;
			}
			// ---�B�z����(Rotate)---end
			break;

		case Depth:
			// ---�B�z�`��(Translate Depth: Z�b)---begin
			float currentPosZ = obj.getTranslation().z
					+ deltaTranslatePositionZ;
			if (currentPosZ > originZ - ZaxisCanMoveDistance
					&& currentPosZ < originZ + ZaxisCanMoveDistance) {
				obj.translate(0, 0, deltaTranslatePositionZ);
			}
			deltaTranslatePositionZ = 0;
			// ---�B�z�`��(Translate Depth: Z�b)---end
			break;
		}

		// ---�B�z��j�Y�p(Scale)---begin
		if (deltaScale != 0) {
			float currentScale = obj.getScale() + deltaScale;
			if (currentScale <= 0.5f) {
				obj.setScale(0.5f);
			} else if (currentScale >= 1.5f) {
				obj.setScale(1.5f);
			} else {
				obj.setScale(currentScale);
			}
			deltaScale = 0;
		}
		// ---�B�z��j�Y�p(Scale)---end
	}

	// �B�zŪ��Asset��Ƨ������ҫ�
	private Object3D loadModel(String filename, float scale) {
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

	// // �x�s�Ϥ�
	protected void saveImage(Bitmap bmScreen2) {
		// TODO Auto-generated method stub

		// �]�w�~���x�s��m
		File publicFolder = Environment
				.getExternalStoragePublicDirectory("ImaginationTest");
		if (!publicFolder.exists())
			publicFolder.mkdir();
		// �H�ϥΪ̤H�W��@��Ƨ��W�l
		File userNameFolder = new File(publicFolder, "users");
		if (!userNameFolder.exists())
			userNameFolder.mkdir();
		// �]�w�ɮצW�l

		File fileName = new File(userNameFolder, "Act3_Page" + ".png");

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
}
