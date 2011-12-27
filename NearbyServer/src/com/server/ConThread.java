package com.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.data.Packet;
import com.data.RoomInfo;
import com.data.UserInfo;

public class ConThread extends Thread {
	
	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private Packet packet;
	
	private String roomId;
	private String roomName;
	private ArrayList<String> joinRoom = new ArrayList<String>();
	
	private String phoneNumber;
	private String nickName;
	private static int roomCount = 0;

	//refreshJoinRoom변수들
	private RoomInfo roomPack;
	private ArrayList<UserInfo> userSet;
	private Iterator<UserInfo> userIt;
	private UserInfo userPack;
	
	public ConThread(Socket clientSock) {
		this.clientSocket = clientSock;

		try {
			in = new ObjectInputStream(this.clientSocket.getInputStream());
			out = new ObjectOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				
				System.out.println("\n<Client Start>");
				packet = (Packet) in.readObject();
				System.out.println("클라이언트에게 패킷을 받았습니다 이게 뭘까요?");
				
				if (packet.getOp() == Packet.USER_PACKET) {
					System.out.println("사용자 어플리케이션 실행");
					enrollUser();
				}
				
				else if (packet.getOp() == Packet.ROOM_PACKET) {
					System.out.println("사용자의 방 등록 요청");
					createRoom();
				}
				else if(packet.getOp() == Packet.ROOM_REFRESH){
					System.out.println("사용자의 방 Refresh 요청");
					refreshRoom();
				}else if(packet.getOp() == Packet.JOIN_ROOMREFRESH){
					System.out.println("사용자가 참여한 방 Refresh 요청");
					refreshjoinRoom();
				}
				
				TEST_METHOD_ALL_USER_VIEW();

			} catch (ClassNotFoundException e) {
				System.out.println("뭐가 에러일까...?");

				break;
			} catch (IOException e) {
				System.out.println("사용자가 어플리케이션을 종료 했습니다.");
				break;
			}
		}
		try {
			out.close();
			in.close();
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Server.allUserInfo.remove(phoneNumber);
	}

	// 사용자 등록 메서드
	private void enrollUser() {
		phoneNumber = packet.getPhoneNumber();
		nickName = packet.getNickName();
		
		UserInfo tmpUser = new UserInfo(phoneNumber, nickName);	// OK!
		Server.allUserInfo.put(packet.getPhoneNumber(), tmpUser);						// OK!
	
		//TEST_ROOMMAKE();

	/*	Packet pAllRoom = new Packet(Packet.ALL_ROOM, Server.allRoomInfo);
		
		try {
			out.writeObject(pAllRoom);
			System.out.println("유저가 들어와서 모든 방 정보 송신했어요ㅎㅎ");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	// 방 생성 메서드
	private void createRoom() {
		roomId = "rid" + roomCount; // Room Id
		roomCount++;
		roomName = packet.getRoomName();
		phoneNumber = packet.getPhoneNumber();
		
		UserInfo roomMaker = Server.allUserInfo.get(phoneNumber);
		RoomInfo newRoom = new RoomInfo(roomId, roomName, roomMaker);
		
		Server.allRoomInfo.put(roomId, newRoom);

		System.out.println("Room Name is : " + roomName +"이라는 방을 "+ phoneNumber +"가 만들었어요!");
	}
	
	private void TEST_METHOD_ALL_USER_VIEW() {
		Collection<UserInfo> allUser = Server.allUserInfo.values();
		Iterator<UserInfo> it = allUser.iterator();
		System.out.println("현재 접속중인 유저는...");

		while (it.hasNext()) {
			UserInfo user = (UserInfo) it.next();
			System.out.println("nick name: " + user.getNickName() + ", phone number" + user.getPhoneNumber());
		}
	}
	
	private void TEST_ROOMMAKE() {
		UserInfo user1 = new UserInfo("01011111111", "yoonhok");
		RoomInfo room1 = new RoomInfo("rid001", "서울 사람 모여라~", user1);
		Server.allRoomInfo.put("rid001", room1);
		
		UserInfo user2 = new UserInfo("01022222222", "yoonhok2");
		RoomInfo room2 = new RoomInfo("rid002", "단국대학생만!", user2);
		Server.allRoomInfo.put("rid002", room2);

		UserInfo user3 = new UserInfo("01033333333", "yoonhok3");
		RoomInfo room3 = new RoomInfo("rid003", "심심해~", user3);
		Server.allRoomInfo.put("rid003", room3);
	}
	
	
	
	// 방 Refresh 메서드
	private void refreshRoom() {
		//현재 존재하는 모든 방 출력-----------------------------------
		Collection<RoomInfo> allRoom= Server.allRoomInfo.values();

		Iterator<RoomInfo> it = allRoom.iterator();
		while(it.hasNext()){
			RoomInfo room = it.next();
			System.out.println("보낼 것엔 무슨 방정보가 있냐면 -_-ㅋㅋ"+ room.getRoomId() +"// 방제 : " + room.getRoomName());
		}
		System.out.println("----------------------------------------------------------------------");
		//------------------------------------------------------
		
		
		//방을 refresh해줄 패킷 생성
		Packet reRefreshRoomPacket = new Packet(Packet.ROOM_REFRESH, Server.allRoomInfo);
		
		
		try {

			out.writeObject(reRefreshRoomPacket);
			out.flush();
			System.out.println("방 정보 refresh해서 송신했습니다~ " + reRefreshRoomPacket.getHm_AllRoom().size());
			//보낸 패킷 분석------------------------------------------------
			Collection<RoomInfo> values = reRefreshRoomPacket.getHm_AllRoom().values();
			Iterator<RoomInfo> it2 = values.iterator();
			
			while(it2.hasNext()){
				RoomInfo room2 = it2.next();
				System.out.println("보낸 것엔 무슨 방정보가 있냐면 -_-ㅋㅋ"+ room2.getRoomId() +"// 방제 : " + room2.getRoomName());
			}
			
			System.out.println("----------------------------------------------------------------------");
			//------------------------------------------------------------
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void refreshjoinRoom(){
		
		String phoneNumber = packet.getPhoneNumber();
		
		Collection<RoomInfo> allRoom= Server.allRoomInfo.values();
		Iterator<RoomInfo> roomIt = allRoom.iterator();

		//사용자가 참여한 방의 목록을 얻기 위해 룸 콜렉션을 돈다.
		while(roomIt.hasNext()){

			roomPack = roomIt.next();	//하나씩 룸팩에 넣는다
			System.out.println("이 방이름은 "+roomPack.getRoomName());
			userSet = roomPack.getUserList();	//그 방의 유저리스트 떼서
			userIt = userSet.iterator();		//이터레이터에 넣는다
			//유저 어레이리스트를 돈다.
			while(userIt.hasNext()){
				userPack = userIt.next();	//하나씩 유저팩에 넣는다
				System.out.println("이 유저의 폰번호는 "+userPack.getPhoneNumber());
				if(phoneNumber == userPack.getPhoneNumber()){
					//각 방의 사용자 폰번호로 비교하여 해당 방에 있으면 리스트에 add
					System.out.println("이 유저 여깄다!!");
					joinRoom.add(roomPack.getRoomName());
					System.out.println(roomPack.getRoomName());
				}else{
					System.out.println("이 유저는 이 방에 없군...");
				}
			}
		}
		
		//joinroom을 Refresh해줄 패킷을 생성
		Packet reRefreshJoinRoomPacket = new Packet(Packet.JOIN_ROOMREFRESH, null, null, joinRoom);
		try {
			out.writeObject(reRefreshJoinRoomPacket);
			System.out.println("방 참여 정보를 Refresh했습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
