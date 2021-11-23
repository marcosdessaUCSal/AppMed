package com.pi.upmed.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoConfigHelperMed extends SQLiteOpenHelper {

    private static final String DB_NAME = "configmed";
    private static final int DB_VERSION = 1;


    public BancoConfigHelperMed(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criandoBanco(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void criandoBanco (SQLiteDatabase db) {

        // Cria a tabela 'medico'
        // Esta tabela contém apenas um registro por vez, correspondente ao usuário logado
        db.execSQL("CREATE TABLE medico (" +
                "    _id                INTEGER," +
                "    login_med          TEXT," +
                "    senha_med          TEXT," +
                "    id_grupo           INTEGER," +
                "    nome_grupo         TEXT" +
                ");");
    }
}
