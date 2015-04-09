package com.mz.smstransfer;

import android.text.TextUtils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	EditText et_receivePhone;
	public static final String key_phone="key_phone";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
	}

	private void initView() {
		findViewById(R.id.main_btn_confirm).setOnClickListener(this);
		et_receivePhone=(EditText) findViewById(R.id.main_et_receivePhone);
		
		SharedPreferences sp=getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
		et_receivePhone.setText(sp.getString(key_phone, ""));
		et_receivePhone.setSelection(et_receivePhone.length());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_confirm:
			String phone=et_receivePhone.getText().toString();
			if(TextUtils.isEmpty(phone)){
				Toast.makeText(this, R.string.qingshurujieshouduanxinshoujihao, Toast.LENGTH_LONG).show();
				return;
			}
			SharedPreferences sp=getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
			sp.edit().putString(key_phone, phone).commit();
			Toast.makeText(this, R.string.shezhichenggong, Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}

}
