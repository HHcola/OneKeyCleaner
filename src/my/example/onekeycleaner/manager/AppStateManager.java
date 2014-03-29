package my.example.onekeycleaner.manager;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * APP状态管理器，可通过该管理器注册一系列APP状态监听器
 * 
 * @author wuzhixu01
 *
 */
public class AppStateManager {

    private static final boolean DEBUG = true;

    private static final String TAG = "AppStateManager";

    /** Singleton */
    private static AppStateManager sIntance;

    /** Application Context */
    private Context mContext;

    /** UI线程Handler */
    private Handler mUiHandler;

    /** APP状态变化监听器列表 */
    private ArrayList<AppStateChangeListener> mStateChangeListeners;

    /** APP监听状态过滤器列表 */
    private ArrayList<AppStateFilter> mStateFilters; 

    /** APP下载进度变化监听器列表 */
    private ArrayList<AppProgressChangeListener> mProgressChangeListeners;

    /** APP下载数量变化监听器列表 */
    private ArrayList<AppCountChangeListener> mCountChangeListeners;

    /** APP更新变化监听器列表 */
    private ArrayList<AppUpdateChangeListener> mUpdateChangeListeners;

    private ConcurrentHashMap<String, AppState> mAppStates;

    /**
     * APP下载管理状态，包括未下载，启动下载，等待下载，下载中，暂停，继续，下载完成，删除下载，
     * 正在安装，已安装，卸载，可更新，已更新。
     */
    public enum AppState {
        /** 未下载 */
        UNDOWNLOAD(0x01),
        /** 启动下载 */
        STARTUP(0x01 << 1),
        /** 等待下载 */
        WAITING(0x01 << 2),
        /** 正在下载 */
        DOWNLOADING(0x01 << 3),
        /** 暂停状态 */
        PAUSED(0x01 << 4),
        /** 继续状态 */
        RESUME(0x01 << 5),
        /** 下载完成 */
        FINISH(0x01 << 6),
        /** 下载错误 */
        FAILED(0x01 << 7),
        /** 删除下载 */
        DELETE(0x01 << 8),
        /** 正在安装 */
        INSTALLING(0x01 << 9),
        /** 已安装 */
        INSTALLED(0x01 << 10),
        /** 卸载 */
        UNINSTALLED(0x01 << 11),
        /** 该版本可更新 */
        UPDATABLE(0x01 << 12),
        /** 可更新到该新版本 */
        UPDATETO(0x01 << 13),
        /** 已更新状态 */
        UPDATED(0x01 << 14);

        public int value;
        
        AppState(int v) {
            value = v;
        }

        public int getStateCode() {
            return value;
        }
    }

    /**
     * APP状态过滤器，可为APP状态监听器注册感兴趣的APP状态
     * 
     * @author wuzhixu01
     *
     */
    public static class AppStateFilter {
        // 默认监听所有状态
        private int mFilter = 0xFFFFFFFF;

        /**
         * 监听感兴趣的APP状态，其他状态被忽略
         * 
         * @param appStates
         */
        public void listenState(AppState... appStates) {
            if (appStates != null) {
                mFilter = 0;
                for (AppState state : appStates) {
                    mFilter |= state.value;
                }
            }
        }

        /**
         * 忽略不感兴趣的APP状态，其他状态接受监听
         * 
         * @param appStates
         */
        public void ignoreState(AppState... appStates) {
            if (appStates != null) {
                mFilter = 0xFFFFFFFF;
                for (AppState state : appStates) {
                    mFilter &= ~state.value;
                }
            }
        }

        /**
         * 过滤器中是否包含添加感兴趣的APP状态
         * 
         * @param appStates
         */
        public boolean contains(AppState appState) {
            if (appState != null) {
                return (mFilter & appState.value) != 0;
            }
            return false; 
        }
    }

