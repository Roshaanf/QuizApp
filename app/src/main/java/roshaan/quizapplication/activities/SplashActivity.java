package roshaan.quizapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import roshaan.quizapplication.R;
import roshaan.quizapplication.UserModel;
import roshaan.quizapplication.databinding.ActivitySplashBinding;

import static java.lang.Thread.sleep;

public class SplashActivity extends Activity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference("Users");
      //  binding.imgV.setVisibility(View.INVISIBLE);
        Animation anim= AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fade_in);
        binding.imgV.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() == null) {

                    //imageAnimation();

                    startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                } else {


                    mAuth = FirebaseAuth.getInstance();
                    mRef = FirebaseDatabase.getInstance().getReference("Users");

                    if (mAuth.getCurrentUser() == null) {

                      //  imageAnimation();

                        startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                       // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                    } else {

                        mRef.orderByChild("userID").equalTo(mAuth.getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            Iterable<DataSnapshot> child = dataSnapshot.getChildren();
//                            System.out.println(user);

                                            UserModel user = null;
                                            for (DataSnapshot ch : child)
                                                user = ch.getValue(UserModel.class);

                                            System.out.println(dataSnapshot.getValue());
                                            if (user.getUserType().equals("Player")) {
                                                startActivity(new Intent(SplashActivity.this, PlayerHomeActivity.class));
                                                finish();
                                            } else if (user.getUserType().equals("QuizTaker")) {
                                                startActivity(new Intent(SplashActivity.this, QuizTakerHomeActivity.class));
                                                finish();
                                            }
                                        } else {
                                            Toast.makeText(SplashActivity.this, "User doesnt exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }
                }
            }
        }, 3000);
    }


}

