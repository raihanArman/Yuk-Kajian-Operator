package id.co.myproject.yukkajian_operator.request;

import java.util.List;

import id.co.myproject.yukkajian_operator.model.JenisKajian;
import id.co.myproject.yukkajian_operator.model.Kajian;
import id.co.myproject.yukkajian_operator.model.Kategori;
import id.co.myproject.yukkajian_operator.model.Lampiran;
import id.co.myproject.yukkajian_operator.model.Riwayat;
import id.co.myproject.yukkajian_operator.model.User;
import id.co.myproject.yukkajian_operator.model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {
    @FormUrlEncoded
    @POST("registrasi_user.php")
    Call<Value> registrasiUserRequest(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("login_operator.php")
    Call<Value> loginOperatorRequest(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("tampil_kajian.php")
    Call<List<Kajian>> allKajianRequest();


    @GET("tampil_riwayat.php")
    Call<List<Riwayat>> allRiwayatRequest();

    @GET("tampil_kajian.php")
    Call<List<Kajian>> cariKajianRequest(
            @Query("cari") String cari
    );

    @GET("tampil_kajian.php")
    Call<Kajian> kajianByIdRequest(
            @Query("id_kajian") String idKajian
    );

    @GET("tampil_kajian_usulan.php")
    Call<List<Kajian>> kajianByStatus(
            @Query("status") String status
    );

    @GET("tampil_kategori.php")
    Call<Kategori> kategoriByIdRequest(
            @Query("id_kategori") String idKategori
    );

    @GET("tampil_kategori.php")
    Call<List<Kategori>> allKategoriRequest();

    @GET("tampil_jenis_kajian.php")
    Call<JenisKajian> jenisKajianByIdRequest(
            @Query("id_jenis_kajian") String idjenisKajian
    );

    @GET("tampil_jenis_kajian.php")
    Call<List<JenisKajian>> allJenisKajianRequest();

    @GET("tampil_kajian.php")
    Call<List<Kajian>> kajianByDayRequest(
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2
    );

    @GET("tampil_kajian.php")
    Call<List<Kajian>> kajianByDayAndKategoriRequest(
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2,
            @Query("id_kategori") String idKategori
    );

    @GET("tampil_kajian.php")
    Call<List<Kajian>> kajianByKategoriRequest(
            @Query("id_kategori") String idKategori
    );

    @FormUrlEncoded
    @POST("input_kajian.php")
    Call<Value> inputKajianRequest(
            @Field("id_user") int idUser,
            @Field("id_jenis_kajian") String id_jenis_kajian,
            @Field("id_kategori") String id_kategori,
            @Field("judul_kajian") String judul_kajian,
            @Field("pemateri") String pemateri,
            @Field("tanggal") String tanggal,
            @Field("gambar") String gambar,
            @Field("lokasi") String lokasi,
            @Field("no_telp_pemateri") String noTelpPemateri
    );

    @FormUrlEncoded
    @POST("input_lampiran.php")
    Call<Value> inputLampiranRequest(
            @Field("id_usulan") int idUsulan,
            @Field("keterangan") String keterangan,
            @Field("gambar") String gambar
    );

    @GET("tampil_kajian_usulan.php")
    Call<List<Kajian>> allKajianUsulan(
            @Query("id_user") int idUser
    );

    @GET("tampil_lampiran.php")
    Call<List<Lampiran>> allLampiranRequest(
            @Query("id_usulan") String idUsulan
    );

    @GET("tampil_user.php")
    Call<User> userByIdRequest(
            @Query("id_user") int idUser
    );

    @FormUrlEncoded
    @POST("edit_profil_operator.php")
    Call<Value> editProfilRequest(
            @Field("id_user") int idUser,
            @Field("username") String username,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("no_telp") String no_telp,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("cek_email.php")
    Call<Value> cekEmailRequest(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("lupa_password.php")
    Call<Value> lupaPasswordRequest(
            @Field("id_user") int idUser,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("tambah_pesan.php")
    Call<Value> tambahPesanRequest(
            @Field("id_user") String idUser,
            @Field("pengirim") int pengirim,
            @Field("isi") String isi
    );

    @FormUrlEncoded
    @POST("update_usulan.php")
    Call<Value> updateUsulanRequest(
            @Field("id_usulan") String idUsulan,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("hapus_riwayat.php")
    Call<Value> hapusRiwayatRequest(
            @Field("id_riwayat") String idRiwayat
    );

    @FormUrlEncoded
    @POST("tambah_riwayat.php")
    Call<Value> tambahRiwayatRequest(
            @Field("id_usulan") String idUsulan
    );

}
