package com.example.vietcuisine.ui.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vietcuisine.ui.profile.MyPosts;
import com.example.vietcuisine.ui.profile.MyRecipes;
import com.example.vietcuisine.ui.profile.MySavedRecipes;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyRecipes();
            case 1:
                return new MyPosts();
            case 2:
                return new MySavedRecipes();
            default:
                return new MyRecipes();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
