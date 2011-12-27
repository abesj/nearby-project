package com.nearby;

import java.io.IOException;
import java.io.OptionalDataException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import com.data.Packet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class JoinRoomListActivity extends Activity {
	
	private ArrayList<String> joinRoom;
	private ArrayAdapter<String> adapter;
	private ListView lv_joinRoom;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_joinroomlist);
		
		lv_joinRoom = (ListView)findViewById(R.id.lv_JoinRoomList);
	}

	@Override
	protected void onResume() {

		Toast.makeText(this, "참여한 방리스트 refresh", Toast.LENGTH_SHORT).show();
		requestJoinRoomList();
		super.onResume();
	}

	public void requestJoinRoomList() {
		//참여하고 있는 방 정보를 받기 위해 피킷을 보낸다.
		Packet refreshJoinRoomPacket = new Packet(Packet.JOIN_ROOMREFRESH, null, MainActivity.phoneNumber, null);
		Log.i("My Log", "내가 참여한 방의 정보를 요청 합니다.");
		
		try {
			MainActivity.out.writeObject(refreshJoinRoomPacket);
			Log.i("MY PACKET", "내가 참여하고 있는 방정보 다시 달라고 패킷 보냈어요~");
		} catch (IOException e) {
			Log.i("MY PACKET", "내가 참여하고 있는 방정보 다시 달라고 패킷 보냈는데 실패 ㅠㅠ~");
			e.printStackTrace();
		}
		
		//받으면 해야 할 일
		try {
			Packet reRefreshJoinRoomPacket = (Packet) MainActivity.in.readObject();
			joinRoom = reRefreshJoinRoomPacket.getJoinRoom();
			Log.i("My Log", "내가 참여한 방의 정보를 서버로부터 받았습니다.");
			Iterator<String> itTest = joinRoom.iterator();
			while(itTest.hasNext()){
				String test = itTest.next();
				Log.i("My Log", test);
			}
			
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		joinRoom.clear();
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, joinRoom);
		lv_joinRoom.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
