package com.nearby;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity{
	
	private Button btn_SaveSetting;
	private EditText editText_NickName;
	public static String nickName;
	private SharedPreferences mySharedPreferences;	
    private SharedPreferences.Editor editor;    
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_setting);

	    btn_SaveSetting = (Button)findViewById(R.id.btn_SaveSetting);
	    editText_NickName = (EditText)findViewById(R.id.editText_NickName);
	    
	    //SharedPreferences에 이름과 모드 설정(0은 호출한 application에서만 사용할 수 있는 모드)
	    mySharedPreferences = getSharedPreferences("mysp", 0);	
	    
	    //SharedPreferences를 변경할 수 있는 에디터 연결
	    editor = mySharedPreferences.edit();
	    
	    //디폴트 닉네임 guest가 아니라면 mySharedPreferences에 저장 된 이름으로 setText
	    if(mySharedPreferences.getString("NickName", "")!="guest"){
	    	editText_NickName.setText(mySharedPreferences.getString("NickName", ""));
	    }
	    
	    btn_SaveSetting.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				editmySharedPreferences();
				Toast.makeText(SettingActivity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
				nickName = editText_NickName.getText().toString();
				nickName = mySharedPreferences.getString("NickName", "");
			}
		});
	}
	
	//editmySharedPreferences에 저장할 닉네임 수정
	public void editmySharedPreferences(){
		editor.putString("NickName", editText_NickName.getText().toString());
		editor.commit();
	}
}
