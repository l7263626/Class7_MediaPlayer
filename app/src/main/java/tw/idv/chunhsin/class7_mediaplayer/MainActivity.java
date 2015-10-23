package tw.idv.chunhsin.class7_mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

    Button button,button2,button3;
    SurfaceView surfaceView;
    SurfaceHolder sHolder;
    SeekBar seekBar;
    MediaPlayer mp,nextMp;
    TextView textView;
    int totalSecs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
    }

    void findviews(){
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        sHolder=surfaceView.getHolder();
        sHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(onClickListener);
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(onClickListener);
        button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(onClickListener);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        textView=(TextView)findViewById(R.id.textView);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button:
                    staticCall();
                    /*
                    try {
                        callMediaPlayer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case R.id.button2:
                    mp.pause();
                    break;
                case R.id.button3:
                    if(mp.isPlaying()){
                        mp.stop();
                        mp.release();
                        //mp=null;
                    }
                    break;
            }
        }
    };

    void staticCall(){
        if(mp==null){
            mp = MediaPlayer.create(this, R.raw.littlemonster);
            mp.setDisplay(sHolder);
            totalSecs = mp.getDuration();
            seekBar.setMax(totalSecs);
            //textView.setText(String.valueOf(totalSecs));
        }
        if(!mp.isPlaying()){
            mp.start();

            final Handler mHandler = new Handler();
            //Make sure you update Seekbar on UI thread
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mp!=null && mp.isPlaying()) {
                        int mCurrentPosition = mp.getCurrentPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append(mCurrentPosition / 1000).append("/").append(totalSecs / 1000);
                        textView.setText(sb);
                        seekBar.setProgress(mCurrentPosition);
                        mHandler.postDelayed(this, 100);
                    } else {
                        seekBar.setProgress(totalSecs);
                    }

                }
            });

            /*
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(mp.isPlaying()){
                        final int curPos = mp.getCurrentPosition();
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder sb = new StringBuilder();
                                sb.append(curPos / 1000).append("/").append(totalSecs / 1000);
                                textView.setText(sb);
                            }
                        });

                        seekBar.post(new Runnable() {
                            @Override
                            public void run() {
                                //seekBar.setScaleX(curPos);
                                seekBar.setProgress(curPos);
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            */
        }
    }

    void callMediaPlayer() throws IOException {
        mp = new MediaPlayer();
        mp.setOnPreparedListener(this);
        mp.setOnCompletionListener(this);
//        File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
//        path.toString()
        String path = "android.resource://tw.idv.chunhsin.class7_mediaplayer/"+R.raw.flourish;
        Uri uri = Uri.parse(path);
        nextMp = new MediaPlayer();
        nextMp.setDataSource(this,Uri.parse("android.resource://tw.idv.chunhsin.class7_mediaplayer/"+R.raw.ring));
        nextMp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.setNextMediaPlayer(mediaPlayer);
            }
        });
        nextMp.prepareAsync();
        try {
            //mp.setDataSource(path);
            mp.setDataSource(this,uri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setLooping(false);
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBarr, int progress, boolean fromUser) {
            if(fromUser){
                StringBuilder sb = new StringBuilder();
                sb.append(progress/1000).append("/").append(totalSecs / 1000);
                textView.setText(sb);
                mp.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        totalSecs = mediaPlayer.getDuration();
        seekBar.setMax(totalSecs);
        StringBuilder sb = new StringBuilder();
        sb.append("0/").append(totalSecs/1000);
        textView.setText(sb);
        mediaPlayer.start();
    }
}
