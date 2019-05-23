package com.furb.meajudaveterano;

import android.media.Image;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter groupAdapter;
    private Usuario usuario;
    private Usuario me;
    private TextView editTextChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        ImageView imageViewMessage = viewHolder.itemView.findViewById(R.id.imageViewMessageUser);
            Picasso.get()
                    .load(usuario.getProfileUrl())
                    .into(imageViewMessage);
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usuario = getIntent().getExtras().getParcelable("usuario");
        getSupportActionBar().setTitle(usuario.getNome());

        RecyclerView rv = findViewById(R.id.recyclerChat);
        editTextChat = findViewById(R.id.editTextChat);
        ImageView imageViewSend = findViewById(R.id.imageViewSend);

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }

        });

        groupAdapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(groupAdapter);

        FirebaseFirestore.getInstance().collection("/usuario")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(Usuario.class);
                        fetchMessages();
                    }
                });
    }

    private void fetchMessages() {
        if (me != null) {

            String fromId = me.getUuid();
            String toId = usuario.getUuid();

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(fromId)
                    .collection(toId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                            Log.i("mano?", "sei la");

                            if (documentChanges != null) {
                                for (DocumentChange doc: documentChanges) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Message message = doc.getDocument().toObject(Message.class);
                                        groupAdapter.add(new MessageItem(message));
                                    }
                                }
                            }
                        }
                    });

        }
    }

    private void sendMessage() {
        String text = editTextChat.getText().toString();
        editTextChat.setText(null);

        String fromUid = FirebaseAuth.getInstance().getUid();
        String toUid = usuario.getUuid();
        long timestamp = System.currentTimeMillis();

        Message message = new Message();
        message.setFromId(fromUid);
        message.setToId(toUid);
        message.setTimestamp(timestamp);
        message.setText(text);

        if (!message.getText().isEmpty()) {
            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(fromUid)
                    .collection(toUid)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("ENVIAR", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ENVIAR_ERRO", e.getMessage(), e);
                        }
                    });

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(toUid)
                    .collection(fromUid)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("ENVIAR", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ENVIAR_ERRO", e.getMessage(), e);
                        }
                    });
        }
    }

    private class MessageItem extends Item<ViewHolder> {

        private final Message message;

        public MessageItem(Message message) {
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewMessage = viewHolder.itemView.findViewById(R.id.textViewMessage);
            textViewMessage.setText(message.getText());

        }

        @Override
        public int getLayout() {
            return message.getFromId().equals(FirebaseAuth.getInstance().getUid()) ?
                    R.layout.item_to_message : R.layout.item_from_message;
        }
    }
}
