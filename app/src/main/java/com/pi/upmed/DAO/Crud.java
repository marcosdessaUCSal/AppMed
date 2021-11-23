package com.pi.upmed.DAO;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Esta classe contém os métodos para ler e escrever, criar e modificar tabelas no banco de dados.
// Contém também métodos de consulta que combinam tabelas, fazendo o papel das
// views no MySQL
public class Crud {


    // MÉTODOS DE ESCRITA NAS TABELAS --------------------------------------------------------------

    // Insere um paciente no BD.
    // Este método não checa se já existe um paciente com o mesmo login.
    public static void inserePaciente(SQLiteDatabase db,
                                      String loginPac,
                                      String senhaPac,
                                      String nomePac,
                                      String dataInicPac) {
        ContentValues valores = new ContentValues();
        valores.put("login_pac", loginPac);
        valores.put("senha_pac", senhaPac);
        valores.put("nome_pac", nomePac);
        valores.put("data_inic_pac", dataInicPac);
        db.insert("paciente", null, valores);
    }

    public static void insereMedico(SQLiteDatabase db,
                              String loginMed,
                              String senhaMed,
                              String nomeMed,
                              String especialidades) {
        ContentValues valores = new ContentValues();
        valores.put("login_med",loginMed);
        valores.put("senha_med",senhaMed);
        valores.put("nome_med",nomeMed);
        valores.put("especialidades",especialidades);
        db.insert("medico", null, valores);
        // FALTA VERIFICAR SE EXISTE UM MEDICO COM O MESMO LOGIN
        // TRANSFORMAR DE 'void' PARA 'BOOLEAN'

    }

