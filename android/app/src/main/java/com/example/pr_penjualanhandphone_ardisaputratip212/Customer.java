package com.example.pr_penjualanhandphone_ardisaputratip212;

public class Customer extends koneksi{
    server server = new server();
    String conn = server.conn();
    String URL = conn + "/PR_PenjualanHandPhone_ArdiSaputraTIP-21.2/server/customer.php";
    String url = "";
    String response = "";
    public String index(){
        try{
            url = URL + "?operasi=tampil";
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
    public String search(String key){
        try {
            url = URL + "?operasi=search&key=" + key;
            response = run(url);
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return response;
    }
}
