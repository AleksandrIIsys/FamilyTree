package com.example.family_tree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.family_tree.databinding.FragmentFirstBinding;
import com.example.family_tree.model.GlobalData;
import com.example.family_tree.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FirstFragment extends Fragment {

private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


      binding = FragmentFirstBinding.inflate(inflater, container, false);
      return binding.getRoot();

    }
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                binding.getRoot().getContext(),
                android.R.layout.simple_list_item_1,
                GlobalData.getFamilyFullName());
        binding.familyList.setAdapter(adapter);
        binding.familyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("person",gson.toJson(GlobalData.family.get(position)));
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_treeFragment,bundle);
            }
        });
        binding.next.setOnClickListener(view1 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}