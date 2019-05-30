package com.furb.meajudaveterano.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.furb.meajudaveterano.MainActivity;
import com.furb.meajudaveterano.R;
import com.furb.meajudaveterano.user.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SigninActivity extends AppCompatActivity {

    private EditText mEditUsername;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mButtonCadastrar;
    private Button mButtonFoto;
    private Uri mSelectedUri;
    private ImageView mImageViewFoto;

    // Dependências Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEditUsername = findViewById(R.id.editUserName);
        mEditEmail = findViewById(R.id.editEmail);
        mEditPassword = findViewById(R.id.editPassword);
        mButtonCadastrar = findViewById(R.id.buttonCadastrar);
        mButtonFoto = findViewById(R.id.buttonSelectedPhoto);
        mImageViewFoto = findViewById(R.id.imageViewFoto);

        mButtonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        mButtonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFoto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // requestCode == 0 : requisição de foto
        if (requestCode == 0) {
            mSelectedUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                mImageViewFoto.setImageDrawable(new BitmapDrawable(bitmap));
                mButtonFoto.setAlpha(0);
            } catch (IOException e) {
                Log.e("Error", e.getLocalizedMessage());
            }


        }
    }

    private void createUser() {
        String name = mEditUsername.getText().toString();
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        if (name == null || name.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            Toast.makeText(this, "Nome, e-mail e senha devem ser informados!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "A senha deve conter no mínimo 6 caracteres!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Teste", task.getResult().getUser().getUid());

                            saveUserOnFirebase();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage());
                    }
                });
    }

    private void selectFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void saveUserOnFirebase() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(mSelectedUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uid = FirebaseAuth.getInstance().getUid();
                                String userName = mEditUsername.getText().toString();
                                String profileUrl = uri.toString();
                                String email = mEditEmail.getText().toString();

                                Usuario usuario = new Usuario(uid, userName, profileUrl, email);

                                FirebaseFirestore.getInstance().collection("usuario")
                                        .document(uid)
                                        .set(usuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("Teste", "Usuário criado com sucesso");
                                                Intent intent = new Intent(
                                                        SigninActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("Teste", e.getMessage());
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage());
                    }
                });

    }
}
