package com.unipiazza.lotteriaapp;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {

	private String mNome;
	private String mImgUrl;
	private String type;
	private String mVia;
	private int mGettoni;
	private int mLikes;
	private double lat;
	private double lon;
	private String cover_imgUrl;
	private String descrizione;
	private int id;
	private boolean visited;
	private boolean starred;
	private List<Prize> shop_prizes;

	public Shop(int id, String type, String imgUrl, String nome, String via
			, int gettoni, double lat, double lon
			, String cover_imgUrl, String descrizione, boolean isLiked
			, boolean visited, boolean starred, List<Prize> shop_prizes) {
		this.id = id;
		this.mNome = nome;
		this.mVia = via;
		this.mGettoni = gettoni;
		this.lat = lat;
		this.lon = lon;
		this.type = type;
		this.mImgUrl = imgUrl;

		if (cover_imgUrl.startsWith("http"))
			this.cover_imgUrl = cover_imgUrl;
		else
			this.cover_imgUrl = UnipiazzaParams.BASE_URL + cover_imgUrl;
		this.descrizione = descrizione;
		this.visited = visited;
		this.starred = starred;
		this.shop_prizes = shop_prizes;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String mNome) {
		this.mNome = mNome;
	}

	public String getImgUrl() {
		return mImgUrl;
	}

	public void setImgUrl(String mImgUrl) {
		this.mImgUrl = mImgUrl;
	}

	public String getVia() {
		return mVia;
	}

	public void setVia(String mVia) {
		this.mVia = mVia;
	}

	public int getGettoni() {
		return mGettoni;
	}

	public void setGettoni(int mGettoni) {
		this.mGettoni = mGettoni;
	}

	public int getLikes() {
		return mLikes;
	}

	public void setLikes(int mLikes) {
		this.mLikes = mLikes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getCover_imgUrl() {
		return cover_imgUrl;
	}

	public void setCover_imgUrl(String cover_imgUrl) {
		this.cover_imgUrl = cover_imgUrl;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public List<Prize> getShop_prizes() {
		return shop_prizes;
	}

	public void setShop_prizes(List<Prize> shop_prizes) {
		this.shop_prizes = shop_prizes;
	}

}
