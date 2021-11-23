package com.pi.upmed.view_pac;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pi.upmed.R;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.persist_estado.EstadoAtualPac;

import java.util.ArrayList;


public class GruposPacFragment extends Fragment {

    public GruposPacFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grupos_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        // Referencia o estado do app
        EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        int escolha;

        // Obtém uma lista com todos os grupos vinculados ao paciente e os preenche em
        // um array simples de strings
        ArrayList<ClassesDB.Grupo> grupos = estadoAtualPac.getGrupos(view.getContext());
        String[] arrayGrupo = new String[grupos.size()];
        for (int i = 0; i < grupos.size(); i++) {
            arrayGrupo[i] = grupos.get(i).getNomeGrupo();
        }

        // Referencia o spinner e o vincula aos nomes dos grupos
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_grupo_pac);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                arrayGrupo);
        spinner.setAdapter(adapter);

        TextView msg = getActivity().findViewById(R.id.pac_grupo_msg);
        if (estadoAtualPac.getIdGrupoAtual() == -1 ) {
            msg.setText("Nenhum grupo selecionado");
        } else {
            escolha = estadoAtualPac.getEscolhaNoSpinnerGrupos();
            spinner.setSelection(escolha);
            msg.setText("Selecionado: " + grupos.get(escolha).getNomeGrupo());

            // Elementos da tela onde escrever os dados
            TextView textoLongin = getActivity().findViewById(R.id.pac_grupo_texto1);
            TextView textoNome = getActivity().findViewById(R.id.pac_grupo_texto2);
            TextView textoGrupo = getActivity().findViewById(R.id.pac_grupo_texto3);
            TextView textoInfo = getActivity().findViewById(R.id.pac_grupo_texto4);

            // Preenche os campos do GrupoFragment com as informações sobre o grupo
            textoLongin.setText(estadoAtualPac.getLogin());
            textoNome.setText(estadoAtualPac.getNomePac(view.getContext()));
            textoGrupo.setText(grupos.get(escolha).getNomeGrupo());
            textoInfo.setText(grupos.get(escolha).getInfo());
        }


        // Reagindo ao botão 'SELECIONAR O GRUPO'
        Button selecionarGrupo = getActivity().findViewById(R.id.button_pac_sel_grupo);
        selecionarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

                // Obtendo a escolha do grupo, pela seleção no spinner.
                // O índice lido no spinner coincide (por construção) ao índice do grupo
                // no array list abaixo, com todas as informações relevantes (ver classe EstadoAtual)
                //Spinner spinner = (Spinner) findViewById(R.id.spinner_grupo_pac);
                int escolha = spinner.getSelectedItemPosition();
                ArrayList<ClassesDB.Grupo> grupos = estadoAtualPac.getGrupos(getContext());

                // Elementos da tela onde escrever os dados
                TextView textoLongin = getActivity().findViewById(R.id.pac_grupo_texto1);
                TextView textoNome = getActivity().findViewById(R.id.pac_grupo_texto2);
                TextView textoGrupo = getActivity().findViewById(R.id.pac_grupo_texto3);
                TextView textoInfo = getActivity().findViewById(R.id.pac_grupo_texto4);
                TextView msg = getActivity().findViewById(R.id.pac_grupo_msg);

                // Preenche os campos do GrupoFragment com as informações sobre o grupo
                textoLongin.setText(estadoAtualPac.getLogin());
                textoNome.setText(estadoAtualPac.getNomePac(getContext()));
                textoGrupo.setText(grupos.get(escolha).getNomeGrupo());
                textoInfo.setText(grupos.get(escolha).getInfo());
                msg.setText("Selecionado: " + grupos.get(escolha).getNomeGrupo());

                // Registra o grupo selecionado no 'estado' do app (possivelmente no banco também)
                // Envia o id do grupo, porque o id do paciente já é determinado pelo login
                // Memoriza também a 'escolha' no spimmer, para retorno ao fragmento dos grupos
                estadoAtualPac.selecionaGrupo(getContext(),
                        grupos.get(escolha).getIdGrupo(),
                        grupos.get(escolha).getNomeGrupo());
                estadoAtualPac.setEscolhaNoSpinnerGrupos(escolha);
            }
        });


        // Reagindo ao botão 'VER MÉDICOS'
        Button verMedicos = getActivity().findViewById(R.id.button_pac_ver_med);
        verMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListaMedicosFragmentPac();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });


    }
}
