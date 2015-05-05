package musicplayer.mmad.paul.com.babou;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class PlayScreenActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private ImageButton btnBadPair;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private SongsManager songsManager;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private DBGears dbg;
    private String lastSong, newSong, candidateSong;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playscreen);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.playlistBtn);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnBadPair = (ImageButton) findViewById(R.id.btnBadPair);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        lastSong = null;
        newSong=null;
        candidateSong = null;
        index=0;

        mp = new MediaPlayer();
        songsManager = new SongsManager();
        utils = new Utilities();
        dbg = new DBGears(this);

        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important
        songsList = songsManager.getPlayList();

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.pause);
                    }
                }

            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not

                    if(currentSongIndex < (songsList.size() - 1)){
                        playSong(currentSongIndex + 1, true);
                        currentSongIndex = currentSongIndex + 1;
                    }else{
                        // play first song
                        playSong(0, false);
                        currentSongIndex = 0;
                    }


            }
        });

        btnBadPair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(getLastSong()!=null){
                    if (dbg.isBanned(getLastSong(), getNewSong())==false){
                        System.out.println("banned? "+dbg.banPairing(getLastSong(), getNewSong()));

                }

                }
                System.out.println(getLastSong() + " " + getNewSong());

                    if(currentSongIndex < (songsList.size() - 1)){
                        playSong(currentSongIndex + 1, true);
                        currentSongIndex = currentSongIndex + 1;
                    }else{
                        // play first song
                        playSong(0, false);
                        currentSongIndex = 0;
                    }

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1, false);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(songsList.size() - 1, false);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.repeatoff);
                    btnShuffle.setImageResource(R.drawable.shuffle);
                }
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.shuffle);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.shuffleoff);
                    btnRepeat.setImageResource(R.drawable.repeat);
                }
            }
        });

        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
                startActivityForResult(i, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            currentSongIndex = data.getExtras().getInt("songIndex");
            // play selected song
            playSong(currentSongIndex, false);
        }

    }

    String getLastSong(){return this.lastSong;}
    void setLastSong(String s){this.lastSong = s;}
    String getNewSong(){return this.newSong;}
    void setNewSong(String s){this.newSong = s;}
    String getCandidateSong(){return this.candidateSong;}
    void setCandidateSong(String s){this.candidateSong = s;}
    int getIndex(){return this.index;}
    void setIndex(int i){this.index = i;}




    public void  playSong(int songIndex, boolean goingForward){
        setIndex(songIndex);
        String [][] array = dbg.bannedPairs();
        for(int i = 0; i < array.length;i++){
            System.out.println(array[i][0]+" "+array[i][1]+" & "+ array[i][2]);
        }
        String ls = getLastSong();
        String ns = getNewSong();
        if (goingForward==true){
            System.out.println("1 paying new song");
            System.out.println("2 last sonf " +ls);
            System.out.println("3 newsong " + ns);
            setCandidateSong(songsList.get(getIndex()).get("songTitle"));
            System.out.println("4 candidate song = " + getCandidateSong());
            System.out.println("4.1 shuffle on? "+ isShuffle);
            Random r = new Random();
            System.out.println(ns+", "+getCandidateSong());
            boolean b = dbg.isBanned(ns, getCandidateSong());
            System.out.println("5 befor the while "+b);
            while (b){
                System.out.println("6 while = "+ b);
                System.out.println("7 The candate song is "+getCandidateSong());
                if(isShuffle){
                    System.out.println("isShuffle "+isShuffle);
                    setIndex(r.nextInt((songsList.size() - 1) - 0 + 1) + 0);
                    setCandidateSong(songsList.get(getIndex()).get("songTitle"));
                    b = dbg.isBanned(ns, getCandidateSong());
                }else{
                    System.out.println("isShuffle "+isShuffle);
                    break;
                }System.out.println("8 Song index = "+ getIndex());


            }
            System.out.println("9 last was "+ getLastSong());
            setLastSong(getNewSong());
            setNewSong(getCandidateSong());
            System.out.println("10 last is "+ getLastSong());
        }

        // Play song
        try {
            mp.reset();
            mp.setDataSource(songsList.get(getIndex()).get("songPath"));
            String songbefore = songsList.get(getIndex()).get("songTitle");
            System.out.println("song to play "+songbefore);
            mp.prepare();
            mp.start();
            // Displaying Song title
            String songTitle = songsList.get(getIndex()).get("songTitle");
            setNewSong(songTitle);
            System.out.println("New song was "+getNewSong());
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.millisecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.millisecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // check for repeat is ON or OFF
        if(isRepeat){
            // repeat is on play same song again
            playSong(currentSongIndex, false);
        } else if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex, true);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1, false);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0, false);
                currentSongIndex = 0;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mp.release();
    }
}
