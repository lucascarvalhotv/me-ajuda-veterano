package com.furb.meajudaveterano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class PerfilFragment extends Fragment {

    private Button buttonSair;
    private TextView mTextNome;
    private TextView mTextEmail;
    private ImageView mImageViewFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil,null);
        buttonSair = view.findViewById(R.id.buttonSair);
        mTextNome = view.findViewById(R.id.textViewNome);
        mTextEmail = view.findViewById(R.id.textViewEmail);
        mImageViewFoto = view.findViewById(R.id.imageViewFoto);

        buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mTextNome.setText(firebaseAuth.getCurrentUser().getDisplayName());
        mTextEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        /*Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                    firebaseAuth.getCurrentUser().getPhotoUrl());
            mImageViewFoto.setImageDrawable(new BitmapDrawable(bitmap));
        } catch (IOException e) {
            Log.e("Error", e.getLocalizedMessage());
        }*/

        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        verifyAuthentication();
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}