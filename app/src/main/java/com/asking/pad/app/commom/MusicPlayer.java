package com.asking.pad.app.commom;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.asking.pad.app.widget.AskSimpleDraweeView;

import java.io.IOException;

/**
 * Created by wxwang on 2016/12/13.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {
    private static MusicPlayer player = new MusicPlayer();
    private MediaPlayer mMediaPlayer;

    public boolean isPlaying() {
        return isPlaying;
    }

    private boolean isPlaying;

    public static MusicPlayer getPlayer() {
        return player;
    }

    public AskSimpleDraweeView voice;

    public MusicPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
    }

    public MusicPlayer bindVoice(AskSimpleDraweeView voice) {
        this.voice = voice;
        return player;
    }

    private long mExitTime;
    private String urlTmp;

    public void play(String url) {
        try {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//防止点击过快
                mExitTime = System.currentTimeMillis();
                if (TextUtils.equals(url,urlTmp)) {
                    mMediaPlayer.start();
                    isPlaying = true;
                    if (voice != null) {
                        voice.getController().getAnimatable().start();
                    }
                } else {
                    urlTmp = url;
                    mMediaPlayer.reset();
                    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            mMediaPlayer.pause();
                            mMediaPlayer.reset();
                            return true;
                        }
                    });
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mMediaPlayer.start();
                            isPlaying = true;
                            if (voice != null) {
                                voice.getController().getAnimatable().start();
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            if (isPlaying) {
                isPlaying = false;
                mMediaPlayer.pause();
                if (voice != null) {
                    voice.getController().getAnimatable().stop();
                }
            }
        } catch (Exception e) {
        }
        ;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            isPlaying = false;
            if (voice != null) {
                voice.getController().getAnimatable().stop();
            }
        } catch (Exception e) {
        }
        ;
    }


    public void release() {
        mMediaPlayer.release();
//        mMediaPlayer = null;
    }

}

