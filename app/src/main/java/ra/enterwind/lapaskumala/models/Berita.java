package ra.enterwind.lapaskumala.models;

public class Berita {
    public String id, judul, kontent, image, tanggal, komen;

    public Berita(){

    }

    public Berita(String id, String judul, String kontent, String image, String tanggal, String komen){
        this.id = id;
        this.judul = judul;
        this.kontent = kontent;
        this.image = image;
        this.tanggal = tanggal;
        this.komen = komen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKontent() {
        return kontent;
    }

    public void setKontent(String kontent) {
        this.kontent = kontent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKomen() {
        return komen;
    }

    public void setKomen(String komen) {
        this.komen = komen;
    }
}
