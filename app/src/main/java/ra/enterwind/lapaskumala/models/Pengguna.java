package ra.enterwind.lapaskumala.models;

public class Pengguna {

    public String id, nama, foto, username, plain, one_id, peer_id;

    public Pengguna(){

    }

    public Pengguna(String id, String nama, String foto, String username, String plain, String one_id, String peer_id){
        this.id = id;
        this.nama = nama;
        this.foto = foto;
        this.username = username;
        this.plain = plain;
        this.one_id = one_id;
        this.peer_id = peer_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlain() {
        return plain;
    }

    public void setPlain(String plain) {
        this.plain = plain;
    }

    public String getOne_id() {
        return one_id;
    }

    public void setOne_id(String one_id) {
        this.one_id = one_id;
    }

    public String getPeer_id() {
        return peer_id;
    }

    public void setPeer_id(String peer_id) {
        this.peer_id = peer_id;
    }
}
