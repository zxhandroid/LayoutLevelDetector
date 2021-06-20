package com.zxh.layout_level_detector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.TreeSet;

public class LayoutLevelDetector {

    private int limitLevel = 4;     //最大限制的布局层级，默认是4层
    private static volatile LayoutLevelDetector instance;
    private LayoutLevelDetector(){}

    public static LayoutLevelDetector getInstance() {
        if (instance == null) {
            synchronized (LayoutLevelDetector.class) {
                if (instance == null) {
                    instance = new LayoutLevelDetector();
                }
            }
        }
        return instance;
    }

    /**
     * 设置布局最大限制层级
     * @param limitLevel
     */
    public void setLimitLevel(int limitLevel) {
        this.limitLevel = limitLevel;
    }

    /**
     * 初始化操作，主要是注册生命周期监听
     * @param application
     */
    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(new LayoutLevelLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                super.onActivityStarted(activity);
                performDetector(activity);
            }
        });
    }

    /**
     * 执行布局检测
     * @param activity
     */
    private void performDetector(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        ViewGroup contentParent = decorView.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentParent != null) {
            //将系统默认的 ID_ANDROID_CONTENT 这个层级去除掉
            int maxLevel = getMaxLevel(contentParent) - 1;
            if (maxLevel >= limitLevel) {
                showAlertDialog(activity,maxLevel);
            }
        }
    }


    /**
     * 获取最大层级
     * @param contentParent
     * @return
     */
    private int getMaxLevel(ViewGroup contentParent) {
        int childCount = contentParent.getChildCount();
        TreeSet<Integer> result = new TreeSet<>();
        result.add(0);
        boolean haveViewGroup = false;
        for (int i = 0; i < childCount; i++) {
            if (contentParent.getChildAt(i) instanceof ViewGroup) {
                haveViewGroup = true;
                ViewGroup childAtGroup = (ViewGroup) contentParent.getChildAt(i);
                result.add(getMaxLevel(childAtGroup));
            }
        }
        if (!haveViewGroup) {
            result.add(result.descendingSet().first() + 1);
        }
        return result.descendingSet().first() + 1;
    }

    /**
     * 弹窗提示
     * @param activity
     * @param maxLevel
     */
    private void showAlertDialog(Activity activity, int maxLevel) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Warning")//标题
                .setMessage("请注意\n"+ activity.getComponentName().getClassName() +"\n页面层级为：" + maxLevel)//内容
                .setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }


}
