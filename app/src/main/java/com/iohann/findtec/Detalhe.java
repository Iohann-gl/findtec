package com.iohann.findtec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detalhe extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageButton voltar;
    private String nomeTec, cidadeTec, telefoneTec;
    private Button btnliga, enviarEmail, reportar;


    public Detalhe() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        enviarEmail = (Button) findViewById(R.id.btnEmail);
        btnliga = (Button) findViewById(R.id.btnLigr);
        voltar = (ImageButton) findViewById(R.id.btnvoltar);
        reportar = (Button) findViewById(R.id.btnReportar);

        voltarClick();

        Intent i = getIntent();
        nomeTec = i.getExtras().getString("CIDADE_KEY");
        cidadeTec = i.getExtras().getString("NOME_KEY");
        telefoneTec = i.getExtras().getString("TELEFONE_KEY");

        btnliga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectaFirebase();

                databaseReference=FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Uploads").child("Tecnicos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (nomeTec.equals(postSnapshot.child("nome").getValue(String.class))) {
                                    String tel = postSnapshot.child("telefone").getValue(String.class);
                                    Uri uri = Uri.parse("tel:" + tel);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                    System.out.println(uri);

                                    startActivity(intent);
                                }}}}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        enviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectaFirebase();
                databaseReference=FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Uploads").child("Tecnicos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (nomeTec.equals(postSnapshot.child("nome").getValue(String.class))) {
                                    String email = postSnapshot.child("email").getValue(String.class);
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
                                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Assistência Técnica");
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                }}}}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectaFirebase();
                databaseReference=FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Uploads").child("Tecnicos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (nomeTec.equals(postSnapshot.child("nome").getValue(String.class))) {
                                    String email = "find.tec02@gmail.com";

                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:" + "find.tec02@gmail.com")); // only email apps should handle this
                                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                }}}}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        conectaFirebase();
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail); //Mapeando o componente textview
        TextView txtNome = (TextView) findViewById(R.id.txtNome); //Mapeando o componente textview
        TextView txtRua= (TextView) findViewById(R.id.txtEndereco); //Mapeando o componente textview
        TextView txtNumTelefone = (TextView) findViewById(R.id.txtNumero); //Mapeando o componente textview
        TextView txtPreco = (TextView) findViewById(R.id.txtPreco); //Mapeando o componente textview
        TextView txtEstado = (TextView) findViewById(R.id.txtEstado); //Mapeando o componente textview
        TextView txtCidade = (TextView) findViewById(R.id.txtCidade); //Mapeando o componente textview
        TextView txtDesc = (TextView) findViewById(R.id.txtDesc); //Mapeando o componente textview


        Item item = new Item();
        item.verificaUsuario(nomeTec, cidadeTec, txtDesc, txtEmail, txtNome, txtCidade, txtEstado, txtPreco, txtNumTelefone, txtRua);
    }

    private void voltarClick() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detalhe.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void conectaFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}