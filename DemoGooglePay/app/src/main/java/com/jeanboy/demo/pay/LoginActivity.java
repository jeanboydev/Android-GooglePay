package com.jeanboy.demo.pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final int REQUEST_CODE_RESOLVE_ERR = 1005;
    private static final int REQUEST_CODE_SIGN_IN = 1006;
    private static final String WALLET_SCOPE = "https://www.googleapis.com/auth/payments.make_payments";


//    private GoogleApiClient mGoogleApiClient;

//    private SignInButton sign_in_button;
    private EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
//        sign_in_button.setSize(SignInButton.SIZE_WIDE);
//        et_username = (EditText) findViewById(R.id.et_username);
//        et_password = (EditText) findViewById(R.id.et_password);

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestScopes(new Scope(WALLET_SCOPE))
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Log.e(TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());
//                    }
//                })
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(@Nullable Bundle bundle) {
////                        if (mLoginAction == LoginActivity.Action.LOGOUT) {
////                            logOut();
////                        } else {
//                            silentSignIn();
////                        }
//                    }
//
//                    @Override
//                    public void onConnectionSuspended(int i) {
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        sign_in_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSignInClicked();
//            }
//        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CODE_SIGN_IN:
//                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                handleSignInResult(result);
//                break;
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//                break;
//        }
//    }
//
//    public void toLogin(View v) {
//        Toast.makeText(this, "未实现", Toast.LENGTH_LONG).show();
//
//    }
//
//
//    private void onSignInClicked() {
//        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()) {
//            Log.d(TAG, "googleSignIn:SUCCESS");
//            handleSignInSuccess(result.getSignInAccount());
//        } else {
//            Log.d(TAG, "googleSignIn:FAILURE:" + result.getStatus());
//            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleSignInSuccess(GoogleSignInAccount account) {
//        Toast.makeText(this, getString(R.string.welcome_user, account.getDisplayName()), Toast.LENGTH_SHORT).show();
//
//        // TODO: 2016/9/13 application 记录登录的用户信息
////        ((BikestoreApplication) getActivity().getApplication()).login(account.getEmail());
////        getActivity().setResult(Activity.RESULT_OK);
////        getActivity().finish();
//    }
//
//    private void silentSignIn() {
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//
//        if (opr.isDone()) {
//            handleSignInResult(opr.get());
//        }
//    }
//
//    private void logOut() {
//        if (mGoogleApiClient.isConnected()) {
//            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//
//            // TODO: 2016/9/13 application 登出
////            ((BikestoreApplication) getActivity().getApplication()).logout();
////            Toast.makeText(getActivity(), getString(R.string.logged_out), Toast.LENGTH_LONG).show();
////            getActivity().setResult(Activity.RESULT_OK);
////            getActivity().finish();
//        } else {
////            mLoginAction = LoginActivity.Action.LOGOUT;
//        }
//    }
}
