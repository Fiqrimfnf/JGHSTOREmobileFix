package com.fiqri.jghstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fiqri.jghstore.R;
import com.fiqri.jghstore.adapters.MyCartAdapter;
import com.fiqri.jghstore.models.MyCartModel;
import com.fiqri.jghstore.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    int overAllTotalAmount;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button buyNow;
    Button addAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        toolbar=findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get data from my cart adapter
        overAllAmount=findViewById(R.id.textView3);
        recyclerView=findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList=new ArrayList<>();
        cartAdapter=new MyCartAdapter(this,cartModelList);
        recyclerView.setAdapter(cartAdapter);
        buyNow=findViewById(R.id.buy_now);
        addAddress=findViewById(R.id.add_address_btn);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                        String documentId = doc.getId();
                        myCartModel.setDocumentId(documentId);
                        cartModelList.add(myCartModel);
                        cartAdapter.notifyDataSetChanged();
                    }
                    calculateTotalAmount(cartModelList);

                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), SuccessActivity.class);
                intent.putExtra("itemList",(Serializable) cartModelList);
                startActivity(intent);
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,AddressActivity.class));
            }
        });

    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {
        int totalAmount = 0;
        for (MyCartModel myCartModel: cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }

        overAllAmount.setText("Total Amount: "+totalAmount);
    }

}