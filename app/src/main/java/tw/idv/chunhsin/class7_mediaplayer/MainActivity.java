package tw.idv.chunhsin.class7_mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button button,button2,button3;
    SeekBar seekBar;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
    }

    void findviews(){
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(onClickListener);
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(onClickListener);
        button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(onClickListener);
        seekBar=(SeekBar)findViewById(R.id.seekBar);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button:
                    //staticCall();
                    callMediaPlayer();
                    break;
                case R.id.button2:
                    mp.pause();
                    break;
                case R.id.button3:
                    if(mp.isPlaying()){
                        mp.stop();
                        mp.release();
                        mp=null;
                    }
                    break;
            }
        }
    };

    void staticCall(){
        if(mp==null){
            mp = MediaPlayer.create(this,R.raw.ring);
            seekBar.setMax(mp.getDuration());
        }
        if(!mp.isPlaying()){
            mp.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(mp.isPlaying()){
                        final int curPos = mp.getCurrentPosition();
                        seekBar.post(new Runnable() {
                            @Override
                            public void run() {
                                //seekBar.setScaleX(curPos);
                                seekBar.setProgress(curPos);
                            }
                        });

                    }
                }
            });
        }
    }

    void callMediaPlayer(){
        mp = new MediaPlayer();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
//        File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
//        path.toString()
        String path = "android.resource://tw.idv.chunhsin.class7_mediaplayer/"+R.raw.flourish;
        Uri uri = Uri.parse(path);
        try {
            //mp.setDataSource(path);
            mp.setDataSource(this,uri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBarr, int progress, boolean fromUser) {

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
}
