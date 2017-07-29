package com.bingo.game;


public abstract class BallKicker {
	
	public static interface OnKickListener {
		void onKick();
	}
	
	private OnKickListener listener;
	
	public abstract boolean tryKick(Ball ball);
	
	public void setOnKickListener(OnKickListener listener) {
		this.listener = listener;
	}
	
	public void notifyKicked() {
		if (listener != null) {
			listener.onKick();
		}
	}
}
