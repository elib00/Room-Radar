package com.example.roomradar.Database;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DatabaseManager {
    public static void registerUser(Activity activity, FirebaseAuth auth, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(activity, "User successfully registered", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity, "User successfully registered", Toast.LENGTH_SHORT).show();
                        }
                }
        });
    }


}
