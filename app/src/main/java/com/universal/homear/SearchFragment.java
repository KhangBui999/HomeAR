package com.universal.homear;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    private String TAG = "com.universal.homear.SearchFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FurnitureAdapter mAdapter;
    private ArrayList<String> furnitureId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = root.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FurnitureAdapter.RecyclerViewClickListener listener = new FurnitureAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(View view, int pos){
                launchProductView(pos);
            }
        };
        mAdapter = new FurnitureAdapter(new ArrayList<Furniture>(), listener);
        recyclerView.setAdapter(mAdapter);
        getCatalogue();
        return root;
    }

    private void getCatalogue(){
        List<Furniture> catalogue = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Furniture")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

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

                                furnitureId.add(id);
                                catalogue.add(new Furniture(id, name, price, stock, detail, colour, dimensions, weight));
                            }
                            mAdapter.setCatalogueList(catalogue);
                        }
                        else {
                            //Error handling
                        }
                    }
                });
    }

    public void launchProductView(int position) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", furnitureId.get(position));
        startActivity(intent);
    }



}