package com.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class RoomInfo implements Serializable {
	private String roomId = null;
	private String roomName = null;
	private ArrayList<UserInfo> userList = new ArrayList<UserInfo>();

	public RoomInfo() {

	}
	
	public RoomInfo(String _roomName, UserInfo _roomMaker) {
		this.roomName = _roomName;
		userList.add(_roomMaker);
	}

	public RoomInfo(String _roomId, String _roomName, UserInfo _roomMaker) {
		this.roomId = _roomId;
		this.roomName = _roomName;
		userList.add(_roomMaker);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public ArrayList<UserInfo> getUserList() {
		return userList;
	}

	public void addUser(UserInfo user) {
		userList.add(user);
	}

	public void displayAllUser() {
		Iterator it = userList.iterator();

		while (it.hasNext()) {
			UserInfo user = (UserInfo) it.next();
			System.out.println(user.getNickName());
		}
	}
}
