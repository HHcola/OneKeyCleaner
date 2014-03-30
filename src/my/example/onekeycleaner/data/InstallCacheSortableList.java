package my.example.onekeycleaner.data;

import java.util.Comparator;

import my.example.onekeycleaner.model.CacheInfo;

public class InstallCacheSortableList extends SortableListDecorator<CacheInfo> {
    
    private static class TimeComparator implements Comparator<CacheInfo> {

 
		@Override
		public int compare(CacheInfo lhs, CacheInfo rhs) {
			// TODO Auto-generated method stub
            // int ret = Long.compare(r, l);
            return 0;
		}
    }

    public InstallCacheSortableList(AbstractMapList<CacheInfo> sortblelist) {
        super(sortblelist);
        mSortComparator = new TimeComparator();
    }
}
