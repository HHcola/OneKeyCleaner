package my.example.onekeycleaner;

import my.example.onekeycleaner.ui.BaseActivity;
import my.example.onekeycleaner.ui.CleanerMainTabActivity;

import com.example.onekeycleaner.R;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class CleanerActivity extends BaseActivity {

	private static final String TAG = "CleanerActivity"; 
	private static final int GO_MAIN_ACTIVITY = 1000;
	private static final int GO_ACTIVITY_DELAY_TIME = 3*1000;
	private static String startMainTab = "FROM_CLEANERACTIVITY";
	private int flag = 1000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cleaner);
		mHandler.sendEmptyMessageDelayed(GO_MAIN_ACTIVITY, GO_ACTIVITY_DELAY_TIME);
	}

	
	private void goHomeActivity() {
		Intent intent = new Intent(CleanerActivity.this,CleanerMainTabActivity.class);
		intent.putExtra(startMainTab, flag);
		startActivity(intent);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage (Message msg) {
			switch(msg.what) {
			case GO_MAIN_ACTIVITY:
				goHomeActivity();
				break;
			default:
				break;
					
			}
		}
	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
