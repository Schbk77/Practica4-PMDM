package com.example.serj.videoplayer;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class Worker extends Fragment {

    private Principal actividad;
    private VideoView videoView;
    private Uri path;
    private Hilo h;
    private int msec;

    public Worker() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actividad = (Principal)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initVideoView();
        msec = 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVideoView();
        msec = 0;
        if(savedInstanceState != null && h.isCancelled()) {
            msec = savedInstanceState.getInt("currentPosition");
            reproducir(path);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(h instanceof Hilo && outState != null) {
            h.cancel(true);
        }

    }

    public void reproducir (Uri uri) {
        h = new Hilo();
        h.execute(uri);
    }

    class Hilo extends AsyncTask<Uri, Uri, Void> {

        @Override
        protected Void doInBackground(Uri... params) {
            publishProgress(params);
            return null;
        }

        @Override
        protected void onProgressUpdate(Uri... values) {
            super.onProgressUpdate(values);
            path = values[values.length-1];
            videoView.setVideoURI(path);
            if(msec > 0) {
                videoView.seekTo(msec);
                msec = 0;
            }
            videoView.start();
        }
    }

    private void initVideoView() {
        // VideoView y MediaController
        videoView = (VideoView)actividad.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(actividad);
        mediaController.setMediaPlayer(videoView);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }
}
