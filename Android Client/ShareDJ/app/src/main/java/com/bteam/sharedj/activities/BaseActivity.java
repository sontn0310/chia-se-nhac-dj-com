package com.bteam.sharedj.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.bteam.sharedj.listeners.MusicStateListener;

/**
 * Created by nhungpro on 3/5/2017.
 */

public class BaseActivity extends AppCompatActivity implements ServiceConnection, MusicStateListener {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }
}
