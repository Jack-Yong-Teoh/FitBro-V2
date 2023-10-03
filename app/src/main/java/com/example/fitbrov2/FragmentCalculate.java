package com.example.fitbrov2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalculate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalculate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText age, height, weight, result;
    private RadioGroup radioGroup1;
    private RadioButton btnFemale, btnMale;
    private Button btnCalculate, btnUpdate;
    public Double calculatedBMR, BMR, weightUpdate, heightUpdate, statusValue;
    public String gender, status;
    public int ageUpdate;

    public FragmentCalculate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalculate newInstance(String param1, String param2) {
        FragmentCalculate fragment = new FragmentCalculate();
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
        return inflater.inflate(R.layout.fragment_calculate, container, false);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        age = (EditText) getView().findViewById(R.id.editText4);
        height = (EditText) getView().findViewById(R.id.editText5);
        weight = (EditText) getView().findViewById(R.id.editText6);
        result = (EditText) getView().findViewById(R.id.editText7);
        radioGroup1 = (RadioGroup) getView().findViewById(R.id.radioGroup1);
        btnFemale = (RadioButton) getView().findViewById(R.id.radioOne);
        btnMale = (RadioButton) getView().findViewById(R.id.radioTwo);
        btnCalculate = (Button) getView().findViewById(R.id.btnCalculate);
        btnUpdate = (Button) getView().findViewById(R.id.btnUpdate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(age) && !isEmpty(height) && !isEmpty(weight) && radioGroup1.getCheckedRadioButtonId()!= -1){

                    Double kg = Double.parseDouble(String.valueOf(weight.getText()));
                    Double m = ((Double.parseDouble(String.valueOf(height.getText()))) / 100);

                    statusValue = kg / Math.pow(m,2);
                    if(statusValue < 18.5){
                        status = "Underweight";
                    }
                    else if(statusValue < 25){
                        status = "Normal";
                    }
                    else if(statusValue < 30){
                        status = "Overweight";
                    }
                    else{
                        status = "Obesity";
                    }

                    if(btnMale.isChecked()){
                        gender = "Male";
                        try{
                            calculatedBMR = 88.362 + (13.332 * Float.valueOf(weight.getText().toString())) +
                                    (4.799 * Float.valueOf(height.getText().toString())) - (5.677 * Float.valueOf(age.getText().toString()));

                            result.setText(String.format("%.2f", calculatedBMR));
                        }
                        catch(Exception e){
                            Toast.makeText(getContext(), "Please insert the correct value", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(btnFemale.isChecked()){
                        gender = "Female";
                        try{
                            calculatedBMR = 447.593 + (9.247 * Float.valueOf(weight.getText().toString())) +
                                    (3.098 * Float.valueOf(height.getText().toString())) - (4.330 * Float.valueOf(age.getText().toString()));

                            result.setText(String.format("%.2f", calculatedBMR));
                        }
                        catch(Exception e){
                            Toast.makeText(getContext(), "Please insert the correct value", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(), "Please do not leave the information blank", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(result)){
                    try{
                        ageUpdate = Integer.parseInt(String.valueOf(age.getText()));
                        weightUpdate = Double.parseDouble(String.valueOf(weight.getText()));
                        heightUpdate = Double.parseDouble(String.valueOf(height.getText()));
                        BMR = calculatedBMR;
                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ociValue").setValue(BMR);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("age").setValue(ageUpdate);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("weight").setValue(weightUpdate);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("height").setValue(heightUpdate);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender").setValue(gender);

                        FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(status);
                        Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e){
                        Toast.makeText(getContext(), "Error occurred, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Please do not leave the result blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
