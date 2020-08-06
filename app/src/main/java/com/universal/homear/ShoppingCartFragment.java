package com.universal.homear;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCartFragment extends Fragment {

    private String TAG = "com.universal.homear.ShoppingCartFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CartItemAdapter mAdapter;
    private TextView mTotal, mClear;
    private Button mOrder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private int total = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        recyclerView = root.findViewById(R.id.cart_recycler);
        mTotal = root.findViewById(R.id.tv_total);
        mClear = root.findViewById(R.id.tv_clearAll);
        mOrder = root.findViewById(R.id.btn_checkout);
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutDialog dialog = new CheckoutDialog();
                dialog.show(getParentFragmentManager(), "com.universal.homear.ShoppingCartFragment");
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        CartItemAdapter.RecyclerViewClickListener listener = (view, pos) -> {
        };
        mAdapter = new CartItemAdapter(new ArrayList<Furniture>(), listener);
        recyclerView.setAdapter(mAdapter);

        loadShoppingCart();
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBasket();
            }
        });

        return root;
    }

    private void clearBasket() {
        DocumentReference cartRef = db.collection("ShoppingCart").document(user.getUid());
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(cartRef, "cartItems", new ArrayList<>());
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
                mAdapter.setShoppingCart(new ArrayList<>());
                mTotal.setText("No Items in Shopping Cart.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Cart Clear Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadShoppingCart() {
        DocumentReference docRef = db.collection("ShoppingCart").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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

        if(furnitureList.size() == 0) {
            mTotal.setText("No Items in Shopping Cart.");
        }

        for (String id : furnitureList) {
            DocumentReference docRef = db.collection("Furniture").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
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
                            total += price;
                        }
                        mAdapter.setShoppingCart(shoppingCart);
                        mTotal.setText("Total Cost: AU$" + Integer.toString(total));
                    }
                }
            });
        }
    }

}