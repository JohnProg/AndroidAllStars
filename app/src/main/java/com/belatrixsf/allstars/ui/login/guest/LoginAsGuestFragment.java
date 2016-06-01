/* The MIT License (MIT)
* Copyright (c) 2016 BELATRIX
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.belatrixsf.allstars.ui.login.guest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belatrixsf.allstars.BuildConfig;
import com.belatrixsf.allstars.R;
import com.belatrixsf.allstars.ui.common.AllStarsFragment;
import com.belatrixsf.allstars.utils.AllStarsApplication;
import com.belatrixsf.allstars.utils.di.modules.presenters.LoginAsGuestPresenterModule;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;

/**
 * Created by icerrate on 27/05/16.
 */
public class LoginAsGuestFragment extends AllStarsFragment implements LoginAsGuestView {

    public static final int FACEBOOK_RQ = 64206;
    public static final int TWITTER_RQ = 140;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.facebook_log_in) LoginButton facebookLogInButton;
    @Bind(R.id.twitter_log_in) TwitterLoginButton twitterLogInButton;

    private LoginAsGuestPresenter loginAsGuestPresenter;
    private CallbackManager callbackManager;

    public LoginAsGuestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prepareFacebookSDK();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in_as_guest, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        if (savedInstanceState == null) {
            loginAsGuestPresenter.init();
        }
    }

    private void prepareFacebookSDK(){
        FacebookSdk.setApplicationId(BuildConfig.FB_ID);
        FacebookSdk.sdkInitialize(getActivity());
    }

    private void initViews() {
        fragmentListener.setToolbar(toolbar);
        fragmentListener.setTitle("");

        callbackManager = CallbackManager.Factory.create();
        facebookLogInButton.setReadPermissions(Arrays.asList("email", "public_profile "));
        facebookLogInButton.setFragment(this);
        facebookLogInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                loginAsGuestPresenter.loginWithFacebook(json);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                loginAsGuestPresenter.facebookFailure();
            }
        });

        twitterLogInButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                loginAsGuestPresenter.twitterSessionSuccess();
            }

            @Override
            public void failure(TwitterException exception) {
                loginAsGuestPresenter.twitterFailure();
            }
        });
    }

    @Override
    public void requestEmail() {
        TwitterAuthClient authClient = new TwitterAuthClient();
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                String email = result.data;
                loginAsGuestPresenter.twitterEmailSucces(email);
            }

            @Override
            public void failure(TwitterException exception) {
                loginAsGuestPresenter.twitterFailure();
            }
        });
    }

    @Override
    public void requestUserData() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                User user = userResult.data;
                final String pictureUrl = user.profileImageUrl;
                final String userName = user.name;
                loginAsGuestPresenter.twitterUserDataSuccess(pictureUrl, userName);
            }

            @Override
            public void failure(TwitterException e) {
                loginAsGuestPresenter.twitterFailure();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginAsGuestPresenter = null;
    }

    @Override
    protected void initDependencies(AllStarsApplication allStarsApplication) {
        loginAsGuestPresenter = allStarsApplication.getApplicationComponent()
                .loginAsGuestComponent(new LoginAsGuestPresenterModule(this))
                .logiinAsGuestPresenter();
    }

    @Override
    public void backToLogin() {
        fragmentListener.closeActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FACEBOOK_RQ){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == TWITTER_RQ) {
            twitterLogInButton.onActivityResult(requestCode, resultCode, data);
        }


    }

}
