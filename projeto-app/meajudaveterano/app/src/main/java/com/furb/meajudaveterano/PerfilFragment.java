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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PerfilFragment extends Fragment {

    private Button buttonSair;
    private TextView mTextNome;
    private TextView mTextEmail;
    private ImageView mImageViewFoto;
    private Usuario me;
    private final static String TAG = "PERFIL_FRAGMENT";

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

        Log.i("aaaaa", FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("/usuario")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(Usuario.class);
                        setInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, e.getMessage(), e);
                    }
                });

        return view;
    }

    private void setInfo() {
        if (me != null) {
            Picasso.get()
                    .load(me.getProfileUrl())
                    .into(mImageViewFoto);

            mTextNome.setText(me.getNome());
            mTextEmail.setText(me.getEmail());
        } else {
            Log.i(TAG, "Falha ao carregar informações do usuário");
        }
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