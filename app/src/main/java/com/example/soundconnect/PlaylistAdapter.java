package com.example.soundconnect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> playlistNames;
    private ArrayList<Boolean> selectedView;
    private int modifyPosition=-1;

    private boolean selectedToRemove = false;

    private RecyclerView recyclerView;

    public PlaylistAdapter(Context context, ArrayList playlistNames,RecyclerView recyclerView) {
        this.context = context;
        this.playlistNames = playlistNames;
        this.recyclerView=recyclerView;
    }


    @NonNull
    @Override
    public PlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.playlist_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.playlistName.setText(String.valueOf(playlistNames.get(position)));

        int defaultColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal);
        holder.itemView.setBackgroundColor(defaultColor);
        holder.btnPlay.setOnClickListener(view -> {
        });

        holder.itemView.setOnClickListener(view -> {
            int color = R.color.analogous_blue;
            if(selectedToRemove) {
                color = R.color.error_red;
                modifyPosition=-1;
                selectedView.set(holder.getAdapterPosition(), !selectedView.get(holder.getAdapterPosition()));
                if (selectedView.get(holder.getAdapterPosition())) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), color));
                } else {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal));
                }
            }else{
                if(modifyPosition==holder.getAdapterPosition()){
                    Log.d("itemView","else if");
                    modifyPosition=-1;
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal));
                }else {
                    if(modifyPosition!=-1) {
                        recyclerView.findViewHolderForAdapterPosition(modifyPosition).itemView.setBackgroundColor(ContextCompat.getColor(recyclerView.findViewHolderForAdapterPosition(modifyPosition).itemView.getContext(), R.color.recyclerViewElementNormal));
                        Log.d("itemView", "else else");
                        holder.itemView.setBackgroundColor(holder.itemView.getContext().getColor(color));
                        modifyPosition = holder.getAdapterPosition();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistNames.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;
        Button btnPlay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.lblPlaylistName);
            btnPlay = itemView.findViewById(R.id.btnPlayPlaylist);
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.recyclerViewElementNormal));
        }
    }
}

