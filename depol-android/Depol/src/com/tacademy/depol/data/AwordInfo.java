package com.tacademy.depol.data;

public class AwordInfo {
	public int pk;
	public int year;
	public String text;
	public String subText;
	
	public AwordInfo(int pk, int year, String text, String subText) {
		this.pk = pk;
		this.year = year;
		this.text = text;
		this.subText = subText;
	}
}
