package com.example.gregorio.greenjiin;

import static com.example.gregorio.greenjiin.Constants.AUTH_URL;
import static com.example.gregorio.greenjiin.Constants.REALM_BASE_URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  private Realm realm;

  // private EditText mNicknameTextView;
  // private View mProgressView;
  @BindView(R.id.sign_in_form)
  ScrollView mLoginFormView;
  @BindView(R.id.username)
  EditText mNicknameTextView;
  @BindView(R.id.password)
  EditText mPassword;
  @BindView(R.id.sign_in_progress)
  ProgressBar mProgressView;
  @BindView(R.id.sign_in_button)
  Button mLoginBtn;
  private View rootView;

  private OnFragmentInteractionListener mListener;

  public LoginFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment LoginFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static LoginFragment newInstance(String param1, String param2) {
    LoginFragment fragment = new LoginFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }

    if (SyncUser.current() != null) {
      this.goToMainActivity(realm);
    }

  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_login, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mLoginBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });
  }

  private void attemptLogin() {
    // Reset errors.
    mNicknameTextView.setError(null);
    // Store values at the time of the login attempt.
    String nickname = mNicknameTextView.getText().toString();
    showProgress(true);

    SyncCredentials credentials = SyncCredentials.nickname(nickname, false);
    SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
      @Override
      public void onSuccess(SyncUser user) {
        showProgress(false);
        goToMainActivity(realm);
        setUpRealm();

      }

      @Override
      public void onError(ObjectServerError error) {
        showProgress(false);
        mNicknameTextView.setError("Uh oh something went wrong! (check your logcat please)");
        mNicknameTextView.requestFocus();
        Log.e("Login error", error.toString());
      }
    });
  }

  private RealmResults<MopedModel> setUpRealm() {
    SyncConfiguration configuration = SyncUser.current()
        .createConfiguration(REALM_BASE_URL + "/default")
        .build();
    realm = Realm.getInstance(configuration);
    return realm
        .where(MopedModel.class)
        .sort("timestamp", Sort.DESCENDING)
        .findAllAsync();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Close the Realm Object.
    if (realm != null) {
      realm.close();
    }
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    mLoginFormView.animate().setDuration(shortAnimTime).alpha(
        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    });

    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    mProgressView.animate().setDuration(shortAnimTime).alpha(
        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      }
    });
  }


  public void goToMainActivity(Realm realm) {
    if (mListener != null) {
      mListener.onFragmentInteraction(realm);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
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
   */
  public interface OnFragmentInteractionListener {

    void onFragmentInteraction(Realm realm);
  }
}
