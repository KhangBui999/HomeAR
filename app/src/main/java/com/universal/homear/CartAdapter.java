package com.universal.homear;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Cart> mCart;
    private RecyclerViewClickListener mListener;

    public CartAdapter(List<Cart> carts, RecyclerViewClickListener listener){
        mCart = carts;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, id, price, stock;
        private RecyclerViewClickListener mListener;


        public MyViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            name = v.findViewById(R.id.cart_name);
            id = v.findViewById(R.id.cart_id);
            price = v.findViewById(R.id.cart_price);
            stock = v.findViewById(R.id.cart_stock);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new MyViewHolder(v, mListener);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cart cart = mCart.get(position);
        holder.id.setText(cart.getName());
        holder.stock.setText(cart.getName());
        holder.price.setText(cart.getName());
        holder.name.setText(cart.getName());
    }

    @Override
    public int getItemCount() {
        return mCart.size();
    }

    public void setCartList(List<Cart> list) {
        mCart.clear();
        mCart.addAll(list);
        notifyDataSetChanged();
    }



}
