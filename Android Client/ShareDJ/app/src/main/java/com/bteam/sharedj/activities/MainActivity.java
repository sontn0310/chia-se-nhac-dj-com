package com.bteam.sharedj.activities;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.bteam.sharedj.R;
import com.bteam.sharedj.fragments.ArtistsFragment;
import com.bteam.sharedj.fragments.FavoriteFragment;
import com.bteam.sharedj.fragments.GenresFragment;
import com.bteam.sharedj.fragments.QuickControlsFragment;
import com.bteam.sharedj.fragments.SongsFragment;
import com.bteam.sharedj.permissions.Nammu;
import com.bteam.sharedj.permissions.PermissionCallback;
import com.bteam.sharedj.slidinguppanel.SlidingUpPanelLayout;
import com.bteam.sharedj.utils.Constants;
import com.bteam.sharedj.utils.NavigationUtils;
import com.bteam.sharedj.utils.TimberUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static MainActivity mainActivity;

    String action;
    private DrawerLayout mDrawerLayout;
    private SlidingUpPanelLayout panelLayout;
    private NavigationView navigationView;
    Handler navDrawerRunnable = new Handler();
    Map<String, Runnable> navigationMap = new HashMap<String, Runnable>();
    Runnable runnable;
    Runnable navigateHome = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
            Fragment fragment = new SongsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

        }
    };
    Runnable navigateGenres = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_genres).setChecked(true);
            Fragment fragment = new GenresFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();

        }
    };
    Runnable navigateArtists = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_artist).setChecked(true);
            Fragment fragment = new ArtistsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();

        }
    };
    Runnable navigateFavorite = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
            Fragment fragment = new FavoriteFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        action = getIntent().getAction();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }

    private void init() {
        navigationMap.put(Constants.NAVIGATE_HOME, navigateHome);
        navigationMap.put(Constants.NAVIGATE_GENRES, navigateGenres);
        navigationMap.put(Constants.NAVIGATE_ARTISTS, navigateArtists);
        navigationMap.put(Constants.NAVIGATE_FAVORITE, navigateFavorite);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);

        setPanelSlideListeners(panelLayout);
        navDrawerRunnable.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDrawerContent(navigationView);
                setupNavigationIcons(navigationView);
            }
        }, 700);

        if (TimberUtils.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
            loadEverything();
        }

        addBackstackListener();

        if (Intent.ACTION_VIEW.equals(action)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 350);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        updatePosition(menuItem);
                        return true;

                    }
                });
    }

    private void setupNavigationIcons(NavigationView navigationView) {


        navigationView.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.music_note);
        navigationView.getMenu().findItem(R.id.nav_genres).setIcon(R.drawable.library_music);
        navigationView.getMenu().findItem(R.id.nav_artist).setIcon(R.drawable.playlist_play);
        navigationView.getMenu().findItem(R.id.nav_favorite).setIcon(R.drawable.bookmark_music);
        navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.settings);
        navigationView.getMenu().findItem(R.id.nav_about).setIcon(R.drawable.information);


    }

    private void updatePosition(final MenuItem menuItem) {
        runnable = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                runnable = navigateHome;

                break;
            case R.id.nav_genres:
                runnable = navigateGenres;

                break;
            case R.id.nav_artist:
                runnable = navigateArtists;

                break;
            case R.id.nav_favorite:
                runnable = navigateFavorite;
                break;

            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                mDrawerLayout.closeDrawers();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 350);

                break;

        }

        if (runnable != null) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 350);
        }
    }

    private void loadEverything() {
        Runnable navigation = navigationMap.get(action);
        if (navigation != null) {
            navigation.run();
        } else {
            navigateHome.run();
        }

        new initQuickControls().execute("");
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity = this;
    }

    private void addBackstackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                getSupportFragmentManager().findFragmentById(R.id.fragment_container).onResume();
            }
        });
    }

    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            loadEverything();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    private void checkPermissionAndThenLoad() {
        //check for permission
        if (Nammu.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadEverything();
        } else {
            if (Nammu.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(panelLayout, "Timber will need to read external storage to display songs on your device.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Nammu.askForPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
                            }
                        }).show();
            } else {
                Nammu.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                /*if (isNavigatingMain()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else */
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (panelLayout.isPanelExpanded()) {
            panelLayout.collapsePanel();
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
