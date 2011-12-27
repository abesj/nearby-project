package com.data;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private String phoneNumber;	// User Phone Number
	private String nickName;			// User Nick Name
	
	
	public UserInfo () {
		
	}
	
	public UserInfo (String phoneNumber, String nickName) {
		this.phoneNumber = phoneNumber;
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
