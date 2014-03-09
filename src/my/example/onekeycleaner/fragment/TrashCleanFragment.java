package my.example.onekeycleaner.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.onekeycleaner.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TrashCleanFragment extends ListFragment {

	private String TAG = TrashCleanFragment.class.getName();
	private Context mContent;
	private SimpleAdapter adapter; 
	public TrashCleanFragment(Context mContent) {
		this.mContent = mContent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        final String[] from = new String[] {"title", "img"};
        final int[] to = new int[] {R.id.trash_list_text, R.id.trash_list_image};
		// 填充数据
		adapter = new SimpleAdapter(getActivity(),getData(),R.layout.trash_list_item,
				from,
				to);
		this.setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.trash_clear_fragment, container, false);
		return view;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> mylist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "app cache clear");
		map.put("img", R.drawable.trash_group_cache);
		mylist.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "install package clear");
		map.put("img", R.drawable.trash_group_apk);
		mylist.add(map);
		
		
		map = new HashMap<String, Object>();
		map.put("title", "remain files clear");
		map.put("img", R.drawable.trash_group_file);
		mylist.add(map);
		
		return mylist;
	}

	@Override  
    public void onListItemClick(ListView listview, View v, int position, long id) {  
        super.onListItemClick(listview, v, position, id);  
          
        System.out.println(listview.getChildAt(position));  
        HashMap<String, Object> view= (HashMap<String, Object>) listview.getItemAtPosition(position);  
        System.out.println(view.get("title").toString()+"+++++++++title");  
  
        Toast.makeText(getActivity(), TAG+listview.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();  
        System.out.println(v);  
          
        System.out.println(position);  
          
          
    }  
}
