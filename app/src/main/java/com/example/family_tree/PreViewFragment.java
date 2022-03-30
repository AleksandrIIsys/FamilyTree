package com.example.family_tree;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_tree.databinding.FragmentPreViewBinding;
import com.example.family_tree.model.GlobalData;
import com.example.family_tree.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class PreViewFragment extends Fragment {
    FragmentPreViewBinding binding;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Person you = gson.fromJson(String.valueOf(getArguments().getString("person")),Person.class);
        binding.imageView2.setImageBitmap(BitmapFactory.decodeFile(you.getImage()));
        if(you.getDate() != null)
            binding.preViewDate.setText(simpleDateFormat.format(you.getDate()));
        else
            binding.preViewDate.setText("Нет данных");
        binding.preViewName.setText(you.getName());
        binding.preViewSurname.setText(you.getSurname());
        binding.floatingActionButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("person",gson.toJson(you));
            NavHostFragment.findNavController(this).navigate(R.id.action_preViewFragment_to_SecondFragment,bundle);
        });
        binding.btnDel.setOnClickListener(v -> {
            GlobalData.DeleteMember(you);
            try (FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir() + "people.json", false)) {
                fos.write(gson.toJson(GlobalData.family).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();

            }
            NavHostFragment.findNavController(this).navigate(R.id.action_preViewFragment_to_FirstFragment2);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}