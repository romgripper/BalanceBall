package com.bingo.gravity_ball;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.bingo.game.Ball;
import com.bingo.game.BallKicker;
import com.bingo.game.GameObject;
import com.bingo.game.LineKicker;

public class Border extends BallKicker implements GameObject {
	private Rect borderRect;
	private Paint paint = new Paint();
	private ArrayList<LineKicker> lineKickers = new ArrayList<LineKicker>();
	
	public Border(Rect borderRect, int color, int lineWidth) {
		this.borderRect = borderRect;
		paint.setColor(color);
		paint.setStrokeWidth(lineWidth);
		paint.setStyle(Style.STROKE);
		
		int halfWidth = lineWidth / 2;
		int left = borderRect.left + halfWidth;
		int right = borderRect.right - halfWidth;
		int top = borderRect.top + halfWidth;
		int bottom = borderRect.bottom - halfWidth;
		LineKicker kicker = new LineKicker(new Point(left, top), new Point(right, top));
		lineKickers.add(kicker);
		kicker = new LineKicker(new Point(left, top), new Point(left, bottom));
		lineKickers.add(kicker);
		kicker = new LineKicker(new Point(borderRect.right - halfWidth, top), new Point(right, bottom));
		lineKickers.add(kicker);
		kicker = new LineKicker(new Point(left, bottom), new Point(right, bottom));
		lineKickers.add(kicker);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(borderRect, paint);
	}

	@Override
	public Rect update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryKick(Ball ball) {
		boolean kicked = false;
		for (LineKicker kicker : lineKickers) {
			kicked |= kicker.tryKick(ball);
		}
		if (kicked) {
			notifyKicked();
		}
		return kicked;
	}

}
