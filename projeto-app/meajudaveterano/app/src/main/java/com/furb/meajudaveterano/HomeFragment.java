package com.furb.meajudaveterano;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
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

public class HomeFragment extends Fragment {

    private GroupAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        fetchUsers();

        RecyclerView rv = view.findViewById(R.id.recycler);
        groupAdapter = new GroupAdapter();
        rv.setAdapter(groupAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        groupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);

                UserItem userItem = (UserItem) item;
                intent.putExtra("usuario", userItem.usuario);

                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchUsers() {
        FirebaseFirestore.getInstance().collection("/usuario")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("HOME_FRAGMENT", e.getMessage(), e);
                            return;
                        }

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Usuario usuario = document.toObject(Usuario.class);
                            groupAdapter.add(new UserItem(usuario));
                        }
                    }
                });

    }

    private class UserItem extends Item<ViewHolder> {

        private final Usuario usuario;

        private UserItem(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewNomeUsuario = viewHolder.itemView.findViewById(R.id.textViewNomeUsuario);
            ImageView imageViewFoto = viewHolder.itemView.findViewById(R.id.imageViewFoto);

            textViewNomeUsuario.setText(usuario.getNome());

            Picasso.get()
                    .load(usuario.getProfileUrl())
                    .into(imageViewFoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user;
        }

    }
}