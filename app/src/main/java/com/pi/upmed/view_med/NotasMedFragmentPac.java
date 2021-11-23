package com.pi.upmed.view_med;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pi.upmed.R;
import com.pi.upmed.model.EstruturaNotasPac;
import com.pi.upmed.persist_estado.EstadoAtualMed;
import com.pi.upmed.view_pac.NotasAdapter;


public class NotasMedFragmentPac extends Fragment {

    public NotasMedFragmentPac() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_notas_med_pac, container, false);

        // Obtendo referência ao estado
        final EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

        // Obtém a estrutura de dados contendo todas as notas a serem exibidas
        EstruturaNotasPac lista = estadoAtualMed.preencheNotasPac(getContext(), estadoAtualMed.getIdGrupoAtual());

        // Introduzindo o adaptador: NotasAdapter
        NotasAdapter notasAdapter = new NotasAdapter(lista);

        // Conecta o adaptador ao recycler view, onde as notas devem aparecer
        recyclerView.setAdapter(notasAdapter);

        // Seleciona o gerenciador de layout e o associa ao recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);


        /*
        // Faz referência ao frame layout (que suporta todos os fragments)
        // Depois, reduz o tamanho, para dar espaço para encaixe do quadro de botões
        final FrameLayout contentFrame = getActivity().findViewById(R.id.content_frame);
        ViewGroup.LayoutParams params = contentFrame.getLayoutParams();
        params.height = 900;
        contentFrame.setLayoutParams(params);


        // Referência ao quadro de botões
        final FrameLayout frame = getActivity().findViewById(R.id.botoes_base);
        frame.setVisibility(View.VISIBLE);

        // Referencia os botões "apagar" e "nova" nota
        final Button botaoApagar = getActivity().findViewById(R.id.botao_apagar);
        final Button botaoNovaNota = getActivity().findViewById(R.id.botao_nova_nota);

         */

        // Posiciona a lista de notas no final
        recyclerView.scrollToPosition(lista.getTamanho() - 1);



        return recyclerView;
    }



}