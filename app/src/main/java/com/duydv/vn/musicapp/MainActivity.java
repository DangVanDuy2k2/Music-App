package com.duydv.vn.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView txtNameSong,txtTimeStart,txtTimeEnd;
    private SeekBar skTime;
    private ImageView btnBack,btnPlay,btnNext,imgCD;
    private ArrayList<Song> listSong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();

        addSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_cd);

        createMediaPlay();

        addEvents();
    }

    private void addControls() {
        txtNameSong = this.<TextView>findViewById(R.id.txtNameSong);
        txtTimeStart = this.<TextView>findViewById(R.id.txtTimeStart);
        txtTimeEnd = this.<TextView>findViewById(R.id.txtTimeEnd);
        skTime = this.<SeekBar>findViewById(R.id.skTime);
        btnBack = this.<ImageView>findViewById(R.id.btnBack);
        btnPlay = this.<ImageView>findViewById(R.id.btnPlay);
        btnNext = this.<ImageView>findViewById(R.id.btnNext);
        imgCD = this.<ImageView>findViewById(R.id.imgCD);
    }

    private void addSong() {
        listSong = new ArrayList<>();
        listSong.add(new Song("Nơi Này Có Anh",R.raw.noi_nay_co_anh));
        listSong.add(new Song("Đúng Người Đúng Thời Điểm",R.raw.dung_nguoi_dung_thoi_diem));
        listSong.add(new Song("Sao Mình Chưa Nắm Tay",R.raw.sao_minh_chua_nam_tay));
        listSong.add(new Song("Lời Anh Chưa Thể Nói",R.raw.loi_anh_chua_the_noi));
        listSong.add(new Song("Bật Tình Yêu Lên",R.raw.bat_tinh_yeu_len));
    }

    private void createMediaPlay(){
        mediaPlayer = MediaPlayer.create(MainActivity.this,listSong.get(position).getFile());
        txtNameSong.setText(listSong.get(position).getName());
    }

    private void addEvents() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    //nếu đang phát nhạc -> pause và đổi hình
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }else{
                    //đang ngừng -> phát và đổi hình
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeSeekBar();
                updateTime();
                imgCD.startAnimation(animation);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if(position > listSong.size() -1){
                    position = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                createMediaPlay();
                mediaPlayer.start();
                setTimeSeekBar();
                updateTime();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if(position < 0){
                    position = listSong.size() - 1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                createMediaPlay();
                mediaPlayer.start();
                setTimeSeekBar();
                updateTime();
            }
        });
        skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skTime.getProgress());
            }
        });
    }

    private void setTimeSeekBar(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        txtTimeEnd.setText(timeFormat.format(mediaPlayer.getDuration()));
        //gán max cho skTime
        skTime.setMax(mediaPlayer.getDuration());
    }

    private void updateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                txtTimeStart.setText(timeFormat.format(mediaPlayer.getCurrentPosition())+"");
                //update skTime
                skTime.setProgress(mediaPlayer.getCurrentPosition());
                //kiểm tra thời gian bài hát -> nếu kết thúc -> chuyển sang bài tiếp theo
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position > listSong.size() -1){
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        createMediaPlay();
                        mediaPlayer.start();
                        setTimeSeekBar();
                        updateTime();
                    }
                });
                handler.postDelayed(this,500);
                //                          thời gian thực thi lại hàm
            }
        },100);
    }
}