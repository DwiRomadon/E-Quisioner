package helper;

/**
 * Created by Terminator on 04/06/2017.
 */

public class NewsDataAdmin {

    private String id, nomor, tgl , isi;

    public NewsDataAdmin(){}

    public NewsDataAdmin(String id, String nomor, String tgl, String isi){
        this.id = id;
        this.nomor = nomor;
        this.tgl = tgl;
        this.isi = isi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }
}
