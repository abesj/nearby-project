/*package com.dku.chat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("logo", "Start layout");
		setContentView(R.layout.logo);
		Log.i("logo", "End layout");
		initialize();
	}

	private void initialize() {
		Log.i("logo", "Start Handler");
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.i("logo", "Start Activity");
				finish();
				Log.i("logo", "End Activity");
			}
		};
		Log.i("logo", "End Handler");

		Log.i("logo", "Start ani");
		handler.sendEmptyMessageDelayed(0, 3000);
	}
}*/