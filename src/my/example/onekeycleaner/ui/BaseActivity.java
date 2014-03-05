package my.example.onekeycleaner.ui;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.util.Utils;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

public class BaseActivity extends FragmentActivity {

    protected LayoutInflater mInflater;
    protected Handler mHandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO
		
		InitParams();
		Utils.strategyUi(this,1);
		setContentView(R.layout.activity_base);
		
	}
	
	private void InitParams() {
		mInflater = getLayoutInflater();
		mHandler = new Handler();
		
	}
	
	private void InitView() {
		
	}
}
