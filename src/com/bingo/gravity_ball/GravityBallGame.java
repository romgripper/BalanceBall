package com.bingo.gravity_ball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;

import com.bingo.game.Ball;
import com.bingo.game.BallKicker;
import com.bingo.game.Game;
import com.bingo.game.GameObject;

public class GravityBallGame extends Game implements BallKicker.OnKickListener {

	private static final String LOG_TAG = GravityBallGame.class.getSimpleName();

	private static final String TEXT_DISTANCE = "Distance";
	private static final String TEXT_TIME_LEFT = "Time Left";
	private static final String TEXT_STAGE = "Level";
	private static final int TEXT_PADDING = 10;
	private static final int BORDER_WIDTH = 20;
	private static final int BALL_RADIUS = 20;
	private static final int UNIFIED_WIDTH = 320;
	private static final int UNIFIED_HEIGHT = 480;
	private Ball ball;
	private float distance;
	private int timeLeft;
	private int stage;
	private StageInfo stageInfo;
	private float density;
	private int width, height;

	private static final int COLORS[] = { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA };

	public GravityBallGame(SurfaceHolder holder) {
		super(holder);
	}

	public void init(int width, int height) {
		Rect rect = new Rect();
		if (width * UNIFIED_HEIGHT > height * UNIFIED_WIDTH) {
			density = ((float) height) / UNIFIED_HEIGHT;
			this.height = height;
			this.width = UNIFIED_WIDTH * height / UNIFIED_HEIGHT;
			rect.top = 0;
			rect.bottom = height - 1;
			rect.left = (width - this.width) / 2;
			rect.right = (width + this.width) / 2 - 1;
		} else {
			density = ((float) width) / UNIFIED_WIDTH;
			this.width = width;
			this.height = UNIFIED_HEIGHT * width / UNIFIED_WIDTH;
			rect.left = 0;
			rect.right = width - 1;
			rect.top = (height - this.height) / 2;
			rect.bottom = (height + this.height) / 2 - 1;
		}
		super.init(rect);
		Border border = new Border(rect, Color.WHITE, BORDER_WIDTH);
		addGameObject(border);
		ball = new Ball(BALL_RADIUS * density, width / 2, height / 2);
		addGameObject(ball);

		/*
		 * ball = new Ball(BALL_RADIUS * density, 2 * width / 3, 2 * height /3);
		 * ball.setColor(nextColor()); balls.add(ball); addGameObject(ball);
		 */
	}

	public void setStage(int stage) {
		this.stage = stage;
		stageInfo = StageInfo.getStageInfo(stage);
		timeLeft = stageInfo.getTime() * 1000;
		distance = 0;
		//ball.setColor(COLORS[stage % COLORS.length]);
		ball.setColorNumber(stage + 1);
	}
	
	public int getStage() {
		return stage;
	}

	private void stageUp() {
		setStage(stage + 1);
		notifyLevelUp();
	}

	private void restartStage() {
		setStage(stage);
	}

	public void setGravity(float xGravity, float yGravity) {

		ball.setAcc(xGravity * stageInfo.getAccCoefficient() * density, yGravity
				* stageInfo.getAccCoefficient() * density);

	}

	@Override
	protected void process() {
		distance += ball.getDistance() / density;
		if (distance > stageInfo.getDistance()) {
			stageUp();
			return;
		}
		timeLeft -= getUpdateInterval();
		if (timeLeft <= 0) {
			restartStage();
			return;
		}

		for (GameObject obj : getGameObjects()) {
			if (obj instanceof BallKicker) {
				if (obj == ball) {
					continue;
				}
				BallKicker kicker = (BallKicker) obj;
				kicker.setOnKickListener(this);
				kicker.tryKick(ball);
			}
		}
	}

	protected void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0xa040a0e0);
		paint.setTextSize(30 * density);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		String gameName = "Balance Ball";
		Rect bounds = new Rect();
		paint.getTextBounds(gameName, 0, gameName.length(), bounds);
		int textWidth = bounds.width();
		int textHeight = bounds.height();
		canvas.drawText(gameName, 
				contentRect.left + (width - textWidth) / 2, 
				contentRect.top + (height - textHeight) / 2,
				paint);
	}

	protected void drawInfoTexts(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20 * density);
		canvas.drawText(TEXT_TIME_LEFT, 
				TEXT_PADDING + BORDER_WIDTH / 2 + contentRect.left,
				contentRect.top + 60,
				paint);
		
		Rect bounds = new Rect();
		String distanceText = "" + (int) distance + "/" + (int) stageInfo.getDistance();
		paint.getTextBounds(distanceText, 0, distanceText.length(), bounds);
		int textHeight = bounds.height();
		String timeText = "" + timeLeft / 1000 + "/" + stageInfo.getTime();
		canvas.drawText(timeText,
				TEXT_PADDING + BORDER_WIDTH / 2 + contentRect.left,
				60 + textHeight + TEXT_PADDING + contentRect.top, 
				paint);

		int textWidth = bounds.width();
		canvas.drawText(distanceText,
				contentRect.right - textWidth - TEXT_PADDING - BORDER_WIDTH / 2,
				60 + textHeight + TEXT_PADDING + contentRect.top,
				paint);
		
		paint.getTextBounds(TEXT_DISTANCE, 0, TEXT_DISTANCE.length(), bounds);
		textWidth = bounds.width();
		canvas.drawText(TEXT_DISTANCE,
				contentRect.right - textWidth - TEXT_PADDING - BORDER_WIDTH / 2,
				contentRect.top + 60,
				paint);

		String levelText = TEXT_STAGE + " " + stage;
		paint.getTextBounds(levelText, 0, levelText.length(), bounds);
		textWidth = bounds.width();
		canvas.drawText(levelText,
				contentRect.left + (width - textWidth) / 2, 
				contentRect.bottom - textHeight - TEXT_PADDING,
				paint);
		
	}

	@Override
	public void onKick() {
		restartStage();
	}

}
