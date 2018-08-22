package com.example.gregorio.greenjiin;

import static com.example.gregorio.greenjiin.Constants.REALM_BASE_URL;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.gregorio.greenjiin.LoginFragment.OnFragmentInteractionListener;
import com.example.gregorio.greenjiin.Moped2Fragment.OnFragment2InteractionListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
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
  private FacebookAuth facebookAuth;
  private Realm realm;


  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    loginFragment = new LoginFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment)
        .addToBackStack(null)
        .commit();

  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // Log Out Method
    if (id == R.id.action_log_out) {

      SyncUser syncUser = SyncUser.current();
      if (syncUser != null) {
        syncUser.logOut();
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

    this.realm = realm1;

    Log.i(LOG_TAG, "The Realm Object is: " + realm1);

  }

  @Override
  public void onFragment2Interaction(Uri uri) {

  }


  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private Realm realm;
    private View rootView;


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);

      return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.fragment_main, container, false);
      TextView textView = rootView.findViewById(R.id.section_label);
      textView
          .setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
      ButterKnife.bind(this, rootView);



      return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      SyncConfiguration configuration = SyncUser.current()
          .createConfiguration(REALM_BASE_URL + "/default")
          .build();

      realm = Realm.getInstance(configuration);
      RealmResults<MopedModel> moped = realm
          .where(MopedModel.class)
          .contains("companyName", "mopedCo1")
          .findAllAsync();

      final MopedCo1Adapter mopedCo1Adapter = new MopedCo1Adapter(moped, true);
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
      recyclerView.setAdapter(mopedCo1Adapter);


    }

    @Override
    public void onDestroy() {
      super.onDestroy();
      realm.close();
    }
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
          return PlaceholderFragment.newInstance(0);
        case 1:
          return Moped2Fragment.newInstance(1);
        default:
          return null;
      }
      //return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
      // Show 3 total pages.
      return 2;
    }
  }


}
