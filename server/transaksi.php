<?php
require_once 'DatabaseConnection.php';

$database = new Database();

$tablename = 'transaksi';
$operasi = $_GET['operasi'];
$tanggal = date("d-m-Y");
$randomNumber = mt_rand(1, 1000) or mt_rand(1,100) or mt_rand(1,10);
$formatNUmber = str_pad($randomNumber, 4, '0', STR_PAD_LEFT);
$invoicenumber = "INV-" . date("dmy") . $formatNUmber;
if (isset($_GET['invoice'], $_GET['idbarang'], $_GET['harga'], $_GET['jumlah'], $_GET['total'], $_GET['methode'], $_GET['idpetugas'])){
    $data = [
        'invoice' => $_GET['invoice'],
        'tanggal_transaksi' => $tanggal,
        'idbarang' => $_GET['idbarang'],
        'harga_per_barang' => $_GET['harga'],
        'jumlah_beli' => $_GET['jumlah'],
        'total_transaksi' => intval(str_replace(".", "", $_GET['total'])),
        'methode_bayar' => $_GET['methode'],
        'id_petugas' => $_GET['idpetugas']
    ];
}
if(isset($_GET['id'])){
    $condition = [
        'idpembelian' => $_GET['id']
    ];
}
#for update barang
$barangtable = 'barang';
if(isset($_GET['idbarang'])){
    $barangcondition= [
        'idbarang' => $_GET['idbarang']
    ];
}
//for costumer
$tablenamCustomer = 'costumer';
if (isset($_GET['namacus'], $_GET['notelpcust'])){
    $custData = [
        'nama_customer' => $_GET['namacus'],
        'no_telp_customer' => $_GET['notelpcust']
    ];
}

//for search
if(isset($_GET['key'])){
    $conditionSearch = [
        'invoice' => $_GET['key'],
    ];
}

switch ($operasi) {
    case 'tampil':
        $database->index($tablename);
    break;

    case "tambah":
        $jumlah = $_GET['jumlah'];

        $database->storeTransaction($tablenamCustomer, $custData, $tablename, $data, $barangtable, $jumlah, $barangcondition);
    break;

    case 'getdata':
        $database->getDataForTransaction($condition, $tablename);
    break;

    case 'search':
        $database->searchData($conditionSearch, $tablename);
    break;
    
    default: break;
}