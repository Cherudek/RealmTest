package com.example.gregorio.greenjiin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

public class MopedCo1Fragment extends Fragment {

  private static final String ARG_SECTION_NUMBER = "section_number";
  private final static String LOG_TAG = MopedCo1Fragment.class.getSimpleName();
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  private Realm realm;
  private View rootView;

  public MopedCo1Fragment() {
  }

  public static MopedCo1Fragment newInstance(int sectionNumber) {
    MopedCo1Fragment fragment = new MopedCo1Fragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    realm = Realm.getDefaultInstance();
    rootView = inflater.inflate(R.layout.fragment_main, container, false);
    ButterKnife.bind(this, rootView);
    final MopedCo1Adapter adapter = new MopedCo1Adapter(setUpRealm());
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    recyclerView.setAdapter(adapter);
    return rootView;
  }


  private RealmResults<MopedModel> setUpRealm() {
    if (SyncUser.current() != null) {
      SyncConfiguration configuration = SyncUser.current().getDefaultConfiguration();
      Log.i(LOG_TAG, "SyncConfiguration: " + configuration.toString());
      realm = Realm.getInstance(configuration);
    } else {
      realm = Realm.getDefaultInstance();
    }
    return realm
        .where(MopedModel.class)
        .contains("companyName", "mopedCo1")
        .sort("timestamp", Sort.DESCENDING)
        .findAllAsync();
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    realm.close();
  }
}

