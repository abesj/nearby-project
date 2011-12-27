package com.nearby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import com.data.Packet;
import com.data.RoomInfo;
import com.data.UserInfo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	private Resources res;
	private TabHost tabHost;

	// Socket IP, Port
	public static Socket clientSocket;
	private final static String SERVER_IP = "192.168.0.51";
	private final static int SERVER_PORT = 6000;

	public static ObjectOutputStream out;
	public static ObjectInputStream in;

	private SharedPreferences mySharedPreferences;
	private SharedPreferences.Editor editor;

	// User information
	public static String nickName;
	public static String phoneNumber;

	// Room information
	public static String allRoomId;
	public static String myRoomId;
	public static String roomName;

	private HashMap<String, UserInfo> hm_User = new HashMap<String, UserInfo>();
	private HashMap<String, RoomInfo> hm_AllRoom;

	private Packet packet; // 서버와 클라이언트 간에 송수신될 객체

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Connect to server
		try {
			clientSocket = new Socket(SERVER_IP, SERVER_PORT);
			Log.i("CONNECT", "Connect to server!");
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			Log.i("STREAM", "Open input/output stream!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		settingNickName();
		userRegistrationRequest(); // Server에 사용자 등록을 요청
		
		res = getResources(); // Drawable정보를 얻기 위한 Resource
		tabHost = getTabHost(); // 탭 호스트 객체 생성
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // 탭 터치시 동작을 할당하기 위한 인텐트 생성(세 가지 탭의 인텐트를 전달할 temp 변수와 같은 역할)

		// 인텐트에 RoomListActivity를 넣어서 전달
		// newTabSpec : tabHost에 새로운 TabSpec등록
		// setIndicator : 탭의 이름과 아이콘 설정
		// setContent(Intent intent) : 탭 선택시 동작할 인텐트 명시
		intent = new Intent().setClass(this, RoomListActivity.class);
		spec = tabHost
				.newTabSpec("roomlist")
				.setIndicator("RoomList",
						res.getDrawable(R.drawable.roomtab_icon_change))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// JoinRoomList Tab - 인텐트에 JoinRoomListActivity를 넣어서 전달
		intent = new Intent().setClass(this, JoinRoomListActivity.class);
		spec = tabHost
				.newTabSpec("joinroom")
				.setIndicator("JoinRoom",
						res.getDrawable(R.drawable.joinroomtab_icon_change))
				.setContent(intent);
		tabHost.addTab(spec);

		// Setting Tab - 인텐트에 SettingActivity를 넣어서 전달
		intent = new Intent().setClass(this, SettingActivity.class);
		spec = tabHost
				.newTabSpec("setting")
				.setIndicator("Setting",
						res.getDrawable(R.drawable.settingtab_icon_change))
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0); // 어플 구동시 처음 보여줄 탭 선택(가장 왼쪽탭부터 0,1,2,3...순서)
	}

	@Override
	protected void onDestroy() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public void settingNickName() {
		mySharedPreferences = getSharedPreferences("mysp", 0);
		nickName = mySharedPreferences.getString("NickName", "");
		editor = mySharedPreferences.edit();

		if (nickName == "")
			nickName = "guest";
		Log.i("Setting Nick Name", "Nick name is : " + nickName);
	}

	private void userRegistrationRequest() {
		TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		phoneNumber = telManager.getLine1Number();

		packet = new Packet(Packet.USER_PACKET, nickName, phoneNumber, null);
		Log.i("MY PACKET",
				"Packet : " + packet.getNickName() + ", "
						+ packet.getPhoneNumber());

		try {
			out.writeObject(packet); // Packet instance 전송
			Log.i("Send", "Send user info");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, RoomInfo> getHm_AllRoom() {
		return hm_AllRoom;
	}
	
}