package com.example.family_tree;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.family_tree.databinding.FragmentSecondBinding;
import com.example.family_tree.model.GlobalData;
import com.example.family_tree.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        requestMultiplePermissions();
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    ArrayList<String> tempMale = GlobalData.getMale();
    ArrayList<String> tempFemale = GlobalData.getFemale();
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempMale.add("Выберите отца");
        EditText name = binding.textName;
        EditText surname = binding.textSurname;
        RadioGroup chooser = binding.chooserSex;
        Person you = new Person();
        binding.spinnerFather.setAdapter(new ArrayAdapter(getActivity(), com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, tempMale));
        binding.spinnerFather.setSelection(GlobalData.getMale().size());
        binding.spinnerFather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    you.setFather(GlobalData.findMember(tempMale.get(position)));
                } catch (Exception e) {
                    you.setFather(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                you.setFather(null);
            }
        });
        tempFemale.add("Выберите мать");

        binding.spinnerMother.setAdapter(new ArrayAdapter(getActivity(), com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, tempFemale));
        binding.spinnerMother.setSelection(GlobalData.getFemale().size());
        binding.spinnerMother.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    you.setMother(GlobalData.findMember(tempFemale.get(position)));
                } catch (Exception e) {
                    you.setMother(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                you.setMother(null);
            }
        });

        binding.selectButton.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 1);
        });
        binding.create.setOnClickListener(view1 -> {
            String sex = String.valueOf(((RadioButton) getActivity().findViewById(chooser.getCheckedRadioButtonId())).getText());
            if (name.getText().equals("") || surname.getText().equals("") || sex.equals("") || imgName.equals("")) {
                Toast.makeText(getContext(), "---><---", Toast.LENGTH_SHORT).show();
                return;
            }
            you.setSex(sex);
            you.setName(String.valueOf(name.getText()));
            you.setSurname(String.valueOf(surname.getText()));
            you.setImage(imgName);
            try {
                you.setDate(simpleDateFormat.parse(String.valueOf(binding.edtdate.getText())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(getArguments() != null) {
                Person person = gson.fromJson(getArguments().getString("person"), Person.class);
                if(change)
                    try {
                        Files.delete(Paths.get(person.getImage()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                GlobalData.editChildrens(you);
                GlobalData.family.set(GlobalData.FindMember(person),you);
            }else {
                GlobalData.family.add(you);
            }
            try (FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir() + "people.json", false)) {
                fos.write(gson.toJson(GlobalData.family).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);

        });
        binding.edtdate.setOnClickListener(v -> {
                    final Calendar calendar = Calendar.getInstance();
                    int mYear, mMonth, mDay;
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view12, year, month, dayOfMonth) -> {
                        try {
                            binding.edtdate.setText(simpleDateFormat.format(simpleDateFormat.parse(dayOfMonth+"/"+month+"/"+year)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
        );
        EditPerson();
    }
    private void EditPerson(){
        if(getArguments() != null){
            Person person = gson.fromJson(getArguments().getString("person"),Person.class);
            ArrayList<String> temp;
            if(person.getSex().equals("Female")) {
                binding.chooserSex.check(R.id.female);
                tempFemale.remove(person.getName() + " " + person.getSurname());
                binding.spinnerMother.setAdapter(new ArrayAdapter(getActivity(), com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, tempFemale));
                binding.spinnerMother.setSelection(tempFemale.size()-1);
                if(person.getFather() != null) {
                    binding.spinnerFather.setSelection(GlobalData.getMale().indexOf(person.getFather().getName() + " " + person.getFather().getSurname()));
                }if(person.getMother() != null){
                    binding.spinnerMother.setSelection(tempFemale.indexOf(person.getMother().getName() + " " + person.getMother().getSurname()));
                }
            }else{
                binding.chooserSex.check(R.id.male);
                tempMale.remove(person.getName() + " " + person.getSurname());
                binding.spinnerFather.setAdapter(new ArrayAdapter(getActivity(), com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, tempMale));
                binding.spinnerFather.setSelection(tempMale.size()-1);
                if(person.getFather() != null) {
                    binding.spinnerFather.setSelection(tempMale.indexOf(person.getFather().getName() + " " + person.getFather().getSurname()));
                }
                if(person.getMother() != null) {
                    binding.spinnerMother.setSelection(GlobalData.getFemale().indexOf(person.getMother().getName() + " " + person.getMother().getSurname()));
                }
            }
            binding.imageView.setImageBitmap(BitmapFactory.decodeFile(person.getImage()));
            binding.textName.setText(person.getName());
            binding.textName.setEnabled(false);
            binding.textSurname.setText(person.getSurname());
            binding.textSurname.setEnabled(false);
            if(person.getDate() != null)
                binding.edtdate.setText(simpleDateFormat.format(person.getDate()));
            imgName = person.getImage();
        }
    }
    String imgName = "";
    boolean change = false;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (data != null) {
            Uri contentURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                imgName = saveImage(bitmap);
                binding.imageView.setImageBitmap(bitmap);
                change = true;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        if (!Files.exists(Paths.get(getActivity().getExternalCacheDir().getAbsolutePath() + "/images"))) {
            try {
                Files.createDirectory(Paths.get(getActivity().getExternalCacheDir().getAbsolutePath() + "/images"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File wallpaperDirectory = new File(
                getActivity().getExternalCacheDir().getAbsolutePath() + "/images");
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            String name = Calendar.getInstance()
                    .getTimeInMillis() + ".jpg";
            File f = new File(wallpaperDirectory, name);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(binding.getRoot().getContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}