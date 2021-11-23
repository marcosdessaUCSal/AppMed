package com.pi.upmed.view_pac;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pi.upmed.R;
import com.pi.upmed.persist_estado.EstadoAtualPac;


public class WelcomeFragmentPac extends Fragment {

    public WelcomeFragmentPac() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        // Obtendo referência ao estado
        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Obtendo referência ao text view do fragmento welcome
        TextView textViewWelcome = (TextView) getActivity().findViewById(R.id.texto_welcome);

        // Para construir o texto a ser apresentado no fragmento welcome, conforme
        // o usuário estando logado ou não
        StringBuilder texto = new StringBuilder();

        if (estadoAtualPac.estaLogado()) {
            texto.append("Bem vindo. Você está logado como ");
            texto.append(estadoAtualPac.getLogin() + "!");
            textViewWelcome.setText(texto);

        } else {
            texto.append("Bem vindo. Você não está logado.\n");
            texto.append("Você pode logar-se clicando no menu à esquerda.");
            textViewWelcome.setText(texto);

        }
    }


}
