package com.pi.upmed.view_pac;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pi.upmed.R;
import com.pi.upmed.model.Agora;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.persist_estado.EstadoAtualPac;

import java.util.ArrayList;

public class RemediosPacFragment extends Fragment {


    public RemediosPacFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remedios_pac, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        // Referencia o estado do app
        final EstadoAtualPac estadoAtualPac = EstadoAtualPac.getInstance();

        // Registra inicialmente que nehum remédio foi selecionado ainda
        estadoAtualPac.setRemedioSelecionado(-1);

        // Obtendo lista com informações sobre remédios
        final ArrayList<ClassesDB.ViewMedicoReceitaRemedio> remedios =
                estadoAtualPac.preencheRemedios(getContext(), estadoAtualPac.getIdPac());

        // Criando um array para preencher o spinner
        final String[] arrayRemedios = new String[remedios.size()];
        for (int i = 0; i < remedios.size(); i++) {
            arrayRemedios[i] = remedios.get(i).getNomeRemedio();
        }

        // Referencia o spinner e o vincula aos remédios
        final Spinner spinner = getActivity().findViewById(R.id.spinner_remedios_pac);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                arrayRemedios);
        spinner.setAdapter(adapter);

        // Obtendo referência aos botões deste fragmento
        Button remover = getActivity().findViewById(R.id.btn_remedios_pac1);
        Button selecionar = getActivity().findViewById(R.id.btn_remedios_pac2);
        Button verRegistros = getActivity().findViewById(R.id.btn_remedios_pac3);
        final Button registrarUso = getActivity().findViewById(R.id.btn_remedios_pac4);
        Button sobre = getActivity().findViewById(R.id.btn_remedios_pac_sobre);

        // Obtendo referência às caixas de texto do segundo cartão (info do remédio)
        final TextView textViewSelRemedio = getActivity().findViewById(R.id.pac_textview_remedio_selec);
        final TextView textViewSelecionado = getActivity().findViewById(R.id.pac_textview_remedio_selec);
        final TextView textViewInfo = getActivity().findViewById(R.id.textview_remedios_pac_descr);
        final TextView textViewRegistros = getActivity().findViewById(R.id.textview_remedios_pac_reg);
        final TextView textViewRegUso = getActivity().findViewById(R.id.pac_textview_reg_uso);

        // Se não houver remédios na lista, enfatizar isso na primeira caixa de texto:
        if (remedios.size() == 0) {
            textViewSelRemedio.setText("Sem remédios receitados");
        }

        // Reagindo ao botão 'SOBRE' -> FUNCIONALIDADE NÃO IMPLEMENTADA
        sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avisa que esta funcionalidade ainda não está implementada
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ops!");
                builder.setMessage("Esta funcionalidade ainda não está implementada!");
                builder.setPositiveButton("Entendi", null);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        // Reagindo ao botão 'REMOVER'
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Reagindo ao botão 'SELECIONAR'
        selecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Se não houver remédios na lista... nada fazer!
                if (remedios.size() == 0) return;

                // Descobre qual item do spinner foi selecionado e o registra (temporariamente)
                // no estado geral do aplicativo
                int remedioSelecionado = spinner.getSelectedItemPosition();
                estadoAtualPac.setRemedioSelecionado(remedioSelecionado);

                // Escreve no topo o nome do remédio selecionado
                textViewSelecionado.setText("Selecionado: " + arrayRemedios[remedioSelecionado]);

                // Construindo o texto com as informações relevantes acerca do remédio
                StringBuilder texto = new StringBuilder();
                texto.append("Médico: "
                        + remedios.get(remedioSelecionado).getNomeMed()
                        + " (" + remedios.get(remedioSelecionado).getLoginMed() + ")\n");
                texto.append("Grupo: "
                        + remedios.get(remedioSelecionado).getNomeGrupo() + "\n");
                texto.append("Receitado em: "
                        + remedios.get(remedioSelecionado).getDataReceita() + "\n");
                texto.append("Doses diárias: "
                        + remedios.get(remedioSelecionado).getFreqUso());
                textViewInfo.setText(texto.toString());
            }
        });

        // Reagindo ao botão 'VER REGISTROS'
        verRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Se nenhum remédio foi selecionado... nada fazer!
                if (estadoAtualPac.getRemedioSelecionado() == -1) return;

                // Obtendo uma lista com os registros de usos do remédio selecionado
                ArrayList<ClassesDB.ViewPacienteUsoRemedio> registros =
                        estadoAtualPac.preencheUsosRemedio(getContext(),
                                estadoAtualPac.getRemedioSelecionado());

                // Escrevendo o nome do remédio na barra do segundo quadro
                textViewRegUso.setText("Registros de uso: "
                        + registros.get(estadoAtualPac.getRemedioSelecionado()).getNomeRemedio());


                StringBuilder texto = new StringBuilder();
                if (registros.size() > 0) {
                    for (int i = 0; i < registros.size(); i++) {
                        texto.append(registros.get(i).getDataUso() + " às ");
                        texto.append(registros.get(i).getHoraUso());
                        if (i + 1 < registros.size()) {
                            texto.append("\n");
                        }
                    }
                } else {
                    texto.append("Não há registros");
                }
                textViewRegistros.setText(texto.toString());

            }
        });

        // Reagindo ao botão 'REGISTRA USO'
        registrarUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se nenhum remédio foi selecionado... nada fazer!
                if (estadoAtualPac.getRemedioSelecionado() == -1) return;

                // Esta é a escolha que já foi feita:
                int escolha = estadoAtualPac.getRemedioSelecionado();

                // Coletando informação de data e hora
                final String data = new String(Agora.getData());
                final String hora = new String(Agora.getHora());

                // Definindo o dialog alert
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Registrando...");
                StringBuilder texto = new StringBuilder();
                texto.append("Tomei o remédio ");
                texto.append(remedios.get(escolha).getNomeRemedio());
                texto.append(" em " + data + " às " + hora);
                builder.setMessage(texto.toString());

                // Botão cancelar
                builder.setNegativeButton("Cancelar", null);

                // Botão confirmar
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Registra o uso no banco de dados
                        estadoAtualPac.registraUsoRemedio(getContext(),
                                remedios.get(estadoAtualPac.getRemedioSelecionado()).getIdRemedio(),
                                data,
                                hora);

                        // Mostra um toast
                        Toast toast = Toast.makeText(getContext(), "Registro efetuado!", Toast.LENGTH_LONG);
                        toast.show();

                        // Reinicia o fragmento
                        Fragment fragment = new RemediosPacFragment();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.disallowAddToBackStack();
                        ft.commit();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}