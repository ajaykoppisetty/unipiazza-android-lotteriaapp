package com.unipiazza.lotteriaapp;

public class UnipiazzaParams {

	public static final String BASE_URL = "http://unipiazza.herokuapp.com/";

	public static final String LOGIN_URL = BASE_URL + "oauth/token";

	public static final String REGISTER_URL = BASE_URL + "api/users/register";

	public static final String LOTTERY_URL = BASE_URL + "api/lottery/gamble";

	public static final int MAX_OCCURANCE = 400;
}
