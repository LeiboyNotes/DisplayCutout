package com.displaycutout.zl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1、设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //判断手机是否是刘海
        boolean hasDisplayCutout = hasDisplayCutout(window);
        if (hasDisplayCutout) {
            //2、将内容区域延申进刘海区
            WindowManager.LayoutParams params = window.getAttributes();
            /**
             * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT  全屏模式内容下移，非全屏不受影响
             * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES  允许内容区延申进刘海区
             * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
             */
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

            //设置成沉浸式
            int flag = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flag;//追加沉浸式设置
            window.getDecorView().setSystemUiVisibility(visibility);
        }


        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
        layoutParams.topMargin = heightForDisplaycutout();
        button.setLayoutParams(layoutParams);

        RelativeLayout layout = findViewById(R.id.container);
        layout.setPadding(layout.getPaddingLeft(), heightForDisplaycutout(), layout.getPaddingRight(), layout.getPaddingBottom());
    }

    @SuppressLint("NewApi")
    private boolean hasDisplayCutout(Window window) {
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = rootView.getRootWindowInsets();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets != null) {
            displayCutout = insets.getDisplayCutout();
            if (displayCutout != null) {
                if (displayCutout.getBoundingRects() != null &&
                        displayCutout.getBoundingRects().size() > 0 && //displayCutout.getBoundingRects().size()刘海的数量
                        displayCutout.getSafeInsetTop() > 0) {//displayCutout.getSafeInsetTop()获取刘海的高度
                    return true;
                }
            }
        }
        return true;//模拟器原因   暂时设置为true
    }

    //通常情况下刘海的高度就是状态栏的高度
    public int heightForDisplaycutout() {
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0) {
            return getResources().getDimensionPixelSize(resID);
        }
        return 96;
    }
}
