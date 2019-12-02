package com.yaapps.kikicare.UI;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.AccessToken;
import com.facebook.FacebookActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.color.Material;
import com.yaapps.kikicare.LoginActivity;
import com.yaapps.kikicare.PrefManager;
import com.yaapps.kikicare.R;
import com.yaapps.kikicare.UI.fragments.DashboardFragment;
import com.yaapps.kikicare.UI.fragments.FragmentWelcome;
import com.yaapps.kikicare.UI.fragments.ProfileFragment;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setBackground(getResources().getDrawable(R.color.colorPrimary));
        tb.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(tb);

        //Header
        AccountHeader ac=new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Yosr Jouini").withEmail("yosr.jouini@esprit.tn")
                                .withIcon(getResources().getDrawable(R.drawable.profile))
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
        Drawer result= new DrawerBuilder()
                .withActivity(this)
                .withToolbar(tb)
                .withAccountHeader(ac)
                .addDrawerItems(item1,new DividerDrawerItem(), item2,new DividerDrawerItem(),item3)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){
                    @Override
                    public  boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        //



                        if (drawerItem != null) {
                            Fragment fragment = null;
                            FragmentManager fm = getSupportFragmentManager();

                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    fragment = new DashboardFragment();
                                    break;
                                case 2:
                                    fragment = new FragmentWelcome();
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

                        //

                        return false; }
                })
                .withShowDrawerOnFirstLaunch(true)
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();
        result.addStickyFooterItem( new PrimaryDrawerItem().withName("Logout").withIcon(MaterialDrawerFont.Icon.mdf_arrow_drop_down) .withOnDrawerItemClickListener((view, position, drawerItem) -> {
            String mode = new PrefManager(HomeActivity.this).getUser().getMode();
            if(mode.equals("GMAIL"))
                LoginActivity.mGoogleSignInClient.signOut();
            else if(mode.equals("FACEBOOK")){
                LoginManager.getInstance().logOut();
            }
            new PrefManager(HomeActivity.this).setUser(null);
            Intent ii=new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(ii);
            return false;
        }));
    }
}
