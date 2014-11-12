package com.unipiazza.lotteriaapp;

import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.JsonObject;

public class LoginActivity extends Activity {

	protected EditText email;
	protected EditText password;
	protected Button ok;
	private ProgressBar progressBar;
	private LinearLayout login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		login = (LinearLayout) findViewById(R.id.login);

		login.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);

		login.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);

		ok = (Button) findViewById(R.id.ok);

		ok.setOnClickListener(go);

		password.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					ok.performClick();
				}
				return false;
			}
		});
	}

	private HttpCallback httpCallback = new HttpCallback() {

		@Override
		public void onSuccess(JsonObject response) {
			Intent main = new Intent(LoginActivity.this, WaitingTap.class);
			startActivity(main);
			finish();
		}

		@Override
		public void onFail(JsonObject response, Throwable e) {
			onFailLogin(response, e);
		}
	};

	private OnClickListener go = new OnClickListener() {

		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ok.getWindowToken(), 0);
			login.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			LotteriaAppRESTClient.getInstance(LoginActivity.this).postAuthenticate(LoginActivity.this, email.getText().toString(), password.getText().toString(), httpCallback);

		}
	};

	private void onFailLogin(JsonObject response, Throwable e) {
		login.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		if (e instanceof UnknownHostException) {
			Utils.createErrorDialog(LoginActivity.this, R.string.error_dialog, R.string.error_dialog_internet).show();
		} else if (response != null && response.get("error").getAsBoolean()) {
			Utils.createErrorDialog(LoginActivity.this, R.string.error_dialog, response.get("error").getAsJsonObject()
					.get("msg").getAsString()).show();
		} else
			Utils.createErrorDialog(LoginActivity.this, R.string.error_dialog, R.string.login_fail).show();
	}

}
