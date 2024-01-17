<<<<<<< HEAD
<?php
require_once 'DatabaseConnection.php';

$database = new Database();

$tablename = 'barang';
$operasi = $_GET['operasi'];

if (isset($_GET['kdbarang'], $_GET['merk'], $_GET['tipe'], $_GET['warna'], $_GET['ram'], $_GET['storage'], $_GET['hargabeli'], $_GET['hargajual'], $_GET['stok'])) {
    $data = [
        'KDBARANG' => $_GET['kdbarang'],
        'MERK' => $_GET['merk'],
        'TIPE' => $_GET['tipe'],
        'WARNA' => $_GET['warna'],
        'RAM' => $_GET['ram'],
        'STORAGE' => $_GET['storage'],
        'HARGA_BELI' => $_GET['hargabeli'],
        'HARGA_JUAL' => $_GET['hargajual'],
        'STOK_BARANG' => $_GET['stok']
    ];
}

if(isset($_GET['id'])){
    $condition = [
        'IDBARANG' => $_GET['id']
    ];
}


if(isset($_GET['key'])){
    $conditionSearch = [
        'MERK' => $_GET['key'],
    ];
}
if(isset($_GET['keykode'])){
    $conditionTransaksi = [
        'KDBARANG' => $_GET['keykode'],
    ];
}

switch ($operasi) {
    case 'tampil':
        $database->index($tablename);
    break;

    case 'tambah':
        $database->store($data, $tablename);
    break;

    case 'getdata':
        $database->detailData($condition, $tablename);
    break;

    case 'update':
        $database->update($data, $tablename, $condition);
    break;

    case 'delete':
        $database->destroy($condition, $tablename);
    break;

    case 'search':
        // var_dump($conditionSearch);
        $database->searchData($conditionSearch, $tablename);
    break;
    case 'getdatafortransaksi':
        $database->detailData($conditionTransaksi, $tablename);
    break;
    
    default: break;
