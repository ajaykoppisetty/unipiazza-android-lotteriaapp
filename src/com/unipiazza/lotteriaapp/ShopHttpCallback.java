package com.unipiazza.lotteriaapp;

import com.google.gson.JsonObject;

public interface ShopHttpCallback {

	public void onSuccess(Shop result);

	public void onFail(JsonObject result, Throwable e);

}
