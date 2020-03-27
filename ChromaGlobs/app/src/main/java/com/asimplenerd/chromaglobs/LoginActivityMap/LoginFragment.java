package com.asimplenerd.chromaglobs.LoginActivityMap;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.asimplenerd.chromaglobs.Classes.DatabaseManagerKt;
import com.asimplenerd.chromaglobs.MainActivity;
import com.asimplenerd.chromaglobs.MainMenuFragment;
import com.asimplenerd.chromaglobs.Classes.Player;
import com.asimplenerd.chromaglobs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int toastUserOrPassErr = 2;
    private static int toastUserLoginSuccess = 3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MainActivity mainActivity;

    private ProgressBar loginSpinner;

    private Button loginBtn, forgotPasswordBtn, signUpBtn;

    private EditText usernameField, passwordField;

    private String username, password;

    private FirebaseAuth firebaseAuth;

    private boolean loginSuccessful = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFields.
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_fields, container, false);

        //Find each edit text so we can track them
        usernameField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);

        //Begin setup for button listeners to use this particular fragment's on click listener
        //First, find each button
        loginBtn = view.findViewById(R.id.loginButton);
        signUpBtn = view.findViewById(R.id.signUpButton);
        forgotPasswordBtn = view.findViewById(R.id.forgotPasswordButton);

        //Set each button's listener
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        forgotPasswordBtn.setOnClickListener(this);

        //Setup progress spinner for login
        loginSpinner = view.findViewById(R.id.loginProgressBar);
        loginSpinner.setIndeterminate(true); //Spin without actual progress

        //Begin firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //Now we can return the new view that was created.
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v){
        Log.d("CLICK", "YEAH");
        View fragmentView = getView();
        if(fragmentView == null){
            return;
        }
        switch(v.getId()){
            case R.id.loginButton:
                attemptLogin(fragmentView);
                break;
            case R.id.forgotPasswordButton:
                resetPassword(fragmentView);
                break;
            case R.id.signUpButton:
                createNewAccount(fragmentView);
                break;
            default:
                break;
        }
    }

    private void attemptLogin(View view){
        Log.d("ViewInfo", view.getClass().toString());
        EditText usernameField = view.findViewById(R.id.emailField);
        EditText passwordField = view.findViewById(R.id.passwordField);
        if(usernameField == null || passwordField == null){
            Log.e("NullFields", "Unable to find the username or password field.");
            return;
        }
        username = usernameField.getText().toString().trim();
        password = passwordField.getText().toString().trim();
        if(username.trim().isEmpty()){
            Toast.makeText(getContext(), "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            Toast.makeText(getContext(), "Email is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getContext(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("Login", "Button clicked. User: " + username + "\t Password: " + password);
        AccountManager accMgr = new AccountManager();
        AsyncTask task = accMgr.execute(loginSpinner);
    }

    private class AccountManager extends AsyncTask<ProgressBar, Integer, Long>{

        Player p;

        @Override
        public Long doInBackground(ProgressBar... pb){
            publishProgress(View.VISIBLE); //Show the progress bar
            //Check login info
            //TODO check login info
            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("Login", "User successfully logged in");
                        //Retrieve this user's info
                        p = getInfoAsPlayer(task.getResult());
                        mainActivity.setPlayer(p);

                        onProgressUpdate(toastUserLoginSuccess);
                    }
                    else{
                        onProgressUpdate(toastUserOrPassErr);
                        Log.d("LogIn", "Username or password invalid!");
                    }
                }
            });
            //Log.d("Firebase", user.getKey().toString());
            try{Thread.sleep(2000);}
            catch(Exception e){}
            //Looper.prepare();
            publishProgress(View.GONE);
            return 0L;
        }

        @Override
        public void onProgressUpdate(Integer... update){
            loginSpinner.setVisibility(update[0]);
            if(update[0] == View.VISIBLE){
                loginBtn.setEnabled(false);
                forgotPasswordBtn.setEnabled(false);
                signUpBtn.setEnabled(false);
                usernameField.setEnabled(false);
                passwordField.setEnabled(false);
            }
            else if(update[0] == View.GONE) {
                loginBtn.setEnabled(true);
                forgotPasswordBtn.setEnabled(true);
                signUpBtn.setEnabled(true);
                usernameField.setEnabled(true);
                passwordField.setEnabled(true);
                if(loginSuccessful){
                    mainActivity.swapToNewFragment(new MainMenuFragment(), false);
                }
            }
            else if(update[0] == toastUserOrPassErr){
                Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                onProgressUpdate(View.GONE);
            }
            else if(update[0] == toastUserLoginSuccess){
                //This is used to display Toast messages
                Toast.makeText(getContext(), "User: " + username + " logged in!", Toast.LENGTH_SHORT).show();
                loginSuccessful = true;
                onProgressUpdate(View.GONE);
            }
        }

        private Player getInfoAsPlayer(AuthResult result){
            DatabaseManagerKt.getUsername(result.getUser().getUid());
            Log.d("AuthResLogin", result.getUser().getUid());
            String name = DatabaseManagerKt.getPlayerName();
            String id = result.getUser().getUid();
            ArrayList<Card> ownedCards = new ArrayList<>();
            return new Player(name, id, ownedCards);
        }

    }

    private void resetPassword(View view) {
        Log.d("Password", "Reset requested");
        ((MainActivity) getActivity()).swapToNewFragment(new ForgotPasswordFragment(), true);
    }

    private void createNewAccount(View view) {

        Log.d("Account", "New account creation requested");
        ((MainActivity) getActivity()).swapToNewFragment(new SignUpFragment(), true);
    }
}
