package com.example.vietcuisine.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
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
    private static final int CREATE_POST_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        setContentView(R.layout.activity_main);
        initViews();
        setupBottomNavigation();
        setupFabClick();

        String navTarget = getIntent().getStringExtra("navigate_to");

        if ("shop".equals(navTarget)) {
            // 👇 Gọi fragment thủ công trước khi set selected tab
            loadFragment(new ShopFragment());
            showFab(false); // Ẩn nút FAB trên shop
            bottomNavigation.setSelectedItemId(R.id.nav_shop); // Highlight đúng tab
        } else {
            // Mặc định Home
            loadFragment(new HomeFragment());
            showFab(true);
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }

    }    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent != null && "shop".equals(intent.getStringExtra("navigateTo"))) {
            bottomNavigation.setSelectedItemId(R.id.nav_shop);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ShopFragment()) // hoặc ID tương ứng
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            // Khi tạo bài viết thành công, load lại HomeFragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (currentFragment instanceof HomeFragment) {
                ((HomeFragment) currentFragment).refreshData();
            } else {
                // Load HomeFragment nếu chưa hiển thị
                bottomNavigation.setSelectedItemId(R.id.nav_home);
            }
        }
    }



    private void setupBottomNavigation() {
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
                startActivity(intent);
            } else if (itemId == R.id.menu_create_post) {
                intent = new Intent(MainActivity.this, CreatePostActivity.class);
                startActivityForResult(intent, CREATE_POST_REQUEST_CODE);
            } else if (itemId == R.id.menu_create_reel) {
                intent = new Intent(MainActivity.this, CreateReelActivity.class);
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
