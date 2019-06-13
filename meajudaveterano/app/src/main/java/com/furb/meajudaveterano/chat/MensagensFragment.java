package com.furb.meajudaveterano.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furb.meajudaveterano.login.Contact;
import com.furb.meajudaveterano.R;
import com.furb.meajudaveterano.user.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class MensagensFragment extends Fragment {

    private static final String TAG = "MENSAGENS_FRAGMENT";
    private GroupAdapter groupAdapter;
    private Usuario usuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensagens, null);

        groupAdapter = new GroupAdapter();

        RecyclerView rv = view.findViewById(R.id.recycler_last_message);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setAdapter(groupAdapter);

        groupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                ContactItem contactItem = (ContactItem) item;
                usuario = new Usuario(contactItem.contact.getUuid(),
                        contactItem.contact.getUsername(),
                        contactItem.contact.getPhotoUrl(),
                        "");
                openChat();
            }
        });


        fetchLastMessage();
        return view;
    }

    private void openChat() {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private void fetchLastMessage() {
        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/last-messages")
                .document(uid)
                .collection("contact")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        if (documentChanges != null) {
                            for (DocumentChange doc : documentChanges) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    Contact contact = doc.getDocument().toObject(Contact.class);
                                    groupAdapter.add(new ContactItem(contact));
                                }
                            }
                        }
                    }
                });
    }

    private class ContactItem extends Item<ViewHolder> {

        private final Contact contact;

        private ContactItem(Contact contact) {
            this.contact = contact;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewNomeUsuario = viewHolder.itemView.findViewById(R.id.textViewNomeUsuario);
            TextView textViewLastMessage = viewHolder.itemView.findViewById(R.id.textViewLastMessage);
            ImageView imageViewFoto = viewHolder.itemView.findViewById(R.id.imageViewFoto);

            textViewNomeUsuario.setText(contact.getUsername());
            textViewLastMessage.setText(contact.getLastMessage());
            Picasso.get()
                    .load(contact.getPhotoUrl())
                    .into(imageViewFoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user_last_message;
        }
    }
}