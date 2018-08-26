package com.example.gregorio.greenjiin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Moped2Fragment.OnFragment2InteractionListener} interface
 * to handle interaction events.
 * Use the {@link Moped2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Moped2Fragment extends Fragment {

  private static final String LOG_TAG = Moped2Fragment.class.getSimpleName();

  private static final String ARG_SECTION_NUMBER = "section_number2";
  @BindView(R.id.recycler_view2)
  RecyclerView recyclerView;
  private Realm realm;
  private View rootView;

  private OnFragment2InteractionListener mListener;

  public Moped2Fragment() {
  }

  public static Moped2Fragment newInstance(int sectionNumber) {
    Moped2Fragment fragment = new Moped2Fragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_moped2, container, false);
    ButterKnife.bind(this, rootView);
    realm = Realm.getDefaultInstance();
    final MopedCo2Adapter mopedCo2Adapter = new MopedCo2Adapter(setUpRealm());
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    recyclerView.setAdapter(mopedCo2Adapter);
    return rootView;
  }

  private RealmResults<MopedModel> setUpRealm() {
    if (SyncUser.current() != null) {
      SyncConfiguration configuration = SyncUser.current().getDefaultConfiguration();
      Log.i(LOG_TAG, "SyncConfiguration 1: " + configuration.toString());
      realm = Realm.getInstance(configuration);
    } else {
      realm = Realm.getDefaultInstance();
    }
    return realm
        .where(MopedModel.class)
        .contains("companyName", "mopedCo2")
        .sort("timestamp", Sort.DESCENDING)
        .findAllAsync();
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragment2Interaction(uri);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragment2InteractionListener) {
      mListener = (OnFragment2InteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragment2InteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnFragment2InteractionListener {
    void onFragment2Interaction(Uri uri);
  }
}
