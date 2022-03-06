<?php
if($_GET["requestReason"] != "getPrice") {
?>

<form action="cijene.php" method="get">
	<input type="hidden" name="requestReason" value="getPrice" />
        <input type="text" name="mjesec" />
        <input type="text" name="godina" />
    <input type="submit" value="submit" />
</form>

<?php
} else {
require_once 'classes/bill.php';
	$mjesec = date('m')-1;
    	$godina = date('Y');
    	
    	if (isset($_GET['godina'])) {

            $godina = $_GET['godina'];

        }

        if (isset($_GET['mjesec'])) {

            $mjesec = $_GET['mjesec'];

        }
        
    	$billObject = new Bill();
        $json_array = $billObject->getPrice($mjesec, $godina);
	header('Content-Type: application/json');
        echo json_encode($json_array, JSON_PRETTY_PRINT);

}

?>
