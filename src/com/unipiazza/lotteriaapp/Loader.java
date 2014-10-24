package com.unipiazza.lotteriaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Loader extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loader);
		String SrcPath2 = "http://www.dodoftw.it/Unipiazza/loading.mp4";
	       super.onCreate(savedInstanceState);
	       VideoView myVideoView2 = (VideoView)findViewById(R.id.myvideoview1);
	       myVideoView2.setVideoPath(SrcPath2);
	       myVideoView2.setMediaController(new MediaController(this));
	       myVideoView2.requestFocus();
	       myVideoView2.start();
		final Shop shop = (Shop) getIntent().getExtras().getSerializable("shop");

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						Intent i = new Intent(Loader.this, Result.class);
						i.putExtra("shop", shop);
						startActivity(i);
					}
				});
			}
		}, 9 * 1000);


	}
}
