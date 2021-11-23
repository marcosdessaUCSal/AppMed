package com.pi.upmed.view_med;

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
import com.pi.upmed.persist_estado.EstadoAtualMed;
import com.pi.upmed.view_med.ListaMedicosFragmentMed;

import java.util.ArrayList;

public class GruposMedFragment extends Fragment {


    public GruposMedFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_grupos_med, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        int escolha;

        // Referencia o estado atual do app
        EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

        // Obtém uma lista com todos os grupos vinculados ao médico e os preenche em
        // um array simples de strings
        ArrayList<ClassesDB.ViewPacienteMedicos> listaPacMed =
                estadoAtualMed.getListaPacMed(getContext(), estadoAtualMed.getIdMedAtual());
        String[] arrayGrupo = new String[listaPacMed.size()];
        for (int i = 0; i < listaPacMed.size(); i++) {
            arrayGrupo[i] = listaPacMed.get(i).getNomeGrupo();
        }

        // Referencia o spinner e o vincula aos grupos
        final Spinner spinner = getActivity().findViewById(R.id.spinner_grupo_med);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                arrayGrupo);
        spinner.setAdapter(adapter);

        TextView msg = getActivity().findViewById(R.id.med_grupo_msg);
        if (estadoAtualMed.getIdGrupoAtual() == -1 ) {
            msg.setText("Nenhum grupo selecionado");
        } else {
            escolha = estadoAtualMed.getEscolhaNoSpinnerGrupos();
            spinner.setSelection(escolha);
            msg.setText("Selecionado: " + listaPacMed.get(escolha).getNomeGrupo());

            // Elementos da tela onde escrever os dados
            TextView textoPaciente = getActivity().findViewById(R.id.med_grupo_texto1);
            TextView textoGrupo = getActivity().findViewById(R.id.med_grupo_texto2);
            TextView textoInfo = getActivity().findViewById(R.id.med_grupo_texto3);
            msg = getActivity().findViewById(R.id.pac_grupo_msg);

            // Preenche os campos do GrupoFragment com as informações sobre o grupo
            textoPaciente.setText(listaPacMed.get(escolha).getNomePac() + " (" +
                    listaPacMed.get(escolha).getLoginPac() + ")");
            textoGrupo.setText(listaPacMed.get(escolha).getNomeGrupo());
            textoInfo.setText(estadoAtualMed.getGrupoInfo(getContext(),
                    listaPacMed.get(escolha).getIdGrupo()));
        }

        // Reagindo ao botão 'SELECIONAR O GRUPO'
        Button selecionarGrupo = getActivity().findViewById(R.id.button_med_sel_grupo);
        selecionarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

                // Obtendo a escolha do grupo, pela seleção no spinner.
                // O índice lido no spinner coincide (por construção) com o índice do grupo
                // no array list abaixo, com todas as informações relevantes (ver classe EstadoAtual)
                int escolha = spinner.getSelectedItemPosition();
                ArrayList<ClassesDB.ViewPacienteMedicos> listaPacMed =
                        estadoAtualMed.getListaPacMed(getContext(), estadoAtualMed.getIdMedAtual());

                // Elementos da tela onde escrever os dados
                TextView textoPaciente = getActivity().findViewById(R.id.med_grupo_texto1);
                TextView textoGrupo = getActivity().findViewById(R.id.med_grupo_texto2);
                TextView textoInfo = getActivity().findViewById(R.id.med_grupo_texto3);
                TextView msg = getActivity().findViewById(R.id.med_grupo_msg);

                // Preenche os campos do GrupoFragment com as informações sobre o grupo
                textoPaciente.setText(listaPacMed.get(escolha).getNomePac() + " (" +
                        listaPacMed.get(escolha).getLoginPac() + ")");
                textoGrupo.setText(listaPacMed.get(escolha).getNomeGrupo());
                textoInfo.setText(estadoAtualMed.getGrupoInfo(getContext(),
                        listaPacMed.get(escolha).getIdGrupo()));
                msg.setText("Selecionado: " + listaPacMed.get(escolha).getNomeGrupo());

                // Registra o grupo selecionado no 'estado' do app (possivelmente no banco também)
                // Envia o id do grupo, porque o id do paciente já é determinado pelo login
                // Memoriza também a 'escolha' no spimmer, para retorno ao fragmento dos grupos
                estadoAtualMed.selecionaGrupo(getContext(),
                        listaPacMed.get(escolha).getIdGrupo(),
                        listaPacMed.get(escolha).getNomeGrupo());
                estadoAtualMed.setEscolhaNoSpinnerGrupos(escolha);
            }
        });

        // Reagindo ao botão 'VER MÉDICOS'
        Button verMedicos = getActivity().findViewById(R.id.button_med_ver_med);
        verMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListaMedicosFragmentMed();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });










    }
}