package com.nearby;

import java.io.IOException;

import com.data.Packet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateRoomActivity extends Activity {

	private Intent intent;

	private EditText editText_RoomName;
	private Button btn_Confirm;

	private String checkRoomName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createroom);

		editText_RoomName = (EditText) findViewById(R.id.editText_RoomName);
		btn_Confirm = (Button) findViewById(R.id.btn_Confirm);
		

		// 만든 방으로 이동하기 인텐트
		intent = new Intent(this, ChatService.class);

		// 방 생성 후 액티비티 종료
		btn_Confirm.setOnClickListener(new OnClickListener() {

			
			// Server에게 만들고자 하는 방 정보를 전송
			public void onClick(View v) {
				
				editText_RoomName.getText().toString();
				
				
				
				if (checkRoomName == "") {
					Toast.makeText(CreateRoomActivity.this, "방 제목을 입력하세요.",
							Toast.LENGTH_SHORT);
					Log.i("MyLog", "RoomName err.");
				} else {
					createRoom();
					startActivity(intent);
					finish();
				}
			}
		});
	}

	public void createRoom() {
		MainActivity.roomName = editText_RoomName.getText().toString();
		Packet pNewRoom = new Packet(Packet.ROOM_PACKET, MainActivity.roomName, MainActivity.phoneNumber, null);
		try {
			MainActivity.out.writeObject(pNewRoom);
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("CREATE ROOM", "FAILED");
		}
	}
}
