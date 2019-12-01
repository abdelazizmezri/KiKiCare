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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.R;


public class ProfileFragment extends Fragment {
    ImageView profileImage,update;
    TextView tvname,tvrace,tvsexe,tvbirth;
    private int id;
    private String type;
    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        profileImage=rootView.findViewById(R.id.profilImg);
        tvname=rootView.findViewById(R.id.tv_name);
        tvsexe=rootView.findViewById(R.id.tv_sexe);
        tvrace=rootView.findViewById(R.id.tv_race);
        tvbirth=rootView.findViewById(R.id.tv_birth);
        update=rootView.findViewById(R.id.image_update);
   checkAndFillData();

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

update.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

displayFragment(new UpdateAnimalFragment(),bundle.getString("name"),bundle.getString("race"),bundle.getString("birth"),bundle.getInt("id"));
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
    private void checkAndFillData()
    {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("id",0);
            tvname.setText(bundle.getString("name"));
            tvrace.setText(bundle.getString("race"));
            tvsexe.setText(bundle.getString("sexe"));
            type=bundle.getString("type");
            tvbirth.setText(bundle.getString("birth"));
        }
    }
    protected void displayFragment(Fragment fragment, String name,String race,String birth,int id) {
        FragmentManager ft = getFragmentManager();
        Fragment myFragment = ft.findFragmentByTag("PROFILE");
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("name",name);
        bundle.putString("race",race);
        bundle.putString("birth",birth);
        fragment.setArguments(bundle);
        if (myFragment != null && myFragment.isVisible()) {
            ft.beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                    android.R.animator.fade_out)
                    .hide(myFragment)
                    .replace(R.id.containerList,fragment,"UPDATE")
                    .commit();
        }
    }
}


