package com.pi.upmed.view_med;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.upmed.R;
import com.pi.upmed.model.EstruturaNotasMed;

public class NotasAdapterMed extends RecyclerView.Adapter<NotasAdapterMed.ViewHolder> {

    private Listener listener;

    // Aqui estão os dados que o adaptador deve receber
    // LEMBRE: É uma classe que contém uma estrutura de dados interna privada. Nela há métodos
    // getters e setters que permitem acesso aos registros indexados. Feito isso, este adaptador
    // pode ser usado com qualquer estrutura de dados escolhida, desde que os métodos de acesso
    // sejam estes aqui definidos.
    private EstruturaNotasMed pacoteNotas;

    // CONSTRUTOR. Nos argumentos, entra a estrutura de dados.
    // Cada elemento corresponde a um cartão.
    // Este construtor preencherá os dados na estrutura de dados definida acima.
    public NotasAdapterMed(EstruturaNotasMed notas) {
        this.pacoteNotas = notas;
    }

    // Esta interface deve ser implementada no contexto que usa este adapter
    interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotasAdapterMed.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_nota, parent, false);

        return new ViewHolder(cv);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // Referenciando os campos da card view notas
        CardView cardView = holder.cardView;
        TextView dataHora = (TextView)cardView.findViewById(R.id.cardnota_data_hora);
        TextView autor = (TextView)cardView.findViewById(R.id.cardnota_autor);
        TextView texto = (TextView)cardView.findViewById(R.id.cardnota_texto);

        // Estabelecendo os conteúdos
        // Definir assim: dataHora.setText( DEFINIR A POSIÇÃO AQUI - int );
        dataHora.setText(pacoteNotas.getElemento(position).getData() + ", "
                + pacoteNotas.getElemento(position).getHora());
        autor.setText(pacoteNotas.getElemento(position).getNomeMed() +
                " (" + pacoteNotas.getElemento(position).getLoginMed() + ")\n" +
                pacoteNotas.getElemento(position).getNomeGrupo());
        texto.setText(pacoteNotas.getElemento(position).getTexto());

        // Estabelece o listener no view holder
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    // Este método deve retornar o número de itens de dados
    @Override
    public int getItemCount() {
        return pacoteNotas.getTamanho();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
