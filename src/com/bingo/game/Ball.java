package com.bingo.game;


import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Ball extends BallKicker implements GameObject {
	
	private static final String LOG_TAG = Ball.class.getSimpleName();
	private static final boolean DEBUG = false;

	private float xAcc = 0.0f, yAcc = 0.0f;
	private float xSpeed = 0.0f, ySpeed = 0.0f;
	private float x, y;
	private float prevX, prevY;
	private double distance;

	private Paint paint = new Paint();
	private float radius;
	private float angleSpeed = 0;
	private float angle = 0;
	private int colorNum = 1;
	
	private List<Integer> colors = new ArrayList<Integer>();
	
	public Ball(float radius, float x, float y) {
		this.radius = radius;
		this.x = this.prevX = x;
		this.y = this.prevY = y;
		paint.setAntiAlias(true);
		initColors();
	}
	
	public Ball(float radius) {
		this(radius, 0, 0);
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getPrevX() {
		return prevX;
	}

	public float getPrevY() {
		return prevY;
	}

	public void setAcc(float xAcc, float yAcc) {
		this.xAcc = xAcc;
		this.yAcc = yAcc;
	}
	
	public float getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public float getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setColor(int color) {
		paint.setColor(color);
	}
	
	public void setAngleSpeed(float angleSpeed) {
		this.angleSpeed = angleSpeed;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void draw(Canvas canvas) {
		//canvas.drawCircle(x, y, radius, paint);
		RectF r = getBallRectF();
		float anglePerColor = 360.0f / colorNum;
		for (int i = 0; i < colorNum; i ++) {
			paint.setColor(colors.get(i).intValue());
			canvas.drawArc(r, angle + i * anglePerColor, anglePerColor, true, paint);
		}
		/*paint.setColor(Color.RED);
		canvas.drawArc(r, angle, 120, true, paint);
		paint.setColor(Color.GREEN);
		canvas.drawArc(r, angle + 120, 120, true, paint);
		paint.setColor(Color.BLUE);
		canvas.drawArc(r, angle + 240, 120, true, paint);*/
	}
	
	@Override
	public Rect update() {
		distance = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
		prevX = x;
		prevY = y;
		x += xSpeed;
		y += ySpeed;
		xSpeed += xAcc;
		ySpeed += yAcc;
		xSpeed *= 0.99;
		ySpeed *= 0.99;
		
		angle += angleSpeed;
		angle %= 360;
		
		RectF r = new RectF();
		r.set(x, y, prevX, prevY);
		r.sort();
		r.left -= radius - 1;
		r.right += radius + 1;
		r.top -= radius - 1;
		r.bottom += radius + 1;
		Rect rect = new Rect((int) r.left, (int) r.top, (int) r.right, (int) r.bottom);
		
		if (DEBUG) {
			Log.d(LOG_TAG, "Ball position: (" + x + ", " + y + ")");
		}
		return rect;
	}
	
	private RectF getBallRectF() {
		return new RectF(x - radius, y - radius, x + radius, y + radius);
	}
	
	public double getDistance() {
		return distance;
	}

	@Override
	public boolean tryKick(Ball ball) {
		double distance = Math.sqrt((x - ball.x) * (x - ball.x) + (y - ball.y) * (y - ball.y));
		if (distance < radius + ball.radius) {
			float speed = xSpeed;
			setXSpeed(ball.xSpeed);
			ball.setXSpeed(speed);
			
			speed = ySpeed;
			setYSpeed(ball.ySpeed);
			ball.setYSpeed(speed);
			notifyKicked();
			return true;
		}
		return false;
	}

	private void initColors() {
		colors.add(Integer.valueOf(Color.RED));
		colors.add(Integer.valueOf(Color.GREEN));
		colors.add(Integer.valueOf(Color.BLUE));
	}
	
	public void setColorNumber(int num) {
		colorNum = num;
		while (num > colors.size()) {
			doubleColors();
		}
	}
	
	private void doubleColors() {
		List<Integer> colorsToAdd = new ArrayList<Integer>();
		for (int i = 0; i < colors.size(); i ++) {
			colorsToAdd.add(Integer.valueOf(
					(colors.get(i).intValue() + 
					colors.get((i + 1) % colors.size()).intValue()) / 2));
		}
		for (int i = colors.size(), j = colorsToAdd.size() - 1 ; i > 0; i --, j --) {
			colors.add(i, colorsToAdd.get(j));
		}
	}
}
