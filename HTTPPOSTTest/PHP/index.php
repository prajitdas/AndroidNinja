<html>
    <head>
        <title>CurrApps Data Collection Tool</title>
    </head>
    <body>
        <?php
            class UserData {
                /* Variables to be set and stored in file */
                private $identity = "defaultIdentity";
                private $location = "defaultLocation";
                private $activity = "defaultActivity";
                private $time = "defaultTime";
                private $purpose = "defaultPurpose";
                private $listOfAppsRunning;
                private $listOfAppsInstalled;

                private $filename = "data/collectedData_";

                public function __construct($time) {
					if (!file_exists('data'))
						mkdir('data', 0777, true);
                    $this->listOfAppsRunning[] = "aDefaultApp";
					$this->filename = $this->filename . $time . ".csv";
                }

                public function init($key, $arguments){
                    switch($key){
                        case "location":
                            $this->location = $arguments;
                            break;
                        case "activity":
                            $this->activity = $arguments;
                            break;
                        case "time":
                            $this->time = $arguments;
                            break;
                        case "purpose":
                            $this->purpose = $arguments;
                            break;
                        case "listOfAppsRunning":
//							print_r($arguments);
//							echo "\n";
//							print_r($this->listOfAppsRunning);
//							echo "\n";
                            $this->listOfAppsRunning = $arguments;
//							print_r($arguments);
//							echo "\n";
//							print_r($this->listOfAppsRunning);
//							echo "\n";
                            break;
						case "identity":
							$this->location = $arguments;
                            break;
						case "appsInstalled":
                            $this->listOfAppsInstalled = $arguments;
                        default:
                            die("Unknown method.");
                    }
                }

                public function __toString() {
                    /* Get current EPOCH time for use as a primary key */
                    $temp = time();
                    $temp = $temp . "," . $this->location . "," . $this->activity . "," . $this->time . "," . $this->purpose . ",";
                    /* Changing the array to a semicolon separated string for easier use */
                    $appTempList = implode(";", $this->listOfAppsRunning);
                    $temp = $temp . $appTempList . "\n";
//                    echo($appTempList);
                    return $temp;
                }

                /* Defining a PHP write Function */
                public function storeDataToFile($dataToWrite) {
                    $file = fopen($this->filename, "a") or die("Unable to open file!");
                    fwrite($file,$dataToWrite);
                    fclose($file);
                }
            }

            /* 1. Encode json with the $_POST data */
            $json = json_encode($_POST);

            /* 2. Extract data */
            $phpArray = json_decode($json);

            /* 3. Create a UserData object to store the data */
            $userData = new UserData(date('m_d_Y'));

            /* 4. Initialize the private data variables */
            foreach($phpArray as $key => $value) {
                if($key=="listOfAppsRunning") {
                    echo "<h2>List of Apps Running</h2>";
                    $entityBody = stripslashes($value);
                    $jsonArray = json_decode($entityBody, true);
//                    echo "The array is: ";
//                    var_dump($value);
//                    echo "\n";
//                    var_dump($jsonArray);
//                    echo "\n";
                    foreach((array) $jsonArray as $k => $v)
                        echo "<h3>$k | $v </h3>";
                    $userData->init($key, $jsonArray);
                }
                elseif($key=="appsInstalled") {
                    echo "<h2>List of Apps Installed</h2>";
                    $entityBody = stripslashes($value);
                    $jsonArray = json_decode($entityBody, true);
                    foreach((array) $jsonArray as $k => $v)
                        echo "<h3>$k | $v </h3>";
                    $userData->init($key, $jsonArray);
                }
                else {
                    echo "<h2>$key | $value </h2>";
                    $userData->init($key, $value);
                }
            }

            /* 5. Call a PHP write function for storing data in csv file */
            $userData->storeDataToFile($userData->__toString());
        ?>
    </body>
</html>