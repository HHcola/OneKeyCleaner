package my.example.onekeycleaner.data;

import java.util.Comparator;

import my.example.onekeycleaner.manager.AppInstall;

/**
 * 按照安装时间排列的APP安装列表
 * 
 * @author wuzhixu01
 *
 */
public class InstallSortableList extends SortableListDecorator<AppInstall> {
    
    private static class TimeComparator implements Comparator<AppInstall> {

        @Override
        public int compare(AppInstall lhs, AppInstall rhs) {
            long l = lhs.getLastInstallTime();
            long r = rhs.getLastInstallTime();
            // int ret = Long.compare(r, l);
            return r < l ? -1 : (r == l ? 0 : 1);
        }
    }

    public InstallSortableList(AbstractMapList<AppInstall> sortblelist) {
        super(sortblelist);
        mSortComparator = new TimeComparator();
    }

}
