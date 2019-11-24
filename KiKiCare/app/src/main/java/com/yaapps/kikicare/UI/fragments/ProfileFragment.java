package com.yaapps.kikicare.UI.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.yaapps.kikicare.R;


public class ProfileFragment extends Fragment {
    ImageView profileImage;
    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        profileImage=rootView.findViewById(R.id.profilImg);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_DENIED)  {
                        //PERMISSION NOT GRANTED , REQUEST IT
                        String[]permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);

                    } else {
                        // PERMISSION ALREADY GRANTED
                        pickImageFromGallery();

                    }
                } else {
                    //OS IS LESS THAN M
                    pickImageFromGallery();

                }
            }
        });



        return rootView;
    }

    private void pickImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        switch ( requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(getActivity(),"Permission was Denied !!!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==IMAGE_PICK_CODE){
            profileImage.setImageURI(data.getData());

        }
    }


    //  private void selectImage(Context context) {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Choose your profile picture");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (options[which].equals("Take Photo")) {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
//
//                } else if (options[which].equals("Choose from Gallery")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 1);
//
//                } else if (options[which].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
}


