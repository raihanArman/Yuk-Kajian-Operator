package id.co.myproject.yukkajian_operator.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import id.co.myproject.yukkajian_operator.BuildConfig;
import id.co.myproject.yukkajian_operator.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoViewFragment extends DialogFragment {

    String gambar;
    public static final int GAMBAR_LAMPIRAN = 918;
    public static final int GAMBAR_PAMFLET = 916;
    int typeGambar;
    public PhotoViewFragment(String gambar, int typeGambar) {
        // Required empty public constructor
        this.gambar = gambar;
        this.typeGambar = typeGambar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PhotoView photoView = view.findViewById(R.id.photoView);
        if (typeGambar == GAMBAR_LAMPIRAN){
            Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"lampiran/"+gambar).into(photoView);
        }else {
            Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"pamflet/"+gambar).into(photoView);
        }

    }
}
