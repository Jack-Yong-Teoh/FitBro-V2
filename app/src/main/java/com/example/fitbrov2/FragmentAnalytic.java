package com.example.fitbrov2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnalytic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAnalytic extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView ociValue, calorieConsumed, valueDifference, weight, height, age, gender, status, comments;
    private DatabaseReference reference;
    private FirebaseUser user;

    public FragmentAnalytic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAnalytic.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAnalytic newInstance(String param1, String param2) {
        FragmentAnalytic fragment = new FragmentAnalytic();
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
        return inflater.inflate(R.layout.fragment_analytic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ociValue = (TextView) getView().findViewById(R.id.tv01);
        calorieConsumed = (TextView) getView().findViewById(R.id.tv02);
        valueDifference = (TextView) getView().findViewById(R.id.tv03);
        weight = (TextView) getView().findViewById(R.id.tv04);
        height = (TextView) getView().findViewById(R.id.tv05);
        age = (TextView) getView().findViewById(R.id.tv06);
        gender = (TextView) getView().findViewById(R.id.tv07);
        status = (TextView) getView().findViewById(R.id.tv08);
        comments = (TextView)getView().findViewById(R.id.commentText);

        reference = FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String latestOciValue = String.format("%.2f",userProfile.ociValue);
                    String latestCalorieConsumed = String.format("%.2f", userProfile.calorieConsumed);
                    String ValueDiff = String.format("%.2f", Math.abs(Double.parseDouble(latestOciValue)-Double.parseDouble(latestCalorieConsumed)));
                    String latestWeight = String.format("%.2f", userProfile.weight);
                    String latestHeight = String.format("%.2f", userProfile.height);
                    String latestAge = String.valueOf(userProfile.age);
                    String latestGender = String.valueOf(userProfile.gender);
                    String latestStatus = String.valueOf(userProfile.status);
                    ociValue.setText(latestOciValue + " cal/day");
                    calorieConsumed.setText(latestCalorieConsumed + " cal");
                    valueDifference.setText(ValueDiff + " cal");
                    weight.setText(latestWeight + " kg");
                    height.setText(latestHeight + " cm");
                    age.setText(latestAge);
                    gender.setText(latestGender);
                    status.setText(latestStatus);

                    if(latestStatus.equals("Normal")){
                        status.setTextColor(Color.parseColor("#006400"));
                        comments.setText("Comments: A Normal status indicates that you are at a healthy weight for your height. " +
                                "By maintaining a healthy weight, you lower your risk of developing serious health problems.");
                    }
                    else if(latestStatus.equals("Overweight")){
                        status.setTextColor(Color.parseColor("#FF4500"));
                        comments.setText("Comments: An Overweight status indicates that you are slightly overweight. You may be advised to lose some weight for health reasons. " +
                                "You are recommended to talk to your doctor or a dietitian for advice.");
                    }
                    else if(latestStatus.equals("Underweight")){
                        status.setTextColor(Color.parseColor("#FF4500"));
                        comments.setText("Comments: An Underweight status indicates that you are underweight, so you may need to put on some weight. " +
                                "You are recommended to ask your doctor or a dietitian for advice.");
                    }
                    else if(latestStatus.equals("Obesity")){
                        status.setTextColor(Color.parseColor("#FF4500"));
                        comments.setText("Comments: An Underweight status indicates that you are heavily overweight. Your health may be at risk if you do not lose weight. " +
                                "You are recommended to talk to your doctor or a dietitian for advice.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred, Please try again", Toast.LENGTH_SHORT).show();
            }
        });



    }
}