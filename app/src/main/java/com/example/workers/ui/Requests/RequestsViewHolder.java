package com.example.workers.ui.Requests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.workers.Format;
import com.example.workers.Notification;
import com.example.workers.R;
import com.example.workers.Request;
import com.example.workers.Service.Notify;
import com.example.workers.User;
import com.example.workers.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.workers.Global.WorkerName;

public class RequestsViewHolder extends RecyclerView.ViewHolder {

    private TextView txtUserName, txtStatus, txtDate;
    private CircleImageView icon;
    private Context mContext;
    private FirebaseFirestore db;
    private User user;
    private ConstraintLayout CardRequests;
    private Request mRequest;
    private ImageButton btnPhone, btnPinLocation ,btnjobdone, btnCancel;

    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);

        txtUserName = itemView.findViewById(R.id.txtUserName);
        txtStatus = itemView.findViewById(R.id.txtStatus);
        txtDate = itemView.findViewById(R.id.txtDate);
        icon = itemView.findViewById(R.id.icon);
        CardRequests = itemView.findViewById(R.id.CardRequests);
        btnPhone = itemView.findViewById(R.id.btnPhone);
        btnPinLocation = itemView.findViewById(R.id.btnPinLocation);
        btnjobdone = itemView.findViewById(R.id.btnjobdone);
        btnCancel = itemView.findViewById(R.id.btncancle);






    }

    public void onBind(final Context context, FirebaseFirestore db, Request request, int position) {

        this.mContext = context;
        mRequest = request;
        this.db = db;
        db.document("Users/" + request.getUID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot User) {

                        if (User.exists()) {

                            user = User.toObject(User.class);

                            txtUserName.setText(user.getFullname().trim());

                        }
                    }
                });

        txtDate.setText(Format.DateFormat(request.getRequestdate().getTime()));

        txtStatus.setText(request.getStatus().trim());

        switch (request.getStatus()) {
            case "padding":
                txtStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case "Accepted":
                txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case "Rejected":
                txtStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
            case "Job Done":
                txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        CardRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null) {

                    if (request.getStatus().trim().toLowerCase().equals("padding")) {

                        OnConfirm();
                    }
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestsFragment.requests.remove(position);
                RequestsFragment.adapter.notifyDataSetChanged();
            }
        });



        btnjobdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            //    db.collection("Requests")
                    //    .document(mRequest.getRID());

               //mRequest.setStatus("Job Done");
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);



                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    final RatingBar rating = new RatingBar(mContext);

                     linearLayout.addView(rating);
                    alertDialog.setTitle("Do you want to finish the job");
                    rating.setNumStars(5);
                    alertDialog.setView(rating);
                    alertDialog.setMessage("please Rate user");
                    alertDialog.setView(linearLayout);
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            txtStatus.setText("Job Done");
                            txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));

                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();



            }
        });



        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCall();
            }
        });

        btnPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getLatLng() != null) {

                    String uri = "http://maps.google.com/maps?daddr=" + user.getLatLng().getLatitude()
                            + "," + user.getLatLng().getLongitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                } else
                    Toast.makeText(mContext, "The Location is not specified before", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void OnConfirm() {

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_response_order, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        final AlertDialog Dialog = builder.create();
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtUsername = dialogView.findViewById(R.id.txtUserName);
        TextView txtPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);


        txtUsername.setText(user.getFullname());
        txtPhoneNumber.setText(user.getPhone());


        dialogView.findViewById(R.id.btn_Accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnUpdate(Dialog, true);
            }
        });

        dialogView.findViewById(R.id.btn_Reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnUpdate(Dialog, false);
            }
        });

        Dialog.show();

    }

    private void OnUpdate(AlertDialog dialog, boolean isAccept) {

        Map<String, Object> map = new HashMap<>();
        map.put("status", (isAccept) ? "Accepted" : "Rejected");

        db.collection("Requests")
                .document(mRequest.getRID())
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                int color = 0;
                String Mess = "";
                if (isAccept) {

                    color = R.color.green;
                    Mess = "The request has been accepted";
                    mRequest.setStatus("Accepted");
                } else {

                    color = R.color.red;
                    Mess = "request has been rejected";
                    mRequest.setStatus("Rejected");
                }
                txtStatus.setText(mRequest.getStatus());
                txtStatus.setTextColor(ContextCompat.getColor(mContext, color));
                dialog.dismiss();
                Toast.makeText(mContext, Mess, Toast.LENGTH_LONG).show();

                OnSendNotification(isAccept);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("ERRORS", e.getMessage());
                Toast.makeText(mContext, "A problem occurred, try later", Toast.LENGTH_LONG).show();
            }
        });

    }

    void onClickCall() {

        if (user.getPhone() != null && user.getPhone().length() != 0) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Service");
            alertDialog.setMessage("Do you want call phone number ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", user.getPhone(), null)));
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else
            Toast.makeText(mContext, "He does not have the phone number.", Toast.LENGTH_SHORT).show();
    }

    void OnSendNotification(boolean isAccept){

        Log.i("zozo","UID ="+mRequest.getUID());
        db.collection("Token").document(mRequest.getUID())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult().exists())
                {
                    String Mess="";
                    if(isAccept)
                        Mess="Accepted to the maintenance request";
                    else
                        Mess="Rejected to the maintenance request";

                    Notification notification = new Notification(task.getResult().get("Token").toString(),WorkerName+" "+Mess);
                    notification.execute();


                }else
                    Log.i("ERRORS","Token Not Found : UID ="+mRequest.getUID());
            }
        });

    }


}
