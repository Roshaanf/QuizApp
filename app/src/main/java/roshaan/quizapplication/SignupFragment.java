package roshaan.quizapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import roshaan.quizapplication.databinding.FragmentSignupBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    FragmentSignupBinding binding;

    FirebaseAuth mAuth;
    DatabaseReference mRef;

    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Singing up");

        setupListeners();
        return binding.getRoot();
    }

    private void setupListeners() {

        binding.Signup.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {

        RadioButton rb = getView().findViewById(binding.rg.getCheckedRadioButtonId());

        if (view == binding.Signup) {
            progressDialog.show();
            if (binding.email.length() > 0 &&
                    binding.password.length() > 0) {

                if (performValiations()) {

                    signUp(binding.email.getText().toString(),
                            binding.password.getText().toString(),
                            rb.getText().toString()
                    );
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean performValiations() {

        boolean flag = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText()).matches()) {
            binding.email.setError("Invalid format");
            flag = false;
        }
        if (binding.password.length() < 8) {
            binding.password.setError("Password must be 8 characters long");
            flag = false;
        }

        return flag;
    }

    private void signUp(final String email, String password, final String userType) {

        progressDialog.setMessage("Signing in");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            setUpProfileInDatabase(new UserModel(email, userType, mAuth.getCurrentUser().getUid()));
                        } else {
                            progressDialog.dismiss();
                            System.out.println(task.getException());
                            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setUpProfileInDatabase(final UserModel user) {

        String key = mRef.push().getKey();

        mRef.child(key)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            if (user.getUserType().equals("Player")) {
                                progressDialog.dismiss();
                                mListener.onSignupFragmentInteraction(OnFragmentInteractionListener.PLAYER_FEED);
                            } else if (user.getUserType().equals("QuizTaker")) {
                                progressDialog.dismiss();
                                mListener.onSignupFragmentInteraction(OnFragmentInteractionListener.QUIZTAKER_FEED);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error inserting user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public interface OnFragmentInteractionListener {
        int PLAYER_FEED = 0, QUIZTAKER_FEED = 1;

        // TODO: Update argument type and name
        void onSignupFragmentInteraction(int i);
    }
}
