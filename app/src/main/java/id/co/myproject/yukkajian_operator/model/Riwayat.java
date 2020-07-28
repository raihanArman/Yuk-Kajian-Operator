package id.co.myproject.yukkajian_operator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Riwayat {
    @SerializedName("id_kajian")
    @Expose
    private String idKajian;

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("id_jenis_kajian")
    @Expose
    private String idJenisKajian;

    @SerializedName("id_kategori")
    @Expose
    private String idKategori;

    @SerializedName("judul_kajian")
    @Expose
    private String judul_kajian;

    @SerializedName("pemateri")
    @Expose
    private String pemateri;

    @SerializedName("tanggal")
    @Expose
    private Date tanggal;

    @SerializedName("tanggal_upload")
    @Expose
    private Date tanggalUpload;

    @SerializedName("gambar")
    @Expose
    private String gambar;

    @SerializedName("lokasi")
    @Expose
    private String lokasi;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("id_usulan")
    @Expose
    private String idUsulan;

    @SerializedName("id_riwayat")
    @Expose
    private String idRiwayat;

    @SerializedName("tanggal_update")
    @Expose
    private Date tanggalUpdate;

    @SerializedName("no_telp_pemateri")
    @Expose
    private String noTelpPemateri;

    public Riwayat() {
    }

    public Riwayat(String idKajian, String idUser, String idJenisKajian, String idKategori, String judul_kajian, String pemateri, Date tanggal, Date tanggalUpload, String gambar, String lokasi, String status, String idUsulan, String idRiwayat, Date tanggalUpdate, String noTelpPemateri) {
        this.idKajian = idKajian;
        this.idUser = idUser;
        this.idJenisKajian = idJenisKajian;
        this.idKategori = idKategori;
        this.judul_kajian = judul_kajian;
        this.pemateri = pemateri;
        this.tanggal = tanggal;
        this.tanggalUpload = tanggalUpload;
        this.gambar = gambar;
        this.lokasi = lokasi;
        this.status = status;
        this.idUsulan = idUsulan;
        this.idRiwayat = idRiwayat;
        this.tanggalUpdate = tanggalUpdate;
        this.noTelpPemateri = noTelpPemateri;
    }

    public String getIdKajian() {
        return idKajian;
    }

    public void setIdKajian(String idKajian) {
        this.idKajian = idKajian;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdJenisKajian() {
        return idJenisKajian;
    }

    public void setIdJenisKajian(String idJenisKajian) {
        this.idJenisKajian = idJenisKajian;
    }

    public String getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(String idKategori) {
        this.idKategori = idKategori;
    }

    public String getJudul_kajian() {
        return judul_kajian;
    }

    public void setJudul_kajian(String judul_kajian) {
        this.judul_kajian = judul_kajian;
    }

    public String getPemateri() {
        return pemateri;
    }

    public void setPemateri(String pemateri) {
        this.pemateri = pemateri;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Date getTanggalUpload() {
        return tanggalUpload;
    }

    public void setTanggalUpload(Date tanggalUpload) {
        this.tanggalUpload = tanggalUpload;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdUsulan() {
        return idUsulan;
    }

    public void setIdUsulan(String idUsulan) {
        this.idUsulan = idUsulan;
    }

    public String getIdRiwayat() {
        return idRiwayat;
    }

    public void setIdRiwayat(String idRiwayat) {
        this.idRiwayat = idRiwayat;
    }

    public Date getTanggalUpdate() {
        return tanggalUpdate;
    }

    public void setTanggalUpdate(Date tanggalUpdate) {
        this.tanggalUpdate = tanggalUpdate;
    }

    public String getNoTelpPemateri() {
        return noTelpPemateri;
    }

    public void setNoTelpPemateri(String noTelpPemateri) {
        this.noTelpPemateri = noTelpPemateri;
    }
}
