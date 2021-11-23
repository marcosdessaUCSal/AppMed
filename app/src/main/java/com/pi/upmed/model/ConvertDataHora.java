package com.pi.upmed.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConvertDataHora {

    private String dataHora;
    private Date dt;

    private Calendar cal = Calendar.getInstance();

    private String  dia,
            mes,
            ano,
            h,
            min;

    // 'formato' define a maneira como a data e a hora são escritas
    // É importante que o argumento de getDataHora esteja escrito
    // estritamente dessa maneira, caso contrário, o retorno será null.
    private SimpleDateFormat formatorDataHora =
            new SimpleDateFormat ("dd/MM/yyyy hh:mm");

    // Método privado ------
    // Salva em 'dt' a data, lida a partir da string dataHora contendo
    // a data e a hora, a serem convertidas
    // dt é a variável tipo Date
    // 'formato' define como data e hora devem ser escritos
    // 'dataHora' é a string que contém a data e a hora
    // Se houver erro calcData retorna false
    private boolean calcData() {
        try {

            this.dt = formatorDataHora.parse(dataHora);
            return true;

        } catch (ParseException e) {
            return false;
        }
    }

    // Recebe data (dd/MM/yyy) e hora (hh:mm) e repassa esses valores
    // para calcData(), que escreve em 'dt' (Data) a data-hora correspondente
    public Date getDataHora(String data, String hora) {

        this.dataHora = data + " " + hora;

        // Usa o médoto calcData() para esrever em 'dt' a hora-data
        // Se calcData() retornar falso, este método devolve null
        if (!calcData()) {
            return null;
        }
        return this.dt;
    }

    // Recebe como parâmetro uma data-hora (Date)
    // e responde a data em uma string, como 25/11/1998
    public String getData(Date dt) {
        try {
            cal.setTime(dt);
            dia = "" + cal.get(Calendar.DAY_OF_MONTH);
            mes = "" + (cal.get(Calendar.MONTH) + 1);
            ano = "" + cal.get(Calendar.YEAR);
            return dia + "/" + mes + "/" + ano;

        } catch (Exception e) {
            return null;
        }
    }

    // Recebe como parâmetro uma data-hora (Date)
    // e responde a hora e os minutos, como em 16:25
    public String getHora(Date dt) {
        try {
            cal.setTime(dt);
            h = "" + cal.get(Calendar.HOUR_OF_DAY);
            min = "" + cal.get(Calendar.MINUTE);
            return h + ":" + min;

        } catch (Exception e) {
            return null;
        }
    }
}
