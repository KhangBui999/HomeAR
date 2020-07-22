package com.universal.homear;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CartAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = root.findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        CartAdapter.RecyclerViewClickListener listener = new CartAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(View view, int pos){
                launchProductView(pos);
            }
        };


        mAdapter = new CartAdapter(new ArrayList<Cart>(), listener);
        recyclerView.setAdapter(mAdapter);

        return root;
    }


    public void launchProductView(int position) {
        Intent intent = new Intent(getActivity(), ProductDetail.class);
        intent.putExtra("PRODUCT_ID", position);
        startActivity(intent);
    }



}