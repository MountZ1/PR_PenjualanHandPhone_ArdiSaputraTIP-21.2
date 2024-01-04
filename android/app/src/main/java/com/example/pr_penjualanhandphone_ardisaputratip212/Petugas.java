package com.example.pr_penjualanhandphone_ardisaputratip212;

public class Petugas extends koneksi{
    server server = new server();
    String conn = server.conn();
    String URL = conn + "/PR_PenjualanHandPhone_ArdiSaputraTIP-21.2/server/petugas.php";
    String url = "";
    String response = "";
    public void index(ResponseCallback callback){
        try {
            url = URL + "?operasi=tampil";
            runAsync(url, callback);
        } catch (Exception e){
            e.printStackTrace();
            callback.onFailure(e);
        }
    }
    public String store(String nama, String alamat, String notelp, String jabatan){
        try {
            url = URL + "?operasi=tambah&nama=" + nama + "&alamat=" + alamat + "&notelp="  + notelp + "&jabatan=" + jabatan;
            response = run(url);
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String getData(int id){
        try{
            url = URL + "?operasi=getdata&id=" + id;
            response = run(url);
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String update(String id, String nama, String alamat, String notelp, String jabatan){
        try{
            url = URL + "?operasi=update&nama=" + nama + "&alamat=" + alamat + "&notelp="  + notelp + "&jabatan=" + jabatan + "&id=" + id;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String destroy(int id){
        try {
            url = URL + "?operasi=delete&id=" + id;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
}
