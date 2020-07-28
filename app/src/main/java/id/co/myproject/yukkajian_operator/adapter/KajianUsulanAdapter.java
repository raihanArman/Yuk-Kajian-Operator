package id.co.myproject.yukkajian_operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.view.DetailKajianUsulanActivity;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.Kategori;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.yukkajian_operator.helper.Utils.getDayName;

public class KajianUsulanAdapter extends RecyclerView.Adapter<KajianUsulanAdapter.ViewHolder> {
    Context context;
    List<Kajian> kajianList = new ArrayList<>();
    ApiRequest apiRequest;

    public KajianUsulanAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;
    }

    public void setKajianList(List<Kajian> kajianList){
        this.kajianList.clear();
        this.kajianList.addAll(kajianList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KajianUsulanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kajian_usulan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KajianUsulanAdapter.ViewHolder holder, int position) {
        Kajian kajian = kajianList.get(position);
        Call<Kategori> callKategori = apiRequest.kategoriByIdRequest(kajian.getIdKategori());
        callKategori.enqueue(new Callback<Kategori>() {
            @Override
            public void onResponse(Call<Kategori> call, Response<Kategori> response) {
                holder.tvkategori.setText("Kategori : "+response.body().getNamaKategori());
            }

            @Override
            public void onFailure(Call<Kategori> call, Throwable t) {

            }
        });
        holder.tvJudulKajian.setText(kajian.getJudul_kajian());
        holder.tvLokasi.setText(kajian.getLokasi());

        String tanggalUpload = DateFormat.format("dd MMM yyyy", kajianList.get(position).getTanggalUpload()).toString();
        holder.tvTanggalUpload.setText(tanggalUpload);

        String tanggal = DateFormat.format("dd MMM yyyy", kajianList.get(position).getTanggal()).toString();
        Calendar c = Calendar.getInstance();
        c.setTime(kajian.getTanggal());
        int hari = c.get(Calendar.DAY_OF_WEEK);

        String jam = DateFormat.format("HH:mm", kajianList.get(position).getTanggal()).toString();
        holder.tvTanggal.setText(getDayName(hari)+", "+tanggal);
        holder.tvJam.setText("Jam : "+jam);

        Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"pamflet/"+kajian.getGambar()).into(holder.ivPamflet);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailKajianUsulanActivity.class);
                intent.putExtra("id_kajian", kajian.getIdKajian());
                intent.putExtra("id_usulan", kajian.getIdUsulan());
                intent.putExtra("kajian", kajian);
                intent.putExtra("type_intent", Utils.TYPE_INTENT_HOME);
                context.startActivity(intent);
            }
        });

        Call<User> callUser = apiRequest.userByIdRequest(Integer.parseInt(kajian.getIdUser()));
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

    }

    @Override
    public int getItemCount() {
        return kajianList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPamflet, ivUser;
        TextView tvkategori, tvJudulKajian, tvTanggal, tvJam, tvLokasi, tvUser, tvTanggalUpload;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPamflet = itemView.findViewById(R.id.iv_pamflet);
            tvJudulKajian = itemView.findViewById(R.id.tv_judul_kajian);
            tvkategori = itemView.findViewById(R.id.tv_kategori);
            tvJam = itemView.findViewById(R.id.tv_jam);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvTanggalUpload = itemView.findViewById(R.id.tv_tanggal_upload);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUser = itemView.findViewById(R.id.tv_user);
        }
    }
}
