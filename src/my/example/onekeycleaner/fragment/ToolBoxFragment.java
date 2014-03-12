package my.example.onekeycleaner.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.example.onekeycleaner.R;

public class ToolBoxFragment extends BaseFragment {
	private String TAG = TrashCleanFragment.class.getName();
	private Context mContent;
	private SimpleAdapter simpleAdapter;
	private GridView gridview;
	private ArrayList<HashMap<String, Object>> listImageItem;

	public ToolBoxFragment(Context mContent) {
		this.mContent = mContent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tool_box, container, false);
		InitView(view);
		InitListener();
		return view;
	}
	
	private void InitView(View view) {  
        gridview = (GridView) view.findViewById(R.id.gridview);  
    }
	private void InitListener() {
		gridview.setOnItemClickListener(onItemClickListener);
		gridview.setOnItemLongClickListener(onItemLongClickListener);
	}

	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.btn_round_systemcleaner);
		map.put("ItemText", "System Cleaner");
		mylist.add(map);

		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.btn_round_apkmanager);
		map.put("ItemText", "Apk Manager");
		mylist.add(map);

		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.btn_round_appmove);
		map.put("ItemText", "Apk Move");
		mylist.add(map);

		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.btn_round_actionpoint);
		map.put("ItemText", "Action Point");
		mylist.add(map);

		return mylist;
	}


	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Toast.makeText(ToolBoxFragment.this.getActivity(), " " + position, Toast.LENGTH_SHORT).show();
			
		}
	};

	
	private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			return false;
		}
		
	};
	@Override
	public void onStart() {
		// 生成动态数组，并且转入数据<span style="color:#ff6666;">
		// #在onStart()方法中加载数据，就在Fragment中可以动态刷新数据#</span>
		Log.v(TAG, "ToolBoxFragment:onStart");
		final String[] from = new String[] { "ItemImage", "ItemText" };
		final int[] to = new int[] { R.id.grid_view_list_image,
				R.id.grid_view_list_text };
		simpleAdapter = new SimpleAdapter(
				getActivity().getApplicationContext(), getData(),
				R.layout.grid_view_list, from, to);
		// 添加图片绑定
		simpleAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Drawable) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable) data);
					return true;
				} else
					return false;
			}
		});
		gridview.setAdapter(simpleAdapter);
		super.onStart();
	}
}
