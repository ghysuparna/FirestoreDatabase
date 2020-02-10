package com.example.firestoredatabase;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Adapter> {

    private Context context;
    private ArrayList<Uri> listImage;
//    private Integer [] images = {R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone


    public ImageAdapter(Context context, ArrayList<Uri> listImage) {
        this.context = context;
        this.listImage = listImage;
    }

    @NonNull
    @Override
    public Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_images, parent, false);
        return new ImageAdapter.Adapter(view);    }

    @Override
    public void onBindViewHolder(@NonNull Adapter holder, int position) {
        holder.tutorialImage.setImageURI(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    public class Adapter extends RecyclerView.ViewHolder {
        ImageView tutorialImage;
        public Adapter(@NonNull View itemView) {
            super(itemView);
            tutorialImage=itemView.findViewById(R.id.item_image);
        }
    }
}
