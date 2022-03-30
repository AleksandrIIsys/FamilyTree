package com.example.family_tree;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.family_tree.databinding.FragmentTreeBinding;
import com.example.family_tree.model.GlobalData;
import com.example.family_tree.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hdodenhof.circleimageview.CircleImageView;

public class TreeFragment extends Fragment {
    private FragmentTreeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTreeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = binding.childrensLayout;
        Person you = gson.fromJson(getArguments().getString("person"),Person.class);
        renderPerson(you,5,6,true); // ты
        //твои роидтели
        renderPerson(you.getFather(),4,4,false);
        renderPerson(you.getMother(),6,4,false);
        //родители родителей
        if(you.getFather() != null) {
            renderPerson(you.getFather().getFather(), 1, 2,false);
            renderPerson(you.getFather().getMother(), 4, 2,false);
            if(you.getFather().getMother() != null){
                renderPerson(you.getFather().getMother().getFather(), 3, 0,false);
                renderPerson(you.getFather().getMother().getMother(), 4, 0,false);
            }else{
                renderPerson(null, 3, 0,false);
                renderPerson(null, 4, 0,false);

            }
            if(you.getFather().getFather() != null){
                renderPerson(you.getFather().getFather().getFather(), 0, 0,false);
                renderPerson(you.getFather().getFather().getMother(), 1, 0,false);
            }
            else{
                renderPerson(null, 0, 0,false);
                renderPerson(null, 1, 0,false);

            }
        }else{
            renderPerson(null, 1, 2,false);
            renderPerson(null, 4, 2,false);
            renderPerson(null, 3, 0,false);
            renderPerson(null, 4, 0,false);
            renderPerson(null, 0, 0,false);
            renderPerson(null, 1, 0,false);

        }
        if(you.getMother() != null) {
            renderPerson(you.getMother().getFather(), 6, 2,false);
            renderPerson(you.getMother().getMother(), 9, 2,false);
            //пра дед по бабушке
            if(you.getMother().getFather() != null){
                renderPerson(you.getMother().getFather().getMother(), 6, 0,false);
                renderPerson(you.getMother().getFather().getFather(), 7, 0,false);
            }else{
                renderPerson(null, 6, 0,false);
                renderPerson(null, 7, 0,false);
            }
            if(you.getMother().getMother() != null){
                renderPerson(you.getMother().getMother().getMother(), 9, 0,false);
                renderPerson(you.getMother().getMother().getFather(), 10, 0,false);
            }else{
                renderPerson(null, 9, 0,false);
                renderPerson(null, 10, 0,false);
            }
        }else{
            renderPerson(null, 6, 2,false);
            renderPerson(null, 9, 2,false);
            renderPerson(null, 6, 0,false);
            renderPerson(null, 7, 0,false);
            renderPerson(null, 9, 0,false);
            renderPerson(null, 10, 0,false);
        }
        renderChilds(you);
    }
    private void renderChilds(Person you){
        LinearLayout linearLayout = binding.childrensLayout;
        View item;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (Person p:
                GlobalData.getChildrens(you)) {
            item = getLayoutInflater().inflate(R.layout.tree_node,linearLayout,false);
            item.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("person",gson.toJson(p));
                NavHostFragment.findNavController(this).navigate(R.id.action_treeFragment_to_preViewFragment,bundle);
            });
            ((ImageView) item.findViewById(R.id.node_image)).setImageBitmap(BitmapFactory.decodeFile(p.getImage()));
            ((TextView) item.findViewById(R.id.node_name)).setText(p.getName());
            ((TextView) item.findViewById(R.id.node_surname)).setText(p.getSurname());
            linearLayout.addView(item,params);
        }
    }
    private void renderPerson(Person you,int column,int row, boolean main){

        GridLayout gridLayout = binding.gridLayout;
        View item = getLayoutInflater().inflate(R.layout.tree_node,gridLayout,false);
        item.setOnClickListener(v -> {
            if(you == null){
                Toast.makeText(getContext(),"no info",Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("person",gson.toJson(you));
            NavHostFragment.findNavController(this).navigate(R.id.action_treeFragment_to_preViewFragment,bundle);
        });
        if(main)
            ((CircleImageView) item.findViewById(R.id.node_image)).setBorderColor(Color.parseColor("#ffd700"));
        ((ImageView) item.findViewById(R.id.node_image)).setImageResource(R.drawable.nonephoto);
        ((TextView) item.findViewById(R.id.node_name)).setText("Недостаточно");
        ((TextView) item.findViewById(R.id.node_surname)).setText("информации");
        if(you != null) {
            ((ImageView) item.findViewById(R.id.node_image)).setImageBitmap(BitmapFactory.decodeFile(you.getImage()));
            ((TextView) item.findViewById(R.id.node_name)).setText(you.getName());
            ((TextView) item.findViewById(R.id.node_surname)).setText(you.getSurname());
        }
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(column);
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.rowSpec = GridLayout.spec(row);
        gridLayout.addView(item,layoutParams);

    }
}