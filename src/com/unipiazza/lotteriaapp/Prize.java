package com.unipiazza.lotteriaapp;

import java.io.Serializable;

public class Prize implements Serializable {

	private int coins;
	private String nome;
	private String descrizione;

	public Prize(int coins, String nome, String descrizione) {
		this.coins = coins;
		this.nome = nome;
		this.descrizione = descrizione;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
