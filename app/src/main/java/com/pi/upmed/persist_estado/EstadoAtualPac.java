package com.pi.upmed.persist_estado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pi.upmed.DAO.BancoConfigHelperPac;
import com.pi.upmed.DAO.BancoGeralHelper;
import com.pi.upmed.DAO.Crud;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.model.EstruturaNotasPac;

import java.util.ArrayList;


/*  Esta classe armazena o atual estado do app
 *  É um singleton, portanto só é instanciada uma única vez
 *  A variável do contexto é necessária, porque indica a activity onde o banco foi aberto
 */

public class EstadoAtualPac {
    private long idPac;
    private long idGrupo;
    private long idNota;

    // Esta variável indica se o comando de inicialização já foi chamado. Ao se criar uma
    // referência, se 'ligado' for true, a inicialização não deve ser feita.
    //private boolean inicializado = false;

    // Esta variável guarda a id do grupo que foi selecionado pelo paciente
    private long idGrupoAtual;

    private String nomeGrupoAtual;

    // Esta variável armazena o índice de uma escolha no spinner. Simplesmente um inteiro!
    private int escolhaNoSpinnerGrupos;

    // Esta variável serve para (temporariamente) guardar a posição, dentro da EstruturaNotasPac,
    // da nota selecionada pelo usuário no uso do fragment NotasPacFragment
    private int positionNotaEscolhida;

    // Esta variável serve apenas para (temporariamente) memorizar o remédio selecionado
    // Se -1, nenhum remédio foi selecionado ainda
    private int remedioSelecionado;

    private String nomePac;
    private String nomeGrupo;
    private String infoGrupo;
    private String login;
    private String senha;
    private String dataNota;
    private String horaNota;
    private String texto;
    private boolean logado;

    // Para classes aninhadas de 'ClassesDB'
    private ClassesDB.Grupo grupo = new ClassesDB.Grupo();
    private ClassesDB.NotaPaciente notaPaciente = new ClassesDB.NotaPaciente();
    private ClassesDB.MedicoNoGrupo medicoNoGrupo;
    private ClassesDB.ViewMedicoReceitaRemedio remedio;
    private ClassesDB.UsoRemedio usoRemedio;
    private ClassesDB.ViewPacienteUsoRemedio viewPacienteUsoRemedio;

    // Estruturas de dados
    private ArrayList<ClassesDB.Grupo> listaGrupos = new ArrayList<>();
    private ArrayList<ClassesDB.MedicoNoGrupo> listaMedicosNoGrupo = new ArrayList<>();
    private ArrayList<ClassesDB.ViewMedicoReceitaRemedio> listaRemedios = new ArrayList<>();
    private ArrayList<ClassesDB.ViewPacienteUsoRemedio> listaUsos;
    private EstruturaNotasPac pacoteNotas = new EstruturaNotasPac();

    // Verifica o banco para atualizar o status de logado ou não logado
    // Tabela com conteúdo -> logado
    // Tabela sem conteúdo -> não logado
    public void inicializar(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperPac(context);
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

        Cursor cursor = db.query("paciente",null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            // Se existem dados na tabela, carregam-se os dados. Usuário inicia logado
            this.logado = true;
            this.idPac = cursor.getLong(0);
            this.login = cursor.getString(1);
            this.senha = cursor.getString(2);
            this.idGrupoAtual = cursor.getLong(3);
            this.nomeGrupoAtual = cursor.getString(4);
        } else {
            // Se não há dados, valores default. Usuário não inicia logado
            this.logado = false;
            this.idPac = -1;
            this.login = null;
            this.senha = null;
            this.idGrupoAtual = -1;
            this.nomeGrupoAtual = null;
        }

        cursor.close();
        db.close();
    }

