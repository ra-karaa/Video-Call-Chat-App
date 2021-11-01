package ra.enterwind.lapaskumala.models;

public class Comment {

    public String judul, isi, member, member_foto, waktu;

    public Comment(){

    }

    public Comment(String judul, String isi, String member, String member_foto, String waktu){
        this.judul = judul;
        this.isi = isi;
        this.member = member;
        this.member_foto = member_foto;
        this.waktu = waktu;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMember_foto() {
        return member_foto;
    }

    public void setMember_foto(String member_foto) {
        this.member_foto = member_foto;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
