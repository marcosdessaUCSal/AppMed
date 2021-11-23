package com.pi.upmed.view_pac;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pi.upmed.R;
import com.pi.upmed.model.Agora;
import com.pi.upmed.persist_estado.EstadoAtualPac;
import com.pi.upmed.view_pac.NotasPacFragment;


public class NovaNotaPacFragment extends Fragment {

    public NovaNotaPacFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nova_nota_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Obtendo referência ao estado
        final EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Obtendo data e hora atuais
        // Também, registrando essas informações provisoriamente no cartão mostrado na tela
        final String data = new String(Agora.getData());
        final String hora = new String(Agora.getHora());
        TextView textViewDataHora = getActivity().findViewById(R.id.novanota_pac_datahora);
        TextView textViewNomeLogin = getActivity().findViewById(R.id.novanota_pac_nomelogin);
        textViewDataHora.setText(data + ", " + hora);
        textViewNomeLogin.setText(estadoAtualPac.getNomePac(getContext()) + " ("
                + estadoAtualPac.getLogin() + ")" + "\n"
                + "Grupo: " +estadoAtualPac.getNomeGrupoAtual());

        // Obtendo referência aos botões 'cancelar' e 'enviar'
        Button btnCancelar = getActivity().findViewById(R.id.btn_nova_msg_pac_cancelar);
        Button btnEnviar = getActivity().findViewById(R.id.btn_nova_msg_pac_enviar);

        // Reagindo ao botão 'cancelar' -> voltar à lista de notas
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retorna para o fragmento de notas
                Fragment fragment = new NotasPacFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });

        // Reagindo ao botão 'enviar'
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Captura o texto da nova mensagem digitada
                EditText editText = getActivity().findViewById(R.id.edittext_nova_msg_pac);
                String texto = editText.getText().toString();

                estadoAtualPac.inserirNovaNota(getContext(),
                        estadoAtualPac.getIdGrupoAtual(),
                        data,
                        hora,
                        texto);

                // Retorna para o fragmento de notas
                Fragment fragment = new NotasPacFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });
    }
}