package com.example.mediaplayer;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private Context mContext;
    static ArrayList<MusicFiles> mFiles;

    MusicAdapter(Context mContext,ArrayList<MusicFiles> mFiles)
    {
        this.mFiles=mFiles;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view) ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder holder, int position) {
    holder.file_name.setText(mFiles.get(position).getArtist()+" - "+mFiles.get(position).getTitle());
    byte[] image=getAlbumArt(mFiles.get(position).getPath());
    if(image!=null)
    {
        Glide.with(mContext).asBitmap()
                .load(image)
                .into(holder.album_art);
    }
    else{

        Glide.with(mContext)
            .load(R.drawable.sponge)
            .into(holder.album_art) ;
    }
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,PlayerActivity.class);
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        }
    });
    holder.menuMore.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu=new PopupMenu(mContext,v);
            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener((item -> {
                switch (item.getItemId())
                {
                    case R.id.delete:
                        Toast.makeText(mContext, "Delete clicked", Toast.LENGTH_SHORT).show();
                        deleteFile(position, v);
                        break;
                    case R.id.informations:
                        Intent intent = new Intent(mContext, SongDetails.class);
                        intent.putExtra("position",position);
                        mContext.startActivity(intent);

                        Toast.makeText(mContext, "Informations clicked", Toast.LENGTH_SHORT).show();

                        break;
                }
                return true;

            }));

        }
    });
    }


    public boolean deleteFile(int position, View v) {
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));

        File file=new File(mFiles.get(position).getPath());
        Log.e("cv",file.toString()+contentUri);
        boolean deleted= file.delete();
        Log.e("cvv", String.valueOf(deleted));
        if(file.exists()) {
            if (deleted) {
                mFiles.remove(position);
                mContext.getContentResolver().delete(contentUri, null, null);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mFiles.size());
                Snackbar.make(v, "File deleted : "+ mFiles.get(position).getTitle(), Snackbar.LENGTH_LONG).show();

            } else {
                Snackbar.make(v, "File can't be deleted : "+ mFiles.get(position).getTitle(), Snackbar.LENGTH_LONG).show();

            }
        }
        return deleted;
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        ImageView album_art,menuMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name= itemView.findViewById(R.id.music_file_name);
            album_art=itemView.findViewById(R.id.music_img);
            menuMore=itemView.findViewById(R.id.menuMore);

        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    void updateList(ArrayList<MusicFiles> musicFilesArrayList)
    {
        mFiles=new ArrayList<>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();

    }
}
