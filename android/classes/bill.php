<?php

include_once 'db-connect.php';

class Bill
{

    private $db;

    private $db_table = "racuni";

    public function __construct()
    {
        $this->db = new DbConnect();
    }


////////////////VALIDACIJA
    private function isValid($mjesec, $sta, $lokacija)
    {
        $first = array('epiznos', 'vikiznos', 'ssiznos', 'toplaneiznos', 'teleiznos', 'radiznos', 'pofe', 'cvila', 'hetig');
        return ($mjesec != "" && $sta != "" && ($mjesec >= 1 && $mjesec <= 12) && in_array($sta, $first) && ($lokacija == "pofe" || $lokacija == "cvila"));
    }


//////////////////INSERT BILL
    public function insertBill($mjesec, $sta, $lokacija)
    {
        $json = array();
        $canInsertBill = $this->isValid($mjesec, $sta, $lokacija);

        if ($canInsertBill) {
            $inserted = $this->isInserted($mjesec, $sta, $lokacija);
            if ($inserted) {
                $json['success'] = 1;
                $json['message'] = "Successfully inserted bill";
            } else {
                $json['success'] = 0;
                $json['message'] = "Error while inserting in the database";
            }
        } else {
            $json['success'] = 0;
            $json['message'] = "Incorrect details";
        }
        return $json;
    }


    private function isInserted($mjesec, $sta, $lokacija)
    {

//        $query = "insert into ".$this->db_table." (sta,mjesec,iznos,lokacija,placeno)
//values ('teleiznos', 11, 0, 'pofe', 0)";
        $godina = date('Y');
        $query = "INSERT INTO racuni (sta, mjesec, iznos, lokacija, placeno, godina) VALUES ('$sta', $mjesec, 0, '$lokacija', 0, $godina)";

        $result = mysqli_query($this->db->getDb(), $query);

        if ($result) {

            mysqli_close($this->db->getDb());

            return true;

        } else {
            echo $this->db->getDb()->error;
        }

        mysqli_close($this->db->getDb());

        return false;

    }


///////////////////////////INSERT PAID
    public function insertPaidBill($sta, $iznos, $godina, $mjesec, $lokacija, $referenca)
    {
        $json = array();
        $canInsertBill = $this->isValid($mjesec, $sta, $lokacija);
        if ($canInsertBill) {
            $inserted = $this->isPaidInserted($mjesec, $sta, $lokacija, $iznos, $godina, $referenca);
            if ($inserted) {
                $json['success'] = 1;
                $json['message'] = "Successfully inserted bill";
            } else {
                $json['success'] = 0;
                $json['message'] = "Error while inserting in the database";
            }
        } else {
            $json['success'] = 0;
            $json['message'] = "Incorrect details";
        }
        return $json;
    }

    private function isPaidInserted($mjesec, $sta, $lokacija, $iznos, $godina, $referenca)
    {

//        $query = "insert into ".$this->db_table." (sta,mjesec,iznos,lokacija,placeno)
//values ('teleiznos', 11, 0, 'pofe', 0)";
        $query = "INSERT INTO racuni (sta, mjesec, iznos, lokacija, placeno, godina, referenca) VALUES ('$sta', $mjesec-1, $iznos, '$lokacija', 1, $godina, '$referenca')";

        $result = mysqli_query($this->db->getDb(), $query);

        if ($result) {

            mysqli_close($this->db->getDb());

            return true;

        } else {
            echo $this->db->getDb()->error;
        }

        mysqli_close($this->db->getDb());

        return false;

    }


//////////////////////GET UNPAID
    public function getUnPaid()
    {
        $json = array();

        $unpaidresult = $this->isUnPaidExist();

        if ($unpaidresult != "") {

            $json['success'] = 2;
            $json['message'] = $unpaidresult;

        } else {
            $json['success'] = 0;
            $json['message'] = "No unpaid bills";
        }
        return $json;
    }

    private function isUnPaidExist()
    {
        $query = "select * from " . $this->db_table . " where placeno = 0";

        $result = mysqli_query($this->db->getDb(), $query);

        $mojArray = array();

        while ($row = mysqli_fetch_array($result)) $mojArray[] = $row;
        if (!empty($mojArray)) {

            mysqli_close($this->db->getDb());


            return $mojArray;

        }

        mysqli_close($this->db->getDb());

        return "";
    }


///////////////////GET ALL BILLS
    public function getBills()
    {
        $json = array();

        $result = $this->isgetBillsExist();

        if ($result != "") {

            $json['success'] = 2;
            $json['message'] = $result;

        } else {
            $json['success'] = 0;
            $json['message'] = "No unpaid bills";
        }
        return $json;
    }


    private function isgetBillsExist()
    {
        $query = "select * from " . $this->db_table;

        $result = mysqli_query($this->db->getDb(), $query);

        $mojArray = array();

        while ($row = mysqli_fetch_array($result)) $mojArray[] = $row;
        if (!empty($mojArray)) {

            mysqli_close($this->db->getDb());


            return $mojArray;

        }

        mysqli_close($this->db->getDb());

        return "";
    }


//////////////GET SINGLE BILL    
    public function getSingleBill($mjesec, $sta, $godina)
    {
        $json = array();
        $canGetBill = $this->isValid($mjesec, $sta, "pofe");

        if ($canGetBill) {
            $billko = $this->isGetBillExist($mjesec, $sta, $godina);

            if ($billko != "") {
                $json['success'] = 3;
                $json['message'] = $billko;

            } else {
                $json['success'] = 0;
                $json['message'] = "Bill doesn't exist";
            }

            return $json;
        }
    }

    private function isGetBillExist($mjesec, $sta, $godina)
    {
        $query = "select * from " . $this->db_table . " where sta = '" . $sta . "' AND mjesec = " . $mjesec . " AND godina = " . $godina;

        $result = mysqli_query($this->db->getDb(), $query);

        $mojArray = array();

        while ($row = mysqli_fetch_array($result)) $mojArray[] = $row;
        if (!empty($mojArray)) {

            mysqli_close($this->db->getDb());


            return $mojArray;

        }

        mysqli_close($this->db->getDb());

        return "";
    }


/////////////////////////// GET PRICE
    public function getPrice($mjesec, $godina)
    {
        $json = array();

        $unpaidresult = $this->isGetPriceValid($mjesec, $godina);

        if ($unpaidresult != "") {

            $json['success'] = 2;
            $json['message'] = $unpaidresult;

        } else {
            $json['success'] = 0;
            $json['message'] = "No unpaid bills";
        }
        return $json;
    }

    private function isGetPriceValid($mjesec, $godina)
    {
        //$godina = date('Y');
        //$mjesec = date('m')-1;

        $query = "select * from " . $this->db_table . " where placeno = 0";
        $query = "SELECT r.lokacija 'Lokacija', SUM(r.iznos) 'Iznos po lokaciji' FROM " . $this->db_table . " r WHERE godina = " . $godina . " AND mjesec = " . $mjesec . " GROUP BY r.lokacija UNION SELECT 'Ukupno', SUM(iznos) FROM " . $this->db_table . " WHERE godina=" . $godina . " AND mjesec = " . $mjesec . "";
        $result = mysqli_query($this->db->getDb(), $query);

        $mojArray = array();

        while ($row = mysqli_fetch_array($result)) $mojArray[] = $row;
        if (!empty($mojArray)) {

            mysqli_close($this->db->getDb());


            return $mojArray;

        }

        mysqli_close($this->db->getDb());

        return "";
    }

}

?>