    // Cria um grupo com um paciente e um médico principal, retornando falso se um desses dois
    // não constarem nos registros
    // Escreve dados nas tabelas 'grupo' e 'medico_grupo', incluindo uso de chaves estrangeiras
    public static boolean medicoCriaGrupoComPaciente(SQLiteDatabase db,
                                               String loginPac,
                                               String loginMed,
                                               String nomeGrupo,
                                               String info) {
        long idGrupo, idMed, idPac;
        Cursor cursor;
        ContentValues valores;

        // Pesquisando o _id do paciente
        cursor = db.query("paciente",
                new String[] {"_id", "login_pac"},
                "login_pac = ?",
                new String[]{loginPac},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        idPac = cursor.getInt(0);
        cursor.close();

        // Pesquisando o _id do medico
        cursor = db.query("medico",
                new String[] {"_id", "login_med"},
                "login_med = ?",
                new String[]{loginMed},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        idMed = cursor.getInt(0);
        cursor.close();

        // Registra na tabela grupo
        valores = new ContentValues();
        valores.put("id_pac", idPac);
        valores.put("nome_grupo", nomeGrupo);
        valores.put("info", info);
        idGrupo = db.insert("grupo", null, valores);

        // Registra na tabela megico_grupo
        valores = new ContentValues();
        valores.put("id_grupo", idGrupo);
        valores.put("id_med", idMed);
        valores.put("med_principal", 1);
        db.insert("medico_grupo", null, valores);

        return true;
    }

    // Insere um médico extra em um grupo já existente.
    // O app deverá ter ciência do id do grupo no momento do registro.
    public static boolean insereMedicoExtra(SQLiteDatabase db,
                                      long idGrupo,
                                      String loginMed) {
        long idMed;
        Cursor cursor;
        ContentValues valores;

        // Pesquisando o _id do medico
        cursor = db.query("medico",
                new String[] {"_id", "login_med"},
                "login_med = ?",
                new String[]{loginMed},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        idMed = cursor.getInt(0);
        cursor.close();

        // Registra na tabela megico_grupo
        valores = new ContentValues();
        valores.put("id_grupo", idGrupo);
        valores.put("id_med", idMed);
        valores.put("med_principal", 0);
        db.insert("medico_grupo", null, valores);

        return true;
    }

    // Insere uma nota de um paciente
    // O app deve ter o conhecimento prévio do id do grupo
    public static void insereNotaPaciente(SQLiteDatabase db,
                                    long idGrupo,
                                    String data,
                                    String hora,
                                    String texto) {
        ContentValues valores = new ContentValues();
        valores.put("id_grupo",idGrupo);
        valores.put("data", data);
        valores.put("hora", hora);
        valores.put("texto", texto);
        db.insert("nota_paciente", null, valores);
    }

    // Insere uma nota de um médico
    // A chamada a esse método deve acontecer em um contexto em que o id do registro na tabela
    // de médicos no grupo é conhecido. Para isso, o mesmo contexto deve conhecer ids do grupo e
    // do médico. Uma solução para isso é usar informações nos métodos views que correlacionam
    // as tebelas (com JOINs)
    public static void insereNotaMedico(SQLiteDatabase db,
                                  long idMedGrupo,
                                  String data,
                                  String hora,
                                  String texto) {
        ContentValues valores = new ContentValues();
        valores.put("id_med_grupo",idMedGrupo);
        valores.put("data", data);
        valores.put("hora", hora);
        valores.put("texto", texto);
        db.insert("nota_medico", null, valores);
    }

    // Insere um remédio
    // É necessário saber o id do registro no medico_grupo. Isso significa um médico de um grupo
    // no qual o paciente está inserido. Saber este id significa identificar um contexto em que o
    // grupo, o paciente e o médico estão identificados. Para contultas que possam levar à
    // descoberta dess id, use o método getView:
    public static void insereRemedio(SQLiteDatabase db,
                               long idMedGrupo,
                               String nomeRemedio,
                               int freqUso,
                               String dataReceita) {
        ContentValues valores = new ContentValues();
        valores.put("id_med_grupo", idMedGrupo);
        valores.put("nome_remedio", nomeRemedio);
        valores.put("freq_uso", freqUso);
        valores.put("data_receita", dataReceita);
        db.insert("remedio", null, valores);
    }

    // Registra o uso de um premédio por um paciente em um certo horário em um certo dia
    // É necessário saber o id do remédio. Isso implica saber os id's do médico, do paciente e o
    // grupo. Para consulta das chaves, há o método getView:
    public static void registraUsoRemedio(SQLiteDatabase db,
                                          long idRemedio,
                                          String hora,
                                          String data) {
        ContentValues valores = new ContentValues();
        valores.put("id_remedio", idRemedio);
        valores.put("hora_uso", hora);
        valores.put("data_uso", data);
        db.insert("uso_remedio", null, valores);

    }


    // MÉTODOS DE VIEWS DE TABELAS -----------------------------------------------------------------

    // Retorna um cursor para a view 'view_medico_receita_remedio' do meu banco MySQL original
    // Mas nessa view são mostrados apenas os dados referentes ao paciente identificado por idPac
    public static Cursor getViewMedicoReceitaRemedio (SQLiteDatabase db,
                                                      long idPac) {
        String sql;
        sql = "SELECT " +
                "paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    grupo._id AS id_grupo," +
                "    grupo.nome_grupo AS nome_grupo," +
                "    medico._id AS id_med," +
                "    medico.login_med AS login_med," +
                "    medico.nome_med AS nome_med," +
                "    remedio._id AS id_remedio," +
                "    remedio.nome_remedio AS nome_remedio," +
                "    remedio.data_receita AS data_receita," +
                "    remedio.freq_uso AS freq_uso " +
                "FROM paciente " +
                "JOIN grupo ON paciente._id = grupo.id_pac " +
                "JOIN medico_grupo ON medico_grupo.id_grupo = grupo._id " +
                "JOIN medico ON medico_grupo.id_med = medico._id " +
                "JOIN remedio ON remedio.id_med_grupo = medico_grupo._id " +
                "WHERE id_pac = ?";

        Cursor cursor = db.rawQuery(sql, new String[] {Long.toString(idPac)});

        return cursor;
    }

    // Retorna um cursor para a view 'view_notas_medico' do meu banco MySQL original
    // Mas nessa view são mostrados os dados referentes a um grupo específico
    // Lembrar que todos os médicos de um grupo podem ver os comentários uns dos outros.
    public static Cursor getViewNotasMedico(SQLiteDatabase db,
                                            long idGrupo) {
        String sql;
        sql = "SELECT" +
                "    medico._id AS id_med," +
                "    medico.login_med AS login_med," +
                "    medico.nome_med AS nome_med," +
                "    nota_medico._id AS id_nota," +
                "    nota_medico.data AS data," +
                "    nota_medico.hora AS hora," +
                "    nota_medico.texto AS texto," +
                "    paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    grupo._id AS id_grupo," +
                "    grupo.nome_grupo AS nome_grupo " +
                "FROM" +
                "    nota_medico " +
                "JOIN medico_grupo ON nota_medico.id_med_grupo =  medico_grupo._id " +
                "JOIN medico ON medico_grupo.id_med = medico._id " +
                "JOIN grupo ON medico_grupo.id_grupo = grupo._id " +
                "JOIN paciente ON grupo.id_pac = paciente._id " +
                "WHERE id_grupo = ? " +
                "ORDER BY medico._id, grupo._id";

        Cursor cursor = db.rawQuery(sql,
                new String[] {Long.toString(idGrupo)});

        return cursor;
    }

    // Retorna um cursor para a view 'notas_paciente' do meu banco MySQL original
    // TESTADA - OK - TESTADA DE NOVO -> OK
    public static Cursor getViewNotasPaciente(SQLiteDatabase db) {
        String sql;
        sql = "SELECT" +
                "    paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    grupo._id AS id_grupo," +
                "    grupo.nome_grupo AS nome_grupo," +
                "    nota_paciente._id AS id_nota," +
                "    nota_paciente.data AS data," +
                "    nota_paciente.hora AS hora," +
                "    nota_paciente.texto AS texto " +
                "FROM" +
                "    paciente " +
                "JOIN grupo ON paciente._id = grupo.id_pac " +
                "JOIN nota_paciente ON grupo._id = nota_paciente.id_grupo;";

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;
    }

    // Retorna um cursor para a view 'view_paciente_grupos' do meu banco MySQL original
    // Nesta view estão dados dos grupos aos quais um paciente está vinculado
    // É necessário conhecer o id do paciente.
    public static Cursor getViewPacienteGrupos(SQLiteDatabase db,
                                               long idPac) {
        String sql;
        sql = "SELECT" +
                "    paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    grupo._id AS id_grupo," +
                "    grupo.nome_grupo AS nome_grupo " +
                "FROM paciente " +
                "JOIN grupo ON paciente._id = grupo.id_pac " +
                "WHERE id_pac = ?";

        Cursor cursor = db.rawQuery(sql,
                new String[] {Long.toString(idPac)});

        return cursor;
    }

    // Retorna um cursor para a view 'view_paciente_medicos' do meu banco MySQL original
    // Esta view cria uma "tabela" para múltiplos tipos de consulta, onde se busca correlações
    // entre médicos, grupos e paciente, incluindo suas respectivas chaves primárias.
    // Note que a tabela 'medico_grupo' não faz parte desta pesquisa.
    public static Cursor getViewPacienteMedicos(SQLiteDatabase db) {
        String sql;
        sql = "SELECT" +
                "    paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    grupo._id AS id_grupo," +
                "    grupo.nome_grupo AS nome_grupo," +
                "    medico._id AS id_medico," +
                "    medico.login_med AS login_med," +
                "    medico.nome_med AS nome_med," +
                "    medico_grupo.med_principal AS principal " +
                "FROM paciente " +
                "JOIN grupo ON paciente._id = grupo.id_pac " +
                "JOIN medico_grupo ON grupo._id = medico_grupo.id_grupo " +
                "JOIN medico ON medico_grupo.id_med = medico._id ";

        Cursor cursor = db.rawQuery(sql, null);

        return cursor;
    }

    // Retorna um cursor para a view 'view_paciente_uso_remedio' do meu banco MySQL original.
    // Esta view lista todos os usos dos remédios de um determinado paciente, identificado pelo
    // seu id.
    public static Cursor getViewPacienteUsoRemedio(SQLiteDatabase db,
                                                   long idPac) {
        String sql;
        sql = "SELECT" +
                "    paciente._id AS id_pac," +
                "    paciente.login_pac AS login_pac," +
                "    paciente.nome_pac AS nome_pac," +
                "    remedio._id AS id_remedio," +
                "    remedio.nome_remedio AS nome_remedio," +
                "    remedio.freq_uso AS freq_uso," +
                "    uso_remedio._id AS id_uso_remedio," +
                "    uso_remedio.data_uso AS data_uso," +
                "    uso_remedio.hora_uso AS hora_uso " +
                "FROM " +
                "    paciente " +
                "JOIN grupo ON paciente._id = grupo.id_pac " +
                "JOIN medico_grupo ON grupo._id = medico_grupo._id " +
                "JOIN medico ON medico_grupo.id_med = medico._id " +
                "JOIN remedio ON medico_grupo._id = remedio._id " +
                "JOIN uso_remedio ON remedio._id = uso_remedio.id_remedio " +
                "WHERE id_pac = ?";

        Cursor cursor = db.rawQuery(sql,
                new String[] {Long.toString(idPac)});

        return cursor;
    }

    // MÉTODOS DE VERIFICAÇÃO ----------------------------------------------------------------------

    // Os métodos a seguir respondem quanto à existência de algum elemento no banco de dados que
    // corresponde a alguma chave ou campo específico em alguma tabela. Ou então, verificam dados.
    // São métodos booleanos.



    // Verifica se existe um determinado login do paciente no banco de dados
    public static boolean existePaciente(SQLiteDatabase db,
                                         String loginPac) {
        Cursor cursor = db.query("paciente",
                new String[] {"_id", "login_pac"},
                "login_pac = ?",
                new String[]{loginPac},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        return true;
    }

    // Verifica se existe um determinado login do médico no banco de dados
    public static boolean existeMedico(SQLiteDatabase db,
                                       String loginMed) {
        Cursor cursor = db.query("medico",
                new String[] {"_id", "login_med"},
                "login_med = ?",
                new String[]{loginMed},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        return true;
    }

    // Verifica a senha (digitada) de um paciente, dada sua id
    public static boolean confereSenhaPac(SQLiteDatabase db,
                                          long id,
                                          String senha) {
        String resultado;
        Cursor cursor = db.query("paciente",
                new String[] {"_id", "senha_pac"},
                "_id = ?",
                new String[]{Long.toString(id)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);

        if (resultado.equals(senha)) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    // Verifica a senha (digitada) de um médico, dada sua id
    public static boolean confereSenhaMed(SQLiteDatabase db,
                                          long id,
                                          String senha) {
        String resultado;
        Cursor cursor = db.query("medico",
                new String[] {"_id", "senha_med"},
                "_id = ?",
                new String[]{Long.toString(id)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);

        if (resultado.equals(senha)) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }



    // MÉTODOS DE PROCURA --------------------------------------------------------------------------

    //


    // Encontra o id do paciente a partir do seu login
    public static long achaIdPaciente(SQLiteDatabase db,
                                      String loginPac) {
        long resultado;
        Cursor cursor = db.query("paciente",
                new String[] {"_id", "login_pac"},
                "login_pac = ?",
                new String[]{loginPac},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado =  cursor.getLong(0);
        return resultado;
    }

    // Encontra o id do médico a partir do seu login
    public static long achaIdMedico(SQLiteDatabase db,
                                    String loginMed) {
        long resultado;
        Cursor cursor = db.query("medico",
                new String[] {"_id", "login_med"},
                "login_med = ?",
                new String[] {loginMed},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado =  cursor.getLong(0);
        return resultado;
    }

    // Retorna um string com as especialidades do médico a partir do seu id.
    // TESTADO - OK
    public static String achaEspecMed(SQLiteDatabase db,
                                      long idMed) {
        String resultado = new String();
        Cursor cursor = db.query("medico",
                new String[] {"_id", "especialidades"},
                "_id = ?",
                new String[] {Long.toString(idMed)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);
        cursor.close();
        return resultado;
    }

    // Descobre o valor da chave na tabela 'medico_grupo', dados id do grupo e do médico.
    // TESTADA - OK
    public static long achaIdTabMedicoGrupo(SQLiteDatabase db,
                                            long idGrupo,
                                            long idMed) {
        long resultado;
        Cursor cursor = db.query("medico_grupo",
                new String[] {"_id", "id_grupo", "id_med"},
                "id_grupo = ? AND id_med = ?",
                new String[] {Long.toString(idGrupo), Long.toString(idMed)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getLong(0);
        return resultado;
    }


    // Encontra o nome do médico, dado seu id.
    public static String achaNomeMedico(SQLiteDatabase db,
                                        long idMed) {
        String resultado;
        Cursor cursor = db.query("medico",
                new String[] {"_id", "nome_med"},
                "_id = ?",
                new String[] {Long.toString(idMed)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);
        cursor.close();
        return resultado;
    }




    // MÉTODOS DE DELEÇÃO --------------------------------------------------------------------------

    // Remove uma nota do paciente, identificada pela id.
    public static void apagarNotaPac(SQLiteDatabase db,
                                     long idNota) {
        db.delete("nota_paciente",
                "_id = ?",
                new String[] {Long.toString(idNota)});
    }

    // Remove uma nota de um médico, identificada pela id.
    public static void apagarNotaMed(SQLiteDatabase db,
                                     long idNota) {
        db.delete("nota_medico",
                "_id = ?",
                new String[] {Long.toString(idNota)});
    }


    // MÉTODOS DE PESQUISAS SIMPLES ----------------------------------------------------------------

    // Retorna a info de um grupo, dado seu id
    public static String getInfoGrupo(SQLiteDatabase db,
                                      long idGrupo) {
        String resultado = new String();
        Cursor cursor = db.query("grupo",
                new String[] {"_id", "info"},
                "_id = ?",
                new String[] {Long.toString(idGrupo)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);
        return resultado;
    }

}
