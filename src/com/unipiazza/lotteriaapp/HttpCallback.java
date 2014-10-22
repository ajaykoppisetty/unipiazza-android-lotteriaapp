package com.unipiazza.lotteriaapp;

import com.google.gson.JsonObject;

public interface HttpCallback {

	public void onSuccess(JsonObject result);

	public void onFail(JsonObject result, Throwable e);

}
