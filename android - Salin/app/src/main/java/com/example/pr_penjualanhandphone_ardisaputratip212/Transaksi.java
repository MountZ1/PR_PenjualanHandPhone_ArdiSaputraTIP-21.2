package com.example.pr_penjualanhandphone_ardisaputratip212;

public class Transaksi extends koneksi{
    server server = new server();
    String conn = server.conn();
    String URL = conn + "/PR_PenjualanHandPhone_ArdiSaputraTIP-21.2/server/transaksi.php";
    String url = "";
    String response = "";

    public void index(ResponseCallback callback){
        try{
            url = URL + "?operasi=tampil";
            runAsync(url, callback);
        }catch (Exception e){
            e.printStackTrace();
            callback.onFailure(e);
        }
    }
    public String store(String invoice, String namacust, String notelpcust, String idbarang, String hargaperbarang, String jumlahbeli, String total, String methode, String idpetugas){
        try{
            url = URL + "?operasi=tambah&namacus=" + namacust + "&notelpcust=" + notelpcust + "&idbarang=" +
                    idbarang + "&harga=" + hargaperbarang + "&jumlah=" + jumlahbeli + "&total=" +
                    total + "&methode=" + methode + "&idpetugas=" + idpetugas + "&invoice=" + invoice;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String getData(int id){
        try{
            url = URL + "?operasi=getdata&id=" + id;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
    public String search(String key){
        try{
            url = URL + "?operasi=search&key=" + key;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
}
