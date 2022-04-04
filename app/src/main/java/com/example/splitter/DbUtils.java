package com.example.splitter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DbUtils {

    static void pushDataToFireStore(String docId, Map<String, Object> data) {
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

    static Map<String, Map<String, Object>> getDataFromFireStore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Map<String, Object>> result = new HashMap<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Constant.TAG_DEBUG, document.getId() + " => " + document.getData());
                                result.put(document.getId(), document.getData());
                            }
                        } else {
                            Log.w(Constant.TAG_WARN, "Error getting documents.", task.getException());
                        }
                    }
                });
        return result;
    }
}