    private AppStateManager(Context context) {
        mContext = context.getApplicationContext();
        mStateChangeListeners = new ArrayList<AppStateChangeListener>();
        mStateFilters = new ArrayList<AppStateManager.AppStateFilter>();
        mProgressChangeListeners = new ArrayList<AppProgressChangeListener>();
        mCountChangeListeners = new ArrayList<AppCountChangeListener>();
        mUpdateChangeListeners = new ArrayList<AppUpdateChangeListener>();

        mAppStates = new ConcurrentHashMap<String, AppStateManager.AppState>();

        mUiHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized AppStateManager getInstance(Context context) {
        if(sIntance == null) {
            sIntance = new AppStateManager(context);
        }
        return sIntance;
    }

    /**
     * 仅限用于各个Manager初始化APP数据时被调用，更高级别的状态会覆盖低级别，
     * 防止同个APP在多个Manager中被异步初始化时，覆盖有效的状态。如，安装管
     * 理器会把APP初始化为INSTALLED，更新管理器中会重新初始化为UPDATABLE。
     * 
     * @param appKey
     * @param state
     */
    public void initAppState(String appKey, AppState state) {
        AppState initState = mAppStates.get(appKey);
        if (initState == null) {
            mAppStates.put(appKey, state);
            if (DEBUG) {
                Log.d(TAG, "initAppState= " + appKey + " : " + state);
            }
        } else if (initState.value < state.value) {
            mAppStates.put(appKey, state);
            if (DEBUG) {
                Log.d(TAG, "initAppState= " + appKey + " : " + state);
            }
        }
    }

    /**
     * 删除APP状态管理器中保存的状态
     * 
     * @param appKey
     */
    public void removeAppState(String appKey) {
        mAppStates.remove(appKey);
    }

    /**
     * 获取APP当前的状态
     * 
     * @param appKey
     */
    public AppState getAppState(String appKey) {
        return mAppStates.get(appKey);
    }

    /**
     * 注册APP状态变化监听器，监听所有状态
     * 
     * @param listener
     */
    public void registerAppStateChangeListener(AppStateChangeListener listener) {
        registerAppStateChangeListener(listener, null);
    }

    /**
     * 注册APP状态变化监听器，监听状态过滤器中注册的状态
     * 
     * @param listener APP状态变化监听器
     * @param filter 监听状态过滤器
     */
    public void registerAppStateChangeListener(AppStateChangeListener listener,
            AppStateFilter filter) {
        if (listener == null) {
            return;
        }

        synchronized (mStateChangeListeners) {
            if (!mStateChangeListeners.contains(listener)) {
                mStateChangeListeners.add(listener);
                mStateFilters.add(filter);
            }
        }
    }

    /**
     * 注销下载状态变化监听器
     * 
     * @param listener
     */
    public void unregisterAppStateChangeListener(AppStateChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mStateChangeListeners) {
            int index = mStateChangeListeners.indexOf(listener);
            if (index != -1) {
                mStateChangeListeners.remove(index);
                mStateFilters.remove(index);
            }
        }
    }

