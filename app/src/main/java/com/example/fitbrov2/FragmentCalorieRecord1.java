package com.example.fitbrov2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitbrov2.ml.MobilenetV110224Quant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalorieRecord1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalorieRecord1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imgView;
    private Button predict, add, reset;
    private ImageButton select,camera;
    private Bitmap img;
    private EditText et,et2,et3;
    private Double totalCalorieValue, currentCalorieValue;
    private int foodQuantity;
    private String currentFoodName;
    private Bitmap currentImage;
    private String foodUID;
    private float previousCalorieConsumed, latestCalorieConsumed;
    private String totalCalorieString, quantityString;

    public FragmentCalorieRecord1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalorieRecord1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalorieRecord1 newInstance(String param1, String param2) {
        FragmentCalorieRecord1 fragment = new FragmentCalorieRecord1();
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
        return inflater.inflate(R.layout.fragment_calorie_record1, container, false);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgView = (ImageView) getView().findViewById(R.id.imageView);
        select = (ImageButton) getView().findViewById(R.id.button);
        camera = (ImageButton)getView().findViewById(R.id.button4);
        predict = (Button) getView().findViewById(R.id.button2);
        et = (EditText)getView().findViewById(R.id.editText1);
        et2 = (EditText)getView().findViewById(R.id.editText2);
        et3 = (EditText)getView().findViewById(R.id.editText3);
        add = (Button)getView().findViewById(R.id.button3);
        reset = (Button) getView().findViewById(R.id.ResetButton);

        imgView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_food2)); //add a placeholder to the imageView
        img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_food2); // add a placeholder bitmap image for the model to prevent error

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,101);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_food2)); //add a placeholder to the imageView
                img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_food2);
                et.getText().clear();
                et2.getText().clear();
                et3.getText().clear();
            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img = Bitmap.createScaledBitmap(img,224,224,true); //set a fixed size for image (e.g 224)

                try {
                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                    TensorImage tensorImage = new TensorImage(DataType.UINT8);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();

                    // Logic to find out the largest possibility from the dataset
                    float largest = 0;
                    float calorie = 0;
                    int num = 0;
                    String foodName;

                    for(int i = 0; i < outputFeature0.getFloatArray().length-1; i++){ //find the largest possibility in the dataset
                        if(outputFeature0.getFloatArray()[i] > largest){
                            largest = outputFeature0.getFloatArray()[i];
                            num = i;
                        }
                    }

                    switch(num+1){
                        case (926):
                            foodName = "Guacamole";
                            calorie = 45.00F;
                            break;
                        case (928):
                            foodName = "Hot Pot";
                            calorie = 1530.00F;
                            break;
                        case (930):
                            foodName = "Ice cream";
                            calorie = 207.00F;
                            break;
                        case (932):
                            foodName = "French Loaf";
                            calorie = 190.00F;
                            break;
                        case (933):
                            foodName = "Bagel";
                            calorie = 250.00F;
                            break;
                        case (934):
                            foodName = "Pretzel";
                            calorie = 380.00F;
                            break;
                        case (935):
                            foodName = "Cheeseburger";
                            calorie = 303.00F;
                            break;
                        case (936):
                            foodName = "Hot Dog";
                            calorie = 290.00F;
                            break;
                        case (937):
                            foodName = "Mashed Potato";
                            calorie = 88.00F;
                            break;
                        case (939):
                            foodName = "Broccoli";
                            calorie = 31.00F;
                            break;
                        case (940):
                            foodName = "Cauliflower";
                            calorie = 25.00F;
                            break;
                        case (945):
                            foodName = "Cucumber";
                            calorie = 30.00F;
                            break;
                        case (949):
                            foodName = "Mushroom";
                            calorie = 22.00F;
                            break;
                        case (950):
                            foodName = "Green Apple";
                            calorie = 95.00F;
                        case (951):
                            foodName = "Strawberry";
                            calorie = 4.00F;
                            break;
                        case (952):
                            foodName = "Orange";
                            calorie = 47.00F;
                            break;
                        case (953):
                            foodName = "Lemon";
                            calorie = 29.00F;
                            break;
                        case (955):
                            foodName = "Pineapple";
                            calorie = 50.00F;
                            break;
                        case (956):
                            foodName = "Banana";
                            calorie = 89.00F;
                            break;
                        case (959):
                            foodName = "Apple";
                            calorie = 64.00F;
                            break;
                        case (961):
                            foodName = "Carbonara";
                            calorie = 191.00F;
                            break;
                        case (965):
                            foodName = "Pizza";
                            calorie = 240.00F;
                            break;
                        case (967):
                            foodName = "Burrito";
                            calorie = 206.00F;
                            break;
                        case (989):
                            foodName = "Corn";
                            calorie = 90.00F;
                            break;
                        default:
                            foodName = "Undefined";
                            calorie = 0.00F;
                    } //foodAdapter categorisation

                    et.setText(String.valueOf(foodName));
                    et2.setText(String.format("%.2f", calorie));

                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });



        // update to the firebase for the foodAdapter
        DatabaseReference reference = FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("UserInfo");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFoodName =  String.valueOf(et.getText());
                currentCalorieValue = Double.parseDouble(String.valueOf(et2.getText()));

                if(!isEmpty(et) && !isEmpty(et2) && !isEmpty(et3)){

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            if(userProfile != null){
                                String userOciValue = String.format("%.2f",userProfile.ociValue);
                                String userCalorieConsumed = String.format("%.2f", userProfile.calorieConsumed);
                                previousCalorieConsumed = Float.parseFloat(userCalorieConsumed);
                                latestCalorieConsumed = Float.parseFloat(String.valueOf(previousCalorieConsumed + totalCalorieValue));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

                    String value = et3.getText().toString();
                    foodQuantity = Integer.parseInt(value);
                    totalCalorieValue = currentCalorieValue * foodQuantity;
                    totalCalorieString = String.format("%.2f", totalCalorieValue);
                    quantityString = String.valueOf(foodQuantity);


                    //Alert Box with message
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to add this to the history? (WARNING: You cannot modify it after adding it)");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //press YES to add the data to the database
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String date = sdf.format(System.currentTimeMillis());
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                            String time = df.format(Calendar.getInstance().getTime());

                            foodUID = UUID.randomUUID().toString();
                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo").child(foodUID).child("foodName").setValue(currentFoodName);

                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo").child(foodUID).child("totalCalorie").setValue(totalCalorieString);

                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo").child(foodUID).child("quantity").setValue(quantityString);

                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo").child(foodUID).child("date").setValue(date);

                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodInfo").child(foodUID).child("time").setValue(time);

                            FirebaseDatabase.getInstance("https://fitbro-4fe4a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfo")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("calorieConsumed").setValue(latestCalorieConsumed);

                            Toast.makeText(getContext(), "Food added successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(getContext(), "Please do not leave anything blank", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            if(requestCode == 100){
                imgView.setImageURI(data.getData());

                Uri uri = data.getData();
                try{
                    img = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == 101){
                img = (Bitmap)data.getExtras().get("data");
                imgView.setImageBitmap(img);
            }
        }
        else {
            imgView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_food2)); //add a placeholder to the imageView
            img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_food2); // add a placeholder bitmap image for the model to prevent error
        }
    }
}
