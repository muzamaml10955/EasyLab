package com.wmwebs.easylab;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.wmwebs.easylab.ui.Biocare.biocareFragment;
import com.wmwebs.easylab.ui.aboutus.AboutusFragment;
import com.wmwebs.easylab.ui.advance.advance;
import com.wmwebs.easylab.ui.capital.capital;
import com.wmwebs.easylab.ui.chughtai.chughtai;
import com.wmwebs.easylab.ui.city.city;
import com.wmwebs.easylab.ui.conatactus.ContactusFragment;
import com.wmwebs.easylab.ui.excel.excellab;
import com.wmwebs.easylab.ui.ibnesena.ibnesena;
import com.wmwebs.easylab.ui.islamabad.islamabad;
import com.wmwebs.easylab.ui.nayyab.nayyab;
import com.wmwebs.easylab.ui.shifa.shifa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
WebView webView;
    private AppBarConfiguration mAppBarConfiguration;
Button share;

ContactusFragment cf;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    String url = "https://easylab.pk/advanced-diagnostic/";
    public static final int RESULT_OK = -1;
    SwipeRefreshLayout swipeRefreshLayout;
    private final static int FCR = 1;

    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    ContactusFragment cu;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
      share=findViewById(R.id.sharebutton);
        setSupportActionBar(toolbar);


        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();



        getSupportActionBar().setDisplayShowTitleEnabled(false);
       /*toolbar.setLogo(R.drawable.ic_menu_camera); // setting a logo in toolba
       toolbar.setNavigationIcon(R.drawable.ic_menu_camera); // set icon for navigation button
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_camera));*/ // setting a navigation icon in Toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide the


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        webView=findViewById(R.id.homewebview);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     /*   mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_nayyab, R.id.nav_islamabad,R.id.nav_excellab,
                R.id.nav_biocare,R.id.nav_chugtaie,R.id.nav_advance,R.id.nav_advance,
                R.id.nav_city,R.id.nave_ibnesena,R.id.nav_shifa,R.id.nav_capital,R.id.nav_contactus,R.id.nav_aboutus)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Log.e("T", "Unable to create Image File");
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    /*    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       getSupportActionBar().setDisplayUseLogoEnabled(true);*/
//*/
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getApplicationContext(),"Toolbarclicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "EasyLab One Lab for All Labs.Check out EasyLab app at: https://play.google.com/store/apps/details?id=com.wmwebs.easylab" );
        sendIntent.setType("text/plain");
        //startActivity(sendIntent);
        Intent intent;
        intent = (Intent.createChooser(sendIntent, "Share using"));
         startActivity(intent);
       // AdmobPassIntent();
     /*   Toast.makeText(MainActivity.this, "You clicked share button", Toast.LENGTH_LONG).show();
/*
        ContactusFragment fragment = new ContactusFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();

       /* FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment, ContactusFragment);
        fr.addToBackStack(null);
        fr.commit();*/
    }
});





    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void prepareMenuData() {

        MenuModel menuModel = new MenuModel("Home", true, false); //Menu of Android Tutorial. No sub menus

        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }



        menuModel = new MenuModel("About Us", true, false); //Menu of Android Tutorial. No sub menus

        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }



        menuModel  = new MenuModel("Our Labs  +", true, true); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(getString(R.string.menu_nayab), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_islamabad), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_excel), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_bicare), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_chughtai), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_advanced), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_city), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_ibnesena), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_shifa), false, false);
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.menu_capital), false, false);
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            Log.d("API123","here");
            childList.put(menuModel, childModelsList);
        }



         menuModel = new MenuModel("Contact Us", true, false); //Menu of Android Tutorial. No sub menus

        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


       /* childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Python Tutorials", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Python AST – Abstract Syntax Tree", false, false, "https://www.journaldev.com/19243/python-ast-abstract-syntax-tree");
        childModelsList.add(childModel);

        childModel = new MenuModel("Python Fractions", false, false, "https://www.journaldev.com/19226/python-fractions");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/


    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                      if (groupPosition==0){
                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                          startActivity(intent);
                          Toast.makeText(getApplicationContext(), "you clicked parent home", Toast.LENGTH_SHORT).show();

                      }
                        if (groupPosition==1)
                        {



                            Toast.makeText(getApplicationContext(), "you clicked aboutus", Toast.LENGTH_SHORT).show();
                            AboutusFragment fragment = new AboutusFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, fragment);
                            transaction.commit();
                            onBackPressed();







                        }
                        if (groupPosition==2)
                        {


                            Toast.makeText(getApplicationContext(), "you clicked our Labs", Toast.LENGTH_SHORT).show();




                        }
                        if (groupPosition==3)
                        {

                            Toast.makeText(getApplicationContext(), "you clicked contactus", Toast.LENGTH_SHORT).show();

                            ContactusFragment fragmentcontactus = new ContactusFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, fragmentcontactus);
                            transaction.commit();
                            onBackPressed();

                            //  Toast.makeText(getContext(),"Page is loading",Toast.LENGTH_SHORT).show();


                            /*
                            Intent intent = new Intent(getApplicationContext(), ContactusFragment.class);
                            startActivity(intent);*/
                            //  Toast.makeText(getContext(),"Page is loading",Toast.LENGTH_SHORT).show();



                        }
                        /*  WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(headerList.get(groupPosition).url);
                        onBackPressed();*/
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                   // Toast.makeText(getApplicationContext(), "you clicked child", Toast.LENGTH_SHORT).show();
                    if(childPosition==0)
                    {
                        Toast.makeText(MainActivity.this, "youclicked nayyab", Toast.LENGTH_SHORT).show();

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new nayyab());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==1)
                    {
                        Toast.makeText(MainActivity.this, "youclicked islamabad", Toast.LENGTH_SHORT).show();

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new islamabad());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==2)
                    {
                        Toast.makeText(MainActivity.this, "youclicked excel", Toast.LENGTH_SHORT).show();
                        AboutusFragment fragment = new AboutusFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new excellab());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==3)
                    {
                        Toast.makeText(MainActivity.this, "youclicked biocare", Toast.LENGTH_SHORT).show();
                        AboutusFragment fragment = new AboutusFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new biocareFragment());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==4)
                    {
                        Toast.makeText(MainActivity.this, "youclicked chugtai", Toast.LENGTH_SHORT).show();
                        AboutusFragment fragment = new AboutusFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new chughtai());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==5)
                    {
                        Toast.makeText(MainActivity.this, "youclicked advance", Toast.LENGTH_SHORT).show();
                        AboutusFragment fragment = new AboutusFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new advance());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==6)
                    {
                        Toast.makeText(MainActivity.this, "youclicked citi", Toast.LENGTH_SHORT).show();
                        AboutusFragment fragment = new AboutusFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new city());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==7)
                    {
                        Toast.makeText(MainActivity.this, "youclicked ibnesena", Toast.LENGTH_SHORT).show();

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new ibnesena());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==8)
                    {
                        Toast.makeText(MainActivity.this, "youclicked shifa", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new shifa());
                        transaction.commit();
                        onBackPressed();
                    }

                    if(childPosition==9)
                    {
                        Toast.makeText(MainActivity.this, "youclicked capital", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, new capital());
                        transaction.commit();
                        onBackPressed();
                    }



                 /*   if (model.url.length() > 0) {

                        WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(model.url);
                        onBackPressed();
                    }*/
                }

                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



}