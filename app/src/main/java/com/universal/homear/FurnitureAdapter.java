package com.universal.homear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter class for the Search fragment RecyclerView
 */
public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.MyViewHolder> {

    private List<Furniture> mCatalogue;
    private RecyclerViewClickListener mListener;

    public FurnitureAdapter(List<Furniture> catalogue, RecyclerViewClickListener listener) {
        mCatalogue = catalogue;
        mListener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, price, stock;
        private ImageView image;
        private Button button;
        private RecyclerViewClickListener mListener;


        public MyViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            name = v.findViewById(R.id.cart_name);
            price = v.findViewById(R.id.cart_price);
            stock = v.findViewById(R.id.cart_stock);
            image = v.findViewById(R.id.cart_image);
            button = v.findViewById(R.id.btn_cart);
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
        if (furniture.getStock() > 10) {
            holder.stock.setText("In Stock");
        } else if (furniture.getStock() < 1) {
            holder.stock.setText("Out of Stock");
        } else {
            holder.stock.setText(Integer.toString(furniture.getStock()) + " stock left!");
        }
        loadImage(holder.itemView.getContext(), holder.image, furniture.getId());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(furniture.getId(), holder);
            }
        });

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

    /**
     * Allows a user to add furniture items to their shopping cart.
     * @param id
     * @param holder
     */
    private void addItemToCart(String id, MyViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final boolean[] change = {false};

        DocumentReference cartRef = db.collection("ShoppingCart").document(user.getUid());
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(cartRef);
                ArrayList<String> cartList = (ArrayList<String>) snapshot.get("cartItems");
                if (!cartList.contains(id)) {
                    cartList.add(id);
                    transaction.update(cartRef, "cartItems", cartList);
                    change[0] = true;
                }
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (change[0]) {
                    Toast.makeText(holder.itemView.getContext(), "Item Added to Cart!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Item Already in Cart!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(holder.itemView.getContext(), "Transaction Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
