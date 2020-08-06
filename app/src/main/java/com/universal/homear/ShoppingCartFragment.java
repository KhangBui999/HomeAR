package com.universal.homear;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartFragment extends Fragment {

    private String TAG = "com.universal.homear.ShoppingCartFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CartItemAdapter mAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        recyclerView = root.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        CartItemAdapter.RecyclerViewClickListener listener = (view, pos) -> {
        };
        mAdapter = new CartItemAdapter(new ArrayList<Furniture>(), listener);
        recyclerView.setAdapter(mAdapter);

        loadShoppingCart();

        return root;
    }

    private void loadShoppingCart() {
        DocumentReference docRef = db.collection("ShoppingCart").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> cartMap = document.getData();
                        ArrayList<String> furnitureList = (ArrayList<String>) cartMap.get("cartItems");
                        loadFurniture(furnitureList);
                    }
                }
            }
        });
    }

    private void loadFurniture(ArrayList<String> furnitureList) {
        List<Furniture> shoppingCart = new ArrayList<>();
        for(String id : furnitureList) {
            DocumentReference docRef = db.collection("Furniture").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Map<String, Object> furnitureMap = document.getData();
                            Long ucPrice = (Long) furnitureMap.get("price");
                            Long ucStock = (Long) furnitureMap.get("stock");

                            String id = document.getId();
                            String name = furnitureMap.get("name").toString();
                            int price = ucPrice.intValue();
                            int stock = ucStock.intValue();
                            String detail = furnitureMap.get("detail").toString();
                            String colour = furnitureMap.get("colour").toString();
                            String dimensions = furnitureMap.get("dimensions").toString();
                            String weight = furnitureMap.get("weight").toString();

                            shoppingCart.add(new Furniture(id, name, price, stock, detail, colour, dimensions, weight));
                        }
                        mAdapter.setShoppingCart(shoppingCart);
                    }
                }
            });
        }
    }

}