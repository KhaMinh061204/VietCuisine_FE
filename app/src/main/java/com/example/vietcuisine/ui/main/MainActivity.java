package com.example.vietcuisine.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.vietcuisine.R;
import com.example.vietcuisine.ui.auth.LoginActivity;
import com.example.vietcuisine.ui.fragments.HomeFragment;
import com.example.vietcuisine.ui.fragments.RecipeFragment;
import com.example.vietcuisine.ui.fragments.ReelsFragment;
import com.example.vietcuisine.ui.fragments.ShopFragment;
import com.example.vietcuisine.ui.fragments.ProfileFragment;
import com.example.vietcuisine.ui.recipe.CreateRecipeActivity;
import com.example.vietcuisine.ui.post.CreatePostActivity;
import com.example.vietcuisine.ui.reel.CreateReelActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if user is logged in
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }
        
        setContentView(R.layout.activity_main);        
        initViews();
        setupBottomNavigation();
        setupFabClick();
          // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            showFab(true); // Show FAB for home fragment
        }
    }    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);
    }private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
              try {
                if (itemId == R.id.nav_home) {
                    fragment = new HomeFragment();
                    showFab(true);
                } else if (itemId == R.id.nav_recipes) {
                    fragment = new RecipeFragment();
                    showFab(true);
                } else if (itemId == R.id.nav_reels) {
                    fragment = new ReelsFragment();
                    showFab(true);
                } else if (itemId == R.id.nav_shop) {
                    fragment = new ShopFragment();
                    showFab(false); // Hide FAB on shop page
                } else if (itemId == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                    showFab(false); // Hide FAB on profile page
                }
                
                return loadFragment(fragment);
            } catch (Exception e) {
                Log.e(TAG, "Error in navigation: " + e.getMessage());
                return false;
            }
        });
    }private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error loading fragment: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        return prefs.getBoolean("is_logged_in", false);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }    private void setupFabClick() {
        fabAdd.setOnClickListener(v -> {
            // Add rotation animation
            Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_rotate);
            fabAdd.startAnimation(rotateAnimation);
            
            showAddOptionsDialog();
        });
    }    private void showAddOptionsDialog() {
        PopupMenu popupMenu = new PopupMenu(this, fabAdd);
        popupMenu.getMenuInflater().inflate(R.menu.fab_add_menu, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.menu_create_recipe) {
                intent = new Intent(MainActivity.this, CreateRecipeActivity.class);
            } else if (itemId == R.id.menu_create_post) {
                intent = new Intent(MainActivity.this, CreatePostActivity.class);
            } else if (itemId == R.id.menu_create_reel) {
                intent = new Intent(MainActivity.this, CreateReelActivity.class);
            }
            
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        });
        
        popupMenu.setOnDismissListener(menu -> {
            // Rotate back when menu is dismissed
            Animation rotateBackAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_back);
            fabAdd.startAnimation(rotateBackAnimation);
        });
        
        popupMenu.show();
    }

    private void showFab(boolean show) {
        if (show) {
            fabAdd.show();
        } else {
            fabAdd.hide();
        }
    }
}
