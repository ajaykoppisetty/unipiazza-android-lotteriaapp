package com.unipiazza.lotteriaapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.VideoView;

import com.google.gson.JsonObject;

public class Loader extends Activity {

	private boolean downloaded;
	private boolean videoFinished = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loader);
		VideoView myVideoView2 = (VideoView) findViewById(R.id.myvideoview1);
		myVideoView2.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.loading);
		myVideoView2.setMediaController(null);
		myVideoView2.requestFocus();
		myVideoView2.start();
		final String tag_id_string = getIntent().getExtras().getString("tag_id_string");
		final Intent i = new Intent(Loader.this, Result.class);

		LotteriaAppRESTClient.getInstance(getApplicationContext()).getPrize(getApplicationContext(), tag_id_string, new ShopHttpCallback() {

			@Override
			public void onSuccess(final Shop result) {
				downloaded = true;
				i.putExtra("shop", result);
				saveUserPass(tag_id_string);
				if (videoFinished) {
					startActivity(i);
					finish();
				}
			}

			@Override
			public void onFail(JsonObject result, Throwable e) {
				if (result != null && result.get("error") != null) {
					Dialog dialog = Utils.createErrorDialog(Loader.this, R.string.error_dialog, result.get("msg").getAsString());
					dialog.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					dialog.show();
				}

			}
		});

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						if (downloaded) {
							startActivity(i);
							finish();
						} else
							videoFinished = true;
					}
				});
			}
		}, 6 * 1000);

	}

	protected void saveUserPass(String tag_id_string) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = pref.edit();
		editor.putBoolean(tag_id_string, true);
		editor.commit();
	}
}
