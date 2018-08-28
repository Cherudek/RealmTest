package com.example.gregorio.greenjiin;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.gregorio.greenjiin.LoginFragment.OnFragmentInteractionListener;
import com.example.gregorio.greenjiin.Moped2Fragment.OnFragment2InteractionListener;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import io.realm.Realm;
import io.realm.SyncUser;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener,
    OnFragment2InteractionListener {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */

  private static final String LOG_TAG = MainActivity.class.getSimpleName();
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private LoginFragment loginFragment;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  @BindView(R.id.container)
  ViewPager mViewPager;
  @BindView(R.id.tabs)
  TabLayout tabLayout;
  @BindView(R.id.toolbar)
  Toolbar toolbar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    // Facebook Call Manager setUp.
    CallbackManager callbackManager = CallbackManager.Factory.create();
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    // Set up the ViewPager with the sections adapter.
    mViewPager.setAdapter(mSectionsPagerAdapter);
    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    if(SyncUser.current()==null){
      loginFragment = new LoginFragment();
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment)
          .addToBackStack(null)
          .commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User Logout
    int id = item.getItemId();
    // Log Out Method
    if (id == R.id.action_log_out) {
      SyncUser syncUser = SyncUser.current();
      if (syncUser != null) {
        // Realm Logout
        syncUser.logOut();
        // Facebook Logout
        LoginManager.getInstance().logOut();
        // New Login Fragment
        loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment)
            .addToBackStack(null)
            .commit();
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onFragmentInteraction(Realm realm1) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().remove(loginFragment)
        .addToBackStack(null)
        .commit();
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mSectionsPagerAdapter);
    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    Log.i(LOG_TAG, "The Realm Object is: " + realm1);
  }

  @Override
  public void onFragment2Interaction(Uri uri) {
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }
    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      switch (position) {
        case 0:
          return MopedCo1Fragment.newInstance(0);
        case 1:
          return Moped2Fragment.newInstance(1);
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 2;
    }
  }
}
