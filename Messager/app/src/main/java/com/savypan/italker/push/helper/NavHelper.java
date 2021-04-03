package com.savypan.italker.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * fragment util helper for reusability
 */
public class NavHelper<T> {

    private final Context context;
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    private final OnTabChangedListener<T> listener;

    private Tab<T> currentTab;

    public NavHelper(Context context,
                     int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }

    /***
     * add tab with associated menuId
     * @param menuId
     * @param tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }


    /***
     * get current focused tab
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    //distribution solution
    //return value is if this click is handled
    public boolean performClickMenu(int menuId) {

        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            handlingSelectedTab(tab);
            return true;
        }

        return false;
    }


    private void handlingSelectedTab(Tab<T> tab) {
        Tab<T> oldTab = null;

        if (currentTab != null) {
            oldTab = currentTab;

            if (oldTab == tab) {
                //click the same tab, then return by doing nothing
                notifyReselect1(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }


    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //remove view from viewtree but fragment still lives in the cache of fragment
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                Fragment fragment = Fragment.instantiate(context, newTab.klass.getName(), null);
                newTab.fragment = fragment;
                ft.add(containerId, fragment, newTab.klass.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }

        ft.commit();

        //callback with notification
        notifyReselect2(newTab, oldTab);
    }


    /***
     * todo: when click multi times, we can do some extra ops etc...
     * @param tab
     */
    private void notifyReselect1(Tab<T> tab) {

    }

    /***
     * callback when tab switch DONE
     * @param newTab
     * @param oldTab
     */
    private void notifyReselect2(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    public static class Tab<T> {
        public Class<? extends Fragment> klass;
        public T extra;

        //keep this cache for recording, package name spacing
        Fragment fragment;

        public Tab(Class<? extends Fragment> klass, T extra) {
            this.klass = klass;
            this.extra = extra;
        }
    }

    /**
     * defined an event to notify Activity
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
