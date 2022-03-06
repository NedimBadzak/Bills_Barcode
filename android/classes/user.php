<?php
    
    include_once 'db-connect.php';
    
    class User{
        
        private $db;
        
        private $db_table = "users";
        
        public function __construct(){
            $this->db = new DbConnect();
        }
        
        public function isLoginExist($username, $password){
            
            $query = "select * from ".$this->db_table." where username = '$username' AND password = '$password' Limit 1";
            
            $result = mysqli_query($this->db->getDb(), $query);
            
            if(mysqli_num_rows($result) > 0){
                
                mysqli_close($this->db->getDb());
                
                
                return true;
                
            }
            
            mysqli_close($this->db->getDb());
            
            return false;
            
        }
        

        

        public function loginUsers($username, $password){
            
            $json = array();
            
            $canUserLogin = $this->isLoginExist($username, $password);
            
            if($canUserLogin){
                
                $json['success'] = 1;
                $json['message'] = "Successfully logged in";
                
            }else{
                $json['success'] = 0;
                $json['message'] = "Incorrect details";
            }
            return $json;
        }
    }
    ?>

