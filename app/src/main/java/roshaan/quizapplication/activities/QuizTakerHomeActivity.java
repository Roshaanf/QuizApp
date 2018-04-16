package roshaan.quizapplication.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import roshaan.quizapplication.HomeFragment;
import roshaan.quizapplication.NewQuestionFragment;
import roshaan.quizapplication.NewQuizFragment;
import roshaan.quizapplication.R;
import roshaan.quizapplication.databinding.ActivityQuizTakerHomeBinding;

public class QuizTakerHomeActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, View.OnClickListener,
        NewQuestionFragment.OnFragmentInteractionListener,
        NewQuizFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener {

    ActivityQuizTakerHomeBinding binding;

    FragmentManager mng;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_taker_home);

        mng = getSupportFragmentManager();

        mng.beginTransaction()
                .replace(R.id.content, new HomeFragment())
                .commit();


        binding.drawer.addDrawerListener(this);
        setupListeners();
    }

    private void setupListeners() {
        binding.newQuestion.setOnClickListener(this);
        binding.newQuiz.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        binding.drawer.closeDrawer(GravityCompat.START);
        this.view = view;

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {


    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {


        if (view == binding.newQuestion) {

            mng.beginTransaction()
                    .replace(R.id.content, new NewQuestionFragment())
                    .commit();
        } else if (view == binding.newQuiz) {

            mng.beginTransaction()
                    .replace(R.id.content, new NewQuizFragment())
                    .commit();
        } else if (view == binding.logout) {

            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        }

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }



    @Override
    public void onFragmentInteraction(int i) {

        binding.drawer.openDrawer(GravityCompat.START);
    }
}
