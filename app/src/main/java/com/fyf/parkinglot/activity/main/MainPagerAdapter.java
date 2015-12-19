package com.fyf.parkinglot.activity.main;

/**
 * Created by fengyifei on 15/11/24.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fyf.parkinglot.fragment.friends.FriendsFragment;
import com.fyf.parkinglot.fragment.mine.MineFragment;
import com.fyf.parkinglot.fragment.order.OrderFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
      switch(position){
          case 0:
              return new OrderFragment();
          case 1:
              return new FriendsFragment();
          case 2:
              return new MineFragment();
          default:
              return new OrderFragment();
      }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "预约";
            case 1:
                return "车友";
            case 2:
                return "我的";
        }
        return null;
    }
}