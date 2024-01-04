<?php

class Database{
    private $host = 'localhost';
    private $username = 'mountz';
    private $pass = 'z8579973#&+';
    private $dbname = 'penjualan_handphone';

    private $dbh;
    private $statement;
    private $message;

    public function __construct()
    {
        try {
            $this->dbh = new mysqli($this->host, $this->username, $this->pass, $this->dbname);
        } catch (Exception $e) {
            throw new ErrorException($e->getMessage());
        }
    }

    public function retriveData($data){
        $columns = implode(', ', array_keys($data));
        $values = "'" . implode("', '", array_values($data)) . "'";
        
        return [$columns, $values];
    }

    public function index($tbname){
        $this->statement = $this->dbh->query("SELECT * FROM $tbname");
        while($data = $this->statement->fetch_assoc()){
            $datakirim[] = $data;
        }
        
        echo json_encode($datakirim);
    }

    public function store($data, $tbname){
        list($columns, $values) = $this->retriveData($data);
        // var_dump($columns);
        // var_dump($values);
        $this->statement = $this->dbh->query("INSERT INTO $tbname ($columns) VALUES ($values)");

        $this->message = (!$this->statement) ? 'Data gagal disimpan' . mysqli_errno($this->dbh) : 'Data berhasil disimpan';
        echo $this->message;
    }

    public function detailData($condition, $tbname){
        list($columns, $values) = $this->retriveData($condition);

        $this->statement = $this->dbh->query("SELECT * FROM $tbname WHERE $columns=$values");
        //statement = mysqli_query("select * from petugas where idpetugas='id'")
        $data = $this->statement->fetch_assoc();

        echo json_encode($data);
    }
    
    public function update($data, $tbname, $condition){
        list($conditionColumns, $conditionValues) = $this->retriveData($condition);
        foreach ($data as $key => $value) {
            $updateData[] = "\n".$key."" ." = "."'".$value."'";
         }
        $finalData = implode(",",$updateData);

        $this->statement = $this->dbh->query("UPDATE $tbname SET $finalData WHERE $conditionColumns=$conditionValues");
        $this->message = (!$this->statement) ? 'Data gagal diupdate' . mysqli_errno($this->dbh) : 'Data berhasil diupdate';

        echo $this->message;
    }

    public function destroy($condition, $tbname){
        list($columns, $values) = $this->retriveData($condition);

        $this->statement = $this->dbh->query("DELETE FROM $tbname WHERE $columns=$values");
        $this->message = (!$this->statement) ? 'Data gagal dihapus' . mysqli_errno($this->dbh) : 'Data berhasil dihapus';

        echo $this->message;
    }
    
    //untuk fitur search
    public function searchData($condition, $tbname){
        list($columns, $values) = $this->retriveData($condition);
        $val = str_replace("''", "", $values);
        
        $this->statement = $this->dbh->query("SELECT * FROM $tbname WHERE $columns LIKE $values");
        while($data = $this->statement->fetch_assoc()){
            $datakirim[] = $data;
        }

        echo json_encode($datakirim);
        // echo json_encode()
    }

    public function storeTransaction($tbcust, $datacust, $tbtransaksi, $dattransaksi, $barangtable, $jumlahbeli, $barangconditon){
        list($colcust, $valcust) = $this->retriveData($datacust);
        list($colbarangconditin, $valbarangcondition) = $this->retriveData($barangconditon);
        list($coltransaksi, $valtransaksi) = $this->retriveData($dattransaksi);

        $this->dbh->query("INSERT INTO $tbcust ($colcust) VALUES ($valcust)");

        // Execute the second query
        $this->dbh->query("SET @idcustomer = LAST_INSERT_ID()");

        // Execute the third query
        $this->dbh->query("INSERT INTO $tbtransaksi ($coltransaksi, id_costumer) VALUES ($valtransaksi, @idcustomer)");

        // Execute the fourth query
        $this->dbh->query("UPDATE $barangtable SET STOK_BARANG = STOK_BARANG - $jumlahbeli WHERE $colbarangconditin = $valbarangcondition");

        echo $this->message;
    }
    
    public function getDataForTransaction($condition, $tbname){
        list($columns, $values) = $this->retriveData($condition);

        $this->statement = $this->dbh->query("SELECT transaksi.invoice, transaksi.tanggal_transaksi, transaksi.harga_per_barang, transaksi.jumlah_beli, transaksi.total_transaksi, transaksi.methode_bayar, costumer.nama_customer, barang.KDBARANG, barang.MERK, barang.TIPE, barang.WARNA, barang.RAM, barang.STORAGE, petugas.nama_petugas FROM $tbname INNER JOIN costumer ON transaksi.id_costumer = costumer.id_customer INNER JOIN barang ON transaksi.idbarang = barang.IDBARANG INNER JOIN petugas ON transaksi.id_petugas = petugas.id_petugas WHERE $columns=$values");

        $data = $this->statement->fetch_assoc();

        echo json_encode($data);
    }
}