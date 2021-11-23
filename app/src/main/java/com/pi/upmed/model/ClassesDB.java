package com.pi.upmed.model;


// Esta classe contém classes aninhadas projetadas para conter dados de registros
// tabelas do banco de dados ou parte delas.
// Estas classes internas precisam ser estáticas para poderem ser instanciadas de forma
// independente da classe externa (ClassesDB), que neste caso serve apenas como um "container"

public class ClassesDB {

    // Corresponde a UM ÚNICO registro na view NotasPaciente da classe 'Crud'
    // A definição dos atributos fica a cargo dos métodos set
    public static class NotaPaciente {

        private long idPac;
        private String loginPac;
        private String nomePac;
        private long idGrupo;
        private String nomeGrupo;
        private long idNota;
        private String data;
        private String hora;
        private String texto;

        // GETTERS ---------------------------------------------------------------------------------

        public long getIdPac() {
            return this.idPac;
        }

        public String getLoginPac() {
            return this.loginPac;
        }

        public String getNomePac() {
            return this.nomePac;
        }

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }

        public long getIdNota() {
            return this.idNota;
        }

        public String getData() {
            return this.data;
        }

        public String getHora() {
            return this.hora;
        }

        public String getTexto() {
            return this.texto;
        }

        // SETTERS ---------------------------------------------------------------------------------

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setLoginPac(String loginPac) {
            this.loginPac = loginPac;
        }

