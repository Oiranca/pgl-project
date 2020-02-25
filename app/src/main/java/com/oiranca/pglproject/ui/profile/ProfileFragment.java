package com.oiranca.pglproject.ui.profile;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oiranca.pglproject.Profile;
import com.oiranca.pglproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    FirebaseStorage storage;
    StorageReference storageReference;


    private ImageView imageView;
    private final int COD_SELECCIONA = 10;

    private static final int CAMERA_PERMISSION = 100;
    private static final int WRITE_PERMISSION = 101;
    static final int READ_PERMISSION = 102;
    private Uri photoURI;
    private String timeStamp;

    private InputStream imageStream;
    private Bitmap selectedImage;

    private String currentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;

    private String emailProf;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.profile_fragment, container, false);
        final FloatingActionButton profileEdit = root.findViewById(R.id.profileEdit);
        final FloatingActionButton floatProfile = root.findViewById(R.id.floatProfile);
        final EditText profileName = root.findViewById(R.id.nameProf);
        final EditText profileSurname = root.findViewById(R.id.profileSurname);
        final EditText profileMail = root.findViewById(R.id.profileEmailF);
        final EditText profilePass = root.findViewById(R.id.profilePass);


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        assert user != null;
        emailProf = user.getString("Admin");

        Intent idFam = Objects.requireNonNull(getActivity()).getIntent();
        Bundle userF = idFam.getExtras();
        assert userF != null;
        emailProf = user.getString("Family");

        imageView = root.findViewById(R.id.imageProfile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


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


        return root;
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
            System.out.println("Permiso de lectura garantizado");

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
                // Continue only if the File was successfully created
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
        // Create an image file name
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
                        Uri miPath = data.getData();
                        assert miPath != null;

                        imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(miPath);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        selectedImage = getResizedBitmap(selectedImage, 1024);


                        imageView.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                    break;

                case REQUEST_TAKE_PHOTO:

                    try {


                        imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(photoURI);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        selectedImage = getResizedBitmap(selectedImage, 1024);


                        imageView.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    break;
            }


        }
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}
