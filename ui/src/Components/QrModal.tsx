import React, {useEffect, useState } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import Modal from "./Modal";
import SplitExpense from "./SplitExpense";
import { Groupcardprop } from "./GroupCard";

type QrModalprop = {
  onclose : () => void,
} & Groupcardprop

const QrModal  = ({onclose, groupUsers,id, name}:QrModalprop) => {
  const [showModal, setShowModal] = React.useState(false);
  const [scanResultWebCam, setScanResultWebCam] =  useState('');

    useEffect(() => {
      let scanner : any = null; // Declare the scanner variable
    
      if (showModal) {
        scanner = new Html5QrcodeScanner('reader', {
          qrbox: {
            width: 250,
            height: 250,
          },
          fps: 5,
        }, false);
    
        const success = (result : any) => {
          setScanResultWebCam(result);
          
          if (scanner) {
            scanner.clear();
          }
        };
    
        const handleErrorWebCam = (error : any) => {
          console.log(error);
        };
    
        scanner.render(success, handleErrorWebCam);
      }
    

      return () => {
        if (scanner) {
          scanner.clear(); // Clear the scanner when the component unmounts
        }
      };
    }, [showModal]);
        
  
  
  return (
   <Modal onClose={()=>{setShowModal(false); onclose()}} onShow={()=> {setShowModal(true)}}>
   {scanResultWebCam ? <SplitExpense groupid={id} scanresult={scanResultWebCam} usersList={groupUsers} /> : <div> <div id = 'reader'></div></div>}
   </Modal>
  );
}

export default QrModal