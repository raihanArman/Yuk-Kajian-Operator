package id.co.myproject.yukkajian_operator.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.yukkajian_operator.view.MainActivity;
import id.co.myproject.yukkajian_operator.R;
import id.co.myproject.yukkajian_operator.helper.Utils;
import id.co.myproject.yukkajian_operator.model.Value;
import id.co.myproject.yukkajian_operator.request.ApiRequest;
import id.co.myproject.yukkajian_operator.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.yukkajian_operator.helper.Utils.ID_USER_KEY;
import static id.co.myproject.yukkajian_operator.helper.Utils.LOGIN_KEY;
import static id.co.myproject.yukkajian_operator.helper.Utils.LOGIN_STATUS;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    ImageView signInButton;
    EditText etUsername, etPassword;
    Button btnSignIn;
    TextView tvLupaPassword, tvUsername;
    FrameLayout parentFrameLayout;
    int idUser, login_level;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    ApiRequest apiRequest;
    private boolean userExists = false;
    ProgressDialog progressDialog;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsername = view.findViewById(R.id.et_usename);
        etPassword = view.findViewById(R.id.et_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        tvUsername = view.findViewById(R.id.tv_username);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memproses ...");
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectionInternet(getActivity()))
                    cekForm();
                else
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void cekForm(){
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (!TextUtils.isEmpty(etUsername.getText())){
            if (!TextUtils.isEmpty(etPassword.getText())){
                progressDialog.show();
                prosesLogin(email, password);
            }else {
                etPassword.setError("Password tidak boleh kosong");
            }
        }else {
            etUsername.setError("Username tidak boleh kosong");
        }

    }

    private void updateUI() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        boolean statusLogin = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        if (statusLogin){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else {
            progressDialog.dismiss();
            setFragment(new SignInFragment());
        }
    }

    private void prosesLogin(final String username, final String password) {
        Call<Value> callLoginUser = apiRequest.loginOperatorRequest(username, password);
        callLoginUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue() == 1) {
                        idUser = response.body().getIdUser();
                        progressDialog.dismiss();
                        editor.putInt(ID_USER_KEY, idUser);
                        editor.putBoolean(LOGIN_STATUS, true);
                        editor.commit();
                        updateUI();
                    } else {
                        btnSignIn.setEnabled(true);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }
}
