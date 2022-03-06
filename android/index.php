<?php

require_once 'classes/user.php';
require_once 'classes/bill.php';
//$_POST = json_decode(file_get_contents('php://input'), true);
//error_reporting(E_ALL);
//ini_set('display_errors', 'on');
if (isset($_POST['requestReason'])) {

    if ($_POST['requestReason'] == 'login') {
        echo "U loginuu";
        $username = "";

        $password = "";

        $email = "";

        if (isset($_POST['username'])) {

            $username = $_POST['username'];

        }

        if (isset($_POST['password'])) {

            $password = $_POST['password'];

        }


        $userObject = new User();

        // Registration

        /*if(!empty($username) && !empty($password)){

            $hashed_password = sha1($password);

            $json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email);

            echo json_encode($json_registration);

        }*/

        // Login

        if (!empty($username) && !empty($password)) {

            $hashed_password = sha1($password);

            $json_array = $userObject->loginUsers($username, $hashed_password);

            echo json_encode($json_array);


        }
    } else if ($_POST['requestReason'] == 'insertBill') {
        $sta = "";

        $mjesec = "";

        $lokacija = "pofe";

        if (isset($_POST['sta'])) {

            $sta = $_POST['sta'];

        }

        if (isset($_POST['mjesec'])) {

            $mjesec = $_POST['mjesec'];

        }

        if (isset($_POST['lokacija'])) {

            $lokacija = $_POST['lokacija'];

        }


        $billObject = new Bill();

        if (!empty($sta) && !empty($mjesec) && !empty($lokacija)) {
            $json_array = $billObject->insertBill($mjesec, $sta, $lokacija);
            echo json_encode($json_array);
        }

    } else if ($_POST['requestReason'] == 'payBill') {

    } else if ($_POST['requestReason'] == 'insertPaidBill') {
        $sta = "";

        $iznos = "";

        $godina = date('Y');

        $mjesec = date('m') - 1;

        $lokacija = "pofe";


        if (isset($_POST['sta'])) {

            $sta = $_POST['sta'];

        }

        if (isset($_POST['iznos'])) {

            $iznos = $_POST['iznos'];

        }

        if (isset($_POST['godina'])) {

            $godina = $_POST['godina'];

        }

        if (isset($_POST['mjesec'])) {

            $mjesec = $_POST['mjesec'];

        }

        if (isset($_POST['lokacija'])) {

            $lokacija = $_POST['lokacija'];

        }

        $billObject = new Bill();
        $json_array = $billObject->insertPaidBill($sta, $iznos, $godina, $mjesec, $lokacija, $_POST['referenca']);
        echo json_encode($json_array);
    } else if ($_POST['requestReason'] == 'viewBills') {

    } else if ($_POST['requestReason'] == 'getSingleBill') {
        $sta = "";

        $godina = date('Y');

        $mjesec = date('m') - 1;

        if (isset($_POST['sta'])) {

            $sta = $_POST['sta'];

        }

        if (isset($_POST['mjesec'])) {

            $mjesec = $_POST['mjesec'];

        }


        if (isset($_POST['godina'])) {

            $godina = $_POST['godina'];

        }

        $billObject = new Bill();

        if (!empty($sta) && !empty($mjesec) && !empty($godina)) {
            $json_array = $billObject->getSingleBill($mjesec, $sta, $godina);
            echo json_encode($json_array);
        }


    }
}

if ($_GET["requestReason"] == "viewBills") {
    $billObject = new Bill();
    $json_array = $billObject->getBills();
    echo json_encode($json_array);
} else if ($_GET["requestReason"] == "getSingleBill") {

    $sta = "";

    $godina = date('Y');

    $mjesec = date('m') - 1;

    if (isset($_GET['sta'])) {

        $sta = $_GET['sta'];

    }

    if (isset($_GET['mjesec'])) {

        $mjesec = $_GET['mjesec'];

    }


    if (isset($_GET['godina'])) {

        $godina = $_GET['godina'];

    }

    $billObject = new Bill();

    if (!empty($sta) && !empty($mjesec) && !empty($godina)) {
        $json_array = $billObject->getSingleBill($mjesec, $sta, $godina);
        echo json_encode($json_array);
    } else {
        echo "Nista od te ljubavi";
    }


} else if ($_GET["requestReason"] == "getNonPaid") {
    $billObject = new Bill();
    $json_array = $billObject->getUnPaid();
    echo json_encode($json_array);
} else if ($_GET['requestReason'] == 'getPrice') {
    $mjesec = "";
    $godina = "";

    if (isset($_GET['godina'])) {

        $godina = $_GET['godina'];

    }

    if (isset($_GET['mjesec'])) {

        $mjesec = $_GET['mjesec'];

    }

    $billObject = new Bill();
    $json_array = $billObject->getPrice($mjesec, $godina);
    echo json_encode($json_array);
}

?>

