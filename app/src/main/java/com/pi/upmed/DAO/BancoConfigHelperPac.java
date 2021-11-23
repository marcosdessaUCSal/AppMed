package com.pi.upmed.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoConfigHelperPac extends SQLiteOpenHelper {

    private static final String DB_NAME = "configpac";
    private static final int DB_VERSION = 1;

    public BancoConfigHelperPac(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criandoBanco(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void criandoBanco(SQLiteDatabase db) {

        // Cria a tabela `paciente`
        // Esta tabela contém apenas um registro por vez, correspondente ao usuário logado
        // se 'id_grupo' valer -1, o paciente não tem nenhum grupo selecionado
        db.execSQL("CREATE TABLE paciente (" +
                "    _id           INTEGER," +
                "    login_pac     TEXT," +
                "    senha_pac     TEXT," +
                "    id_grupo      INTEGER," +
                "    nome_grupo    TEXT" +
                ");");

    }
}
