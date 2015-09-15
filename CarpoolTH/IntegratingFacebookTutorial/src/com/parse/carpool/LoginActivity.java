package com.parse.carpool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.integratingfacebooktutorial.R;



import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {
    boolean doubleBackToExitPressedOnce = false;

  private Dialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.login);

    String text = getString(R.string.term_policy);
    String termLink = getString(R.string.term);
    String policyLink = getString(R.string.policy);
    int startTerm = text.indexOf(termLink);
    int endTerm = startTerm + termLink.length();
    int startPolicy = text.indexOf(policyLink);
    int endPolicy = startPolicy + policyLink.length();

      SpannableString spannableString = new SpannableString(text);
      spannableString.setSpan(new CallTerm(), startTerm, endTerm, 0);
      spannableString.setSpan(new CallPolicy(), startPolicy, endPolicy, 0);

      TextView textView = (TextView)findViewById(R.id.termPrivacyTV);
      textView.setText(spannableString);
      textView.setMovementMethod(new LinkMovementMethod());

    // Check if there is a currently logged in user
    // and it's linked to a Facebook account.
    ParseUser currentUser = ParseUser.getCurrentUser();
    if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
      // Go to the user info activity
      showUserDetailsActivity();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  public void onLoginClick(View v) {
    progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

    List<String> permissions = Arrays.asList("public_profile", "email");
    // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
    // (https://developers.facebook.com/docs/facebook-login/permissions/)

    ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException err) {
            progressDialog.dismiss();
            if (user == null) {
                Log.d(IntegratingFacebook.TAG, "Uh oh. The user cancelled the Facebook login.");
                Toast.makeText(getApplicationContext(), "Log out successfully...", Toast.LENGTH_SHORT).show();
            } else if (user.isNew()) {
                Log.d(IntegratingFacebook.TAG, "User signed up and logged in through Facebook!");
                Toast.makeText(getApplicationContext(), "Register with Facebook...", Toast.LENGTH_SHORT).show();
                showUserDetailsActivity();
            } else {
                Log.d(IntegratingFacebook.TAG, "User logged in through Facebook!");
                Toast.makeText(getApplicationContext(), "Login successfully...", Toast.LENGTH_SHORT).show();
                showUserDetailsActivity();
            }
        }
    });
  }

  private void showUserDetailsActivity() {
    Intent intent = new Intent(this, com.parse.carpool.MainActivity.class);
    startActivity(intent);
      finish();
  }

    private class CallTerm extends ClickableSpan{
        @Override
        public void onClick(View widget){
            Intent intent = new Intent(LoginActivity.this, Terms.class);
            startActivity(intent);
        }
    }

    private class CallPolicy extends ClickableSpan{
        @Override
        public void onClick(View widget){
            Intent intent = new Intent(LoginActivity.this, Policy.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

}
