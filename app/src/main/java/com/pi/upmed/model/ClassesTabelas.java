package com.pi.upmed.model;

// Essa classe e suas classes aninhadas são projetadas para
// refletir exatamente os possíveis conteúdos nos bancos de dados,
// incluindo as chaves primárias e estrangeiras

public class ClassesTabelas {

    public static class Paciente {
        long id;
        String login, senha, nome, dataInic;

        public Paciente(long id, String login, String senha, String nome, String dataInic) {
            this.id = id;
            this.login = login;
            this.nome = nome;
            this.dataInic = dataInic;
        }
    }

    public static class Medico {
        long id;
        String login, senha, nome, especialidades;

        public Medico(long id, String login, String senha, String nome, String especialidades) {
            this.id = id;
            this.login = login;
            this.senha = senha;
            this.nome = nome;
            this.especialidades = especialidades;
        }
    }

    public static class Grupo {
        long id, idPac;
        String nome, info;

        public Grupo(long id, long idPac, String nome, String info) {
            this.id = id;
            this.idPac = idPac;
            this.nome = nome;
            this.info = info;
        }
    }

    public static class MedicoGrupo {
        long id, idGrupo, idMed;
        boolean medPrincipal;

        public MedicoGrupo(long id, long idGrupo, long idMed, boolean medPrincipal) {
            this.id = id;
            this.idGrupo = idGrupo;
            this.idMed = idMed;
            this.medPrincipal = medPrincipal;
        }
    }

    public static class NotaMedico {
        long id, idMedGrupo;
        String data, hora, texto;

        public NotaMedico(long id, long idMedGrupo, String data, String hora, String texto) {
            this.id = id;
            this.idMedGrupo = idMedGrupo;
            this.data = data;
            this.hora = hora;
            this.texto = texto;
        }
    }

    public static class NotaPaciente {
        long id, idGrupo;
        String data, hora, texto;

        public NotaPaciente(long id, long idGrupo, String data, String hora, String texto) {
            this.id = id;
            this.idGrupo = idGrupo;
            this.data = data;
            this.hora = hora;
            this.texto = texto;
        }
    }

    public static class Remedio {
        long id, idMedGrupo;
        String nome;
        int freqUso;
        String dataReceita;

        public Remedio(long id, long idMedGrupo, String nome, int freqUso, String dataReceita) {
            this.id = id;
            this.idMedGrupo = idMedGrupo;
            this.nome = nome;
            this.freqUso = freqUso;
            this.dataReceita = dataReceita;
        }
    }

    public static class UsoRemedio {
        long id, idRemedio;
        String hora, data;

        public UsoRemedio(long id, long idRemedio, String hora, String data) {
            this.id = id;
            this.idRemedio = idRemedio;
            this.hora = hora;
            this.data = data;
        }
    }
}