        public void setNomePac(String nomePac) {
            this.nomePac = nomePac;
        }

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }

        public void setIdNota(long idNota) {
            this.idNota = idNota;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }
    }


    // Corresponde ao registro na View NotasMedico da classe 'Crud'
    // A definição dos atributos fica a cargo dos métodos ser
    // Lembrar que as notas dos médicos não são visíveis aos pacientes
    public static class NotaMedico {
        private long idMed;
        private String loginMed;
        private String nomeMed;
        private long idNota;
        private String data;
        private String hora;
        private String texto;
        private long idPac;
        private String loginPac;
        private String nomePac;
        private long idGrupo;
        private String nomeGrupo;


        // SETTERS

        public void setIdMed(long idMed) {
            this.idMed = idMed;
        }

        public void setLoginMed(String loginMed) {
            this.loginMed = loginMed;
        }

        public void setNomeMed(String nomeMed) {
            this.nomeMed = nomeMed;
        }

        public void setIdNota(long idNota) {
            this.idNota = idNota;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setLoginPac(String loginPac) {
            this.loginPac = loginPac;
        }

        public void setNomePac(String nomePac) {
            this.nomePac = nomePac;
        }

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }


        // GETTERS

        public long getIdMed() {
            return this.idMed;
        }

        public String getLoginMed() {
            return this.loginMed;
        }

        public String getNomeMed() {
            return this.nomeMed;
        }

        public long getIdNota() {
            return this.idNota;
        }

        public String getData() {
            return this.data;
        }

        public String getHora() {
            return this.hora;
        }

        public String getTexto() {
            return this.texto;
        }

        public long getIdPac() {
            return this.idPac;
        }

        public String getLoginPac() {
            return this.loginPac;
        }

        public String getNomePac() {
            return this.nomePac;
        }

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }
    }


    // Armazena registro da tabela 'grupo'
    // Seus objetos instanciados podem servir para usuário médico ou paciente
    public static class Grupo {
        private long idGrupo;
        private long idPac;
        private String nomeGrupo;
        private String info;

        // SETTERS

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        // GETTERS

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public long getIdPac() {
            return this.idPac;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }

        public String getInfo() {
            return this.info;
        }
    }


    // Armazena informações a respeito de algum médico em determinado grupo
    public static class MedicoNoGrupo {
        private long idMed;
        private String loginMed;
        private String nomeMed;
        private int principal;
        private long idGrupo;
        private String nomeGrupo;


        // SETTERS

        public void setIdMed(long idMed) {
            this.idMed = idMed;
        }

        public void setLoginMed(String loginMed) {
            this.loginMed = loginMed;
        }

        public void setNomeMed(String nomeMed) {
            this.nomeMed = nomeMed;
        }

        public void setPrincipal(int principal) {
            this.principal = principal;
        }

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }

        // GETTERS

        public long getIdMed() {
            return this.idMed;
        }

        public String getLoginMed() {
            return this.loginMed;
        }

        public String getNomeMed() {
            return this.nomeMed;
        }

        public int isPrincipal() {
            return this.principal;
        }

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }
    }


    // Corresponde UM ÚNICO registro na view MedicoReceitaRemedio da classe Crud
    // Armazena informações sobre todos os remédios associados a um determinado paciente.
    // Guarda informações de paciente, médico, remédio, grupo, data da receita e freq de uso
    public static class ViewMedicoReceitaRemedio {
        private long idPac;
        private String loginPac;
        private String nomePac;
        private long idGrupo;
        private String nomeGrupo;
        private long idMed;
        private String loginMed;
        private String nomeMed;
        private long idRemedio;
        private String nomeRemedio;
        private String dataReceita;
        private int freqUso;


        // SETTERS

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setLoginPac(String loginPac) {
            this.loginPac = loginPac;
        }

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }

        public void setLoginMed(String loginMed) {
            this.loginMed = loginMed;
        }

        public void setNomeMed(String nomeMed) {
            this.nomeMed = nomeMed;
        }

        public void setIdRemedio(long idRemedio) {
            this.idRemedio = idRemedio;
        }

        public void setNomeRemedio(String nomeRemedio) {
            this.nomeRemedio = nomeRemedio;
        }

        public void setDataReceita(String dataReceita) {
            this.dataReceita = dataReceita;
        }

        public void setFreqUso(int freqUso) {
            this.freqUso = freqUso;
        }

        public void setNomePac(String nomePac) {
            this.nomePac = nomePac;
        }

        public void setIdMed(long idMed) {
            this.idMed = idMed;
        }

        // GETTERS

        public long getIdPac() {
            return this.idPac;
        }

        public String getLoginPac() {
            return this.loginPac;
        }

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }

        public String getLoginMed() {
            return this.loginMed;
        }

        public String getNomeMed() {
            return this.nomeMed;
        }

        public long getIdRemedio() {
            return this.idRemedio;
        }

        public String getNomeRemedio() {
            return this.nomeRemedio;
        }

        public String getDataReceita() {
            return this.dataReceita;
        }

        public int getFreqUso() {
            return this.freqUso;
        }

        public String getNomePac() {
            return this.nomePac;
        }

        public long getIdMed() {
            return this.idMed;
        }
    }


    // Corresponde a UM ÚNICO registro na view PacienteUsoRemedio da classe Crud
    // Armazena informações de todos os usos dos remédios de um determinado paciente, identificado
    // pelo seu id.
    public static class ViewPacienteUsoRemedio {
        private long idPac;
        private String loginPac;
        private String nomePac;
        private long idRemedio;
        private String nomeRemedio;
        private int freqUso;
        private long idUsoRemedio;
        private String dataUso;
        private String horaUso;


        // SETTERS

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setLoginPac(String loginPac) {
            this.loginPac = loginPac;
        }

        public void setNomePac(String nomePac) {
            this.nomePac = nomePac;
        }

        public void setIdRemedio(long idRemedio) {
            this.idRemedio = idRemedio;
        }

        public void setNomeRemedio(String nomeRemedio) {
            this.nomeRemedio = nomeRemedio;
        }

        public void setFreqUso(int freqUso) {
            this.freqUso = freqUso;
        }

        public void setIdUsoRemedio(long idUsoRemedio) {
            this.idUsoRemedio = idUsoRemedio;
        }

        public void setDataUso(String dataUso) {
            this.dataUso = dataUso;
        }

        public void setHoraUso(String horaUso) {
            this.horaUso = horaUso;
        }


        // GETTERS

        public long getIdPac() {
            return this.idPac;
        }

        public String getLoginPac() {
            return this.loginPac;
        }

        public String getNomePac() {
            return this.nomePac;
        }

        public long getIdRemedio() {
            return this.idRemedio;
        }

        public String getNomeRemedio() {
            return this.nomeRemedio;
        }

        public int getFreqUso() {
            return this.freqUso;
        }

        public long getIdUsoRemedio() {
            return this.idUsoRemedio;
        }

        public String getDataUso() {
            return this.dataUso;
        }

        public String getHoraUso() {
            return this.horaUso;
        }
    }


    // Armazena UM ÚNICO registro da tabela 'uso_remedio'
    public static class UsoRemedio {
        private long idUso;
        private long idRemedio;
        private String horaUso;
        private String dataUso;


        // SETTERS

        public void setIdUso(long idUso) {
            this.idUso = idUso;
        }

        public void setIdRemedio(long idRemedio) {
            this.idRemedio = idRemedio;
        }

        public void setHoraUso(String horaUso) {
            this.horaUso = horaUso;
        }

        public void setDataUso(String dataUso) {
            this.dataUso = dataUso;
        }


        // GETTERS

        public long getIdUso() {
            return this.idUso;
        }

        public long getIdRemedio() {
            return this.idRemedio;
        }

        public String getHoraUso() {
            return this.horaUso;
        }

        public String getDataUso() {
            return this.dataUso;
        }
    }


    // Corresponde a UM ÚNICO registro na view PacienteMedicos
    public static class ViewPacienteMedicos{
        private long idPac;
        private String loginPac;
        private String nomePac;
        private long idGrupo;
        private String nomeGrupo;
        private long idMedico;
        private String loginMed;
        private String nomeMed;
        private int principal;


        // SETTERS

        public void setIdPac(long idPac) {
            this.idPac = idPac;
        }

        public void setLoginPac(String loginPac) {
            this.loginPac = loginPac;
        }

        public void setNomePac(String nomePac) {
            this.nomePac = nomePac;
        }

        public void setIdGrupo(long idGrupo) {
            this.idGrupo = idGrupo;
        }

        public void setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
        }

        public void setIdMedico(long idMedico) {
            this.idMedico = idMedico;
        }

        public void setLoginMed(String loginMed) {
            this.loginMed = loginMed;
        }

        public void setNomeMed(String nomeMed) {
            this.nomeMed = nomeMed;
        }

        public void setPrincipal(int principal) {
            this.principal = principal;
        }


        // GETTERS


        public long getIdPac() {
            return this.idPac;
        }

        public String getLoginPac() {
            return this.loginPac;
        }

        public String getNomePac() {
            return this.nomePac;
        }

        public long getIdGrupo() {
            return this.idGrupo;
        }

        public String getNomeGrupo() {
            return this.nomeGrupo;
        }

        public long getIdMedico() {
            return this.idMedico;
        }

        public String getLoginMed() {
            return this.loginMed;
        }

        public String getNomeMed() {
            return this.nomeMed;
        }

        public int getPrincipal() {
            return this.principal;
        }
    }

}
