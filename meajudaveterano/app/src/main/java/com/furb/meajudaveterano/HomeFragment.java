package com.furb.meajudaveterano;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.furb.meajudaveterano.chat.ChatActivity;
import com.furb.meajudaveterano.user.Usuario;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ArrayList<Usuario> listaUsuarios;
    private GroupAdapter groupAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fetchUsers();

        RecyclerView rv = view.findViewById(R.id.recycler);

        groupAdapter = new GroupAdapter();
        groupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);

                UserItem userItem = (UserItem) item;
                intent.putExtra("usuario", userItem.usuario);

                startActivity(intent);
            }
        });

        rv.setAdapter(groupAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    if (newText.toLowerCase().length() == 0) {
                        fetchUsers();
                    }

                    if (newText.toLowerCase().length() < 2) {
                        groupAdapter.clear();
                        return true;
                    }

                    findUsers(newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void fetchUsers() {
        listaUsuarios = new ArrayList<Usuario>();
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
                            listaUsuarios.add(usuario);
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

    private void findUsers(String query){
        groupAdapter.clear();
        for (Usuario usuario: listaUsuarios) {
            if (usuario.getNome().toLowerCase().contains(query.toLowerCase()))
                groupAdapter.add(new UserItem(usuario));
        }

    }
}