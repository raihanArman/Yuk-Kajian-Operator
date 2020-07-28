package id.co.myproject.yukkajian_operator.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.adapter.KajianUsulanAdapter;
import id.co.myproject.yukkajian_operator.adapter.RiwayatAdapter;
import id.co.myproject.yukkajian_operator.helper.RecyclerItemTouchHelper;
import id.co.myproject.yukkajian_operator.helper.RecyclerItemTouchHelperListener;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.Riwayat;
import id.co.myproject.yukkajian_operator.model.Value;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment implements RecyclerItemTouchHelperListener {

    private static final String TAG = "RiwayatFragment";

    LinearLayout rootRiwayatFragment, lvKoneksi, lvHistory;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvRiwayat;
    ApiRequest apiRequest;
    RiwayatAdapter riwayatAdapter;

    public RiwayatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        rvRiwayat = view.findViewById(R.id.rv_riwayat);
        lvHistory = view.findViewById(R.id.lv_no_history);
        rootRiwayatFragment = view.findViewById(R.id.rootRiwayatFragment);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        lvKoneksi = view.findViewById(R.id.lv_connection);

        rvRiwayat.setVisibility(View.INVISIBLE);
        riwayatAdapter = new RiwayatAdapter(getActivity(), apiRequest);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRiwayat.setAdapter(riwayatAdapter);


        ItemTouchHelper.SimpleCallback itemSimpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemSimpleCallback).attachToRecyclerView(rvRiwayat);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isConnectionInternet(getActivity())){
                    rootRiwayatFragment.setVisibility(View.VISIBLE);
                    lvKoneksi.setVisibility(View.GONE);
                    loadData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    rootRiwayatFragment.setVisibility(View.INVISIBLE);
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
                    rootRiwayatFragment.setVisibility(View.VISIBLE);
                    lvKoneksi.setVisibility(View.GONE);
                    loadData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    rootRiwayatFragment.setVisibility(View.INVISIBLE);
                    lvKoneksi.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        if (Utils.isConnectionInternet(getActivity())){
            rootRiwayatFragment.setVisibility(View.VISIBLE);
            lvKoneksi.setVisibility(View.GONE);
            loadData();
        }else {
            rootRiwayatFragment.setVisibility(View.INVISIBLE);
            lvKoneksi.setVisibility(View.VISIBLE);
            return;
        }
    }

    private void loadData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses...");
        progressDialog.show();
        swipeRefreshLayout.setRefreshing(false);
        Call<List<Riwayat>> callKajianUsulan = apiRequest.allRiwayatRequest();
        callKajianUsulan.enqueue(new Callback<List<Riwayat>>() {
            @Override
            public void onResponse(Call<List<Riwayat>> call, Response<List<Riwayat>> response) {
                List<Riwayat> kajianList = response.body();
                if (kajianList.size() > 0){
                    rvRiwayat.setVisibility(View.VISIBLE);
                    lvHistory.setVisibility(View.GONE);
                }else {
                    rvRiwayat.setVisibility(View.INVISIBLE);
                    lvHistory.setVisibility(View.VISIBLE);
                }
                riwayatAdapter.setRiwayatList(kajianList);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Riwayat>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RiwayatAdapter.ViewHolder){
            final Riwayat deleteItem = ((RiwayatAdapter)rvRiwayat.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            riwayatAdapter.removeItem(deleteIndex);
            Call<Value> callHapusPesan = apiRequest.hapusRiwayatRequest(deleteItem.getIdRiwayat());
            callHapusPesan.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    if (response.isSuccessful()){
                        if (response.body().getValue() == 1){
                            Snackbar snackbar = Snackbar.make(rootRiwayatFragment, response.body().getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
