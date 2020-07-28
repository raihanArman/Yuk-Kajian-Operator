package id.co.myproject.yukkajian_operator.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.adapter.KajianUsulanAdapter;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    
    ProgressDialog progressDialog;
    LinearLayout lvKoneksi, lvNoRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvKajianUsulan;
    ImageView ivUser;
    TextView tvUser;
    LinearLayout lvHome;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    int idUser;

    KajianUsulanAdapter kajianUsulanAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        idUser = sharedPreferences.getInt(Utils.ID_USER_KEY, 0);
        rvKajianUsulan = view.findViewById(R.id.rv_kajian_usulan);
        lvNoRequest = view.findViewById(R.id.lv_no_request);
        lvHome = view.findViewById(R.id.lv_home);
        lvKoneksi = view.findViewById(R.id.lv_connection);
        tvUser = view.findViewById(R.id.tv_user);
        ivUser = view.findViewById(R.id.iv_user);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        kajianUsulanAdapter = new KajianUsulanAdapter(getActivity(), apiRequest);
        rvKajianUsulan.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvKajianUsulan.setAdapter(kajianUsulanAdapter);
        rvKajianUsulan.setVisibility(View.INVISIBLE);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog.show();
                if (Utils.isConnectionInternet(getActivity())){
                    loadData();
                    loadDataUser();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();
                    lvHome.setVisibility(View.INVISIBLE);
                    lvKoneksi.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
                if (Utils.isConnectionInternet(getActivity())){
                    loadData();
                    loadDataUser();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();
                    lvHome.setVisibility(View.INVISIBLE);
                    lvKoneksi.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        if (Utils.isConnectionInternet(getActivity())){
            loadData();
            loadDataUser();
        }else {
            lvHome.setVisibility(View.INVISIBLE);
            lvKoneksi.setVisibility(View.VISIBLE);
            return;
        }

    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        Call<List<Kajian>> callKajianUsulan = apiRequest.kajianByStatus("sedang proses");
        callKajianUsulan.enqueue(new Callback<List<Kajian>>() {
            @Override
            public void onResponse(Call<List<Kajian>> call, Response<List<Kajian>> response) {
                List<Kajian> kajianList = response.body();
                if (kajianList.size() > 0){
                    Collections.sort(kajianList, new Comparator<Kajian>() {
                        @Override
                        public int compare(Kajian o1, Kajian o2) {
                            return o2.getTanggalUpload().compareTo(o1.getTanggalUpload());
                        }
                    });
                    rvKajianUsulan.setVisibility(View.VISIBLE);
                    lvNoRequest.setVisibility(View.GONE);
                }else {
                    rvKajianUsulan.setVisibility(View.INVISIBLE);
                    lvNoRequest.setVisibility(View.VISIBLE);
                }
                kajianUsulanAdapter.setKajianList(kajianList);
            }

            @Override
            public void onFailure(Call<List<Kajian>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void loadDataUser() {
        swipeRefreshLayout.setRefreshing(false);
        Call<User> callUser = apiRequest.userByIdRequest(idUser);
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    tvUser.setText(user.getNama());
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getGambar()).into(ivUser);
                    progressDialog.dismiss();
                    lvHome.setVisibility(View.VISIBLE);
                    lvKoneksi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
