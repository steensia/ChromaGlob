package com.asimplenerd.chromaglobs;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int CREATION_SUCCESSFUL = 1;
    private int CREATION_FAILED_DUPLICATE_USER = 2;
    private int CREATION_FAILED_OTHER = 3;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;

    private EditText user, pass, passConf;

    private FirebaseAuth firebaseAuth;

    private String username, password;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
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

        view = inflater.inflate(R.layout.fragment_create_account, container, false);

        user = view.findViewById(R.id.emailField);
        pass = view.findViewById(R.id.passwordField);
        passConf = view.findViewById(R.id.passConfirmField);

        //Set Confirm button listener to this fragment
        view.findViewById(R.id.signUpButton).setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.signUpButton:
                signUserUp();
                break;
            default:
                break;
        }
    }

    private void returnToLogin(){

    }

    private void signUserUp(){
        String uname = user.getText().toString().trim();
        String pword = pass.getText().toString().trim();
        String conf = pass.getText().toString().trim();
        if(uname.isEmpty()){
            Toast.makeText(getContext(), "Username cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if(pword.isEmpty()){
            Toast.makeText(getContext(), "Password cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!pword.equals(conf)){
            Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
            return;
        }
        signUpIfNewUser(uname, conf);
    }


    private void signUpIfNewUser(String username, String password){
        //TODO implement checking for existing user on firebase!
        AccountCreator ac = new AccountCreator();
        this.username = username;
        this.password = password;
        ac.execute(this);
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

    private class AccountCreator extends AsyncTask<Fragment, Integer, Long>{
        @Override
        public Long doInBackground(Fragment... obj){
            firebaseAuth = FirebaseAuth.getInstance();
            final Fragment current = obj[0];
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        publishProgress(CREATION_SUCCESSFUL);
                        current.getFragmentManager().popBackStack();
                    }
                    else{
                        Log.d("UserCreateFailed", task.getException().toString());
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            publishProgress(CREATION_FAILED_DUPLICATE_USER);
                        }
                        else{
                            publishProgress(CREATION_FAILED_OTHER);
                        }
                    }
                }
            });
            return 0L;
        }

        @Override
        public void onProgressUpdate(Integer... val){
            switch(val[0]){
                case 1:
                    Toast.makeText(getContext(), "User successfully created! Please log in.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getContext(), "An unknown error occurred!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
