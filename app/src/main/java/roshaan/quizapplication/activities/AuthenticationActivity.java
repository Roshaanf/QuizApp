package roshaan.quizapplication.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import roshaan.quizapplication.LoginFragment;
import roshaan.quizapplication.R;
import roshaan.quizapplication.SignupFragment;

public class AuthenticationActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener {

    FragmentManager mng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mng = getSupportFragmentManager();
        mng.beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    @Override
    public void onLoginFragmentInteraction(int uri) {
        switch (uri) {

            case LoginFragment.OnFragmentInteractionListener.PLAYER_FEED:
                startActivity(new Intent(this, PlayerHomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
              finish();
                break;

            case LoginFragment.OnFragmentInteractionListener.QUIZTAKER_FEED:
                startActivity(new Intent(this, QuizTakerHomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
            case LoginFragment.OnFragmentInteractionListener.SIGNUP:
                mng.beginTransaction()
                        .replace(R.id.container, new SignupFragment())
                        .addToBackStack(null)
                        .commit();

        }
    }

    @Override
    public void onSignupFragmentInteraction(int i) {

        switch (i) {

            case SignupFragment.OnFragmentInteractionListener.PLAYER_FEED:
                startActivity(new Intent(this, PlayerHomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;

            case SignupFragment.OnFragmentInteractionListener.QUIZTAKER_FEED:
                startActivity(new Intent(this, QuizTakerHomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
        }
    }
}
