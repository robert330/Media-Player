package com.example.mediaplayer;
import com.example.mediaplayer.id3v1tag.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.mediaplayer.AlbumDetailsAdapter.albumFiles;
import static com.example.mediaplayer.MusicAdapter.mFiles;

public class SongDetails extends AppCompatActivity {
    TextView song_name, artist_name, album_name, year, size, genre, comment;
    int position =-1;
    static ArrayList<MusicFiles> listSongs= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
        initView();
        try {
            getIntentMethod();
        } catch ( IOException  e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        song_name = findViewById(R.id.song_name_details);
        artist_name = findViewById(R.id.artist_info_details);
        album_name = findViewById(R.id.album_info_details);
        year = findViewById(R.id.year_info_details);
        size = findViewById(R.id.size_info_details);
        genre = findViewById(R.id.genre_info_details);
        comment = findViewById(R.id.comment_info_details);

    }
    @SuppressLint("SetTextI18n")
    private void getIntentMethod() throws  IOException {
        position=getIntent().getIntExtra("position",-1);
        String pathToMp3File;
        listSongs=albumFiles;
        if(listSongs==null)
        {listSongs=mFiles;

                pathToMp3File=listSongs.get(position).getPath();


            Log.e("tag",pathToMp3File);



                try {


                    //ID3 id3 = new ID3();
                    //ID3v1.setMusicInfoV1(pathToMp3File,id3);
                    //id3.setArtist("Warriyo");
                    //id3.setTitle("Mortals");

                    artist_name.setText(ID3v1.getMusicInfoV1(pathToMp3File).getArtist());
                    song_name.setText(ID3v1.getMusicInfoV1(pathToMp3File).getTitle());
                    album_name.setText(ID3v1.getMusicInfoV1(pathToMp3File).getAlbum());
                    year.setText(ID3v1.getMusicInfoV1(pathToMp3File).getYear());
                    comment.setText(ID3v1.getMusicInfoV1(pathToMp3File).getComment());
                    DecimalFormat format = new DecimalFormat("0.00");
                    size.setText(format.format((float) ID3v1.getMusicInfoV1(pathToMp3File).getLengthOfFile()/1048576) + " MB");
                    genre.setText(ID3v1.getMusicInfoV1(pathToMp3File).getGenre() + " (" + ID3v1.getMusicInfoV1(pathToMp3File).getGenreDescription() + ")");
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }



        }











    }
}