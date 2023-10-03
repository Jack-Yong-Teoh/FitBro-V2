package com.example.fitbrov2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    foodAdapter foodAdapter;
    DatabaseReference reference;
    ArrayList<food> updateFoodArrayList = new ArrayList<>();


    public FragmentHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
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
        return inflater.inflate(R.layout.fragment_history, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        recyclerView = (RecyclerView) getView().findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList1((Map<String, foodAdapter>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred, Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        EditText searchBar = getView().findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        ImageButton btnRefresh = getView().findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.getText().clear();
                foodAdapter.setFilteredList(updateFoodArrayList);
            }
        });
    }

    private void filter(String s){
        ArrayList<food> filteredList = new ArrayList<>();
        for(food item: foodAdapter.foodArrayList){
            if(item.getFoodName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            }
            else if(item.getDate().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            //foodAdapter.foodArrayList.clear();
            foodAdapter.setFilteredList(filteredList);
            Toast.makeText(getContext(), "No records found!", Toast.LENGTH_SHORT).show();
        }
        else{
            foodAdapter.setFilteredList(filteredList);
        }
    }

    private void foodList1(Map<String,foodAdapter> food) {
        ArrayList<food> foodArrayList = new ArrayList<>();
        if(food != null){
            for (Map.Entry<String, foodAdapter>entry:food.entrySet()){
                Map list = (Map) entry.getValue();
                foodArrayList.add(new food(list.get("date").toString(), list.get("foodName").toString(), list.get("time").toString(), list.get("quantity").toString(), list.get("totalCalorie").toString()));
            }
        }
        else{
            Toast.makeText(getContext(), "No records found, please add in new data in calories record feature!", Toast.LENGTH_SHORT).show();
        }

        foodAdapter = new foodAdapter(foodArrayList);
        updateFoodArrayList = foodArrayList;
        recyclerView.setAdapter(foodAdapter);
    }
}