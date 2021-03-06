package com.cmput301f20t14.bookbox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An Activity that will allow a user to register with a unique username
 * @author Carter Sabadash
 * @version 2020.10.22
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f20t14.bookbox.R;
import com.cmput301f20t14.bookbox.entities.User;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This the activity allowing a user to register an account
 * for the app. The user is required to enter:
 * - a unique username
 * - a phone number
 * - a password
 * The user can optionally add an email address.
 * It also handles user input validation by setting
 * errors when invalid data is entered by the user.
 * User account information is stored in the Firestore
 * database.
 * @author Olivier Vadiavaloo
 * @version 2020.11.15
 * @see com.google.firebase.firestore.FirebaseFirestore
 */

public class RegisterUserActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private Button registerButton;
    private ImageButton backButton;
    private FirebaseFirestore database;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Retrieve the EditText views from the layout
        username = (EditText) findViewById(R.id.register_username_editText);
        password = (EditText) findViewById(R.id.register_password_editText);
        email = (EditText) findViewById(R.id.register_email_editText);
        phone = (EditText) findViewById(R.id.register_phone_editText);

        // Retrieve the register Button view
        registerButton = (Button) findViewById(R.id.register_activity_button);

        //Initialize imageUrl
        imageUrl = "";

        // Retrieve the back ImageButton view
        backButton = (ImageButton) findViewById(R.id.register_back_button);

        // Initialise database
        database = FirebaseFirestore.getInstance();

        // Get reference to the users collection
        final CollectionReference collectionReference = database.collection(User.USERS);

        // Set the onClickListener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // isErrorSet is true if a required field is empty
                boolean isErrorSet = false;
                final String enteredUsername = username.getText().toString().trim();
                final String enteredPassword = password.getText().toString().trim();
                final String enteredEmail = email.getText().toString().trim();
                final String enteredPhone = phone.getText().toString().trim();

                if (enteredUsername.isEmpty()) {
                    isErrorSet = true;
                    username.setError("Required");
                }

                if (enteredPassword.isEmpty()) {
                    isErrorSet = true;
                    password.setError("Required");
                }

                if (enteredEmail.isEmpty()) {
                    isErrorSet = true;
                    email.setError("Required");
                }

                if (!User.isEmailSyntaxValid(enteredEmail)) {
                    isErrorSet = true;
                    email.setError("Invalid email");
                }

                if (enteredPhone.isEmpty()) {
                    isErrorSet = true;
                    phone.setError("Required");
                }

                if (!User.isPhoneSyntaxValid(enteredPhone)) {
                    isErrorSet = true;
                    phone.setError("Invalid phone");
                }

                // if the required fields are not empty, check if the
                // username is unique by getting a DocumentReference
                // with the entered username as argument
                if (!isErrorSet) {

                    final DocumentReference documentReference = collectionReference.document(enteredUsername);

                    // Try to get data from database
                    // The login in onCompleteListener will
                    // check if there is user account with the
                    // entered username already
                    documentReference.get()
                            .addOnCompleteListener(
                                    new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();

                                                // exists() returns false if the username is not taken already
                                                if (documentSnapshot != null && !documentSnapshot.exists()) {

                                                    // create hash that contains information entered by user
                                                    final HashMap<String, String> userInfo = new HashMap<>();
                                                    userInfo.put(User.USERNAME, enteredUsername);
                                                    userInfo.put(User.EMAIL, enteredEmail);
                                                    userInfo.put(User.PHONE, enteredPhone);
                                                    userInfo.put(User.IMAGE_URL, imageUrl);

                                                    // create a firebase user before adding data to database
                                                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                    mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                // update displayName to reflect username
                                                                // this way there are minimal changes in the rest of the project
                                                                //https://stackoverflow.com/questions/38114358/firebase-setdisplayname-of-user-while-creating-user-android

                                                                FirebaseUser mUser = mAuth.getCurrentUser();

                                                                UserProfileChangeRequest profile_change = new UserProfileChangeRequest.Builder()
                                                                        .setDisplayName(enteredUsername).build();

                                                                mUser.updateProfile(profile_change).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            // put the rest of the user info in the database and start homeactivity
                                                                            // documentReference will add the user information
                                                                            // to the database at this point since it is confirmed
                                                                            // that the entered username is unique. If the addition failed,
                                                                            // the onFailureListener will show a toast on the screen
                                                                            // notifying that an error occurred
                                                                            documentReference
                                                                                    .set(userInfo)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            register(v, enteredUsername);
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(RegisterUserActivity.this,
                                                                                                            "An error occurred",
                                                                                                            Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });

                                                                        } else {
                                                                            Toast.makeText(RegisterUserActivity.this,
                                                                                            "An error occurred",
                                                                                            Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                // the email is already taken, or it's a weak password
                                                                try {
                                                                    throw task.getException();
                                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                                    password.setError("Weak Password");
                                                                } catch (Exception e) {
                                                                    email.setError("Email already in use!");
                                                                }
                                                                email.requestFocus();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    //  if the username is already taken, an error
                                                    // is set for the username EditText view
                                                    username.setError("Username already taken");
                                                    username.requestFocus();
                                                }
                                            }
                                        }
                                    }
                            );
                }
            }
        });

        // set the onClickListener for the back button
        // register activity is finished on clicking
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(CommonStatusCodes.CANCELED);
                finish();
            }
        });
    }

    /**
     * Launches the Home activity because user information
     * has been successfully save on the database
     * @author Olivier Vadiavaloo
     * @version 2020.11.13
     * @param view
     * @param enteredUsername
     */
    private void register(View view, String enteredUsername) {
        // User registered successful
        // go back to login activity so they can login

        // finish activity to prevent user from
        // going back to the registering activity
        setResult(CommonStatusCodes.SUCCESS);
        finish();
    }
}
