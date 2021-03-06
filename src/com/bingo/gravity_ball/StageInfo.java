package com.bingo.gravity_ball;

public class StageInfo {
	
	private static final boolean DEBUG = true;
	private int distance;
	private int time;
	private float accCoefficient;
	private static final float ACC_COEF_START = 0.2f;
	private static final float ACC_COEF_STEP = 0.01f;
	private static final int SUPER_STAGE = 10;
	
	private StageInfo(int distance, int time, float accCoefficient) {
		this.distance = distance;
		this.time = time;
		this.accCoefficient = accCoefficient;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public int getTime() {
		return time;
	}
	
	public float getAccCoefficient() {
		return accCoefficient;
	}
	
	public static StageInfo getStageInfo(int stage) {
		return new StageInfo(getDistance(stage),
				getTime(stage),
				getAccCoeffient(stage));
	}
	
	private static float getAccCoeffient(int stage) {
		return ACC_COEF_START + ACC_COEF_STEP * stage;
	}
	
	private static int getTime(int stage) {
		stage = stage % SUPER_STAGE + 1;
		return stage * 10;
	}
	
	private static int getDistance(int stage) {
		stage = stage % SUPER_STAGE + 1;
		int distance = 0;
		for (int i = 0; i < stage; i ++) {
			distance += 1000 + i * 100;
		}
		return DEBUG ? distance / 100 : distance;
	}
}
