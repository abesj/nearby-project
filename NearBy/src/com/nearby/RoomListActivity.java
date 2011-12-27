package com.nearby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.data.Packet;
import com.data.RoomInfo;

public class RoomListActivity extends Activity {

	private Intent intent;
	
	private Button btn_CreateRoom;
	private Button btn_Refresh;
	private ListView lv_AllRoomList;
	private ArrayList<String> roomList = new ArrayList<String>();

	private HashMap<String, RoomInfo> hm_AllRoom;
	private ArrayAdapter<String> adapter;
	public static ArrayList<String> al_AllRoomList = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_roomlist);

		btn_CreateRoom = (Button) findViewById(R.id.btn_CreateRoom);
		btn_Refresh = (Button) findViewById(R.id.btn_Refresh);
		lv_AllRoomList = (ListView) findViewById(R.id.lv_AllRoomList);
		
		// 방 만들기 페이지 이동 인텐트
		intent = new Intent(this, CreateRoomActivity.class);

		btn_CreateRoom.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				startActivity(intent);
			}
		});
		
		//test refresh

				btn_Refresh.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						requestRoomList2();
						roomList.clear();
						recvroomlist();
					}
				});
		
	}
	
	@Override
	protected void onResume() {
		
		Toast.makeText(this, "방리스트 refresh", Toast.LENGTH_SHORT).show();
		requestRoomList2();
		roomList.clear();
		recvroomlist();
		
		super.onResume();
	}

	
/*
	//로그인시 서버가 바로 패킷을 보내줄 경우 사용하는 방등록 메소드
	private void requestRoomList() {
		try {
			Packet reRoomPacket = (Packet) MainActivity.in.readObject();
			if (reRoomPacket.getOp() == Packet.ALL_ROOM) {
				Collection<RoomInfo> values = reRoomPacket.getHm_AllRoom().values();
				Iterator<RoomInfo> it = values.iterator();

				while (it.hasNext()) {
					RoomInfo room = it.next();
					String strRoomInfo = "[" + room.getRoomId() + "] " + room.getRoomName();
					roomList.add(strRoomInfo);
				}
				
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomList);
				
				lv_AllRoomList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	//onResume하거나 Refresh버튼 눌렀을 때 서버에 패킷을 보내 방리스트 새로 받아오는 메소드
	private void requestRoomList2() {
		Packet refreshRoomPacket = new Packet(Packet.ROOM_REFRESH, null, null, null);
		
		try {
			
			MainActivity.out.writeObject(refreshRoomPacket); // Packet instance 전송
			MainActivity.out.flush();
			Log.i("MY PACKET", "방정보 다시 달라고 패킷 보냈어요~");
		} catch (IOException e) {
			Log.i("MY PACKET", "방정보 다시 달라고 패킷 보냈는데 실패 ㅠㅠ~");
			e.printStackTrace();                                                                                                                                                                                                                                             
		}
		
			/*Packet reRefreshRoomPacket;
			while(true)
			{
			reRefreshRoomPacket = (Packet)MainActivity.in.readObject();
			if(reRefreshRoomPacket.getRoomId()  == MainActivity.myRoomId){
			}
			else
			break;
			}*/
			
			
		
		/*한번 더 readObject하는 루트 
		try {

			Packet reRefreshRoomPacket2;
			try {
				reRefreshRoomPacket2 = (Packet) MainActivity.in.readObject();
				
				Log.i("MyLog","패킷은 일단 받아 왔습니다.");
				Log.i("MyLog", "Values: " + reRefreshRoomPacket2.getHm_AllRoom().size());

				//받은 패킷의 방정보를 출력------------------------------------------------
				Collection<RoomInfo> values2 = reRefreshRoomPacket2.getHm_AllRoom().values();
				Iterator<RoomInfo> it2 = values2.iterator();
				
				while(it2.hasNext()){
					RoomInfo room2 = it2.next();
					Log.i("MyLog","받은 것엔 무슨 방정보가 있냐면 -_-ㅋㅋ"+ room2.getRoomId() +"// 방제 : " + room2.getRoomName());
				}
				//---------------------------------------------------------------
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	void recvroomlist(){

		Packet reRefreshRoomPacket;
		try {
			reRefreshRoomPacket = (Packet) MainActivity.in.readObject();
		
			Log.i("MyLog","패킷은 일단 받아 왔습니다.");
			Log.i("MyLog", "Values: " + reRefreshRoomPacket.getHm_AllRoom().size());

			//받은 패킷의 방정보를 출력------------------------------------------------
			Collection<RoomInfo> values2 = reRefreshRoomPacket.getHm_AllRoom().values();
			Iterator<RoomInfo> it2 = values2.iterator();
		
			while(it2.hasNext()){
				RoomInfo room2 = it2.next();
				Log.i("MyLog","받은 것엔 무슨 방정보가 있냐면 -_-ㅋㅋ"+ room2.getRoomId() +"// 방제 : " + room2.getRoomName());
			}
			//---------------------------------------------------------------
		
		
			if (reRefreshRoomPacket.getOp() == Packet.ROOM_REFRESH) {
				Collection<RoomInfo> values = reRefreshRoomPacket.getHm_AllRoom().values();
				Iterator<RoomInfo> it = values.iterator();
			
				while (it.hasNext()) {
					RoomInfo room = it.next();
					String strRoomInfo = "[" + room.getRoomId() + "] " + room.getRoomName();
					roomList.add(strRoomInfo);
					Log.i("MyLog","데이터는 일단 받아 왔습니다.");
				}
			
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomList);
				Log.i("MyLog", "Count: " + adapter.getCount());
				lv_AllRoomList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
