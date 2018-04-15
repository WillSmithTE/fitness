package grp2.fitness;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import grp2.fitness.Fragments.DiaryFragment;
import grp2.fitness.Fragments.HomeFragment;
import grp2.fitness.Fragments.LeaderboardFragment;
import grp2.fitness.Fragments.RecipeFragment;
import grp2.fitness.Fragments.SettingsFragment;
import grp2.fitness.Fragments.ToolFragment;

public class NavigationActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
//            @Override
//            public void onComplete(AWSStartupResult awsStartupResult) {
//                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
//            }
//        }).execute();

        setContentView(R.layout.activity_navigation);

        toolbar         = findViewById(R.id.navigation_toolbar);
        drawerLayout    = findViewById(R.id.drawer_layout);
        navigationView  = findViewById(R.id.navigation_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        setSupportActionBar(toolbar);
        drawerLayout.addDrawerListener(drawerToggle);
        initDrawer(navigationView);

        updateView(navigationView.getMenu().findItem(R.id.home));
    }


    private void initDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        updateView(menuItem);
                        return true;
                    }
                });
    }

    public void updateView(MenuItem menuItem) {
        Fragment fragment = null;

        if (menuItem.getItemId() == R.id.logout) {
            startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
            finish();
            return;
        }

        try {
            fragment = (Fragment) getFragment(menuItem.getItemId()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).commit();

        setTitle(menuItem.getTitle());
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }

    private Class getFragment(int menuItemId) {
        switch (menuItemId) {
            case R.id.home:
                return HomeFragment.class;
            case R.id.recipes:
                return RecipeFragment.class;
            case R.id.diary:
                return DiaryFragment.class;
            case R.id.tools:
                return ToolFragment.class;
            case R.id.leaderboard:
                return LeaderboardFragment.class;
            case R.id.share:
                Toast.makeText(NavigationActivity.this, "Could be moved to a screen specific button rather than menu item",
                        Toast.LENGTH_LONG).show();
                return HomeFragment.class;
            case R.id.settings:
                return SettingsFragment.class;
            default:
                return HomeFragment.class;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
