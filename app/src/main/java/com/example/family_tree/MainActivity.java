package com.example.family_tree;

import android.os.Bundle;

import com.example.family_tree.model.GlobalData;
import com.example.family_tree.model.Person;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Contacts;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.family_tree.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivityMainBinding binding;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Files.exists(Paths.get(getFilesDir() + "people.json"))){
            try {
                Files.createFile(Paths.get(getFilesDir() + "people.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try(FileInputStream fis = new FileInputStream(getFilesDir()+"people.json")){
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer, 0, buffer.length);
            Person[] ps = gson.fromJson(new String(buffer, StandardCharsets.UTF_8),Person[].class);
            if(ps != null)
            for (Person p:ps) {
                if(p != null)
                    GlobalData.family.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         binding = ActivityMainBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

            setSupportActionBar(binding.toolbar);

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}