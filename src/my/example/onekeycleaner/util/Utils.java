package my.example.onekeycleaner.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
	 /**
     * 是否隐藏title，是否全屏等
     * 
     * @param act
     *            需要隐藏界面
     * @param strategy
     *            策略类型 1-隐藏title，2-全屏，3-隐藏title并全屏
     * @return void
     */
    public static void strategyUi(Activity act, int strategy) {
        if (strategy == 1) {
            act.requestWindowFeature(Window.FEATURE_NO_TITLE);
        } else if (strategy == 2) {
            act.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (strategy == 3) {
            act.requestWindowFeature(Window.FEATURE_NO_TITLE);
            act.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
