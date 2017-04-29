package com.tacademy.depol.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MultplePhotoItem implements Parcelable {
	
	public int pk;
	public String path;
	
	public MultplePhotoItem(int pk, String path) {
		this.pk = pk;
		this.path = path;
	}
	
	public MultplePhotoItem(Parcel source) {
		pk = source.readInt();	
		path = source.readString();
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pk);
		dest.writeString(path);
	}
	
	public static Parcelable.Creator<MultplePhotoItem> CREATOR = new Parcelable.Creator<MultplePhotoItem>() {

		@Override
		public MultplePhotoItem createFromParcel(Parcel source) {
			
			return new MultplePhotoItem(source);
		}

		@Override
		public MultplePhotoItem[] newArray(int size) {
			return new MultplePhotoItem[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}
}
