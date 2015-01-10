package ca.rmen.android.palidamuerte;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Copyright 2014 Carmen Alvarez
 * 
 * This file is part of Pálida Muerte.
 * 
 * Pálida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Pálida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Pálida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
public class MusicPlayer {

    public static class Song {
        public final String composer;
        public final String title;

        public Song(String composer, String title) {
            this.composer = composer;
            this.title = title;
        }
    }

    private static final String TAG = Constants.TAG + MusicPlayer.class.getSimpleName();
    private static final String PREF_LAST_SONG_PLAYED = "pref_last_song_played";
    private static MusicPlayer sInstance = null;

    private final Context mContext;
    private MediaPlayer mMediaPlayer = null;
    private String[] mMusicFileNames;
    private int mSongIndex = -1;

    public static synchronized MusicPlayer getInstance(Context context) {
        if (sInstance == null) sInstance = new MusicPlayer(context);
        return sInstance;
    }

    private MusicPlayer(Context context) {
        mContext = context.getApplicationContext();
        mSongIndex = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_LAST_SONG_PLAYED, -1);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        try {
            mMusicFileNames = context.getResources().getAssets().list("music");
            shuffle();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void shuffle() {
        Log.v(TAG, "shuffle");
        Random random = new Random();
        for (int i = mMusicFileNames.length - 1; i > 0; i--) {
            int j = random.nextInt(i);
            String tempFileName = mMusicFileNames[i];
            mMusicFileNames[i] = mMusicFileNames[j];
            mMusicFileNames[j] = tempFileName;
        }
        Log.v(TAG, "play list is now: " + Arrays.toString(mMusicFileNames));
    }

    public void play() {
        Log.v(TAG, "play");
        mSongIndex++;
        if (mSongIndex == mMusicFileNames.length) mSongIndex = 0;
        if (mMediaPlayer.isPlaying()) return;
        playSong(mSongIndex);
    }

    /**
     * @return The currently playing song, if any. If no song is currently playing, return null.
     */
    public Song getCurrentSong() {
        if (isPlaying()) {
            String fileName = mMusicFileNames[mSongIndex];
            fileName = fileName.replaceAll("-", " ");
            fileName = fileName.replaceAll(".mid", "");
            fileName = fileName.replaceAll("No([0-9])", "n°$1");
            fileName = fileName.replaceAll("Op([0-9])", "Op. $1");
            String[] tokens = fileName.split("_");
            String composer = tokens[0];
            String songName = tokens[1];
            return new Song(composer, songName);
        }
        return null;
    }

    private void playSong(int index) {
        Log.v(TAG, "playSong " + index + " (" + mMusicFileNames[index] + ")");
        mMediaPlayer.reset();
        AssetFileDescriptor fd;
        try {
            fd = mContext.getResources().getAssets().openFd("music/" + mMusicFileNames[index]);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        }
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return;
        }
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        mMediaPlayer.start();
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(PREF_LAST_SONG_PLAYED, mSongIndex).commit();
    }

    public void stop() {
        Log.v(TAG, "stop");
        mMediaPlayer.stop();
    }

    public void toggle() {
        Log.v(TAG, "toggle");
        if (mMediaPlayer.isPlaying()) stop();
        else
            play();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    private OnErrorListener mOnErrorListener = new OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.v(TAG, "onError: what = " + what + ", extra = " + extra);
            return false;
        }
    };

    private OnCompletionListener mOnCompletionListener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "onCompletion");
            play();
        }
    };

    private OnInfoListener mOnInfoListener = new OnInfoListener() {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.v(TAG, "onInfo: what=" + what + ", extra = " + extra);
            return false;
        }
    };
}
