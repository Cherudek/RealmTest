package com.example.gregorio.greenjiin;

import android.content.Context;
import android.net.Uri;
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
import android.widget.TextView;
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

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private static final String LOG_TAG = Moped2Fragment.class.getSimpleName();


  private static final String ARG_SECTION_NUMBER = "section_number2";
  @BindView(R.id.recycler_view2)
  RecyclerView recyclerView;
  private Realm realm;
  private View rootView;

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragment2InteractionListener mListener;

  public Moped2Fragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment Moped2Fragment.
   */
  // TODO: Rename and change types and number of parameters
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
    TextView textView = rootView.findViewById(R.id.section_label2);
    textView
        .setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
    ButterKnife.bind(this, rootView);

    realm = Realm.getDefaultInstance();

    final MopedCo2Adapter mopedCo2Adapter = new MopedCo2Adapter(setUpRealm());

    Log.i(LOG_TAG, "Number of Mopeds: " + realm.where(MopedModel.class)
        .contains("companyName", "mopedCo1")
        .findAllAsync());


    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    recyclerView.setAdapter(mopedCo2Adapter);


    return rootView;
  }

  private RealmResults<MopedModel> setUpRealm() {

    if (SyncUser.current() != null) {
      SyncConfiguration configuration = SyncUser.current().getDefaultConfiguration();
      Log.i(LOG_TAG, "SyncConfiguration 1: " + configuration.toString());

//          .createConfiguration(REALM_BASE_URL + "/default")
//          .build();
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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

//      try {
//
//
//            RealmResults<MopedModel> moped = realm
//                .where(MopedModel.class)
//                .contains("companyName", "mopedCo2")
//                .findAllAsync();
//
//            final MopedCo2Adapter mopedCo2Adapter = new MopedCo2Adapter(moped, true);
//            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            recyclerView.setAdapter(mopedCo2Adapter);
//
//
//      } finally {
//        if(realm !=null){
//          realm.close();
//        }
//      }

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

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragment2InteractionListener {

    // TODO: Update argument type and name
    void onFragment2Interaction(Uri uri);
  }
}
