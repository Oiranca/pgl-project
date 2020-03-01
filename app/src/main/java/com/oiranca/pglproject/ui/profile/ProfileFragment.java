package com.oiranca.pglproject.ui.profile;

import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oiranca.pglproject.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    private ImageView imageView;
    private EditText profilePass;

    private final int COD_SELECCIONA = 10;
    private static final int CAMERA_PERMISSION = 100;
    private static final int WRITE_PERMISSION = 101;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private Uri photoURI;
    private String timeStamp;

    private InputStream imageStream;
    private Bitmap selectedImage;

    private String currentPhotoPath;
    private String profileURL;


    private String emailProf;
    private String photoCharge;
    private String photoNew;


    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.profile_fragment, container, false);
        final FloatingActionButton profileEdit = root.findViewById(R.id.profileEdit);
        final FloatingActionButton floatProfile = root.findViewById(R.id.floatProfile);
        final EditText profileName = root.findViewById(R.id.nameProf);
        final EditText profileSurname = root.findViewById(R.id.profileSurname);
        EditText profileMail = root.findViewById(R.id.profileEmailF);
        profilePass = root.findViewById(R.id.profilePass);


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();

        if (user != null && user.getString("Admin") != null) {
            emailProf = user.getString("Admin");
            profileMail.setText(emailProf);

            if ((user.getString("PhotoAd") != null)) {
                photoCharge = user.getString("PhotoAd");
            }
            if ((user.getString("PassAd") != null)) {
                profilePass.setText(user.getString("PassAd"));
            }
            if ((user.getString("NameAd") != null)) {
                profileName.setText(user.getString("NameAd"));
            }
            if ((user.getString("SurnameAd") != null)) {
                profileSurname.setText(user.getString("SurnameAd"));
            }


        } else {
            if (user != null && user.getString("Family") != null) {
                emailProf = user.getString("Family");
                profileMail.setText(emailProf);

                if ((user.getString("PhotoFam") != null)) {
                    photoCharge = user.getString("PhotoFam");
                }
                if ((user.getString("PhotoFam") != null)) {
                    photoCharge = user.getString("PhotoFam");
                }
                if ((user.getString("PassFam") != null)) {
                    profilePass.setText(user.getString("PassFam"));
                }
                if ((user.getString("NameFam") != null)) {
                    profileName.setText(user.getString("NameFam"));
                }
                if ((user.getString("SurnameFam") != null)) {
                    profileSurname.setText(user.getString("SurnameFam"));
                }

            }


        }
        imageView = root.findViewById(R.id.imageProfile);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if (photoCharge != null) {
            if (photoNew != null) {
                Glide.with(ProfileFragment.this).
                        load(photoNew).fitCenter().centerCrop().into(imageView);
            } else {
                Glide.with(ProfileFragment.this).
                        load(photoCharge).fitCenter().centerCrop().into(imageView);
            }

        } else {
            imageView.setImageResource(R.drawable.ic_redes);
        }


        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] option = {"Cargar desde Camara", "Cargar desde Galeria", "Cancelar"};
                final AlertDialog.Builder optionAlert = new AlertDialog.Builder(getContext());
                optionAlert.setTitle("Selecciona una opción");
                optionAlert.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (option[which].equals("Cargar desde Camara")) {
                            checkPermission();

                        }

                        if (option[which].equals("Cargar desde Galeria")) {
                            Intent intentPick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intentPick, COD_SELECCIONA);

                        } else {
                            dialog.dismiss();
                        }

                    }
                });
                optionAlert.show();


            }
        });

        floatProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chanceDataProfile();
            }
        });


        return root;
    }

    private void chanceDataProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot chanceData : dataSnapshot.getChildren()) {
                    String keyForChance = chanceData.getKey();
                    String passWord;



                    assert keyForChance != null;
                    passWord = dataSnapshot.child(keyForChance).child(emailProf.replace(".", "-")).child("pass").getValue(String.class);



                    if (passWord != null && !passWord.contentEquals(profilePass.getText())) {
                        databaseReference.child(keyForChance).child(emailProf.replace(".", "-")).child("pass").setValue(profilePass.getText().toString());
                    } else {

                        passWord = dataSnapshot.child(keyForChance).child(emailProf.replace(".", "-")).child("passF").getValue(String.class);

                        if (passWord != null && !passWord.contentEquals(profilePass.getText())) {


                            databaseReference.child(keyForChance).child(emailProf.replace(".", "-")).child("passF").setValue(profilePass.getText().toString());

                        }
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);


        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);

        } else {
            takePhoto();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPermission();

                } else {
                    Toast.makeText(getContext(), "Permiso camara denegado", Toast.LENGTH_SHORT).show();
                }

            }
            break;

            case WRITE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();

                } else {
                    Toast.makeText(getContext(), "Permiso guardado denegado", Toast.LENGTH_SHORT).show();
                }

            }
            break;


        }
    }

    private void takePhoto() {

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permiso lectura denegado", Toast.LENGTH_SHORT).show();

        } else {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }

                if (photoFile != null) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, timeStamp);
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Foto tomada en HomeWork");
                    photoURI = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


                }
            }
        }


    }


    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:

                    try {
                        final Uri miPath = data.getData();
                        assert miPath != null;

                        imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(miPath);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        selectedImage = getResizedBitmap(selectedImage);


                        if (requestCode == COD_SELECCIONA && resultCode == RESULT_OK) {
                            StorageReference filepath = storageReference.child(emailProf.replace(".", "-")).child(Objects.requireNonNull(miPath.getLastPathSegment()));
                            filepath.putFile(miPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            Toast.makeText(getContext(), "Foto subida correctmanete", Toast.LENGTH_SHORT).show();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final String imageUrl = uri.toString();

                                                    Glide.with(ProfileFragment.this).load(imageUrl).fitCenter().centerCrop().into(imageView);

                                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot image : dataSnapshot.getChildren()) {
                                                                String keyValue = image.getKey();


                                                                if (keyValue != null) {
                                                                    String emailComp;

                                                                    emailComp = dataSnapshot.child(keyValue).child(emailProf.replace(".", "-"))
                                                                            .child("email").getValue(String.class);

                                                                    if (emailComp != null) {

                                                                        databaseReference.child(keyValue).child(emailProf.replace(".", "-")).child("profileAd").setValue(imageUrl);
                                                                    } else {

                                                                        emailComp = dataSnapshot.child(keyValue).child(emailProf.replace(".", "-"))
                                                                                .child("emailF").getValue(String.class);

                                                                        if (emailComp != null) {


                                                                            databaseReference.child(keyValue).child(emailProf.replace(".", "-")).child("profileF").setValue(imageUrl);
                                                                            photoNew = imageUrl;
                                                                        }

                                                                    }


                                                                }


                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                    break;

                case REQUEST_TAKE_PHOTO:

                    try {


                        imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(photoURI);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        selectedImage = getResizedBitmap(selectedImage);


                        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                            StorageReference filepath = storageReference.child(emailProf.replace(".", "-")).child(Objects.requireNonNull(photoURI.getLastPathSegment()));
                            filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            Toast.makeText(getContext(), "Foto subida correctmanete", Toast.LENGTH_SHORT).show();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final String imageUrl = uri.toString();


                                                    Glide.with(ProfileFragment.this).load(imageUrl).fitCenter().centerCrop().into(imageView);

                                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot image : dataSnapshot.getChildren()) {
                                                                String keyValue = image.getKey();


                                                                if (keyValue != null) {
                                                                    String emailComp;

                                                                    emailComp = dataSnapshot.child(keyValue).child(emailProf.replace(".", "-"))
                                                                            .child("email").getValue(String.class);

                                                                    if (emailComp != null) {

                                                                        databaseReference.child(keyValue).child(emailProf.replace(".", "-")).child("profileAd").setValue(imageUrl);
                                                                    } else {

                                                                        emailComp = dataSnapshot.child(keyValue).child(emailProf.replace(".", "-"))
                                                                                .child("emailF").getValue(String.class);

                                                                        if (emailComp != null) {


                                                                            databaseReference.child(keyValue).child(emailProf.replace(".", "-")).child("profileF").setValue(imageUrl);
                                                                            photoNew = imageUrl;
                                                                        }

                                                                    }


                                                                }


                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    break;
            }


        }
    }


    private Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 1024;
            height = (int) (width / bitmapRatio);
        } else {
            height = 1024;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}
