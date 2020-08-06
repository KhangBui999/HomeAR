package com.universal.homear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * An adapter class for the shopping cart RecyclerView.
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private List<Furniture> mShoppingCart;
    private RecyclerViewClickListener mListener;

    public CartItemAdapter(List<Furniture> shoppingCart, RecyclerViewClickListener listener) {
        mShoppingCart = shoppingCart;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName, mQuantity, mPrice, mRemove;
        private ImageView mImage;
        private RecyclerViewClickListener mListener;

        public MyViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            mName = v.findViewById(R.id.cart_name);
            mImage = v.findViewById(R.id.cart_image);
            mQuantity = v.findViewById(R.id.tv_quantity);
            mPrice = v.findViewById(R.id.tv_price);
            mRemove = v.findViewById(R.id.tv_remove);
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
        Furniture cartItem = mShoppingCart.get(position);
        loadImage(holder.itemView.getContext(), holder.mImage, cartItem.getId());
        holder.mName.setText(cartItem.getName());
        holder.mQuantity.setText("Quantity: 1");
        holder.mPrice.setText(Integer.toString(cartItem.getPrice()));
    }

    /**
     * Uses the Glide library to retrieve images from the Firebase server and render it in the app
     * @param context
     * @param mImage
     * @param id
     */
    private void loadImage(Context context, ImageView mImage, String id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(id + ".jpeg");
        Glide.with(context)
                .load(modelRef)
                .into(mImage);
    }

    @Override
    public int getItemCount() {
        return mShoppingCart.size();
    }

    /**
     * Used for real-time updates of the shopping cart
     * @param list
     */
    public void setShoppingCart(List<Furniture> list) {
        mShoppingCart.clear();
        mShoppingCart.addAll(list);
        notifyDataSetChanged();
    }
}
