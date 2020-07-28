package id.co.myproject.yukkajian_operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.Kategori;
import id.co.myproject.yukkajian_operator.model.Riwayat;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.view.DetailKajianUsulanActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.yukkajian_operator.helper.Utils.getDayName;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    Context context;
    List<Riwayat> riwayatList = new ArrayList<>();
    ApiRequest apiRequest;

    public RiwayatAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;
    }

    public void setRiwayatList(List<Riwayat> riwayatList){
        this.riwayatList.clear();
        this.riwayatList.addAll(riwayatList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RiwayatAdapter.ViewHolder holder, int position) {
        Riwayat riwayat = riwayatList.get(position);
        Call<Kategori> callKategori = apiRequest.kategoriByIdRequest(riwayat.getIdKategori());
        callKategori.enqueue(new Callback<Kategori>() {
            @Override
            public void onResponse(Call<Kategori> call, Response<Kategori> response) {
                holder.tvkategori.setText("Kategori : "+response.body().getNamaKategori());
            }

            @Override
            public void onFailure(Call<Kategori> call, Throwable t) {

            }
        });
        holder.tvJudulKajian.setText(riwayat.getJudul_kajian());
        holder.tvLokasi.setText(riwayat.getLokasi());

        if (riwayat.getStatus().equals("ditolak")){
            holder.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red_main));
            holder.tvStatus.setText("Di tolak");
        }else if(riwayat.getStatus().equals("diterima")){
            holder.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.success));
            holder.tvStatus.setText("Di terima");
        }else if(riwayat.getStatus().equals("sedang proses")){
            holder.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.proses));
            holder.tvStatus.setText("sedang proses");
        }

        String tanggalUpload = DateFormat.format("dd MMM yyyy", riwayatList.get(position).getTanggalUpload()).toString();
        holder.tvTanggalUpload.setText(tanggalUpload);

        String tanggal = DateFormat.format("dd MMM yyyy", riwayatList.get(position).getTanggal()).toString();
        Calendar c = Calendar.getInstance();
        c.setTime(riwayat.getTanggal());
        int hari = c.get(Calendar.DAY_OF_WEEK);

        String jam = DateFormat.format("HH:mm", riwayatList.get(position).getTanggal()).toString();
        holder.tvTanggal.setText(getDayName(hari)+", "+tanggal);
        holder.tvJam.setText("Jam : "+jam);



        Call<User> callUser = apiRequest.userByIdRequest(Integer.parseInt(riwayat.getIdUser()));
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getGambar()).into(holder.ivUser);
                    holder.tvUser.setText(user.getNama());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailKajianUsulanActivity.class);
                Kajian kajian = new Kajian();
                kajian.setIdKajian(riwayat.getIdKajian());
                kajian.setIdUser(riwayat.getIdUser());
                kajian.setIdJenisKajian(riwayat.getIdJenisKajian());
                kajian.setIdKategori(riwayat.getIdKategori());
                kajian.setJudul_kajian(riwayat.getJudul_kajian());
                kajian.setPemateri(riwayat.getPemateri());
                kajian.setTanggal(riwayat.getTanggal());
                kajian.setTanggalUpload(riwayat.getTanggalUpload());
                kajian.setGambar(riwayat.getGambar());
                kajian.setLokasi(riwayat.getLokasi());
                kajian.setStatus(riwayat.getStatus());
                kajian.setIdUsulan(riwayat.getIdUsulan());
                kajian.setNoTelpPemateri(riwayat.getNoTelpPemateri());
                intent.putExtra("id_kajian", kajian.getIdKajian());
                intent.putExtra("id_usulan", kajian.getIdUsulan());
                intent.putExtra("kajian", kajian);
                intent.putExtra("type_intent", Utils.TYPE_INTENT_RIWAYAT);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public Riwayat getItem(int position){
        return riwayatList.get(position);
    }

    public void removeItem(int position){
        riwayatList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivUser;
        LinearLayout lvStatus;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;
        TextView tvkategori, tvJudulKajian, tvTanggal, tvJam, tvLokasi, tvUser, tvTanggalUpload, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulKajian = itemView.findViewById(R.id.tv_judul_kajian);
            tvkategori = itemView.findViewById(R.id.tv_kategori);
            tvJam = itemView.findViewById(R.id.tv_jam);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvTanggalUpload = itemView.findViewById(R.id.tv_tanggal_upload);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUser = itemView.findViewById(R.id.tv_user);
            tvStatus = itemView.findViewById(R.id.tv_status);
            lvStatus = itemView.findViewById(R.id.lv_status);
            view_background =  itemView.findViewById(R.id.view_background);
            view_foreground =  itemView.findViewById(R.id.view_foreground);

        }
    }
}
