package com.iohann.findtec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class frag_adocao extends Fragment {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mdatabaseRef;
    private ValueEventListener mDBListener;
    private ProgressBar mProgressBar;
    private List<TecnicoUser> mUploads;
    private FirebaseStorage mStorage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_frag_adocao, container, false);


        mRecyclerView = view.findViewById(R.id.mRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressBar = view.findViewById(R.id.progressCircle);
        mProgressBar.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(getActivity(), mUploads);
        mRecyclerView.setAdapter(mAdapter);

        mdatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads").child("Tecnicos");
        mDBListener = mdatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if(postSnapshot.exists()){
                        TecnicoUser a = postSnapshot.getValue(TecnicoUser.class);
                        a.setMkey(postSnapshot.getKey());
                        reverseDashboard(mUploads);
                        mUploads.add(a);
                        mAdapter.notifyDataSetChanged();
                        reverseDashboard(mUploads);
                    }
                }

                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alert(databaseError.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TecnicoUser tec = mUploads.get(position);
                String[] tecnicoDados = {tec.getNome(), tec.getPreco(), tec.getmImageUrl(), tec.getCidade(), tec.getMkey()};
                openDetailActivity(tecnicoDados);
            }
        });

        return view;
    }
    private static void reverseDashboard(List list){
        Collections.reverse(list);
    }

    private void alert (String a){
        Toast.makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mdatabaseRef.removeEventListener(mDBListener);
    }

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(getActivity(), Detalhe.class);
        intent.putExtra("CIDADE_KEY", data[0]);
        intent.putExtra("NOME_KEY", data[1]);
        intent.putExtra("IMAGE_KEY", data[2]);
        startActivity(intent);
    }
}

