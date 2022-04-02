package com.example.splitter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class DbUtils {

    static void pushDataToDB(String docId, Map<String, Object> data) {
        if (docId == null)
            docId = String.valueOf(data.hashCode());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(docId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Constant.TAG_DEBUG, "DocumentSnapshot added ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constant.TAG_WARN, "Error adding document", e);
                    }
                });
    }
}
