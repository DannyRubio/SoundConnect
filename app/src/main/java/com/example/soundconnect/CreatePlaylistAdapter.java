package com.example.soundconnect;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CreatePlaylistAdapter extends RecyclerView.Adapter<CreatePlaylistAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> songTitles, groupNames, filePaths;
    private ArrayList<Boolean> selectedView;
    private int playingPosition = -1, seconds = 0, minutes = 0;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable timer;

    private boolean selectedToRemove = false;

    public CreatePlaylistAdapter(Context context, ArrayList songTitles, ArrayList groupNames, ArrayList filePaths) {
        this.context = context;
        this.songTitles = songTitles;
        this.groupNames = groupNames;
        this.filePaths = filePaths;
    }


    @NonNull
    @Override
    public CreatePlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        holder.songTitle.setText(String.valueOf(songTitles.get(position)));
        holder.groupName.setText(String.valueOf(groupNames.get(position)));
        holder.btnPause.setOnClickListener(view -> pauseAudio());

        holder.btnPlay.setOnClickListener(view -> {
            handler.removeCallbacks(timer);

            playAudio(filePaths.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            String[] totalTime = getTotalSongTime();
            holder.totalTime.setText(totalTime[0] + ":" + totalTime[1]);
            holder.totalTime.setVisibility(View.VISIBLE);
            holder.currentTime.setVisibility(View.VISIBLE);
            holder.backslash.setVisibility(View.VISIBLE);
            holder.btnRestart.setVisibility(View.VISIBLE);
            holder.btnPlay.setVisibility(View.INVISIBLE);
            holder.btnPause.setVisibility(View.VISIBLE);

            timer = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        seconds++;
                        if (seconds == 60) {
                            minutes++;
                            seconds = 0;
                        }

                        String sec = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);
                        String min = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);

                        holder.currentTime.setText(min + ":" + sec);
                        handler.postDelayed(timer, 1000);
                    }
                }
            };

            playingPosition = holder.getAdapterPosition();
            for (int i = 0; i < getItemCount(); i++) {
                if (i != holder.getAdapterPosition()) {
                    RecyclerView recyclerView = ((Activity) holder.itemView.getContext()).findViewById(R.id.rvSongsList);
                    MyViewHolder iteratedViewHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    if (iteratedViewHolder != null) {
                        if (iteratedViewHolder.backslash.getVisibility() == View.VISIBLE) {
                            Log.d("value of", "i");
                            holder.currentTime.setText("00:00");
                            seconds = 0;
                            minutes = 0;
                            iteratedViewHolder.totalTime.setVisibility(View.INVISIBLE);
                            iteratedViewHolder.backslash.setVisibility(View.INVISIBLE);
                            iteratedViewHolder.currentTime.setVisibility(View.INVISIBLE);
                            iteratedViewHolder.btnPlay.setVisibility(View.VISIBLE);
                            iteratedViewHolder.btnPause.setVisibility(View.INVISIBLE);
                            iteratedViewHolder.btnRestart.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            handler.postDelayed(timer, 1000);
            notifyDataSetChanged();
        });

        holder.btnPause.setOnClickListener(view -> {
            pauseAudio();
            holder.btnPlay.setVisibility(View.VISIBLE);
            holder.btnPause.setVisibility(View.INVISIBLE);
            notifyDataSetChanged();
            handler.removeCallbacks(timer);
        });

        holder.btnRestart.setOnClickListener(view -> {
            handler.removeCallbacks(timer);
            mediaPlayer.release();
            mediaPlayer = null;
            playAudio(filePaths.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            handler.postDelayed(timer, 1000);
            holder.btnPlay.setVisibility(View.INVISIBLE);
            holder.btnPause.setVisibility(View.VISIBLE);
            holder.currentTime.setText("00:00");
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(view -> {
            int color = R.color.analogous_blue;
            if (selectedToRemove) {
                color = R.color.error_red;
            }
            selectedView.set(holder.getAdapterPosition(), !selectedView.get(holder.getAdapterPosition()));

            if (selectedView.get(holder.getAdapterPosition())) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), color));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal));
            }
            notifyDataSetChanged();
        });
        int defaultColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal);
        holder.itemView.setBackgroundColor(defaultColor);

        // Update the view based on selectedView list
        int colorSelected = R.color.analogous_blue;
        if (selectedToRemove) {
            colorSelected = R.color.error_red;
        }

        if (selectedView == null) {
            this.selectedView = new ArrayList<>(Collections.nCopies(songTitles.size(), false));
        }

        if (selectedView.get(position) && selectedView.size() != 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), colorSelected));
        }


    }

    @Override
    public int getItemCount() {
        return songTitles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, groupName, totalTime, currentTime, backslash;
        Button btnPlay, btnPause, btnRestart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.valSongTitle);
            groupName = itemView.findViewById(R.id.valGroupName);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnPause = itemView.findViewById(R.id.btnPause);
            btnRestart = itemView.findViewById(R.id.btnRestart);
            totalTime = itemView.findViewById(R.id.totalTime);
            currentTime = itemView.findViewById(R.id.currentTime);
            backslash = itemView.findViewById(R.id.backslash);
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.recyclerViewElementNormal));
        }
    }

    private void playAudio(String filePath, int adapterPosition) {
        Log.d("adapterPosition",adapterPosition+"");
        if (mediaPlayer != null && adapterPosition != playingPosition) {
            mediaPlayer.release();
            mediaPlayer = null;
            playAudio(filePath, adapterPosition);
        } else if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                seconds = 0;
                minutes = 0;
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.start();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private String[] getTotalSongTime() {
        String mins = String.valueOf((mediaPlayer.getDuration() / 1000) / 60);
        String secs = String.valueOf((mediaPlayer.getDuration() / 1000) % 60);
        if (Integer.parseInt(secs) < 10) {
            secs = "0" + secs;
        }
        String[] result = {mins, secs};
        return result;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDetachedFromRecyclerView(recyclerView);
        if (playingPosition != -1) {
            notifyItemChanged(playingPosition);
            notifyDataSetChanged();
        }
    }
}

