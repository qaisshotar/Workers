package com.example.workers.ui.Requests;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workers.R;
import com.example.workers.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView RCV_Requests;
    public static ArrayList<Request> requests;
    public static RequestsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        init(root);

        getMyRequests();

        return root;
    }

    void init(View root) {

        /** Connection With FireStore */
        db = FirebaseFirestore.getInstance();

        RCV_Requests = root.findViewById(R.id.RCV_Requests);
        requests = new ArrayList<>();

        adapter = new RequestsAdapter(getContext(), db, requests);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RCV_Requests.setLayoutManager(layoutManager);
        RCV_Requests.setAdapter(adapter);

    }

    public void getMyRequests() {

        db.collection("Requests")
                .whereEqualTo("wid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("requestdate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot Request : queryDocumentSnapshots) {

                            Request request = Request.toObject(Request.class);
                            request.setRID(Request.getId());
                            requests.add(request);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i("ERRORS", e.getMessage());
                    }
                });
    }
}
