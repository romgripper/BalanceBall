package com.bingo.gravity_ball;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bingo.gravityball.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelSelectActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	
	
	private SeekBar levelSeekBar;
	private TextView levelTextView;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select);
		setTitle(R.string.balance_ball);
		
		SharedPreferences pref = getSharedPreferences(GravityBallConstant.PREFERENCE_NAME, MODE_PRIVATE);
		int savedLevel = pref.getInt(GravityBallConstant.PREFERENCE_KEY_LEVEL, GravityBallConstant.DEFAULT_LEVEL);
		levelTextView = (TextView) findViewById(R.id.start_level);
		levelTextView.setText(String.valueOf(savedLevel));
		levelSeekBar = (SeekBar) findViewById(R.id.level_seek);
		levelSeekBar.setMax(savedLevel);
		levelSeekBar.setProgress(savedLevel);
		levelSeekBar.setOnSeekBarChangeListener(this);
		
		Button startGameButton = (Button) findViewById(R.id.start_game);
		startGameButton.setOnClickListener(this);	
		
		adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
        		//addTestDevice("7D054CA8EB0E904EEB7EF9CC8F465127").
        		build();
        adView.loadAd(adRequest);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, GravityBallGameActivity.class);
		intent.putExtra(GravityBallGameActivity.KEY_LEVEL, levelSeekBar.getProgress());
		startActivity(intent);
		finish();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		levelTextView.setText(String.valueOf(progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		adView.pause();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    adView.resume();
	}
}
