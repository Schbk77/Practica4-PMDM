package com.example.serj.videoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Principal extends Activity {

    private final int REPRODUCIR_DESDE_GALERIA = 1;
    private Worker f;
    private static final String TAG = "worker";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REPRODUCIR_DESDE_GALERIA && resultCode == RESULT_OK && null != data) {
            f.reproducir(data.getData());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);
        FragmentManager fm = getFragmentManager();
        f = (Worker)fm.findFragmentByTag(TAG);
        if (f == null) {
            f = new Worker();
            fm.beginTransaction().add(f, TAG).commit();
        }

        Intent intent = getIntent();
        if(intent.getAction().equals(Intent.ACTION_VIEW) && intent != null) {
            f.reproducir(intent.getData());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_playvideo) {
            return playVideo();
        } else if (id == R.id.action_playuri) {
            return playUri();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean playVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REPRODUCIR_DESDE_GALERIA);
        return true;
    }

    public boolean playUri() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View entradaUri = inflater.inflate(R.layout.uri, null);
        builder.setTitle("Reproducir desde URL");
        builder.setView(entradaUri);
        final EditText et = (EditText)entradaUri.findViewById(R.id.etUri);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!et.getText().toString().isEmpty()){
                    f.reproducir(Uri.parse(et.getText().toString()));
                } else {
                    Toast.makeText(Principal.this, "URL vacía", Toast.LENGTH_SHORT);
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
