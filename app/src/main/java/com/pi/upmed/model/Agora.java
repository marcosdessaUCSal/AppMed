package com.pi.upmed.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Esta classe tem dois métodos estáticos:
// getData() -> retorna string com data atual, tipo 30/03/2020
// getHora() -> retorna string com hora e minutos, tipo 20:35
// IMPORTANTE: Para executar isto, a API mínima é 26
public class Agora {

    // Obtém a data/hora atual
    static LocalDateTime agora = LocalDateTime.now();

    static DateTimeFormatter formatadorData =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static DateTimeFormatter formatadorHora =
            DateTimeFormatter.ofPattern("HH:mm");

    static String hora = formatadorHora.format(agora);
    static String data = formatadorData.format(agora);

    public static String getData() {
        return data;
    }

    public static String getHora() {
        return hora;
    }
}