    private void limparTabela(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperPac(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("paciente", null, null);
        db.close();
    }

    private void salvarEstado(Context context) {
        SQLiteOpenHelper helper = new BancoConfigHelperPac(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        limparTabela(context);
        ContentValues valores = new ContentValues();
        valores.put("_id", idPac);
        valores.put("login_pac", login);
        valores.put("senha_pac", senha);
        valores.put("id_grupo", idGrupoAtual);
        db.insert("paciente", null, valores);
        db.close();
    }


    // ----- GETTERS -------------------------------------------------------------------------------

    // Cria um array list contendo os grupos vinculados ao paciente
    // Os dados são retirados do banco de dados.
    // Estes dados não ficam armazenados nas variáveis de estado, são recuperados sempre que
    // este método é chamado. Isto é importante, porque os grupos podem ser acrescentados ou
    // deletados.
    public ArrayList<ClassesDB.Grupo> getGrupos(Context context) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        this.listaGrupos.clear();
        Cursor cursor = db.query("grupo",
                null,
                "id_pac = ?",
                new String[] {Long.toString(this.idPac)},
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            if (cursor.getLong(1) == idPac) {
                this.idGrupo = cursor.getLong(0);
                this.nomeGrupo = cursor.getString(2);
                this.infoGrupo = cursor.getString(3);
                this.grupo = new ClassesDB.Grupo();
                this.grupo.setIdGrupo(this.idGrupo);
                this.grupo.setIdPac(this.idPac);
                this.grupo.setNomeGrupo(this.nomeGrupo);
                this.grupo.setInfo(this.infoGrupo);
                this.listaGrupos.add(this.grupo);
            }
        }
        cursor.close();
        db.close();
        return this.listaGrupos;
    }

    // Preenche um objeto 'EstruturaNotasPac' com todas as notas que o paciente (id definido aqui)
    // tem registrado no banco de dados
    // OBS: É necessário criar uma nova instância para 'notaPaciente'. Caso contrário, todas
    // as notas da lista irão referenciar o último objeto criado.
    public EstruturaNotasPac preencheNotas(Context context) {

        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = Crud.getViewNotasPaciente(db);
        pacoteNotas.limpaRegistros();
        while (cursor.moveToNext()) {
            if (cursor.getLong(0) == idPac) {
                this.login = cursor.getString(1);
                this.nomePac = cursor.getString(2);
                this.idGrupo = cursor.getLong(3);
                this.nomeGrupo = cursor.getString(4);
                this.idNota = cursor.getLong(5);
                this.dataNota = cursor.getString(6);
                this.horaNota = cursor.getString(7);
                this.texto = cursor.getString(8);
                this.notaPaciente = new ClassesDB.NotaPaciente(); // Isto aqui é muito importante
                notaPaciente.setIdPac(this.idPac);
                notaPaciente.setLoginPac(this.login);
                notaPaciente.setNomePac(this.nomePac);
                notaPaciente.setIdGrupo(this.idGrupo);
                notaPaciente.setNomeGrupo(this.nomeGrupo);
                notaPaciente.setIdNota(this.idNota);
                notaPaciente.setData(this.dataNota);
                notaPaciente.setHora(this.horaNota);
                notaPaciente.setTexto(this.texto);
                pacoteNotas.addNotaPac(notaPaciente);
            }
        }
        cursor.close();
        db.close();
        return this.pacoteNotas;
    }

    public String getLogin() {
        return this.login;
    }

    // Retorna uma nota de um paciente (índice 'position'), uma vez criada a lista 'pacoteNotas'
    // É IMPORTANTE QUE O MÉTODO 'preencheNotas' TENHA SIDO EXECUTADO ANTES.
    // NÃO DEIXAR DE VERIFICAR ISSO EM HIPÓTESE ALGUMA
    public ClassesDB.NotaPaciente getNotaPaciente(int position) {

        return pacoteNotas.getElemento(position);
    }

    // Retorna o nome do paciente
    public String getNomePac(Context context) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String resultado;

        Cursor cursor = db.query("paciente",
                new String[] {"_id", "nome_pac"},
                "_id = ?",
                new String[] {Long.toString(idPac)},
                null,
                null,
                null);
        cursor.moveToFirst();
        resultado = cursor.getString(1);
        cursor.close();
        db.close();
        return resultado;
    }

    public String getSenha() {
        return this.senha;
    }

    public boolean estaLogado() {
        return this.logado;
    }

    public int getPositionNotaEscolhida() {
        return this.positionNotaEscolhida;
    }

    public long getIdGrupoAtual() {
        return this.idGrupoAtual;
    }

    public long getIdPac() {
        return this.idPac;
    }

    public int getEscolhaNoSpinnerGrupos() {
        return escolhaNoSpinnerGrupos;
    }

    public int getRemedioSelecionado() {
        return this.remedioSelecionado;
    }


    // Constrói uma lista contendo todos os médicos que estão presentes no grupo mencionado
    // A lista é constituída de objetos da classe ClassesDB.MedicoNoGrupo
    // TESTADO - OK
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

    // Constrói uma lista contendo todos os remédios vinculados a um determinado paciente
    // A lista é constituída de objetos da classe ClassesDB.MedicoReceitaRemedio
    // Note que o método da classe Crud só retorna dados referentes ao paciente selecionado (id)
    // TESTADA - OK
    public ArrayList<ClassesDB.ViewMedicoReceitaRemedio> preencheRemedios(
            Context context,
            long idPac) {

        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = Crud.getViewMedicoReceitaRemedio(db, idPac);
        listaRemedios = new ArrayList<>();

        while (cursor.moveToNext()) {
            remedio = new ClassesDB.ViewMedicoReceitaRemedio();
            this.remedio.setIdPac(cursor.getLong(0));
            this.remedio.setLoginPac(cursor.getString(1));
            this.remedio.setNomePac(cursor.getString(2));
            this.remedio.setIdGrupo(cursor.getLong(3));
            this.remedio.setNomeGrupo(cursor.getString(4));
            this.remedio.setIdMed(cursor.getLong(5));
            this.remedio.setLoginMed(cursor.getString(6));
            this.remedio.setNomeMed(cursor.getString(7));
            this.remedio.setIdRemedio(cursor.getInt(8));
            this.remedio.setNomeRemedio(cursor.getString(9));
            this.remedio.setDataReceita(cursor.getString(10));
            this.remedio.setFreqUso(cursor.getInt(11));
            listaRemedios.add(remedio);
        }
        cursor.close();
        db.close();
        return this.listaRemedios;
    }


