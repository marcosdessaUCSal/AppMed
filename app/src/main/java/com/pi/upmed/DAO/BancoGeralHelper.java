package com.pi.upmed.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoGeralHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bancodedadosgeral";
    private static final int DB_VERSION = 1;

    public BancoGeralHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criandoBanco(db);
        PopBanco.inicializarBancoGeral(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void criandoBanco (SQLiteDatabase db) {

        // Cria a tabela medico
        db.execSQL("CREATE TABLE medico (" +
                "    _id            INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    login_med      TEXT    NOT NULL," +
                "    senha_med      TEXT    NOT NULL," +
                "    nome_med       TEXT    NOT NULL," +
                "    especialidades TEXT    NOT NULL" +
                ");");

        // Cria a tabela paciente
        db.execSQL("CREATE TABLE paciente (" +
                "    _id           INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    login_pac     TEXT    NOT NULL," +
                "    senha_pac     TEXT    NOT NULL," +
                "    nome_pac      TEXT    NOT NULL," +
                "    data_inic_pac TEXT    NOT NULL" +
                ");");

        // Cria a tabela grupo
        db.execSQL("CREATE TABLE grupo (" +
                "    _id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_pac     INTEGER NOT NULL," +
                "    nome_grupo TEXT    NOT NULL," +
                "    info       TEXT," +
                "    CONSTRAINT fk_grupo_paciente FOREIGN KEY (" +
                "        id_pac" +
                "    )" +
                "    REFERENCES paciente (_id)" +
                ");");

        // Cria a tabela medico_grupo
        db.execSQL("CREATE TABLE medico_grupo (" +
                "    _id           INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_grupo      INTEGER NOT NULL," +
                "    id_med        INTEGER NOT NULL," +
                "    med_principal INTEGER NOT NULL," +
                "    CONSTRAINT fk_medico_grupo_grupo FOREIGN KEY (" +
                "        id_grupo" +
                "    )" +
                "    REFERENCES grupo (_id)," +
                "    CONSTRAINT fk_medico_grupo_medico FOREIGN KEY (" +
                "        id_med" +
                "    )" +
                "    REFERENCES medico (_id)" +
                ");");

        // Cria a tabela nota_medico
        db.execSQL("CREATE TABLE nota_medico (" +
                "    _id          INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_med_grupo INTEGER NOT NULL," +
                "    data         TEXT    NOT NULL," +
                "    hora         TEXT    NOT NULL," +
                "    texto        TEXT    NOT NULL," +
                "    CONSTRAINT fk_nota_medico_medico_grupo FOREIGN KEY (" +
                "        id_med_grupo" +
                "    )" +
                "    REFERENCES medico_grupo (_id)" +
                ");");

        // Cria a tabela nota_paciente
        db.execSQL("CREATE TABLE nota_paciente (" +
                "    _id      INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_grupo INTEGER NOT NULL," +
                "    data     TEXT    NOT NULL," +
                "    hora     TEXT    NOT NULL," +
                "    texto    TEXT    NOT NULL," +
                "    CONSTRAINT fk_nota_paciente_grupo FOREIGN KEY (" +
                "        id_grupo" +
                "    )" +
                "    REFERENCES grupo (_id)" +
                ");");

        // Cria tabela remedio
        db.execSQL("CREATE TABLE remedio (" +
                "    _id          INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_med_grupo INTEGER NOT NULL," +
                "    nome_remedio TEXT    NOT NULL," +
                "    freq_uso     INTEGER NOT NULL," +
                "    data_receita TEXT    NOT NULL," +
                "    CONSTRAINT fk_remedio_medico_grupo FOREIGN KEY (" +
                "        id_med_grupo" +
                "    )" +
                "    REFERENCES medico_grupo (_id)" +
                ");");

        // Cria tabela uso_remedio
        db.execSQL("CREATE TABLE uso_remedio (" +
                "    _id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    id_remedio INTEGER NOT NULL," +
                "    hora_uso   TEXT    NOT NULL," +
                "    data_uso   TEXT    NOT NULL," +
                "    CONSTRAINT fk_uso_remedio_remedio FOREIGN KEY (" +
                "        id_remedio" +
                "    )" +
                "    REFERENCES remedio (_id)" +
                ");");
    }
}
