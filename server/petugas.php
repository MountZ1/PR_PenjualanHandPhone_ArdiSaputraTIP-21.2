<?php
//include 'database.php';
require_once 'DatabaseConnection.php';

$database = new Database();

$tablename = 'petugas';
$operasi = $_GET['operasi'];

if (isset($_GET['nama'], $_GET['alamat'], $_GET['notelp'], $_GET['jabatan'])) {
    $data = [
        'nama_petugas' => $_GET['nama'],
        'alamat' => $_GET['alamat'],
        'no_telp' => $_GET['notelp'],
        'jabatan' => $_GET['jabatan']
    ];
}

if(isset($_GET['id'])){
    $condition = [
        'id_petugas' => $_GET['id']
    ];
}
//var_dump($data);

switch ($operasi) {
    case 'tampil':
        $database->index($tablename);
        //echo json_encode($datakirim);
    break;
    
    case 'tambah':
        //var_dump($data);
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
    
    default: break;
}