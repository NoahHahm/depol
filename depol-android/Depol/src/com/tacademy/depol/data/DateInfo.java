package com.tacademy.depol.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class DateInfo implements Serializable {

	public int pk;
	public int startYear;
	public int startMonth;
	public int endYear;
	public int endMonth;	
	public int year;
	public String text;
	public String subText;
	
	
	public DateInfo() {
		
	}
	
}
