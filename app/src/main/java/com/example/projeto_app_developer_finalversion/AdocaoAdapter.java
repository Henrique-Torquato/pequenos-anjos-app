package com.example.projeto_app_developer_finalversion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdocaoAdapter extends RecyclerView.Adapter<AdocaoAdapter.AdocaoViewHolder> {

    private List<AdocaoItem> adocaoItemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AdocaoItem adocaoItem);
    }

    public AdocaoAdapter(List<AdocaoItem> adocaoItemList, OnItemClickListener listener) {
        this.adocaoItemList = adocaoItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdocaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_adocoes_list, parent, false);
        return new AdocaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdocaoViewHolder holder, int position) {
        AdocaoItem currentItem = adocaoItemList.get(position);

        holder.adocaoTitulo.setText(currentItem.getTitulo());
        holder.adocaoIdadeDescricao.setText(currentItem.getIdade());
        holder.adocaoDescricao.setText(currentItem.getDescricao());
        holder.adocaoImg.setImageResource(currentItem.getImagem());

        // Verifica o status da adoção e ajusta o texto e ícone
        if (currentItem.isAdotado()) {
            holder.adocaoStatus.setText("Adotado");
        } else {
            holder.adocaoStatus.setText("Adoção disponível");
        }

        // Configura o clique no item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(currentItem));
    }

    @Override
    public int getItemCount() {
        return adocaoItemList.size();
    }

    public static class AdocaoViewHolder extends RecyclerView.ViewHolder {

        TextView adocaoTitulo, adocaoIdadeDescricao, adocaoDescricao, adocaoStatus;
        ImageView adocaoImg;
        CardView cardView;

        public AdocaoViewHolder(View itemView) {
            super(itemView);
            adocaoTitulo = itemView.findViewById(R.id.adocaoTitulo);
            adocaoIdadeDescricao = itemView.findViewById(R.id.adocaoIdadeDescricao);
            adocaoDescricao = itemView.findViewById(R.id.adocaoDescricao);
            adocaoStatus = itemView.findViewById(R.id.adocaoStatus);
            adocaoImg = itemView.findViewById(R.id.adocaoimg);
            cardView = itemView.findViewById(R.id.card_view); // caso queira usar algum card específico
        }
    }
}
