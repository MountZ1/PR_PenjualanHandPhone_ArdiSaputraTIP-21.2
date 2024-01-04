package com.example.pr_penjualanhandphone_ardisaputratip212;

public class Barang extends koneksi{
    server server = new server();
    String conn = server.conn();
    String URL = conn + "/PR_PenjualanHandPhone_ArdiSaputraTIP-21.2/server/barang.php";
    String url = "";
    String response = "";

    public void index(ResponseCallback callback){
        try{
            url = URL + "?operasi=tampil";
            runAsync(url, callback);
        } catch (Exception e){
            e.printStackTrace();
            callback.onFailure(e);
        }
    }
    public String store(String kdbarang, String merk, String tipe, String warna, String ram, String storage, String hargabeli, String hargajual, String stok){
        try {
            url = URL + "?operasi=tambah&kdbarang=" + kdbarang + "&merk=" + merk + "&tipe="  + tipe +
                    "&warna=" + warna + "&ram=" + ram + "&storage=" + storage + "&hargabeli=" + hargabeli +
                    "&hargajual=" + hargajual + "&stok=" + stok;
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
    public String update(String id, String kdbarang, String merk, String tipe, String warna, String ram, String storage, String hargabeli, String hargajual, String stok){
        try{
            url = URL + "?operasi=update&kdbarang=" + kdbarang + "&merk=" + merk + "&tipe="  + tipe +
                    "&warna=" + warna + "&ram=" + ram + "&storage=" + storage + "&hargabeli=" + hargabeli +
                    "&hargajual=" + hargajual + "&stok=" + stok + "&id=" + id;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String destroy(int kdbarang){
        try {
            url = URL + "?operasi=delete&id=" + kdbarang;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public void search(String key, ResponseCallback callback){
        try{
            url = URL + "?operasi=search&key=" + key;
            runAsync(url, callback);
        } catch (Exception e){
            e.printStackTrace();
            callback.onFailure(e);
        }
    }
    public String selectDataonkd(String key){
        try{
            url = URL + "?operasi=getdatafortransaksi&keykode=" + key;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
