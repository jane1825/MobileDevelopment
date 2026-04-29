package ru.mirea.stolyarovael.mireaproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import ru.mirea.stolyarovael.mireaproject.ui.files.FilesFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }

        navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_data,
                R.id.nav_webview,
                R.id.nav_worker,
                R.id.nav_recorder,
                R.id.nav_camera,
                R.id.nav_sensor,
                R.id.nav_files,
                R.id.nav_profile,
                R.id.nav_institution,
                R.id.nav_internet_resource
        ).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fabCamera = findViewById(R.id.fab_camera);
        
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentId = navController.getCurrentDestination().getId();
                if (currentId == R.id.nav_files) {
                    Fragment navHost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                    if (navHost != null) {
                        Fragment currentFragment = navHost.getChildFragmentManager().getFragments().get(0);
                        if (currentFragment instanceof FilesFragment) {
                            ((FilesFragment) currentFragment).showCreateFileDialog();
                        }
                    }
                } else {
                    navController.navigate(R.id.nav_camera);
                }
            }
        });

        FloatingActionButton fabProfile = findViewById(R.id.fab_profile);
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_profile);
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_camera) {
                fabCamera.setVisibility(View.GONE);
            } else {
                fabCamera.setVisibility(View.VISIBLE);
                if (destination.getId() == R.id.nav_files) {
                    fabCamera.setImageResource(android.R.drawable.ic_input_add);
                } else {
                    fabCamera.setImageResource(android.R.drawable.ic_menu_camera);
                }
            }
            
            if (destination.getId() == R.id.nav_profile) {
                fabProfile.setVisibility(View.GONE);
            } else {
                fabProfile.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
