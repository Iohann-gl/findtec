package com.iohann.findtec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private AbasAdapter mAbasAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicilizarComponentes();
    }

    private void inicilizarComponentes() {
        mAbasAdapter = new AbasAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        AbasAdapter adapter = new AbasAdapter(getSupportFragmentManager());
        adapter.addFragment(new frag_adocao(), "Técnicos");
        viewPager.setAdapter(adapter);
    }


    public void abrirCadastro(View view) {
        Intent intentChat = new Intent(MainActivity.this,
                CadastroTecnico.class);
        startActivity(intentChat);
    }
}