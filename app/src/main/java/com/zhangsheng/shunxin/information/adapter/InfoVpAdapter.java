package com.zhangsheng.shunxin.information.adapter;

import android.os.Parcelable;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;


import com.zhangsheng.shunxin.information.InformationFragment;
import com.zhangsheng.shunxin.information.bean.TabBean;
import com.zhangsheng.shunxin.information.InformationFragment;
import com.zhangsheng.shunxin.information.bean.TabBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻适配器
 */
public class InfoVpAdapter extends FragmentPagerAdapter {

    private List<TabBean> mTabList = new ArrayList<>();

    public InfoVpAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;

    }

    public void replace(List<TabBean> tabList) {
        this.mTabList = tabList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        TabBean newsTab = mTabList.get(i);
        if (newsTab == null) {
            return new Fragment();
        }

//        if(newsTab.code.equals("news_smallvideo")){
//            return SmallVideoUtils.initGridWidget().getFragment();
//        }else {
            InformationFragment fragment = null;
            if (fragment == null) {
                fragment = InformationFragment.newInstance(newsTab.code);
            }
            return fragment;
//        }
    }

    @Override
    public int getCount() {
        if (mTabList == null) {
            return 0;
        }
        return mTabList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTabList == null) {
            return "未知";
        }

        if (position > mTabList.size()) {
            return "未知";
        }

        TabBean newsTab = mTabList.get(position);
        if (newsTab == null) {
            return "未知";
        }

        return newsTab.title;
    }

    public int getTabBeanPosition(int position){
        if (mTabList == null) {
            return 0;
        }
        if(position>=mTabList.size()){
            return 0;
        }
        TabBean newsTab = mTabList.get(position);
        if (newsTab == null) {
            return 0;
        }
        return newsTab.position;
    }
    private final FragmentManager mFragmentManager;
    private Fragment mCurrentPrimaryItem = null;

    private FragmentTransaction mCurTransaction = null;
    public void startUpdate(@NonNull ViewGroup container) {
        if (container.getId() == -1) {
            throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
        }
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        int curPos=getTabBeanPosition(position);

        long itemId = this.getItemId(curPos);
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            this.mCurTransaction.attach(fragment);
//            Log.w("lpb--------------------","fragment != null");

        } else {
            fragment = this.getItem(position);
            this.mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));

//            Log.w("lpb--------------------","fragment = null");

        }

        if (fragment != this.mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        this.mCurTransaction.detach((Fragment)object);
    }

    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }

            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
            this.mCurrentPrimaryItem = fragment;
        }

    }

    public void finishUpdate(@NonNull ViewGroup container) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }

    }

    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment)object).getView() == view;
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public long getItemId(int position) {
        return (long)position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}