    // Constrói uma lista contendo todos os registros de uso de um determinado remédio
    // (identificado pelo id) de um paciente
    // A lista é constituída de objetos da classe ClassesDB.ViewPacienteUsoRemedio
    // TESTADA -> OK
    public ArrayList<ClassesDB.ViewPacienteUsoRemedio> preencheUsosRemedio(
            Context context,
            long idRemedio) {

        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = Crud.getViewPacienteUsoRemedio(db, this.idPac);
        this.listaUsos = new ArrayList<>();

        while (cursor.moveToNext()) {
            if (cursor.getLong(3) == this.idPac) {
                this.viewPacienteUsoRemedio = new ClassesDB.ViewPacienteUsoRemedio();
                this.viewPacienteUsoRemedio.setIdPac(this.idPac);
                this.viewPacienteUsoRemedio.setLoginPac(cursor.getString(1));
                this.viewPacienteUsoRemedio.setNomePac(cursor.getString(2));
                this.viewPacienteUsoRemedio.setIdRemedio(cursor.getLong(3));
                this.viewPacienteUsoRemedio.setNomeRemedio(cursor.getString(4));
                this.viewPacienteUsoRemedio.setFreqUso(cursor.getInt(5));
                this.viewPacienteUsoRemedio.setIdUsoRemedio(cursor.getLong(6));
                this.viewPacienteUsoRemedio.setDataUso(cursor.getString(7));
                this.viewPacienteUsoRemedio.setHoraUso(cursor.getString(8));
                this.listaUsos.add(this.viewPacienteUsoRemedio);
            }
        }
        cursor.close();
        db.close();
        return this.listaUsos;
    }





    // Retorna o nome do grupo atualmente selecionado pelo paciente
    public String getNomeGrupoAtual() {
        return this.nomeGrupoAtual;
    }



    

    // ---- SETTERS --------------------------------------------------------------------------------

    // Define o estado (atributos usuário - logado)
    public void fazLogin(Context context,
                         long id,
                         String login,
                         String senha,
                         boolean lembrar) {
        this.idPac = id;
        this.login = login;
        this.senha = senha;
        this.logado = true;
        this.idGrupoAtual = -1;

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
        this.login = null;
        this.senha = null;
        this.logado = false;
        limparTabela(context);
    }

    // Seleciona um grupo ativo (memoriza seleção), atualizando a tabela paciente
    // São memorizados o id e o nome do grupo selecionado
    // ===== IMPLEMENTAR O REGISTRO NA TABELA ===================
    public void selecionaGrupo(Context context,
                               long idGrupo,
                               String nomeGrupoAtual) {

        this.idGrupoAtual = idGrupo;
        this.nomeGrupoAtual = nomeGrupoAtual;


        // IMPLEMENTAR COMANDOS QUE ESCREVAM NO BANCO DE DADOS

    }

    public void setRemedioSelecionado(int valor) {
        this.remedioSelecionado = valor;
    }


    // IDENTIFICAR O USO DESTA VARIÁVEL
    public void setPositionNotaEscolhida(int position) {
        this.positionNotaEscolhida = position;
    }


    // Esta variável armazena o índice de uma escolha no spinner.
    public void setEscolhaNoSpinnerGrupos(int escolha) {
        this.escolhaNoSpinnerGrupos = escolha;
    }

    // Registra uma nova nota no banco de dados
    // IMPORTANTE: GARANTIR QUE UM GRUPO ESTEJA ATUALMENTE SELECIONADO
    // TESTADA - OK
    public void inserirNovaNota(Context context,
                                long idGrupo,
                                String data,
                                String hora,
                                String texto) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        Crud.insereNotaPaciente(db,
                idGrupo,
                data,
                hora,
                texto);
        db.close();
    }


    // Insere um registro de uso de um remédio por um paciente
    // TESTADA - OK
    public void registraUsoRemedio(Context context,
                                    long idRemedio,
                                    String data,
                                    String hora) {

        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Crud.registraUsoRemedio(db,
                idRemedio,
                hora,
                data);

        db.close();

    }



    // --- MÉTODOS DE DELEÇÃO ----------------------------------------------------------------------



    // Apaga a nota selecionada pelo paciente
    // TESTADA - OK
    public void apagarNota(Context context,
                           long idNota) {
        SQLiteOpenHelper helper = new BancoGeralHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Crud.apagarNotaPac(db, idNota);
        db.close();
    }



    //----------------------------------------------------------------------------------------------
    // PARTE ESTRUTURAL DO SINGLETON - NÃO ALTERE
    //----------------------------------------------------------------------------------------------

    public static EstadoAtualPac getInstance() {
        return instanciaUnica;
    }

    private static final EstadoAtualPac instanciaUnica = new EstadoAtualPac(); // NÃO ALTERE ISTO!

    private EstadoAtualPac() {
    }
}
