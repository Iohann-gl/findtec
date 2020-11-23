package com.iohann.findtec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<TecnicoUser> tecnicos;
    private OnItemClickListener mListener;

    public ImageAdapter(Context context, List<TecnicoUser> uploads) {
        mContext = context;
        tecnicos = uploads;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.modelo, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        TecnicoUser currentEvent = tecnicos.get(position);

        holder.mPreco.setText("Preço: " + currentEvent.getPreco());
        holder.mNome.setText(currentEvent.getNome());
        holder.mCidade.setText(currentEvent.getCidade());
        Picasso.with(mContext)
                .load(currentEvent.getmImageUrl())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(holder.imagemPostagem);
    }


    @Override
    public int getItemCount() {
        return tecnicos.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView mCidade, mNome, mPreco;
        public ImageView imagemPostagem;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            mPreco = itemView.findViewById(R.id.txtModeloPreco);
            mCidade = itemView.findViewById(R.id.txtModeloCidade);
            mNome = itemView.findViewById(R.id.txtModeloNome);
            imagemPostagem = itemView.findViewById(R.id.imgModelo);

            mNome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
