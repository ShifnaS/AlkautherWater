package infogateway.alkautherwater.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import infogateway.alkautherwater.app.Config;
import infogateway.alkautherwater.fragments.AboutFragment;
import infogateway.alkautherwater.fragments.ContactUsFragment;
import infogateway.alkautherwater.fragments.HomeFragment;
import infogateway.alkautherwater.fragments.ProductsFragment;
import infogateway.alkautherwater.fragments.ServiceFragment;
import infogateway.alkautherwater.helper.AppPreferences;
import infogateway.alkautherwater.helper.DBHelper;
import infogateway.alkautherwater.helper.SQLiteOperations;
import infogateway.alkautherwater.utils.NotificationUtils;
import infogateway.alkautherwater.R;

public class Products extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    private View navHeader;
    DrawerLayout drawer;
    private Handler mHandler;
    private String[] activityTitles;
    private boolean shouldLoadHomeFragOnBackPress = true;

    private static final String TAG_HOME = "home";
    private static final String TAG_ABOUT_US = "about us";
    private static final String TAG_Services = "services";
    private static final String TAG_CONTACT_US = "contact us";
    private static final String TAG_PRODUCTS = "products";

    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;

    NavigationView navigationView;
    TextView notification;
    //notification
    int  mlaunchCount=0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //notification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home2);
        this.setFinishOnTouchOutside(true);

        //////rate start
        AppPreferences.getInstance(getApplicationContext()).incrementLaunchCount();
        mlaunchCount= AppPreferences.getInstance(getApplicationContext()).getLaunchCount();

        showRateAppDialogIfNeeded();
        ////rate end


        DBHelper obj=new DBHelper(getApplicationContext());
        SQLiteOperations sqLiteOperations=new SQLiteOperations(getApplicationContext());

       
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        navHeader = navigationView.getHeaderView(0);
        TextView  tv_url = navHeader.findViewById(R.id.url);
        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Products.this, "dfdf", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.alkautherwater.com/"));
                startActivity(intent);
            }
        });

        notification=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_notification));
        int count=sqLiteOperations.getcount(0);
        initializeCountDrawer(count);



    }
//rate me
    private void showRateAppDialogIfNeeded() {
        boolean bool = AppPreferences.getInstance(getApplicationContext()).getAppRate();
        int i = AppPreferences.getInstance(getApplicationContext()).getLaunchCount();
        if ((bool) && (i == 5)) {
            createAppRatingDialog(getString(R.string.rate_app_title), getString(R.string.rate_app_message)).show();
        }
    }

    private AlertDialog createAppRatingDialog(String rateAppTitle, String rateAppMessage) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.dialog_app_rate), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                //openAppInPlayStore(getApplicationContext());
                rateMe();
                AppPreferences.getInstance(getApplicationContext()).setAppRate(false);
            }
        }).setNegativeButton(getString(R.string.dialog_your_feedback), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                openFeedback(getApplicationContext());
              //  AppPreferences.getInstance(getApplicationContext()).resetLaunchCount();
                AppPreferences.getInstance(getApplicationContext()).setAppRate(false);
            }
        }).setNeutralButton(getString(R.string.dialog_ask_later), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                AppPreferences.getInstance(getApplicationContext()).resetLaunchCount();
            }
        }).setMessage(rateAppMessage).setTitle(rateAppTitle).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    public static void openFeedback(Context paramContext) {
        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sq7150@hotmail.com"});
        localIntent.putExtra(Intent.EXTRA_CC, "");
        String str = null;
        try {
            str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
            localIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Al Kauther Water App");

            localIntent.setType("message/rfc822");
            paramContext.startActivity(Intent.createChooser(localIntent, "Choose an Email client :"));
        } catch (Exception e) {
            Log.d("OpenFeedback", e.getMessage());
        }
    }

  /*  public static void openAppInPlayStore(Context paramContext) {
        paramContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));
    }*/
/*  public  void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=infogateway.sample.alkautherwater")));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }*/

    public  void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                 Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }
    ///rate me end
    private void initializeCountDrawer(int count) {


        notification.setGravity(Gravity.CENTER_VERTICAL);
        notification.setTypeface(null, Typeface.BOLD);
        notification.setTextColor(getResources().getColor(R.color.colorAccent));
        notification.setText(count > 0 ? String.valueOf(count) : null);

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);


    }



    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setToolbarTitle()
    {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            // toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //  toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                AboutFragment aboutFragment=new AboutFragment();
                return aboutFragment;
            case 2:
               ServiceFragment serviceFragment=new ServiceFragment();
                return serviceFragment;


            case 3:
               ProductsFragment productsFragment=new ProductsFragment();
               return  productsFragment;

            case 4:
               ContactUsFragment contactUsFragment=new ContactUsFragment();
               return contactUsFragment;
            default:
                return new HomeFragment();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        switch (id) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;
            case R.id.nav_about:
                navItemIndex = 1;
                CURRENT_TAG = TAG_ABOUT_US;
                break;
            case R.id.nav_services:
                navItemIndex = 2;
                CURRENT_TAG = TAG_Services;
                break;

            case R.id.nav_products:
                navItemIndex = 3;
                CURRENT_TAG = TAG_PRODUCTS;
                break;

            case R.id.nav_contact:
                navItemIndex = 4;
                CURRENT_TAG = TAG_CONTACT_US;
                break;
            case R.id.nav_notification:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Products.this, NotificationActivity.class));
                drawer.closeDrawers();
                return true;
            case R.id.nav_qac:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Products.this, QAC.class));
                drawer.closeDrawers();
                return true;
            case R.id.nav_faq:
                // launch new intent instead of loading fragment
                startActivity(new Intent(Products.this, FAQ.class));
                drawer.closeDrawers();
                return true;
            default:
                navItemIndex = 0;
        }


        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        item.setChecked(true);

        loadHomeFragment();

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


}