    /**
     * 注册下载进度变化监听器
     * 
     * @param listener
     */
    public void registerAppProgressChangeListener(
            AppProgressChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mProgressChangeListeners) {
            if (!mProgressChangeListeners.contains(listener)) {
                mProgressChangeListeners.add(listener);
            }
        }
    }

    /**
     * 注销下载进度变化监听器
     * 
     * @param listener
     */
    public void unregisterAppProgressChangeListener(
            AppProgressChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mProgressChangeListeners) {
            mProgressChangeListeners.remove(listener);
        }
    }

    /**
     * 注册APP数量变化监听器
     * 
     * @param listener
     */
    public void registerAppCountChangeListener(
            AppCountChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mCountChangeListeners) {
            if (!mCountChangeListeners.contains(listener)) {
                mCountChangeListeners.add(listener);
            }
        }
    }

    /**
     * 注销APP数量变化监听器
     * 
     * @param listener
     */
    public void unregisterAppCountChangeListener(
            AppCountChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mCountChangeListeners) {
            mCountChangeListeners.remove(listener);
        }
    }

    /**
     * 注册APP数量变化监听器
     * 
     * @param listener
     */
    public void registerAppUpdateChangeListener(
            AppUpdateChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mUpdateChangeListeners) {
            if (!mUpdateChangeListeners.contains(listener)) {
                mUpdateChangeListeners.add(listener);
            }
        }
    }

    /**
     * 注销APP数量变化监听器
     * 
     * @param listener
     */
    public void unregisterAppUpdateChangeListener(
            AppUpdateChangeListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (mUpdateChangeListeners) {
            mUpdateChangeListeners.remove(listener);
        }
    }

    /**
     * 通知下载状态变化的所有监听器
     * 
     * @param downloadId
     */
    public void notifyStateChange(final String appKey, final AppState state) {
        // 保存所有APP状态
        mAppStates.put(appKey, state);

        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mStateChangeListeners) {
                    if (DEBUG) {
                        Log.d(TAG, "notifyStateChange appKey=" + appKey
                                + " state=" + state);
                    }
                    AppStateChangeListener listener;
                    AppStateFilter filter;
                    int size = mStateChangeListeners.size();
                    for (int i = 0; i < size; i++) {
                        listener = mStateChangeListeners.get(i);
                        filter = mStateFilters.get(i);

                        // 过滤器为空，则通知所有状态变化
                        // 否则只通知注册的状态变化
                        if (filter == null || filter.contains(state)) {
                            listener.onStateChanged(appKey, state);
                        }
                    }
                }
            }
        });
    }

    /**
     * 通知下载进度变化的所有监听器
     * 
     * @param downloadId
     * @param percentage
     */
    public void notifyProgressChange(final String appKey, final int percentage) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mProgressChangeListeners) {
                    if (DEBUG) {
                        Log.d(TAG, "notifyProgressChange appKey=" + appKey
                                + " percentage=" + percentage);
                    }
                    for (AppProgressChangeListener listener : mProgressChangeListeners) {
                        listener.onProgressChanged(appKey, percentage);
                    }
                }
            }
        });
    }

    /**
     * 通知APP数量变化的所有监听器
     * 
     * @param downloadId
     * @param percentage
     */
    public void notifyCountChange(final int dCount) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mCountChangeListeners) {
                    if (DEBUG) {
                        Log.d(TAG, "notifyCountChange downloading count=" + dCount);
                    }
                    for (AppCountChangeListener listener : mCountChangeListeners) {
                        listener.onCountChanged(dCount);
                    }
                }
            }
        });
    }

    /**
     * 通知APP可更新数量变化的所有监听器
     * 
     * @param downloadId
     * @param percentage
     */
    public void notifyUpdateChange(final int count) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mUpdateChangeListeners) {
                    if (DEBUG) {
                        Log.d(TAG, "notifyUpdateChange updatable app count=" + count);
                    }
                    for (AppUpdateChangeListener listener : mUpdateChangeListeners) {
                        listener.onUpdateChanged(count);
                    }
                }
            }
        });
    }

    /**
     * 下载状态变化监听器
     * 
     * @author wuzhixu01
     * 
     */
    public interface AppStateChangeListener {
        void onStateChanged(String appKey, AppState state);
    }

    /**
     * 下载进度变化监听器
     * 
     * @author wuzhixu01
     * 
     */
    public interface AppProgressChangeListener {
        void onProgressChanged(String appKey, int percentage);
    }

    /**
     * 下载数量变化监听器
     * 
     * @author wuzhixu01
     * 
     */
    public interface AppCountChangeListener {
        void onCountChanged(int dCount);
    }

    /**
     * APP更新变化监听器
     * 
     * @author wuzhixu01
     * 
     */
    public interface AppUpdateChangeListener {
        void onUpdateChanged(int count);
    }
}
