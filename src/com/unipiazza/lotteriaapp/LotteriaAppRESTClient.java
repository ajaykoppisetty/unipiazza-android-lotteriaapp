package com.unipiazza.lotteriaapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class LotteriaAppRESTClient {

	private static final String HEADER_STRING1 = "Accept";
	private static final String HEADER_STRING2 = "application/unipiazza.v3";

	private static LotteriaAppRESTClient instance;

	public static LotteriaAppRESTClient getInstance(Context context) {
		if (instance == null) {
			return new LotteriaAppRESTClient();
		} else {
			synchronized (instance) {
				return instance;
			}
		}
	}

	public void postAuthenticate(final Context context, final String email, final String password, final HttpCallback callback) {
		Log.v("UNIPIAZZA", "postAuthenticate");
		JsonObject json = new JsonObject();
		json.addProperty("grant_type", "password");
		json.addProperty("email", email);
		json.addProperty("password", password);
		json.addProperty("scope", "admin");

		Ion.with(context)
				.load(UnipiazzaParams.LOGIN_URL)
				.setJsonObjectBody(json)
				.asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {
						Log.v("UNIPIAZZA", "postAuthenticate result=" + result);
						Log.v("UNIPIAZZA", "postAuthenticate e=" + e);
						if (e == null) {
							try {
								String access_token = result.get("access_token").getAsString();
								String refresh_token = result.get("refresh_token").getAsString();
								int expires_in = result.get("expires_in").getAsInt();
								CurrentAdmin.getInstance().setAuthenticated(context
										, access_token, refresh_token, expires_in
										, password);
								callback.onSuccess(result);
							} catch (Exception ex) {
								ex.printStackTrace();
								callback.onFail(result, ex);
							}
						} else
							callback.onFail(result, e);
					}

				});

	}

	public void refreshToken(final Context context, final String refresh_token, final HttpCallback callback) {
		Log.v("UNIPIAZZA", "refreshToken");

		JsonObject json = new JsonObject();
		json.addProperty("grant_type", "refresh_token");
		json.addProperty("refresh_token", refresh_token);
		Log.v("UNIPIAZZA", "refresh_token=" + refresh_token);

		Ion.with(context)
				.load(UnipiazzaParams.LOGIN_URL)
				.setJsonObjectBody(json)
				.asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {
						Log.v("UNIPIAZZA", "result=" + result);
						if (e == null) {
							try {
								String access_token = result.get("access_token").getAsString();
								String refresh_token = result.get("refresh_token").getAsString();
								int expires_in = result.get("expires_in").getAsInt();
								CurrentAdmin.getInstance().setToken(context, access_token, refresh_token, expires_in);
								if (callback != null)
									callback.onSuccess(result);
							} catch (Exception ex) {
								ex.printStackTrace();
								if (callback != null)
									callback.onFail(result, ex);
							}
						} else {
							if (callback != null)
								callback.onFail(result, e);
						}
					}

				});

	}

	public void postRegistration(final Context context, final String hash, final String name, final String surname
			, final String email, final boolean checked, final boolean sex, final HttpCallback callback) {
		JsonObject json = new JsonObject();
		JsonObject jsonUser = new JsonObject();
		jsonUser.addProperty("first_name", name);
		jsonUser.addProperty("last_name", surname);
		jsonUser.addProperty("email", email);
		if (checked)
			jsonUser.addProperty("hash_keychain", hash);
		else
			jsonUser.addProperty("hash_card", hash);
		jsonUser.addProperty("gender", sex);
		json.add("user", jsonUser);

		String url;
		String access_token = CurrentAdmin.getInstance().getAccessToken(context);
		url = UnipiazzaParams.REGISTER_URL + "?access_token=" + access_token;

		Ion.with(context)
				.load(url)
				.setJsonObjectBody(json)
				.asJsonObject()
				.withResponse()
				.setCallback(new FutureCallback<Response<JsonObject>>() {
					@Override
					public void onCompleted(Exception e, Response<JsonObject> result) {
						if (e == null) {
							Log.v("UNIPIAZZA", "postRegistration=" + result.getResult());
							if (result.getHeaders().getResponseCode() == 401) {
								refreshToken(context, CurrentAdmin.getInstance().getRefreshToken(context), new HttpCallback() {

									@Override
									public void onSuccess(JsonObject result) {
										postRegistration(context, hash, name, surname, email, checked, sex, callback);
									}

									@Override
									public void onFail(JsonObject result, Throwable e) {
										callback.onFail(result, e);
									}
								});
								return;
							}
							try {
								if (!result.getResult().get("error").getAsBoolean())
									callback.onSuccess(result.getResult());
								else
									callback.onFail(result.getResult(), e);
							} catch (Exception ex) {
								ex.printStackTrace();
								callback.onFail(result.getResult(), ex);
							}
						} else
						if (result != null)
							callback.onFail(result.getResult(), e);
						else
							callback.onFail(null, e);
					}
				});
	}

	public void getPrize(final Context context, final String hash, final ShopHttpCallback callback) {
		JsonObject json = new JsonObject();
		json.addProperty("hash_pass", hash);

		String url;
		String access_token = CurrentAdmin.getInstance().getAccessToken(context);
		url = UnipiazzaParams.LOTTERY_URL + "?access_token=" + access_token;

		Ion.with(context)
				.load("GET", url)
				.addHeader(HEADER_STRING1, HEADER_STRING2)
				.setJsonObjectBody(json)
				.asJsonObject()
				.withResponse()
				.setCallback(new FutureCallback<Response<JsonObject>>() {
					@Override
					public void onCompleted(Exception e, Response<JsonObject> result) {
						if (result != null && result.getHeaders().getResponseCode() == 401) {
							refreshToken(context, CurrentAdmin.getInstance().getRefreshToken(context), new HttpCallback() {

								@Override
								public void onSuccess(JsonObject result) {
									getPrize(context, hash, callback);
								}

								@Override
								public void onFail(JsonObject result, Throwable e) {
									callback.onFail(result, e);
								}
							});
							return;
						}
						if (e == null) {
							try {
								Log.v("UNIPIAZZA", "result=" + result.getResult());
								Log.v("UNIPIAZZA", "result=" + result.getHeaders().getResponseCode());
								if (result.getResult().get("error") == null) {
									JsonObject json = result.getResult().getAsJsonObject();
									Prize prize = new Prize(0
											, json.get("image_title").getAsString()
											, json.get("name").getAsString());
									Shop shop = new Shop(0, ""
											, "", json.get("shop_name").getAsString()
											, json.get("shop_address").getAsString()
											, 0
											, 0
											, 0
											, ""
											, ""
											//, json.get("user_name").getAsString()
											, false
											, false
											, false
											, null);
									List<Prize> prizes = new ArrayList<Prize>();
									prizes.add(prize);
									shop.setShop_prizes(prizes);
									callback.onSuccess(shop);
								} else {
									callback.onFail(result.getResult(), e);
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								callback.onFail(result.getResult(), ex);
							}
						} else
						if (result != null)
							callback.onFail(result.getResult(), e);
						else
							callback.onFail(null, e);
					}
				});
	}
}
