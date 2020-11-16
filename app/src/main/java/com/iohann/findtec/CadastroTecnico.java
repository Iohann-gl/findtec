package com.iohann.findtec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CadastroTecnico extends AppCompatActivity {

    private EditText cidade, rua, preco, estado, telefone, nome, email, desc;
    private Button cadastrar;
    private LinearLayout adicionaFoto;
    private ImageButton voltar;
    private ImageView addFoto;
    private ProgressBar mProgressCircle;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference storageReference;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private int img = R.drawable.ic_vazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tecnico);
        inicializaComponentes();
        eventosClicks();

    }

    public void  eventosClicks(){
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroTecnico.this, MainActivity.class);
                startActivity(intent);
            }
        });

        adicionaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoto.setEnabled(false);
                abrirGaleria();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressCircle.setVisibility(View.VISIBLE);
                final String edtCidade = cidade.getText().toString().trim();
                final String edtRua = rua.getText().toString().trim();
                final String edtEstado = estado.getText().toString().trim();
                final String edtNome = nome.getText().toString().trim();
                final String edtTelefone = telefone.getText().toString().trim();
                final String edtPreco = preco.getText().toString().trim();
                final String edtEmail = email.getText().toString().trim();
                final String edtDesc = desc.getText().toString().trim();
                addFoto.setEnabled(false);
                cidade.setEnabled(false);
                desc.setEnabled(false);
                nome.setEnabled(false);
                rua.setEnabled(false);
                estado.setEnabled(false);
                preco.setEnabled(false);
                email.setEnabled(false);
                telefone.setEnabled(false);
                cadastrar.setEnabled(false);
                if (edtDesc.equals("") || edtEmail.equals("") || edtCidade.equals("") || edtNome.equals("") || edtRua.equals("") || edtEstado.equals("")||edtPreco.equals("")
                        || edtTelefone.equals("") || mImageUri == null){
                    alert("Preencha todos os campos");
                    addFoto.setEnabled(true);
                    preco.setEnabled(true);
                    cidade.setEnabled(true);
                    estado.setEnabled(true);
                    rua.setEnabled(true);
                    desc.setEnabled(true);
                    telefone.setEnabled(true);
                    nome.setEnabled(true);
                    email.setEnabled(true);
                    cadastrar.setEnabled(true);
                    mProgressCircle.setVisibility(View.INVISIBLE);

                } else {

                    final String eventId = databaseReference.push().getKey();
                    final StorageReference fileReference = storageReference.child(eventId + "." + getFileExtension(mImageUri));
                    fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            TecnicoUser tec = new TecnicoUser();
                            tec.setmImageUrl(downloadUrl.toString());
                            tec.setCidade(edtCidade);
                            tec.setPreco(edtPreco);
                            tec.setNome(edtNome);
                            tec.setDesc(edtDesc);
                            tec.setEmail(edtEmail);
                            tec.setEstado(edtEstado);
                            tec.setTelefone(edtTelefone);
                            tec.setRua(edtRua);
                            databaseReference.child("Uploads").child("Tecnicos").child(eventId).setValue(tec);
                            mProgressCircle.setVisibility(View.INVISIBLE);
                            alert("Cadastrado com sucesso");
                            resetCampos();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alert(e.getMessage());
                        }
                    });
                }
            }
        });
    }
    public void resetCampos(){
        addFoto.setEnabled(true);
        cidade.setEnabled(true);
        rua.setEnabled(true);
        estado.setEnabled(true);
        preco.setEnabled(true);
        nome.setEnabled(true);
        email.setEnabled(true);
        telefone.setEnabled(true);
        cadastrar.setEnabled(true);
        desc.setEnabled(true);
        cidade.setText("");
        desc.setText("");
        rua.setText("");
        estado.setText("");
        preco.setText("");
        nome.setText("");
        email.setText("");
        telefone.setText("");
        Picasso.with(CadastroTecnico.this).load(img).into(addFoto);
        adicionaFoto.setVisibility(View.VISIBLE);

    }

    public void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void inicializaComponentes(){
        desc = (EditText) findViewById(R.id.edtDescricao);
        nome = (EditText) findViewById(R.id.edtNome);
        email = (EditText) findViewById(R.id.edtEmail);
        cidade = (EditText) findViewById(R.id.edtCidade);
        rua = (EditText) findViewById(R.id.edtRua);
        preco = (EditText) findViewById(R.id.edtPreco);
        estado = (EditText) findViewById(R.id.edtUF);
        telefone = (EditText) findViewById(R.id.edtTelefone);
        cadastrar = (Button) findViewById(R.id.btnCadTelaCad);
        addFoto = (ImageView) findViewById(R.id.addImagemAnimal);
        voltar = (ImageButton) findViewById(R.id.btnvoltar);
        adicionaFoto = (LinearLayout) findViewById(R.id.adicionaFoto);
        mProgressCircle = (ProgressBar) findViewById(R.id.progressCircle);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
    }
    private void alert (String a){
        Toast.makeText(CadastroTecnico.this, a, Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = (CadastroTecnico.this).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            adicionaFoto.setVisibility(View.INVISIBLE);
            Picasso.with(CadastroTecnico.this).load(mImageUri)
                    .fit()
                    .centerCrop()
                    .into(addFoto);
        }
        addFoto.setEnabled(true);
    }
}