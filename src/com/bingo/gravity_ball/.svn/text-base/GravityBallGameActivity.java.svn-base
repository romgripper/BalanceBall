package com.bingo.gravity_ball;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

import com.bingo.gravityball.R;

public class GravityBallGameActivity extends Activity implements SensorEventListener, SurfaceHolder.Callback, GravityBallGame.Client {
	public static final String KEY_LEVEL = "level";
	
	private static final boolean DEBUG = false;
	private static final String LOG_TAG = GravityBallGameActivity.class.getSimpleName();
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private GravityBallGame game;
	private SurfaceView surface;
	private int orientation;
	private int startLevel = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		startLevel = intent.getIntExtra(KEY_LEVEL, startLevel);
		surface = new SurfaceView(this);		
		surface.getHolder().addCallback(this);
		setContentView(surface);
		surface.setKeepScreenOn(true);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accelerometer == null) {
			Toast.makeText(this, R.string.no_sensor, Toast.LENGTH_LONG).show();
			finish();
		}
		orientation = getWindowManager().getDefaultDisplay().getOrientation();
		Log.d(LOG_TAG, "" + orientation);
		
	}
	
	private void saveHighestLevel() {
		SharedPreferences pref = getSharedPreferences(GravityBallConstant.PREFERENCE_NAME, MODE_PRIVATE);
		int savedLevel = pref.getInt(GravityBallConstant.PREFERENCE_KEY_LEVEL, GravityBallConstant.DEFAULT_LEVEL);
		if (game != null && game.getStage() > savedLevel) {
			Editor editor = pref.edit();
			editor.putInt(GravityBallConstant.PREFERENCE_KEY_LEVEL, game.getStage());
			editor.commit();
		}
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		if (game != null) {
			game.resume();
		}
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_GAME);
	}

	// Unregister listener
	@Override
	protected void onPause() {
		sensorManager.unregisterListener(this);
		if (game != null) {
			game.pause();
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		saveHighestLevel();
	}

	// Process new reading
	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && game != null) {
			switch(orientation) {
			case Surface.ROTATION_90:
				game.setGravity(event.values[1], event.values[0]);
				break;
			case Surface.ROTATION_180:
				game.setGravity(event.values[0], - event.values[1]);
				break;			
			case Surface.ROTATION_270:
				game.setGravity(- event.values[1], - event.values[0]);	
				break;
			case Surface.ROTATION_0:
			default:
				game.setGravity(- event.values[0], event.values[1]);
				break;
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// N/A
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (game != null) {
			return;
		}
		game = new GravityBallGame(surface.getHolder());
		game.init(surface.getWidth(), surface.getHeight());
		game.setClient(this);
		game.setStage(startLevel);
		game.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (game != null) {
			game.end();
			game = null;
		}
	}

	@Override
	public void onLevelUp() {
		if (game != null) {
			Toast.makeText(this, "Level up", Toast.LENGTH_LONG).show();
			saveHighestLevel();
		}
	}

	@Override
	public void onGameOver() {
		// TODO Auto-generated method stub
		
	}
}
