package com.universal.homear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ShoppingCartFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CartAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        recyclerView = root.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        CartAdapter.RecyclerViewClickListener listener = new CartAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(View view, int pos){

            }
        };


        mAdapter = new CartAdapter(new ArrayList<Cart>(), listener);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

}