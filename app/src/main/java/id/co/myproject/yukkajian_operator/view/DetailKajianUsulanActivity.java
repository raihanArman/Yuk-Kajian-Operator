package id.co.myproject.yukkajian_operator.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.adapter.LampiranAdapter;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.Kategori;
import id.co.myproject.yukkajian_operator.model.Lampiran;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.model.Value;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.request.RetrofitRequest;
import id.co.myproject.yukkajian_operator.helper.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

public class DetailKajianUsulanActivity extends AppCompatActivity {


    ImageView ivPamflet, ivWaPemateri, ivNoTelpPemateri, ivUser, ivWaPengusul, ivNoTelpPengusul;
    TextView tvKategori, tvJudulKajian, tvTanggal, tvJam, tvLokasi, tvUser, tvPemateri, tvNoTelpPengusul, tvNoTelpPemateri;
    Button btnTerima, btnTolak;


    CardView cvLink;
    TextView tvLink;
    LinearLayout lvKunjungi;
    LinearLayout lvContainerDetail, lvKoneksi, layoutKonfirmasi;
    ApiRequest apiRequest;
    String idKajian, idUsulan;
    Kajian kajian;
    RecyclerView rvLampiran;
    LampiranAdapter lampiranAdapter;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    int idUser, typeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kajian_usulan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Kajian");

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt(Utils.ID_USER_KEY, 0);


        kajian = getIntent().getParcelableExtra("kajian");
        idKajian = getIntent().getStringExtra("id_kajian");
        idUsulan = getIntent().getStringExtra("id_usulan");
        typeIntent = getIntent().getIntExtra("type_intent", 0);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");


        cvLink =findViewById(R.id.cv_link);
        tvLink = findViewById(R.id.tv_link);
        lvKunjungi = findViewById(R.id.lv_kunjungi);
        ivPamflet = findViewById(R.id.iv_pamflet);
        ivWaPemateri = findViewById(R.id.iv_wa_pemateri);
        ivWaPengusul = findViewById(R.id.iv_wa_pengusul);
        ivNoTelpPemateri = findViewById(R.id.iv_no_telp_pemateri);
        ivNoTelpPengusul = findViewById(R.id.iv_no_telp_pengusul);
        ivUser = findViewById(R.id.iv_user);
        tvKategori = findViewById(R.id.tv_kategori);
        tvJudulKajian = findViewById(R.id.tv_judul_kajian);
        tvTanggal = findViewById(R.id.tv_tanggal);
        tvJam = findViewById(R.id.tv_jam);
        tvLokasi = findViewById(R.id.tv_lokasi);
        tvUser = findViewById(R.id.tv_user);
        tvPemateri = findViewById(R.id.tv_pemateri);
        tvNoTelpPemateri = findViewById(R.id.tv_no_hp_pemateri);
        tvNoTelpPengusul = findViewById(R.id.tv_no_hp_pengusul);
        rvLampiran = findViewById(R.id.rv_lampiran);
        btnTerima = findViewById(R.id.btn_terima);
        btnTolak = findViewById(R.id.btn_tolak);
        lvContainerDetail = findViewById(R.id.lv_container_detail);
        lvKoneksi = findViewById(R.id.lv_connection);
        layoutKonfirmasi = findViewById(R.id.layout_konfirmasi);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvLampiran.setLayoutManager(linearLayoutManager);
        lampiranAdapter = new LampiranAdapter(this);
        rvLampiran.setAdapter(lampiranAdapter);

        if (typeIntent == Utils.TYPE_INTENT_HOME){
            layoutKonfirmasi.setVisibility(View.VISIBLE);
        }else if (typeIntent == Utils.TYPE_INTENT_RIWAYAT){
            layoutKonfirmasi.setVisibility(View.GONE);
        }


        if (Utils.isConnectionInternet(DetailKajianUsulanActivity.this)){
            lvContainerDetail.setVisibility(View.VISIBLE);
            lvKoneksi.setVisibility(View.GONE);
            loadData();
            loadLampiran();
        }else {
            lvContainerDetail.setVisibility(View.INVISIBLE);
            lvKoneksi.setVisibility(View.VISIBLE);
            return;
        }

        ivWaPemateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + tvNoTelpPemateri.getText();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ivWaPengusul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + tvNoTelpPengusul.getText();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ivNoTelpPemateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+tvNoTelpPemateri.getText().toString()));
                startActivity(intent);
            }
        });

        ivNoTelpPengusul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+tvNoTelpPengusul.getText().toString()));
                startActivity(intent);
            }
        });

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectionInternet(DetailKajianUsulanActivity.this)){
                    updateUsulanTerima();
                }else {
                    Toast.makeText(DetailKajianUsulanActivity.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectionInternet(DetailKajianUsulanActivity.this)){
                    updateUsulanTolak();
                }else {
                    Toast.makeText(DetailKajianUsulanActivity.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lvKunjungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://"+tvLink.getText().toString()));
                startActivity(browserIntent);
            }
        });

        ivPamflet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = DetailKajianUsulanActivity.this.getSupportFragmentManager();

                PhotoViewFragment photoViewFragment = new PhotoViewFragment(kajian.getGambar(), PhotoViewFragment.GAMBAR_PAMFLET);
                photoViewFragment.show(fm, "");
            }
        });

    }

    private void updateUsulanTolak() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailKajianUsulanActivity.this);
        builder.setTitle("Pesan");
        EditText etIsi = new EditText(DetailKajianUsulanActivity.this);
        etIsi.setHint("Isi pesan");
        builder.setView(etIsi);
        builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                Call<Value> callTambahPesan = apiRequest.tambahPesanRequest(
                        kajian.getIdUser(),
                        idUser,
                        "Kajian anda yang berjudul ("+kajian.getJudul_kajian()+") telah ditolak "+etIsi.getText().toString()
                );
                callTambahPesan.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            if (response.body().getValue() == 1){
                                Call<Value> callUpdateUsulan = apiRequest.updateUsulanRequest(
                                        kajian.getIdUsulan(),
                                        "ditolak"
                                );
                                callUpdateUsulan.enqueue(new Callback<Value>() {
                                    @Override
                                    public void onResponse(Call<Value> call, Response<Value> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(DetailKajianUsulanActivity.this, "Kajian : "+kajian.getJudul_kajian()+", Ditolak", Toast.LENGTH_SHORT).show();
                                            if (response.body().getValue() == 1){
                                                inputRiwayat();
                                            }
                                        }else {
                                            Toast.makeText(DetailKajianUsulanActivity.this, "Gagal memproses kajian", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Value> call, Throwable t) {
                                        Toast.makeText(DetailKajianUsulanActivity.this, "Gagal memproses kajian", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        Toast.makeText(DetailKajianUsulanActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void inputRiwayat() {
        Call<Value> callTambahRiwayat = apiRequest.tambahRiwayatRequest(idUsulan);
        callTambahRiwayat.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(DetailKajianUsulanActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUsulanTerima() {
        progressDialog.show();
        Call<Value> callTambahPesan = apiRequest.tambahPesanRequest(
                kajian.getIdUser(),
                idUser,
                "Kajian anda yang berjudul ("+kajian.getJudul_kajian()+") telah diterima. Tunggu beberapa saat, kajian anda segera akan di publish"
        );
        callTambahPesan.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue() == 1){
                        Call<Value> callUpdateUsulan = apiRequest.updateUsulanRequest(
                                kajian.getIdUsulan(),
                                "diterima"
                        );
                        callUpdateUsulan.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(DetailKajianUsulanActivity.this, "Kajian : "+kajian.getJudul_kajian()+", Diterima", Toast.LENGTH_SHORT).show();
                                    if (response.body().getValue() == 1){
                                        inputRiwayat();
                                    }
                                }else {
                                    Toast.makeText(DetailKajianUsulanActivity.this, "Gagal memproses kajian", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(DetailKajianUsulanActivity.this, "Gagal memproses kajian", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(DetailKajianUsulanActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLampiran() {
        Call<List<Lampiran>> callLampiran = apiRequest.allLampiranRequest(idUsulan);
        callLampiran.enqueue(new Callback<List<Lampiran>>() {
            @Override
            public void onResponse(Call<List<Lampiran>> call, Response<List<Lampiran>> response) {
                if (response.isSuccessful()){
                    List<Lampiran> lampiranList = response.body();
                    lampiranAdapter.setKajianList(lampiranList);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Lampiran>> call, Throwable t) {

            }
        });
    }

    private void loadData() {
        progressDialog.show();
        Glide.with(this).load(BuildConfig.BASE_URL_GAMBAR+"pamflet/"+kajian.getGambar()).into(ivPamflet);
        tvJudulKajian.setText(kajian.getJudul_kajian());
        tvPemateri.setText(kajian.getPemateri());
        tvNoTelpPemateri.setText(kajian.getNoTelpPemateri());

        String tanggal = DateFormat.format("dd MM yyyy", kajian.getTanggal()).toString();
        String jam = DateFormat.format("HH:mm", kajian.getTanggal()).toString();
        Calendar c = Calendar.getInstance();
        c.setTime(kajian.getTanggal());
        int hari = c.get(Calendar.DAY_OF_WEEK);

        tvTanggal.setText(Utils.getDayName(hari)+", "+tanggal);
        tvJam.setText("Jam : "+jam);
        tvLokasi.setText(kajian.getLokasi());


        if (kajian.getLink().equals("")){
            cvLink.setVisibility(View.GONE);
        }else {
            tvLink.setText(kajian.getLink());
        }

        Call<Kategori> callKategoriById = apiRequest.kategoriByIdRequest(kajian.getIdKategori());
        callKategoriById.enqueue(new Callback<Kategori>() {
            @Override
            public void onResponse(Call<Kategori> call, Response<Kategori> response) {
                tvKategori.setText(response.body().getNamaKategori());
            }

            @Override
            public void onFailure(Call<Kategori> call, Throwable t) {

            }
        });

        Call<User> callUser = apiRequest.userByIdRequest(Integer.parseInt(kajian.getIdUser()));
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                tvUser.setText(response.body().getNama());
                tvNoTelpPengusul.setText(response.body().getNoTelp());
                Glide.with(DetailKajianUsulanActivity.this).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+response.body().getGambar()).into(ivUser);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
