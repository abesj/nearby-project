package com.nearby;

import java.util.ArrayList;

import com.data.Packet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChatService extends Activity{

	private Intent intent;
	private Intent serviceIntent;
	public static ArrayList<String> chatList;
	private ArrayAdapter<String> Adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatservice);
		
		//====================================================
		//서버로부터 받은 메세지를 담을 어레이리스트 준비
		chatList = new ArrayList<String>();
		// 어댑터 준비
		Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatList);
		// 어댑터 연결
		ListView list = (ListView)findViewById(R.id.lv_ChatPage);
		list.setAdapter(Adapter);
		//=====================================================
		
		//서비스 시작
		serviceIntent = new Intent(this, RealChatService.class);
		startService(serviceIntent);
	}
	
	//서비스를 종료 시키는 메소드(채팅방 나가기 기능)
	public void serviceDestroy(){
	stopService(serviceIntent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//결과는 true일 때 메뉴 popup, false일 때 메뉴 disappear
		boolean result = super.onCreateOptionsMenu(menu);
		
		// Group ID없으며 참여인원, 나가기 아이템을 menu객체에 추가
		// 아이템은 순서대로 1, 2의 아이템 ID를 부여 받았다.
		// 또 순서가 0, 1순으로 되어있는데 이는 앞에 표시될지 뒤에 표시될지를 결정한다.
		menu.add(Menu.NONE, 1, 0, "참여인원");
		menu.add(Menu.NONE, 2, 1, "나가기");
		
		return result;
	}

	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	//switch case 를 이용한 메뉴 사용 이벤트 처리
	    	switch (item.getItemId()) {
	    	case 1:
	    		//현재 인원
	    		intent = new Intent(this,UserList.class);
	    		startActivity(intent);
	    		return true;
	    	case 2:
	    		//채팅방 나가기
	    		sendExitMessage();
	    		serviceDestroy();
	    		finish();
	    		return true;
	    	}
	    	return (super.onOptionsItemSelected(item));
	}
	public void sendExitMessage(){
	}
}
