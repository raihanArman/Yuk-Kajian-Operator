package id.co.myproject.yukkajian_operator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.view.DetailKajianUsulanActivity;
import id.co.myproject.yukkajian_operator.view.PhotoViewFragment;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.model.Lampiran;

public class LampiranAdapter extends RecyclerView.Adapter<LampiranAdapter.ViewHolder> {
    Context context;
    List<Lampiran> lampiranList = new ArrayList<>();

    public LampiranAdapter(Context context) {
        this.context = context;
    }

    public void setKajianList(List<Lampiran> lampiranList){
        this.lampiranList.clear();
        this.lampiranList.addAll(lampiranList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LampiranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lampiran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LampiranAdapter.ViewHolder holder, int position) {
        Lampiran lampiran = lampiranList.get(position);
        Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"lampiran/"+lampiran.getGambar()).into(holder.ivLampiran);
        holder.tvKeterangan.setText(lampiran.getKeterangan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailKajianUsulanActivity activity = (DetailKajianUsulanActivity) context;
                FragmentManager fm = activity.getSupportFragmentManager();

                PhotoViewFragment photoViewFragment = new PhotoViewFragment(lampiran.getGambar(), PhotoViewFragment.GAMBAR_LAMPIRAN);
                photoViewFragment.show(fm, "");
            }
        });

    }

    @Override
    public int getItemCount() {
        return lampiranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivLampiran;
        TextView tvKeterangan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLampiran = itemView.findViewById(R.id.iv_lampiran);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
    }
}
