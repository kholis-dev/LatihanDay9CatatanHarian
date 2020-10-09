package com.kholis.dev.catatanharian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE_STORAGE = 100;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listCatatan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= 23){
            if (periksaIjinPenyimpanan()){
                megambilListFilePadaFolder();
            }
        } else {
            megambilListFilePadaFolder();
        }
    }

    private void megambilListFilePadaFolder() {
        String path = Environment.getExternalStorageDirectory().toString() + "kominfo.kholis";
        File dir = new File(path);
        if (dir.exists()){
            File[] files = dir.listFiles();
            String[] fileNames = new String[files.length];
            String[] dateCreates = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
            ArrayList<Map<String,Object>> itemList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
                Date lastModData = new Date(files[i].lastModified());
                dateCreates[i] = simpleDateFormat.format(lastModData);
                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("name",fileNames[i]);
                listItemMap.put("date",dateCreates[i]);
                itemList.add(listItemMap);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,itemList,android.R.layout.simple_list_item_2,new String[]{"name","date"},new int[]{android.R.id.text1,android.R.id.text2});
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private boolean periksaIjinPenyimpanan() {
        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQ_CODE_STORAGE:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    megambilListFilePadaFolder();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.actAdd){
            startActivity(new Intent(this,AddCatatanActivity.class));
        }
        return true;
    }
}