package com.universal.homear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.MyViewHolder> {

    private List<Furniture> mCatalogue;
    private RecyclerViewClickListener mListener;

    public FurnitureAdapter(List<Furniture> catalogue, RecyclerViewClickListener listener){
        mCatalogue = catalogue;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, price, stock;
        private ImageView image;
        private RecyclerViewClickListener mListener;


        public MyViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            name = v.findViewById(R.id.cart_name);
            price = v.findViewById(R.id.cart_price);
            stock = v.findViewById(R.id.cart_stock);
            image = v.findViewById(R.id.cart_image);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public FurnitureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.furniture_item_row, parent, false);
        return new MyViewHolder(v, mListener);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Furniture furniture = mCatalogue.get(position);
        //holder.stock.setText(furniture.getStock());
        holder.price.setText(Integer.toString(furniture.getPrice()));
        holder.name.setText(furniture.getName());
        loadImage(holder.itemView.getContext(), holder.image, furniture.getId());
    }

    @Override
    public int getItemCount() {
        return mCatalogue.size();
    }

    public void setCatalogueList(List<Furniture> list) {
        mCatalogue.clear();
        mCatalogue.addAll(list);
        notifyDataSetChanged();
    }

    private void loadImage(Context context, ImageView mImage, String id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(id + ".jpeg");
        Glide.with(context)
                .load(modelRef)
                .into(mImage);
    }

}
