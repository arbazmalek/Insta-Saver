package com.arbaz.instagramsaver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewerAdapter extends FragmentStateAdapter {

    private final String[] titles = new String[]{"Reel/IGTV","Photo"};


    public viewerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new reelfragment();

            case 1:
                return new photofragment();

        }
        return new photofragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
