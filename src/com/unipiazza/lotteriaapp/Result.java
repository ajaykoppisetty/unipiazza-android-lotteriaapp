package com.unipiazza.lotteriaapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Result extends Activity {

	private ImageView prize_image;
	private TextView prize_text;
	private TextView shop_name;
	private TextView shop_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
	       super.onCreate(savedInstanceState);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						finish();
					}
				});
			}
		}, 9 * 2000);

		prize_image = (ImageView) findViewById(R.id.prize_image);
		prize_text = (TextView) findViewById(R.id.prize_text);
		shop_name = (TextView) findViewById(R.id.shop_name);
		shop_text = (TextView) findViewById(R.id.shop_text);

		Shop shop = (Shop) getIntent().getExtras().getSerializable("shop");

		prize_text.setText(shop.getShop_prizes().get(0).getDescrizione());
		prize_image.setImageResource(Utils.getPrizeImageByTitle(shop.getShop_prizes().get(0).getNome()));

		shop_name.setText(shop.getNome());
		shop_text.setText(shop.getVia());
	}
}
