package com.pi.upmed.view_med;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.pi.upmed.R;
import com.pi.upmed.model.ClassesDB;
import com.pi.upmed.model.EstruturaNotasMed;
import com.pi.upmed.persist_estado.EstadoAtualMed;


public class NotasMedFragmentMed extends Fragment {

    public NotasMedFragmentMed() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_notas_med_med, container, false);

        // Obtendo referência ao estado
        final EstadoAtualMed estadoAtualMed = EstadoAtualMed.getInstance();

        // Obtém a estrutura de dados contendo todas as notas a serem exibidas
        EstruturaNotasMed lista = estadoAtualMed.preencheNotasMed(getContext(),
                estadoAtualMed.getIdGrupoAtual());

        // Introduzindo o adaptador: NotasAdapterMed
        NotasAdapterMed notasAdapterMed = new NotasAdapterMed(lista);

        // Conecta o adaptador ao recycler view, onde as notas devem aparecer
        recyclerView.setAdapter(notasAdapterMed);

        // Seleciona o gerenciador de layout e o associa ao recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(container.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

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




        // Posiciona a lista de notas no final
        recyclerView.scrollToPosition(lista.getTamanho() - 1);

        // Reação ao botão "nova"
        botaoNovaNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Torna o botão apagar invisível, que é o default, quando ele não referencia
                // nenhuma nota a ser apagada
                botaoApagar.setVisibility(View.INVISIBLE);

                // Oculta o quadro de botões
                frame.setVisibility(View.GONE);

                // Faz com que o frame layout (que suporta os fragments) assuma o tamanho original
                ViewGroup.LayoutParams params = contentFrame.getLayoutParams();
                params.height = params.MATCH_PARENT;
                contentFrame.setLayoutParams(params);


                // Chama o fragmento
                Fragment fragment = new NovaNotaMedFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.disallowAddToBackStack();
                ft.commit();
            }
        });

        notasAdapterMed.setListener(new NotasAdapterMed.Listener() {
            @Override
            public void onClick(int position) {

                final int posEscolhida = position;

                // Busca informações sobre o estado do app para registrar na variável (privada)
                // 'positionNotaEscolhida' o valor do índice da nota clicada pelo usuário
                estadoAtualMed.setPositionNotaEscolhida(position);

                // Reação ao botão "apagar"
                botaoApagar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Obtém referência a dados sobre a nota selecionada
                        ClassesDB.NotaMedico nota = estadoAtualMed.getNotaMedico(posEscolhida);

                        // Dá a definição de uma janela alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Remover");
                        builder.setMessage("Isto apagará a nota permanentemente!");

                        // Reação à confirmação da exclusão
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Identifica qual nota foi selecionada
                                ClassesDB.NotaMedico nota = estadoAtualMed.getNotaMedico(posEscolhida);

                                // Apaga a nota especificada
                                estadoAtualMed.apagarNotaMed(getContext(), nota.getIdNota());

                                // Oculta o botão apagar, antes de efetuar a atualização da view
                                botaoApagar.setVisibility(View.INVISIBLE);

                                // Recarrega o fragment para atualizar a lista de notas
                                Fragment fragment = new NotasMedFragmentMed();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft.disallowAddToBackStack();
                                ft.commit();
                            }
                        });

                        // Caso da desistência
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Não há nenhuma ação a ser feita aqui. A presença disto já garante
                                // o botão 'cancelar'. Por outro lado, este método for removido,
                                // o botão cancelar não aparece.
                            }
                        });

                        AlertDialog confirma = builder.create();
                        confirma.show();

                    }
                });

                // Se alguma nota foi clicada, o botão "apagar" é mostrado para dar a oportunidade
                // de se remover a nota selecionada.
                // Mas se, em vez disso, outra nota for clicada, tudo volta ao padrão.
                if (botaoApagar.isShown()) {
                    botaoApagar.setVisibility(View.INVISIBLE);
                } else {
                    botaoApagar.setVisibility(View.VISIBLE);
                }
            }
        });




        return recyclerView;
    }
}