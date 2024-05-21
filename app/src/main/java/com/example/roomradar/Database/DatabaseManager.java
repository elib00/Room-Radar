package com.example.roomradar.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.roomradar.Entities.BoardingHouse;
import com.example.roomradar.Entities.User;
import com.example.roomradar.MainActivity;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DatabaseManager {

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    public static void registerUser(Activity activity, FirebaseAuth auth, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();

                            } else {
                                Toast.makeText(activity, "Failed to get user UID", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(activity, "User successfully registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "User successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void validateUser(Activity activity, FirebaseAuth auth, String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Context context = activity.getBaseContext();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(activity, "Login Successful. ", Toast.LENGTH_SHORT).show();

                        String userUID = Objects.requireNonNull(authResult.getUser()).getUid();
                        System.out.println(userUID);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        CollectionReference userProfileCollection = db.collection("userprofile");

                        //USERPROFILE
                        User user = new User("Zhazted", "Valles", true, "default");
                        userProfileCollection.document(userUID).set(user);

                        //BOARDINGHOUSES
                        BoardingHouse bh = new BoardingHouse.Builder().setPropertyName("Rizwill Apartelle").setAddress("Cebu", "Bogo", "Poblacion", "Tres De Abril").setLandlordID(userUID).build();

                        CollectionReference boardingHousesCollection = db.collection("boardinghouses");
                        boardingHousesCollection.document("bh1").set(bh);

                        //STORAGE CREATE FOLDER
                        createFolderToCloud(activity, userUID);

                        //STORAGE UPLOAD PICTURE TO FOLDER
                        if (hasStoragePermission(activity)) {
                            List<String> galleryImagePaths = getImagePathsFromGallery(activity);
                            if (!galleryImagePaths.isEmpty()) {
                                uploadImageToFolder(activity, userUID, "sample", galleryImagePaths.get(0));
                            } else {
                                Toast.makeText(activity, "No images found in gallery", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            requestStoragePermission(activity);
                        }


                        context.startActivity(intent);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Login unsuccessful. Please enter valid credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static boolean hasStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    private static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
    }

    public static void createFolderToCloud(Activity activity, String userUID) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference folderRef = storage.getReference().child(userUID);

        folderRef.putBytes(new byte[0])
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(activity, "Folder created successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Failed to create folder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static void uploadImageToFolder(Activity activity, String folderName, String fileName, String imagePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference folderRef = storage.getReference().child(folderName);
        StorageReference imageRef = folderRef.child(fileName);

        File file = new File(imagePath);
        Uri imageUri = Uri.fromFile(file);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(activity, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static List<String> getImagePathsFromGallery(Context context) {
        List<String> imagePaths = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imagePaths.add(path);
            }
            cursor.close();
        }
        return imagePaths;
    }
}
