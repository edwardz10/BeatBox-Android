package com.bignerdranch.android.beatbox.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void play(final Sound sound) {
        final Integer soundId = sound.getSoundId();

        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void release() {
        mSoundPool.release();
    }

    private void loadSounds() {
        final String[] soundNames;

        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    private void load(final Sound sound) throws IOException {
        final AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        sound.setSoundId(mSoundPool.load(afd, 1));
    }

}
