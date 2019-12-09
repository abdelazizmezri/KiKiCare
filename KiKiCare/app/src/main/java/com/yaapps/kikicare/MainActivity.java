package com.yaapps.kikicare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.yaapps.kikicare.Entity.User;
import com.yaapps.kikicare.UI.LoginActivity;
import com.yaapps.kikicare.UI.fragments.DashboardFragment;
import com.yaapps.kikicare.UI.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView bottomNavigationView;
    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;
    Drawer drawer;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigationMyProfile:
                        return true;
                    case R.id.navigationMyCourses:
                        return true;
                    case R.id.navigationHome:
                        return true;
                    case  R.id.navigationSearch:
                        return true;
                    case  R.id.navigationMenu:
                        drawer.openDrawer();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        //Toolbar
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setBackground(getResources().getDrawable(R.color.colorPrimary));
        tb.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(tb);

        User user = new PrefManager(this).getUser();

        Drawable dr = Drawable.createFromPath(user.getUrlImage());

        //Header
        AccountHeader ac=new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(dr)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(user.getFirstName()+" "+user.getLastName()).withEmail(user.getEmail())
                )
                .build();

        //Drawer Items
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home")
                .withIcon(MaterialDrawerFont.Icon.mdf_person)
                ;
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("User Account")
                .withIcon(MaterialDrawerFont.Icon.mdf_person)
                ;
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Settings").withIcon(MaterialDrawerFont.Icon.mdf_person);

        //Drawer Build
        drawer= new DrawerBuilder()
                .withActivity(this)
                .withToolbar(tb)
                .withAccountHeader(ac)
                .addDrawerItems(item1,new DividerDrawerItem(), item2,new DividerDrawerItem(),item3)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem != null) {
                        Fragment fragment = null;
                        FragmentManager fm = getSupportFragmentManager();
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                fragment = new DashboardFragment();
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, ProfilActivity.class));
                                finish();
                                break;
                            case 3:
                                fragment = new ProfileFragment();
                                break;
                        }
                        if (fragment != null) {
                            fm.beginTransaction().replace(R.id.container2, fragment,"MY_FRAGMENT").commit();
                        }
                        if (drawerItem instanceof Nameable) {
                            setTitle(((Nameable) drawerItem).getName().getText(getApplicationContext()));
                        }
                    }
                    return false; })
                .withShowDrawerOnFirstLaunch(true)
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();
        drawer.addStickyFooterItem( new PrimaryDrawerItem().withName("Logout").withIcon(MaterialDrawerFont.Icon.mdf_arrow_drop_down) .withOnDrawerItemClickListener((view, position, drawerItem) -> {
            String mode = new PrefManager(MainActivity.this).getUser().getMode();
            if(mode.equals("GMAIL"))
                LoginActivity.mGoogleSignInClient.signOut();
            else if(mode.equals("FACEBOOK")){
                LoginManager.getInstance().logOut();
            }
            new PrefManager(MainActivity.this).setUser(null);
            Intent ii=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(ii);
            return false;
        }));
    }

    @Override
    public void onBackPressed() {

    }
}
