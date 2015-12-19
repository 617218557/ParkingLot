package com.fyf.parkinglot.fragment.friends;

/**
 * Created by fengyifei on 15/11/24.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fyf.parkinglot.fragment.myFriends.MyFriendsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FriendsPagerAdapter extends FragmentPagerAdapter {

    public FriendsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFriendsFragment myFriendsFragment= new MyFriendsFragment();
    public MyFriendsFragment myFriendsFragment1= new MyFriendsFragment();

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return myFriendsFragment;
            case 1:
                return myFriendsFragment1;
            default:
                return myFriendsFragment;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "好友";
            case 1:
                return "群组";
        }
        return null;
    }
}