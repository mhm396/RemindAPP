package com.example.mifirestore01.Notas2;

public class ShareNote {

    private String idNote;
    private String idUsuarioDestino;
    private String idUsuarioOrigen;

    public ShareNote() {

    }

    public ShareNote(String idNote, String idUsuarioDestino, String idUsuarioOrigen) {
        this.idNote = idNote;
        this.idUsuarioDestino = idUsuarioDestino;
        this.idUsuarioOrigen = idUsuarioOrigen;
    }

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    public String getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(String idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }

    public String getIdUsuarioOrigen() {
        return idUsuarioOrigen;
    }

    public void setIdUsuarioOrigen(String idUsuarioOrigen) {
        this.idUsuarioOrigen = idUsuarioOrigen;
    }
}
