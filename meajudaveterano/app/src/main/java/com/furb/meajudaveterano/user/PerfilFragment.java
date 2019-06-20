package com.furb.meajudaveterano.user;

import android.content.Intent;
import android.os.Bundle;
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

import com.furb.meajudaveterano.login.LoginActivity;
import com.furb.meajudaveterano.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PerfilFragment extends Fragment {

    private final static String TAG = "PERFIL_FRAGMENT";

    private Button buttonSair;
    private Button buttonEditar;
    private TextView mTextNome;
    // private TextView mTextEmail;
    private ImageView mImageViewFoto;
    private Usuario me;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, null);
        buttonSair = view.findViewById(R.id.buttonSair);
        buttonEditar = view.findViewById(R.id.buttonEditar);
        mTextNome = view.findViewById(R.id.textViewName);
        // mTextEmail = view.findViewById(R.id.textViewEmail);
        mImageViewFoto = view.findViewById(R.id.imageViewFoto);

        buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUsuarioEdit();
            }
        });

        if (me == null) {
            Log.i(TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAA");
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
        }

        return view;
    }

    private void openUsuarioEdit() {
        Intent intent = new Intent(getContext(), UsuarioEditActivity.class);
        intent.putExtra("usuario", this.me);
        startActivity(intent);
    }

    private void setInfo() {
        if (me != null) {
            Picasso.get().load(me.getProfileUrl()).into(mImageViewFoto);
            mTextNome.setText(me.getNome());
            //mTextEmail.setText(me.getEmail());
        } else {
            Log.i(TAG, "Falha ao carregar informações do usuário");
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        verifyAuthentication();
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}