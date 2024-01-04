<?php
require_once 'DatabaseConnection.php';

$database = new Database();

$tablename = 'costumer';
$operasi = $_GET['operasi'];

if(isset($_GET['key'])){
    $conditionSearch = [
        'nama_customer' => $_GET['key'],
    ];
}

if(isset($_GET['id'])){
    $condition = [
        'id_customer' => $_GET['id'],
    ];
}

switch ($operasi) {
    case 'tampil':
        $database->index($tablename);
    break;

    case 'getdata':
        $database->detailData($condition, $tablename);
    break;
    
    case 'search':
        $database->searchData($conditionSearch, $tablename);
    break;
    default: break;
}