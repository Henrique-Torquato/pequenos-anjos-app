package com.example.projeto_app_developer_finalversion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TrendViewHolder> {

    private final List<Trend> trendList;

    public TrendAdapter(List<Trend> trendList) {
        this.trendList = trendList;
    }

    @NonNull
    @Override
    public TrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Melhorando a inflagem da view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_trend_list, parent, false);
        return new TrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendViewHolder holder, int position) {
        Trend trend = trendList.get(position);
        holder.titleTxt.setText(trend.getTitle());
        holder.subtitleTxt.setText(trend.getSubtitle());

        // Verificar o tema ativo (Claro ou Escuro)
        int nightModeFlags = holder.itemView.getContext().getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        int imageResId;
        // Se o tema for escuro, carregue uma imagem específica, caso contrário, uma imagem para o tema claro
        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            imageResId = R.drawable.trends; // Substitua com a imagem específica para o tema escuro
        } else {
            imageResId = trend.getImageResId(); // Imagem padrão ou específica para o tema claro
        }
        holder.pic.setImageResource(imageResId);
    }


    @Override
    public int getItemCount() {
        return trendList.size();
    }

    static class TrendViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, subtitleTxt;
        ImageView pic;

        public TrendViewHolder(@NonNull View itemView) {
            super(itemView);
            // Iniciando as views
            titleTxt = itemView.findViewById(R.id.titleTxt);
            subtitleTxt = itemView.findViewById(R.id.subtitleTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
