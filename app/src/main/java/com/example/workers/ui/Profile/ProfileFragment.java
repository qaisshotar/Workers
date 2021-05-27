package com.example.workers.ui.Profile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.workers.R;
import com.example.workers.Worker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    private String FullName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        init(root);

        getWorkerInfo();

        return root;
    }

    void init(View root) {

        root.findViewById(R.id.btnApply).setOnClickListener(this::onClick);
        root.findViewById(R.id.LayoutFullName).setOnClickListener(this::onClick);

        /** Connection With FireStore */
        db = FirebaseFirestore.getInstance();
    }

    void getWorkerInfo() {

        db.document("Workers/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            Worker user = documentSnapshot.toObject(Worker.class);
                            ((TextView) getActivity().findViewById(R.id.txtFullName)).setText(user.getName());
                            ((TextView) getActivity().findViewById(R.id.txtEmail)).setText(user.getEmail());
                            ((TextView) getActivity().findViewById(R.id.txtPhone)).setText(user.getPhoneNumber());
                            ((TextView) getActivity().findViewById(R.id.txtJobTitle)).setText(user.getJopTitle());
                            ((TextView) getActivity().findViewById(R.id.txtJobDescription)).setText(user.getJobDescription());

                            SetImage(user.getJopTitle());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERRORS", e.getMessage());
                Toast.makeText(getContext(), "User data was not fetched", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnApply:
                OnApply();
                break;
            case R.id.LayoutFullName:
                OpenChangeFullNameDialog();
        }
    }

    private void OnApply() {

        if (FullName == null) {
            Toast.makeText(getContext(), "I never changed the data", Toast.LENGTH_LONG).show();
            return;
        }
        FullName = ((TextView) getActivity().findViewById(R.id.txtFullName)).getText().toString().trim();

        Map<String, Object> map = new HashMap<>();
        map.put("name", FullName);

        db.collection("Workers")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FullName = null;
                Toast.makeText(getContext(), "The data has been successfully updated", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERRORS", e.getMessage());
                Toast.makeText(getContext(), "The data was not successfully updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void OpenChangeFullNameDialog() {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_data, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        final AlertDialog Dialog = builder.create();
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editTxtFullName = dialogView.findViewById(R.id.editTxtFullName);

        dialogView.findViewById(R.id.btnApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FullName = editTxtFullName.getText().toString().trim();

                if (FullName.isEmpty()) {
                    editTxtFullName.setError("Enter the full name");
                    return;
                }

                ((TextView) getActivity().findViewById(R.id.txtFullName)).setText(FullName);

                Dialog.dismiss();
            }
        });
        Dialog.show();
    }

    private void SetImage(String JopTitle) {

        db.document("Jobs/" + JopTitle)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot Job) {

                        if (Job.exists()) {
                            // Loading Image
                            Glide.with(getContext())
                                    .load(Job.getData().get("Url").toString())
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into((CircleImageView) getActivity().findViewById(R.id.icon));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERRORS", e.getMessage());
            }
        });

    }
}