package com.pi.upmed.DAO;

import android.database.sqlite.SQLiteDatabase;

import com.pi.upmed.DAO.Crud;


// Esta classe é responsável por popular o banco de dados


public class PopBanco {

    public static void inicializarBancoGeral(SQLiteDatabase db) {

            // INSERINDO PACIENTES ---------------------------------------------------------------------
            Crud.inserePaciente(db,
                    "einstein",
                    "123",
                    "Albert Einstein",
                    "26/10/2021");

            Crud.inserePaciente(db,
                    "freud",
                    "123",
                    "Sigmund Freud",
                    "26/10/2021");

            Crud.inserePaciente(db,
                    "newton",
                    "123",
                    "Isaac Newton",
                    "26/10/2021");

            Crud.inserePaciente(db,
                    "feynman",
                    "123",
                    "Richard Feynman",
                    "26/10/2021");

            Crud.inserePaciente(db,
                    "galileo",
                    "123",
                    "Galileo Galilei",
                    "26/3/2020");

            // INSERINDO MÉDICOS -----------------------------------------------------------------------
            Crud.insereMedico(db,
                    "zeus",
                    "123",
                    "Dr. Zeus Olímpio",
                    "Psiquiatria e Neurologia");

            Crud.insereMedico(db,
                    "odim",
                    "123",
                    "Dr. Odim Asgardiano",
                    "Psicologia e Psicanálise");

            Crud.insereMedico(db,
                    "hades",
                    "123",
                    "Dr. Hades",
                    "Ortopedia e Cardiologia");


            // MEDICOS CRIANDO GRUPOS COM PACIENTES ----------------------------------------------------
            Crud.medicoCriaGrupoComPaciente(db,
                    "einstein",
                    "hades",
                    "Grupo do Cabeção",
                    "O Sr. Einstein sofre de cefaleia");

            Crud.medicoCriaGrupoComPaciente(db,
                    "einstein",
                    "zeus",
                    "Grupo Esperto",
                    "Este homem é esperto pra caramba.");

            Crud.medicoCriaGrupoComPaciente(db,
                    "newton",
                    "zeus",
                    "Grupo Gravitacional",
                    "O caso do paciente é da maior gravidade");

            // INSERINDO MÉDICO EXTRA EM UM GRUPO ------------------------------------------------------
            Crud.insereMedicoExtra(db,
                    1,
                    "odim");

            // INSERINDO NOTA DE UM PACIENTE -----------------------------------------------------------
            Crud.insereNotaPaciente(db,
                    1,
                    "1/11/2021",
                    "14:00",
                    "Estou com dor de cabeça");

            Crud.insereNotaPaciente(db,
                    1,
                    "2/11/2021",
                    "09:26",
                    "Estou enjoado");

            Crud.insereNotaPaciente(db,
                    1,
                    "2/11/2021",
                    "14:45",
                    "Estou com dor de barriga");

            //

            Crud.insereNotaPaciente(db,
                    1,
                    "3/11/2021",
                    "14:55",
                    "Eu prefiro ser essa metamorfose ambulante " +
                            "do que ter aquela velha opinião formada sobre tudo.");

            Crud.insereNotaPaciente(db,
                    1,
                    "3/11/2021",
                    "09:35",
                    "Quero dizer o oposto do que disse antes.");

            Crud.insereNotaPaciente(db,
                    1,
                    "4/11/2021",
                    "14:47",
                    "Eu é que não me sento no trono de um apartamento\n" +
                            "Com a boca escancarada, cheia de dentes\n" +
                            "Esperando a morte chegar");

            Crud.insereNotaPaciente(db,
                    1,
                    "5/11/2021",
                    "09:45",
                    "Eu devia estar feliz pelo Senhor\n" +
                            "Ter me concedido o domingo\n" +
                            "Pra ir com a família no jardim zoológico dar pipocas aos macacos");

            Crud.insereNotaPaciente(db,
                    1,
                    "6/11/2021",
                    "14:45",
                    "Eu sou a mosca que pousou em sua sopa");

            Crud.insereNotaPaciente(db,
                    1,
                    "7/11/2021",
                    "09:45",
                    "Eu sou a mosca que pintou pra lhe abusar");


            // INSERINDO NOTA DE UM MÉDICO -------------------------------------------------------------
            Crud.insereNotaMedico(db,
                    1,
                    "25/10/2021",
                    "13:00",
                    "O paciente está diabético");

            Crud.insereNotaMedico(db,
                    1,
                    "27/10/2021",
                    "13:00",
                    "O paciente tem dor de barriga crônica");

            Crud.insereNotaMedico(db,
                    1,
                    "26/10/2021",
                    "14:25",
                    "O cara tá ficando doido");


            // INSERINDO UM REMEDIO --------------------------------------------------------------------
            Crud.insereRemedio(db,
                    1,
                    "Rivotril",
                    2,
                    "25/10/2021");

            // REGISTRANDO UM REMÉDIO ------------------------------------------------------------------
            Crud.registraUsoRemedio(db,
                    1,
                    "21:00",
                    "26/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "9:15",
                    "27/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "9:02",
                    "28/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "21:12",
                    "28/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "9:22",
                    "29/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "21:00",
                    "29/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "9:07",
                    "30/10/2021");

            Crud.registraUsoRemedio(db,
                    1,
                    "21:16",
                    "30/10/2021");

    }

}
