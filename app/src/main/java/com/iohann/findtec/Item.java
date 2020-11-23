package com.iohann.findtec;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Item {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public Item() {
        // Required empty public constructor
    }


    private void conectaFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void verificaUsuario(final String nomeTec, String cidadeTec, final TextView txtdesc, final TextView txtEmail, final TextView txtNome, final TextView txtCidade, final TextView txtEstado,
                                final TextView txtPreco, final TextView txtNumTelefone, final TextView txtRua) {
        conectaFirebase();

        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Uploads").child("Tecnicos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (nomeTec.equals(postSnapshot.child("nome").getValue(String.class))) {
                            String nome = postSnapshot.child("nome").getValue(String.class);
                            String email = postSnapshot.child("email").getValue(String.class);
                            String cidade = postSnapshot.child("cidade").getValue(String.class);
                            String estado = postSnapshot.child("estado").getValue(String.class);
                            String rua = postSnapshot.child("rua").getValue(String.class);
                            String numero = postSnapshot.child("telefone").getValue(String.class);
                            String preco = postSnapshot.child("preco").getValue(String.class);
                            String desc = postSnapshot.child("desc").getValue(String.class);

                            txtNome.setText(nomeTec);
                            txtdesc.setText(desc);
                            txtCidade.setText(cidade);
                            txtEmail.setText(email);
                            txtEstado.setText(estado);
                            txtRua.setText(rua);
                            txtNumTelefone.setText(numero);
                            txtPreco.setText(preco);
                        }

                    }   }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}



