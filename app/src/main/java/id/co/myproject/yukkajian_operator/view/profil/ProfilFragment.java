package id.co.myproject.yukkajian_operator.view.profil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.view.MainActivity;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.request.RetrofitRequest;
import id.co.myproject.yukkajian_operator.view.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private static final String TAG = "ProfilFragment";

    LinearLayout lvContainerProfil, lvKoneksi;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView ivUser, ivBackground, ivSetting, ivBack;
    TextView tvUser, tvUsername, tvNama, tvNoTelp;
    Button btnLogOut;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;
    int idUser;
    public static boolean statusUpdate = false;


    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        idUser = sharedPreferences.getInt(Utils.ID_USER_KEY, 0);


        lvContainerProfil = view.findViewById(R.id.lv_container_profil);
        lvKoneksi = view.findViewById(R.id.lv_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        ivUser = view.findViewById(R.id.iv_user);
        ivSetting = view.findViewById(R.id.iv_setting);
        tvUser = view.findViewById(R.id.tv_user);
        tvNama = view.findViewById(R.id.tv_nama);
        tvUsername = view.findViewById(R.id.tv_username);
        tvNoTelp = view.findViewById(R.id.tv_no_hp);
        btnLogOut = view.findViewById(R.id.btn_log_out);
        ivSetting.setVisibility(View.VISIBLE);

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectionInternet(getActivity())) {
                    EditProfilFragment editProfilFragment = new EditProfilFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Utils.ID_USER_KEY, idUser);
                    editProfilFragment.setArguments(bundle);
                    ((MainActivity) view.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_home, editProfilFragment)
                            .addToBackStack(null)
                            .commit();
                }else {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isConnectionInternet(getActivity())){
                    lvContainerProfil.setVisibility(View.VISIBLE);
                    lvKoneksi.setVisibility(View.GONE);
                    loadData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    lvContainerProfil.setVisibility(View.INVISIBLE);
                    lvKoneksi.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Utils.isConnectionInternet(getActivity())){
                    lvContainerProfil.setVisibility(View.VISIBLE);
                    lvKoneksi.setVisibility(View.GONE);
                    loadData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    lvContainerProfil.setVisibility(View.INVISIBLE);
                    lvKoneksi.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        if (Utils.isConnectionInternet(getActivity())){
            lvContainerProfil.setVisibility(View.VISIBLE);
            lvKoneksi.setVisibility(View.GONE);
            loadData();
        }else {
            lvContainerProfil.setVisibility(View.INVISIBLE);
            lvKoneksi.setVisibility(View.VISIBLE);
            return;
        }

    }

    private void loadData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses...");
        progressDialog.show();
        swipeRefreshLayout.setRefreshing(false);
        Call<User> callUser = apiRequest.userByIdRequest(idUser);
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    tvUser.setText(user.getNama());
                    tvUsername.setText(user.getUsername());
                    tvNama.setText(user.getNama());
                    tvNoTelp.setText(user.getNoTelp());
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"profil/"+user.getGambar()).into(ivUser);
                }else {
                    Toast.makeText(getActivity(), "Gagal load data operator", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void signOut(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        editor.putInt(Utils.ID_USER_KEY, 0);
        editor.putBoolean(Utils.LOGIN_STATUS, false);
        editor.commit();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusUpdate) {
            loadData();
        }
    }

}
