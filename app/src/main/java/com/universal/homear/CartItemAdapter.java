package com.universal.homear;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private List<CartItem> mShoppingCart;
    private RecyclerViewClickListener mListener;

    public CartItemAdapter(List<CartItem> shoppingCart, RecyclerViewClickListener listener) {
        mShoppingCart = shoppingCart;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName, mQuantity, mRemove, mEdit, mPrice;
        private ImageView mImage;
        private RecyclerViewClickListener mListener;

        public MyViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            mName = v.findViewById(R.id.cart_name);
            mImage = v.findViewById(R.id.cart_image);
            mQuantity = v.findViewById(R.id.tv_quantity);
            mRemove = v.findViewById(R.id.tv_remove);
            mEdit = v.findViewById(R.id.tv_edit);
            mPrice = v.findViewById(R.id.tv_price);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);
        return new MyViewHolder(v, mListener);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartItem cartItem = mShoppingCart.get(position);
        holder.mName.setText(cartItem.getFurniture().getName());
        holder.mQuantity.setText("Quantity: "+cartItem.getQuantity());
        holder.mPrice.setText("$999");
    }

    @Override
    public int getItemCount() {
        return mShoppingCart.size();
    }

    public void setShoppingCart(List<CartItem> list) {
        mShoppingCart.clear();
        mShoppingCart.addAll(list);
        notifyDataSetChanged();
    }
}
