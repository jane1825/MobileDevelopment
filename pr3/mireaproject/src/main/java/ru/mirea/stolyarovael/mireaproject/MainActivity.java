package ru.mirea.stolyarovael.mireaproject;

import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.stolyarovael.mireaproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Проверяем наличие тулбара
        if (binding.appBarMain != null && binding.appBarMain.toolbar != null) {
            setSupportActionBar(binding.appBarMain.toolbar);
        }

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Список всех ID из mobile_navigation.xml
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow,
                    R.id.nav_settings, R.id.nav_data, R.id.nav_browser)
                    .setOpenableLayout(drawer)
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            if (navigationView != null) {
                NavigationUI.setupWithNavController(navigationView, navController);
            }

            // Если есть нижнее меню (для планшетов/горизонтального вида)
            if (binding.appBarMain != null && binding.appBarMain.contentMain != null
                    && binding.appBarMain.contentMain.bottomNavView != null) {
                NavigationUI.setupWithNavController(binding.appBarMain.contentMain.bottomNavView, navController);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            return NavigationUI.navigateUp(navHostFragment.getNavController(), mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}