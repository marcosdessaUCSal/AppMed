package com.pi.upmed.view_pac;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pi.upmed.R;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.persist_estado.EstadoAtualPac;
import com.pi.upmed.view_pac.GruposPacFragment;

import java.util.ArrayList;

public class ListaMedicosFragmentPac extends Fragment {

    public ListaMedicosFragmentPac() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_medicos_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        // Referencia o estado do app
        final EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Descobre o grupo que está selecionado
        long grupo = estadoAtualPac.getIdGrupoAtual();

        // Obtém uma lista com todos os médicos associados ao paciente e os preenche em um
        // array simples de strings
        final ArrayList<ClassesDB.MedicoNoGrupo> medicos = estadoAtualPac.getListaMedicosNoGrupo(
                view.getContext(),
                grupo);
        String[] arrayMedicos = new String[medicos.size()];
        for (int i = 0; i < medicos.size(); i++) {
            arrayMedicos[i] = medicos.get(i).getNomeMed();
        }

        int numeroMedicos = medicos.size();
        TextView textoSpinner = getActivity().findViewById(R.id.pac_med_texto0);
        switch (numeroMedicos) {
            case 0:
                textoSpinner.setText("Nenhum grupo selecionado");
                break;
            case 1:
                textoSpinner.setText("Há um médico no grupo");
                break;
            default:
                textoSpinner.setText("Há " + numeroMedicos + " médicos no grupo");
                break;
        }


        // Referencia o spinner e o vincula aos médicos do grupo
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_medico_pac);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item,
                arrayMedicos);

        spinner.setAdapter(adapter);

        // Estabelece um listener para reagir a uma seleção feita pelo usuário
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Elementos da tela onde escrever os dados
                TextView textoLongin = getActivity().findViewById(R.id.pac_med_texto1);
                TextView textoNome = getActivity().findViewById(R.id.pac_med_texto2);
                TextView textoStatus = getActivity().findViewById(R.id.pac_med_texto3);
                TextView textoEspecialidades = getActivity().findViewById(R.id.pac_med_texto4);
                //TextView textoMsg = view.findViewById(R.id.pac_grupo_msg);

                // Preenche os campos do ListaMedicosFragment com as informações sobre o médico
                textoLongin.setText(medicos.get(position).getLoginMed());
                textoNome.setText(medicos.get(position).getNomeMed());
                if (medicos.get(position).isPrincipal() == 1) {
                    textoStatus.setText("É o médico principal no grupo");
                } else {
                    textoStatus.setText("É um médico adicional no grupo");
                }
                textoEspecialidades.setText(estadoAtualPac.getEspecialidadeMedico(getContext(),
                        medicos.get(position).getIdMed()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Referência ao botão de voltar ao fragment dos grupos
        Button button = getActivity().findViewById(R.id.button_list_med_voltar);

        // Cria um listener para definir uma reação ao botão retornar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GruposPacFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });
    }
}