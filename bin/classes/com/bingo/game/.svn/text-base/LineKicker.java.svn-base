package com.bingo.game;


import android.graphics.Point;
import android.util.Log;

public class LineKicker extends BallKicker {
	
	private static boolean DEBUG = false;
	
	private static final String LOG_TAG = LineKicker.class.getSimpleName();
	
	private Point start;
	private Point end;
	private boolean isHorizontal;
	
	public LineKicker(Point p1, Point p2) {
		start = p1;
		end = p2;
		isHorizontal = false;
		boolean isVertical = false;
		if (start.x == end.x) {
			isVertical = true;
			if (start.y > end.y) {
				int temp = start.y;
				start.y = end.y;
				end.y = temp;
			}
		} else if (start.y == end.y) {
			isHorizontal = true;
			if (start.x > end.x) {
				int temp = start.x;
				start.x = end.x;
				end.x = temp;
			}
		}
		if (isHorizontal && isVertical) {
			throw new RuntimeException("This is a point, not a line");
		} else if (! isHorizontal && ! isVertical) {
			throw new RuntimeException("This line is neither horizontal nor vertical");
		}
		if (DEBUG) {
			Log.d(LOG_TAG, "start: " + start + " end: " + end);
			Log.d(LOG_TAG, "isHorizontal: " + isHorizontal);
		}
	}

	@Override
	public boolean tryKick(Ball ball) {
		if (isHorizontal) {
			float delta1 = ball.getPrevY() - start.y;
			boolean up = delta1 > 0;
			float delta2 = ball.getY() - start.y + (up ? - ball.getRadius() : ball.getRadius());
			if (delta1 * delta2 < 0 && ball.getPrevX() > start.x && ball.getPrevX() < end.x) {
				ball.setYSpeed(- ball.getYSpeed() * 0.99f);
				ball.setY(ball.getPrevY() - start.y > 0 ? start.y + ball.getRadius() : start.y - ball.getRadius());
				notifyKicked();
				return true;
			}
		} else {
			float delta1 = ball.getPrevX() - start.x;
			boolean right = delta1 > 0;
			float delta2 = ball.getX() - start.x + (right ? - ball.getRadius() : ball.getRadius());
			if (delta1 * delta2 < 0 && ball.getPrevY() > start.y && ball.getPrevY() < end.y) {
				ball.setXSpeed(- ball.getXSpeed() * 0.99f);
				ball.setX(ball.getPrevX() - start.x > 0 ? start.x + ball.getRadius() : start.x - ball.getRadius());
				notifyKicked();
				return true;
			}
		}
		return false;
	}

}
