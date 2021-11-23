package com.pi.upmed.view_med;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pi.upmed.R;
import com.pi.upmed.persist_estado.EstadoAtualMed;


public class WelcomeFragmentMed extends Fragment {

    public WelcomeFragmentMed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_welcome_med, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Obtendo referência ao estado
        EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

        // Obtendo referência ao text view do fragmento welcome
        TextView textViewWelcome = (TextView) getActivity().findViewById(R.id.texto_welcome);

        // Para construir o texto a ser apresentado no fragmento welcome, conforme
        // o usuário estando logado ou não
        StringBuilder texto = new StringBuilder();

        if (estadoAtualMed.estaLogado()) {
            texto.append("Bem vindo. Você está logado como ");
            texto.append(estadoAtualMed.getLogin() + "!");
            textViewWelcome.setText(texto);

        } else {
            texto.append("Bem vindo. Você não está logado.\n");
            texto.append("Você pode logar-se clicando no menu à esquerda.");
            textViewWelcome.setText(texto);

        }

    }
}
