package com.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Packet implements Serializable {
	public static final int USER_PACKET = 1;
	public static final int ROOM_PACKET = 2;
	public static final int ALL_USER = 4;
	public static final int ALL_ROOM = 8;
	public static final int MESSAGE = 16;
	public static final int ROOM_REFRESH = 32;
	public static final int JOIN_ROOMREFRESH = 64;
	public static final int ROOM_EXIT= 128;
	
	private int op = 0;

	// User info
	private String nickName;
	private String phoneNumber;
	private ArrayList<String> joinRoom;

	// Room info
	private String roomId;
	private String roomName;
	private ArrayList<UserInfo> userList;
	
	private UserInfo userInfo = null;
	private RoomInfo roomInfo = null;
	private HashMap<String, UserInfo> hm_AllUser = new HashMap<String, UserInfo>();
	private HashMap<String, RoomInfo> hm_AllRoom = new HashMap<String, RoomInfo>();

	/*
	 * Packet 생성자는 3 개의 인자를 전달받아 packet 객체를 생성한다. infoType을 검사하여 해당 Packet이 User
	 * Info인지 Room Info인지 판별한다.
	 */
	public Packet(int infoType, String info, String phoneNumber, ArrayList<String> joinRoom) {
		if (infoType == USER_PACKET) {
			op = infoType;
			nickName = info;
			this.phoneNumber = phoneNumber;
		}

		else if (infoType == ROOM_PACKET) {
			op = infoType;
			this.roomName = info;
			this.phoneNumber = phoneNumber;
		}
		else if (infoType == ROOM_REFRESH) {
			op = infoType;
		}else if (infoType == JOIN_ROOMREFRESH) {
			op = infoType;
			this.joinRoom = joinRoom;
			this.phoneNumber = phoneNumber;
		}
		
	}
	
	// 모든 방에 대한 정보를 갖는 HashMap을 전송하기 위한 Packet 생성자
	public Packet(int infoType, HashMap<String, RoomInfo> hm_AllRoom) {
		op = infoType;
		this.hm_AllRoom = hm_AllRoom;

	}
	
	public HashMap<String, RoomInfo> getHm_AllRoom() {
		return hm_AllRoom;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getOp() {
		return op;
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

	public void setUserInfo(UserInfo uInfo) {
		userInfo = uInfo;
		op = USER_PACKET;
	}

	public void setUserInfo(String phoneNum, String nickName) {
		userInfo = new UserInfo(phoneNum, nickName);
		op = USER_PACKET;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setRoomInfo(RoomInfo rInfo) {
		roomInfo = rInfo;
		op = ROOM_PACKET;
	}

	public void setHashMapAllRoom(HashMap<String, RoomInfo> hm_AllRoom) {
		this.hm_AllRoom = hm_AllRoom;
		op = ALL_ROOM;
	}
	
	public ArrayList<String> getJoinRoom() {
		return joinRoom;
	}

	public void setJoinRoom(ArrayList<String> joinRoom) {
		this.joinRoom = joinRoom;
	}
}
