package com.bingo.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

public abstract class Game {
	
	public static interface Client {
		
		void onLevelUp();
		
		void onGameOver();
	}
	
	private static final int DEFAULT_UPDATE_INTERVAL = 20;
	
	protected Rect contentRect;
	private SurfaceHolder holder;
	private Thread thread;
	private int updateInterval = DEFAULT_UPDATE_INTERVAL;
	private boolean paused;
	private boolean ended;
	private ArrayList<GameObject> gameObjs = new ArrayList<GameObject>();
	private Client client;
	private Handler mainThreadHandler;
	
	public Game(SurfaceHolder holder) {
		this.holder = holder;
		mainThreadHandler = new Handler();
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public void init(Rect contentRect) {
		this.contentRect = contentRect;
	}
	
	protected void setUpdateInterval(int interval) {
		this.updateInterval = interval;
	}
	
	protected int getUpdateInterval() {
		return this.updateInterval;
	}
	
	protected void notifyLevelUp() {
		if (client != null) {
			final Client c = client;
			mainThreadHandler.post(new Runnable() {
				public void run() {
					c.onLevelUp();
				}
			});
		}
	}
	
	protected void notifyGameOver() {
		if (client != null) {
			final Client c = client;
			mainThreadHandler.post(new Runnable() {
				public void run() {
					c.onGameOver();
				}
			});
		}
	}
	
	protected void addGameObject(GameObject obj) {
		gameObjs.add(obj);
	}
	
	protected Rect update() {
		//Rect dirtyRect = new Rect();
		for (GameObject obj : gameObjs) {
			//dirtyRect.union(obj.update());
			obj.update();
		}
		//return dirtyRect;
		return new Rect(contentRect);
	}
	
	public List<GameObject> getGameObjects() {
		return gameObjs;
	}
	
	protected abstract void process();
	
	protected abstract void drawBackground(Canvas canvas);
	
	protected abstract void drawInfoTexts(Canvas canvas);
	
	protected void drawContent(Canvas canvas) {
		for (GameObject obj : gameObjs) {
			obj.draw(canvas);
		}
	}
	
	protected void draw(Rect dirtyRect) {
		Canvas canvas = holder.lockCanvas(dirtyRect);
		if (null != canvas) {
			drawBackground(canvas);
			drawContent(canvas);
			drawInfoTexts(canvas);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void start() {
		thread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && ! ended) {
					try {
						Thread.sleep(updateInterval);
					} catch (InterruptedException e) {
					}
					if (! isPaused()) {
						Rect dirtyRect = update();
						process();
						draw(dirtyRect);
					}
				}
			}
		});
		thread.start();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	private boolean isPaused() {
		return paused;
	}
	
	public void end() {
		ended = true;
		if (null != thread)
			thread.interrupt();
	}
}
