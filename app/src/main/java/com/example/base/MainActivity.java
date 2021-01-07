package com.example.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.library.base.LogConsole;
import com.library.base.widget.bar.navigation.BottomNavigationBar;
import com.library.base.widget.bar.navigation.BottomNavigationItem;


/**
 * 主页面，容器
 * 包含 游戏、资产、个人中心 三个模块
 * 处理网络连接逻辑，启动长连接/离线钱包上传 逻辑
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar mBottomNavigationBar;

    @Override
    protected boolean isSwipeEnable() {
        return false;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    int mTabIconSel[] = {R.mipmap.tab_icon_pool_sel, R.mipmap.tab_icon_property_sel, R.mipmap.tab_icon_mine_sel};
    int mTabIcon[] = {R.mipmap.tab_icon_pool, R.mipmap.tab_icon_property, R.mipmap.tab_icon_mine};
    Class fragmentClass[] = {PoolFragment.class, PoolFragment.class, PoolFragment.class};

    @Override
    protected void initViewsAndEvents() {
        String mTabDescription[] = {getString(R.string.tab_pool), getString(R.string.tab_assets), getString(R.string.tab_mine)};

        mBottomNavigationBar = findViewById(R.id.main_bottom_bar);

        for (int i = 0; i < mTabIcon.length; i++) {
            mBottomNavigationBar
                    .addItem(new BottomNavigationItem(mTabIconSel[i], mTabDescription[i])
                            .setActiveColorResource(R.color.navigation_main_tab_select)
                            .setInactiveIconResource(mTabIcon[i])
                            .setInActiveColorResource(R.color.navigation_main_tab));
        }
        mBottomNavigationBar.setFirstSelectedPosition(0).initialise();
        mBottomNavigationBar.setTabSelectedListener(this);

        setCurrentItem(0);


        lastTime = System.currentTimeMillis();
    }


    long lastTime = -1;


    @Override
    public void onTabSelected(int position) {
        setCurrentItem(position);
    }

    private int currentSelected = -1;

    private Fragment mTabFragment[] = new Fragment[3];

    private void setCurrentItem(int position) {
        if (currentSelected == position) {
            return;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (currentSelected >= 0 && currentSelected < mTabFragment.length && mTabFragment[currentSelected] != null) {
            transaction.hide(mTabFragment[currentSelected]);
        }

        if (mTabFragment[position] == null) {
            try {
                mTabFragment[position] = (Fragment) fragmentClass[position].newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.add(R.id.main_fragment, mTabFragment[position]);
        } else {
            transaction.show(mTabFragment[position]);
        }
        transaction.commit();
        currentSelected = position;
    }

    //Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        for (Fragment fragment : mTabFragment) {
            if (fragment != null)
                transaction.hide(fragment);
        }
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
