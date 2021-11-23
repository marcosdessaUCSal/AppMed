package com.pi.upmed.persist_estado;

/*  Esta classe armazena o atual estado do app
 *  É um singleton, portanto só é instanciada uma única vez
 *  A variável do contexto é necessária, porque indica a activity onde o banco foi aberto
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pi.upmed.DAO.BancoConfigHelperMed;
import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.DAO.Crud;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.model.EstruturaNotasMed;
import com.pi.upmed.model.EstruturaNotasPac;

import java.util.ArrayList;

public class EstadoAtualMed {

    // Para correspondência em 'BancoConfigHelperMed' - variáveis básicas de estado
    private long idMedAtual;
    private String nomeMedAtual;
    private String loginMedAtual;
    private String senhaMedAtual;
    private String infoGrupoAtual;

    // Estas variáveis guardam id e nome do grupo atual selecionado pelo médico
    private long idGrupoAtual;
    private String nomeGrupoAtual;

    // Esta variável armazena o índice de uma escolha no spinner. Simplesmente um inteiro!
    private int escolhaNoSpinnerGrupos;

    // Esta variável serve para (temporariamente) guardar a posição, dentro da EstruturaNotasPac,
    // da nota selecionada pelo usuário no uso do fragment NotasPacFragment
    private int positionNotaEscolhida;




    private long idGrupo;
    private String nomeGrupo;
    private String infoGrupo;

    // Esta variável indica se o comando de inicialização já foi chamado. Ao se criar uma
    // referência, se 'ligado' for true, a inicialização não deve ser feita.
    //private boolean inicializado = false;

    private boolean logado;



    // Para classes aninhadas de 'ClassesDB'
    private ClassesDB.ViewPacienteMedicos pacMed;
    private ClassesDB.NotaPaciente notaPaciente;
    private ClassesDB.NotaMedico notaMedico;
    private ClassesDB.MedicoNoGrupo medicoNoGrupo;


    // Estruturas de dados
    private EstruturaNotasPac pacoteNotasPac = new EstruturaNotasPac();
    private EstruturaNotasMed pacoteNotasMed = new EstruturaNotasMed();
    private ArrayList<ClassesDB.ViewPacienteMedicos> listaPacMed;
    private ArrayList<ClassesDB.MedicoNoGrupo> listaMedicosNoGrupo;




    // Verifica o banco para atualizar o status de logado ou não logado
    // Tabela com conteúdo -> logado
    // Tabela sem conteúdo -> não logado
    public void inicializar(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperMed(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Esta instrução garante que a inicialização só ocorre uma única vez, mesmo que seja
        // requerida outras vezes
        /*
        if (inicializado) {
            return;
        } else {
            inicializado = true;
        }

         */

        Cursor cursor = db.query("medico", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            // Se existem dados na tabela, carregam-se os dados. Usuário inicia logado
            this.logado = true;
            this.idMedAtual = cursor.getLong(0);
            this.loginMedAtual = cursor.getString(1);
            this.senhaMedAtual = cursor.getString(2);
            this.idGrupoAtual = cursor.getLong(3);
            this.nomeGrupoAtual = cursor.getString(4);
            // Encontra o nome do médico
            SQLiteOpenHelper h = new BancoGeralHelper(context);
            SQLiteDatabase d = h.getReadableDatabase();
            this.nomeMedAtual = Crud.achaNomeMedico(d, idMedAtual);
            d.close();
        } else {
            // Se não há dados, valores default. Usuário não inicia logado
            // Valor -1 indica ausência de registro
            this.logado = false;
            this.idMedAtual = -1;
            this.loginMedAtual = null;
            this.senhaMedAtual = null;
            this.idGrupoAtual = -1;
            this.nomeGrupoAtual = null;
        }
        cursor.close();
        db.close();
    }

    private void limparTabela(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperMed(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("medico", null, null);
        db.close();
    }

    private void salvarEstado(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperMed(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        limparTabela(context);
        ContentValues valores = new ContentValues();
        valores.put("_id", idMedAtual);
        valores.put("login_med", loginMedAtual);
        valores.put("senha_med", senhaMedAtual);
        valores.put("id_grupo", idGrupoAtual);
        valores.put("nome_grupo", nomeGrupoAtual);
        db.insert("medico", null, valores);
        db.close();
    }






    // ----- GETTERS -------------------------------------------------------------------------------


    // Cria um array list contendo os grupos vinculados ao médico
    // TESTADA - OK
    public ArrayList<ClassesDB.ViewPacienteMedicos> getListaPacMed(Context context, long idMed) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        listaPacMed = new ArrayList<>();
        Cursor cursor = Crud.getViewPacienteMedicos(db);
        while (cursor.moveToNext()) {
            if (cursor.getLong(5) == idMed) {
                pacMed = new ClassesDB.ViewPacienteMedicos();
                pacMed.setIdPac(cursor.getLong(0));
                pacMed.setLoginPac(cursor.getString(1));
                pacMed.setNomePac(cursor.getString(2));
                pacMed.setIdGrupo(cursor.getLong(3));
                pacMed.setNomeGrupo(cursor.getString(4));
                pacMed.setIdMedico(idMed);
                pacMed.setLoginMed(cursor.getString(6));
                pacMed.setNomeMed(cursor.getString(7));
                pacMed.setPrincipal(cursor.getInt(8));
                listaPacMed.add(pacMed);
            }
        }
        cursor.close();
        db.close();
        return this.listaPacMed;
    }





    public boolean estaLogado() {
        return this.logado;
    }

    public String getLogin() {
        return this.loginMedAtual;
    }

    public Long getIdGrupoAtual() {
        return this.idGrupoAtual;
    }

    public long getIdMedAtual() {
        return this.idMedAtual;
    }

    public int getEscolhaNoSpinnerGrupos() {
        return escolhaNoSpinnerGrupos;
    }

    // Retorna o info de algum grupo, dado seu id
    public String getGrupoInfo(Context context,
                               long idGrupo) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        this.infoGrupo = Crud.getInfoGrupo(db, idGrupo);
        return infoGrupo;
    }


    // Preenche um objeto 'EstruturaNotasPac' com todas as notas que o paciente digitou
    // em UM ÚNICO GRUPO. Notas em outros grupos não são consideradas.
    public EstruturaNotasPac preencheNotasPac(Context context,
                                              long idGrupo) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = Crud.getViewNotasPaciente(db);
        pacoteNotasPac.limpaRegistros();
        while (cursor.moveToNext()) {
            if (cursor.getLong(3) == idGrupo) {
                notaPaciente = new ClassesDB.NotaPaciente();
                notaPaciente.setIdPac(cursor.getLong(0));
                notaPaciente.setLoginPac(cursor.getString(1));
                notaPaciente.setNomePac(cursor.getString(2));
                notaPaciente.setIdGrupo(this.idGrupo);
                notaPaciente.setNomeGrupo(cursor.getString(4));
                notaPaciente.setIdNota(cursor.getLong(5));
                notaPaciente.setData(cursor.getString(6));
                notaPaciente.setHora(cursor.getString(7));
                notaPaciente.setTexto(cursor.getString(8));
                pacoteNotasPac.addNotaPac(notaPaciente);
            }
        }
        cursor.close();
        db.close();
        return this.pacoteNotasPac;
    }


    // Preenche um objeto 'EstruturaNotasMed' com todas as notas que o paciente digitou
    // em UM ÚNICO GRUPO. Notas em outros grupos não são consideradas.
    public EstruturaNotasMed preencheNotasMed(Context context,
                                              long idGrupo) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = Crud.getViewNotasMedico(db, idGrupo);
        pacoteNotasMed.limpaRegistros();
        while (cursor.moveToNext()) {
            notaMedico = new ClassesDB.NotaMedico();
            notaMedico.setIdMed(cursor.getLong(0));
            notaMedico.setLoginMed(cursor.getString(1));
            notaMedico.setNomeMed(cursor.getString(2));
            notaMedico.setIdNota(cursor.getLong(3));
            notaMedico.setData(cursor.getString(4));
            notaMedico.setHora(cursor.getString(5));
            notaMedico.setTexto(cursor.getString(6));
            notaMedico.setIdPac(cursor.getLong(7));
            notaMedico.setLoginPac(cursor.getString(8));
            notaMedico.setNomePac(cursor.getString(9));
            notaMedico.setIdGrupo(cursor.getLong(10));
            notaMedico.setNomeGrupo(cursor.getString(11));
            pacoteNotasMed.addNotaMed(notaMedico);
        }
        cursor.close();
        db.close();
        return this.pacoteNotasMed;
    }


    // Constrói uma lista contendo todos os médicos que estão presentes no grupo mencionado
    // A lista é constituída de objetos da classe ClassesDB.MedicoNoGrupo
    // TESTADA - OK
    public ArrayList<ClassesDB.MedicoNoGrupo> getListaMedicosNoGrupo(
            Context context,
            long idGrupo) {

        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = Crud.getViewPacienteMedicos(db);
        listaMedicosNoGrupo = new ArrayList<>();

        while (cursor.moveToNext()) {
            if (cursor.getLong(3) == idGrupo) {
                medicoNoGrupo = new ClassesDB.MedicoNoGrupo();
                this.medicoNoGrupo.setIdMed(cursor.getLong(5));
                this.medicoNoGrupo.setLoginMed(cursor.getString(6));
                this.medicoNoGrupo.setNomeMed(cursor.getString(7));
                this.medicoNoGrupo.setPrincipal(cursor.getInt(8));
                this.medicoNoGrupo.setIdGrupo(idGrupo);
                this.medicoNoGrupo.setNomeGrupo(cursor.getString(4));
                listaMedicosNoGrupo.add(medicoNoGrupo);
            }
        }
        cursor.close();
        db.close();
        return this.listaMedicosNoGrupo;
    }


    // Retorna informações sobre as especialidades de um determinado médico (identificado por id)
    // TESTADO - OK
    public String getEspecialidadeMedico(
            Context context,
            long idMed) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String especialidades = Crud.achaEspecMed(db, idMed);
        db.close();
        return especialidades;
    }


    // Retorna uma nota de um médico (índice 'position'), uma vez criada a lista 'pacoteNotasMed'
    // É IMPORTANTE QUE O MÉTODO 'preencheNotasMed' TENHA SIDO EXECUTADO ANTES.
    // NÃO DEIXAR DE VERIFICAR ISSO EM HIPÓTESE ALGUMA
    public ClassesDB.NotaMedico getNotaMedico(int position) {
        return pacoteNotasMed.getElemento(position);
    }


    // Retorna o nome do paciente associado ao grupo atual.
    // É IMPORTANTE QUE O MÉTODO 'preencheNotasMed' TENHA SIDO EXECUTADO ANTES.
    // NÃO DEIXAR DE VERIFICAR ISSO EM HIPÓTESE ALGUMA
    public String getNomePacienteDoGrupoAtual() {
        return pacoteNotasMed.getElemento(positionNotaEscolhida).getNomePac();
    }

    // Retorna o nome do grupo atual
    public String getNomeGrupoAtual() {
        return nomeGrupoAtual;
    }

    // Retorna o nome do médico atual
    public String getNomeMedAtual() {
        return this.nomeMedAtual;
    }






    // ---- SETTERS --------------------------------------------------------------------------------

    // Define o estado (atributos usuário - logado)
    public void fazLogin(Context context,
                         long idMed,
                         String loginMed,
                         String senhaMed,
                         boolean lembrar) {
        this.idMedAtual = idMed;
        this.loginMedAtual = loginMed;
        this.senhaMedAtual = senhaMed;
        this.logado = true;
        this.idGrupoAtual = -1; // Ao logar, não há grupo predefinido

        if (lembrar) {
            salvarEstado(context);
            inicializar(context);

        } else {
            limparTabela(context);
            inicializar(context);
        }

    }


    // Faz logoff do usuário.
    // Não é necessário definir idGrupoAtual = -1, porque já é inicializado assim no login
    public void setLogoff(Context context) {
        limparTabela(context);
        this.loginMedAtual = null;
        this.senhaMedAtual = null;
        this.logado = false;
        this.nomeMedAtual = null;

        limparTabela(context);
    }

    // Seleciona um grupo ativo (memoriza seleção), atualizando a tabela paciente
    // São memorizados o id e o nome do grupo selecionado
    // ===== IMPLEMENTAR O REGISTRO NA TABELA ===================
    public void selecionaGrupo(Context context,
                               long idGrupo,
                               String nomeGrupo) {
        this.idGrupoAtual = idGrupo;
        this.nomeGrupoAtual = nomeGrupo;

        // IMPLEMENTAR COMANDOS QUE ESCREVAM NO BANCO DE DADOS

    }

    public void setEscolhaNoSpinnerGrupos(int escolha) {
        this.escolhaNoSpinnerGrupos = escolha;
    }

    public void setPositionNotaEscolhida(int position) {
        this.positionNotaEscolhida = position;
    }


    // Registra uma nova nota de um médico no banco de dados
    // IMPORTANTE: GARANTIR QUE UM GRUPO ESTEJA ATUALMENTE SELECIONADO
    // PRECISA SER TESTADA
    public void inserirNovaNotaMed(Context context,
                                long idGrupo,
                                String data,
                                String hora,
                                String texto) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // Para descobrir o id na tabela 'medico_grupo'
        long idMedicoGrupo = Crud.achaIdTabMedicoGrupo(db,
                idGrupo,
                idMedAtual);

        Crud.insereNotaMedico(db,
                idMedicoGrupo,
                data,
                hora,
                texto);
        db.close();
    }




    // --- MÉTODOS DE DELEÇÃO ----------------------------------------------------------------------



    // Apaga a nota selecionada pelo médico
    // TESTADA - OK
    public void apagarNotaMed(Context context,
                              long idNota) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Crud.apagarNotaMed(db, idNota);
        db.close();
    }













    //----------------------------------------------------------------------------------------------
    // PARTE ESTRUTURAL DO SINGLETON - NÃO ALTERE
    //----------------------------------------------------------------------------------------------

    public static EstadoAtualMed getInstance() {
        return instanciaUnica;
    }

    private static final EstadoAtualMed instanciaUnica = new EstadoAtualMed(); // NÃO ALTERE ISTO!

    private EstadoAtualMed() {
    }
}
