package com.example.soundconnect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> songTitles, groupNames, filePaths;
    private ArrayList<Boolean> selectedView, songAdapterPlaying;
    private int playingPosition = -1, seconds = 0, minutes = 0;
    private Handler handler = new Handler();
    private Runnable timer;

    private boolean selectedToRemove = false;


    public AllSongsAdapter(Context context, ArrayList st, ArrayList groupNames, ArrayList filePaths) {
        this.context = context;
        this.songTitles = st;
        this.groupNames = groupNames;
        this.filePaths = filePaths;
        songAdapterPlaying = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            songAdapterPlaying.add(false);
        }
    }

    @NonNull
    @Override
    public AllSongsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_view, parent, false);

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        holder.songTitle.setText(String.valueOf(songTitles.get(position)));
        holder.groupName.setText(String.valueOf(groupNames.get(position)));
        holder.btnPause.setOnClickListener(view -> MediaPlayerSingleton.pauseAudio());

        RecyclerView rv = ((Activity) holder.itemView.getContext()).findViewById(R.id.rvSongsList);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                for (int i = 0; i <= getItemCount(); i++) {
                    MyViewHolder viewHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);

                    if (viewHolder != null && viewHolder.containerIsVisible()) {
                        Log.d("onBindViewHolder holder.getAdapterPosition()", holder.getAdapterPosition() + "  ");

                        if (holder.getAdapterPosition() != -1) {
                            if (i != playingPosition && songAdapterPlaying.get(i)) {
                                if (viewHolder.backslash.getVisibility() == View.VISIBLE) {
                                    viewHolder.totalTime.setVisibility(View.INVISIBLE);
                                    viewHolder.backslash.setVisibility(View.INVISIBLE);
                                    viewHolder.currentTime.setVisibility(View.INVISIBLE);
                                    viewHolder.btnPlay.setVisibility(View.VISIBLE);
                                    viewHolder.btnPause.setVisibility(View.INVISIBLE);
                                    viewHolder.btnRestart.setVisibility(View.INVISIBLE);
                                    notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                            }
                        }
                    }
                }
            }
        });

        holder.btnPlay.setOnClickListener(view -> {
            handler.removeCallbacks(timer);
            playAudio(filePaths.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            String[] totalTime = MediaPlayerSingleton.getTotalSongTime();
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
                    if (MediaPlayerSingleton.isInitialized() && MediaPlayerSingleton.isPlaying()) {
                        Log.d("currentTime", minutes + " : " + seconds);
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

            Log.d("getItemCount() holder.getAdapterPosition()", holder.getAdapterPosition() + "");
            for (int i = 0; i < getItemCount(); i++) {
                if (i != holder.getAdapterPosition()) {
                    RecyclerView recyclerView = ((Activity) holder.itemView.getContext()).findViewById(R.id.rvSongsList);
                    MyViewHolder iteratedViewHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    Log.d(i + " iteratedViewHolder", iteratedViewHolder + " " + i);

                    Log.d("trtes holder.getAdapterPosition() 1", "" + holder.getAdapterPosition());
                    Log.d("trtes playingPos 1", "" + playingPosition);
                    if (iteratedViewHolder!=null&&playingPosition!=holder.getAdapterPosition()) {
                        holder.currentTime.setText("00:00");
                        seconds = 0;
                        minutes = 0;
                        iteratedViewHolder.totalTime.setVisibility(View.INVISIBLE);
                        iteratedViewHolder.backslash.setVisibility(View.INVISIBLE);
                        iteratedViewHolder.currentTime.setVisibility(View.INVISIBLE);
                        iteratedViewHolder.btnPlay.setVisibility(View.VISIBLE);
                        iteratedViewHolder.btnPause.setVisibility(View.INVISIBLE);
                        iteratedViewHolder.btnRestart.setVisibility(View.INVISIBLE);
                        recyclerView.getAdapter().notifyItemChanged(i);
                        Log.d("songAdapterPlayingasd" + i,songAdapterPlaying.toString());
                        songAdapterPlaying.set(i,false);
                        Log.d("songAdapterPlayingasd" + i,songAdapterPlaying.toString());
                    }
                }
            }
            playingPosition = holder.getAdapterPosition();
            songAdapterPlaying.set(playingPosition,true);
            handler.postDelayed(timer, 1000);
        });

        holder.btnPause.setOnClickListener(view -> {
            MediaPlayerSingleton.pauseAudio();
            holder.btnPlay.setVisibility(View.VISIBLE);
            holder.btnPause.setVisibility(View.INVISIBLE);
            handler.removeCallbacks(timer);
        });

        holder.btnRestart.setOnClickListener(view -> {
            handler.removeCallbacks(timer);
            MediaPlayerSingleton.release();
            MediaPlayerSingleton.setMediaPlayer(null);
            playAudio(filePaths.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            handler.postDelayed(timer, 1000);
            holder.btnPlay.setVisibility(View.INVISIBLE);
            holder.btnPause.setVisibility(View.VISIBLE);
            holder.currentTime.setText("00:00");

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

                Log.d("selectedView",selectedView.toString());
        });

        int defaultColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.recyclerViewElementNormal);
        holder.itemView.setBackgroundColor(defaultColor);

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

        public boolean containerIsVisible() {
            Rect scrollBounds = new Rect();
            itemView.getHitRect(scrollBounds);
            return itemView.getLocalVisibleRect(scrollBounds);
        }

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
        Log.d("playAudio mediaPlayer", MediaPlayerSingleton.getMediaPlayer() + "");
        if (MediaPlayerSingleton.getMediaPlayer() != null && adapterPosition != playingPosition) {
            MediaPlayerSingleton.getMediaPlayer().release();
            MediaPlayerSingleton.setMediaPlayer(null);
            playAudio(filePath, adapterPosition);
        } else if (MediaPlayerSingleton.getMediaPlayer() == null) {
            MediaPlayerSingleton.setDataSource(filePath);
            MediaPlayerSingleton.start();
        } else {
            MediaPlayerSingleton.start();
        }
    }

    public boolean activateRemove() {
        RecyclerView recyclerView = ((Activity) context).findViewById(R.id.rvSongsList);
        selectedToRemove = !selectedToRemove;
        Log.d("selectedToRemove", String.valueOf(selectedToRemove));
        Log.d("selectedView", String.valueOf(selectedView));
        if (selectedView != null) {
            for (int i = 0; i < selectedView.size(); i++) {
                MyViewHolder viewHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                selectedView.set(i, false);
                if (viewHolder != null) {
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.recyclerViewElementNormal));
                }
            }
        } else {
            selectedToRemove = false;
        }
        return selectedToRemove;
    }

    public ArrayList<Integer> getSelectedSongs() {
        ArrayList songNums = new ArrayList<Integer>();
        for (int i = 0; i < selectedView.size(); i++) {
            if (selectedView.get(i)) {
                songNums.add(i);
                Log.d("songNums", songNums.toString());
            }
        }
        return songNums;
    }
}