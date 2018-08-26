package com.example.gregorio.greenjiin;

import static com.example.gregorio.greenjiin.Constants.AUTH_URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.SyncUser.Callback;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

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

  private static final String LOG_TAG = LoginFragment.class.getSimpleName();
  private static final String EMAIL = "email";
  private static final String PUBLIC_PROFILE = "public_profile";



  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  private Realm realm;
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
  @BindView(R.id.login_button)
  LoginButton mFacebookLogin;
  private CallbackManager callbackManager;
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

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_login, container, false);
    ButterKnife.bind(this, rootView);
    realm = Realm.getDefaultInstance();
    callbackManager = CallbackManager.Factory.create();
    mFacebookLogin.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
    mFacebookLogin.setFragment(this);
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mLoginBtn.setOnClickListener(view1 -> attemptEmailLogin());
    mFacebookLogin.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        attemptFacebookLogin();
      }
    });
  }

  private void attemptEmailLogin() {
    // Reset errors.
    mNicknameTextView.setError(null);
    // Store values at the time of the login attempt.
    String nickname = mNicknameTextView.getText().toString();
    String password = mPassword.getText().toString();
    showProgress(true);
    SyncCredentials credentials = SyncCredentials.nickname(nickname, false);
    SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
      @Override
      public void onSuccess(SyncUser user) {
        showProgress(false);
        goToMainActivity(realm);
      }

      @Override
      public void onError(ObjectServerError error) {
        showProgress(false);
        mNicknameTextView.setError("Uh oh something went wrong! (check your logcat and Internet)");
        mNicknameTextView.requestFocus();
        Log.e(LOG_TAG, "Email Login error: " + error.toString());
      }
    });
  }

  private void attemptFacebookLogin() {
    mFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        String userId = loginResult.getAccessToken().getUserId();
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
            new GraphJSONObjectCallback() {
              @Override
              public void onCompleted(JSONObject object, GraphResponse response) {
                displayUserInfo(object);
              }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "firstName, lastName, email, id");
        graphRequest.setParameters(parameters);

        // Realm Sync Credential with Facebook Login
        SyncCredentials credentials = SyncCredentials.nickname(userId, false);
        SyncUser.logInAsync(credentials, AUTH_URL, new Callback<SyncUser>() {
          @Override
          public void onSuccess(SyncUser result) {
            goToMainActivity(realm);
          }

          @Override
          public void onError(ObjectServerError error) {
            Log.e(LOG_TAG, "Realm Facebook Login Sync error: " + error.toString());
          }
        });

      }

      @Override
      public void onCancel() {

      }

      @Override
      public void onError(FacebookException error) {
        Log.i(LOG_TAG, "Facebook Login error: " + error.toString());

      }
    });
  }

  public void displayUserInfo(JSONObject object) {
    String firstName, lastName, email, id;
    firstName = "";
    lastName = "";
    email = "";
    id = "";
    try {
      firstName = object.getString("firstName");
      lastName = object.getString("lastName");
      email = object.getString("email");
      id = object.getString("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
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
