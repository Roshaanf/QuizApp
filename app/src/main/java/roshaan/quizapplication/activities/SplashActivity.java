package roshaan.quizapplication.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import roshaan.quizapplication.R;
import roshaan.quizapplication.UserModel;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference("Users");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() == null) {

                    startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));

                    finish();
                } else {


                    mAuth = FirebaseAuth.getInstance();
                    mRef = FirebaseDatabase.getInstance().getReference("Users");

                    if (mAuth.getCurrentUser() == null) {

                        startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
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
        }, 2000);
    }
}

