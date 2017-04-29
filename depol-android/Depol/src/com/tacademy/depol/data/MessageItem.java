package com.tacademy.depol.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageItem implements Parcelable {
	
	public int messageId;
	public int userId;
	public String userName;
	public String userPropicUri;
	public String userPosition;
	public int userRecruitStatus;
	public String text;
	public String timeStamp;
	public int isFollowed;
	public int isRead;
	

	public MessageItem(Parcel source) {
		messageId = source.readInt();	
		userId = source.readInt();		
		userName = source.readString();	
		userPropicUri = source.readString();	
		userPosition = source.readString();	
		userRecruitStatus = source.readInt();	
		text = source.readString();		
		timeStamp = source.readString();	
		isFollowed = source.readInt();	
		isRead = source.readInt();		
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(messageId);
		dest.writeInt(userId);
		dest.writeString(userName);
		dest.writeString(userPropicUri);
		dest.writeString(userPosition);
		dest.writeInt(userRecruitStatus);
		dest.writeString(text);
		dest.writeString(timeStamp);
		dest.writeInt(isFollowed);
		dest.writeInt(isRead);
	}
	
	public static Parcelable.Creator<MessageItem> CREATOR = new Parcelable.Creator<MessageItem>() {

		@Override
		public MessageItem createFromParcel(Parcel source) {
			
			return new MessageItem(source);
		}

		@Override
		public MessageItem[] newArray(int size) {
			return new MessageItem[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}
}
