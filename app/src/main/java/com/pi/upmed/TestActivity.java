package com.pi.upmed;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.model.EstruturaNotasPac;
import com.pi.upmed.persist_estado.EstadoAtualPac;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        StringBuilder texto = new StringBuilder();
        texto.append("O que resultou da consulta:\n\n");

        // Referenciando o banco de dados
        SQLiteOpenHelper bancoGeral = new BancoGeralHelper(this);
        SQLiteDatabase db = bancoGeral.getReadableDatabase();


        /*
        Cursor cursor = Crud.getViewNotasPaciente(db);
        while (cursor.moveToNext()) {

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                texto.append(cursor.getString(i) + "*");
            }
            texto.append("\n");

        }
        cursor.close();*/

        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();
        estadoAtualPac.inicializar(this);
        EstruturaNotasPac notas = estadoAtualPac.preencheNotas(this);



        int i = notas.getTamanho();
        texto.append("Numero de elementos: " + i + "\n\n");



        for (i = 0; i < notas.getTamanho(); i++) {
            texto.append(i +  "\n" + notas.getElemento(i).getNomePac() + "\n");
            texto.append(notas.getElemento(i).getLoginPac() + "\n");
            texto.append(notas.getElemento(i).getTexto() + "\n");
        }








        TextView tela = findViewById(R.id.tela);
        tela.setText(texto.toString());

    }
}
