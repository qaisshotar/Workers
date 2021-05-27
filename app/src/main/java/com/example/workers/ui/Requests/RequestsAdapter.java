package com.example.workers.ui.Requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workers.R;
import com.example.workers.Request;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Request> requests;
    private FirebaseFirestore db;
    public RequestsAdapter(Context context, FirebaseFirestore db, ArrayList<Request> requests){
        this.context=context;
        this.requests=requests;
        this.db = db;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new RequestsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_requests,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(true);
        ((RequestsViewHolder)holder).onBind(context,db,requests.get(position), position);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
