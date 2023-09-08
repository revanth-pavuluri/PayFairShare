import axios from "axios";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getRandomColor } from "./Groupdash";
import { useNavigate, useParams } from "react-router-dom";
import { User } from "./GroupCard";
import { useUserdetails } from "./UserContext";
import { reverse } from "./Dasboard";

type balance ={
   id : number,
   groupId : number,
   payTo : User,
   payBy : User,
   amount : number,
   status : string
}
export default function Balances(){
   
   const {id} = useParams();
   const [balances, setbalances] = useState<balance[]>();
   const navigate = useNavigate();
   const {userdetails} = useUserdetails();
   const [update, setupdate] = useState<number>();

   const onLoad = async () => {
      axios.defaults.withCredentials = true;
      await axios.get(`/finalsplit/get/${id}`, {
        headers: {
        },withCredentials : true
      })
        .then((response) => {
          if (response.status === 200){
            if (response) {
              setbalances(reverse(response.data))
            }
          }  
        })
        .catch((error) => {
          console.error(error);
          if(error.response){
          if(error.response.data.error === "No such a group exists"){
            toast.error(error.response.data.error)
            navigate("/")
          }
        }else{
          toast.error(error.message);
          }
        })
      }
      useEffect(
        () => {
      
        if(userdetails?.data.id){
        onLoad();
      }
      
    },[userdetails,update])

    const Settle = async (data:balance) => {
      axios.defaults.withCredentials = true;
      await axios.put(`/finalsplit/update/${userdetails?.data.id}`, data,{
        headers: {
        },withCredentials : true
      })
        .then((response) => {
          if (response.status === 200 && response.data){
            toast.success("Balance marked as settled !")
            setupdate(Math.random())
          }
        })
        .catch((error) => {
          console.error(error);
          if(error.response.data.error){ 
            toast.error(error.response.data.error)
          }else{
          toast.error(error.message);
          }
        })
    }
    return (
    
    <div>

        <ul className="max-w-md divide-y max-h-72 overflow-scroll divide-gray-200 dark:divide-gray-700">
        
        {balances ? balances?.map(balance => (

           <li className="py-3 sm:pb-4">
              <div className="flex items-center space-x-4">
              <div style={{"background":getRandomColor()}}
              className={`inline-block text-xl pt-1.5 align-text-middle w-10 h-10 rounded-full text-center text-white`}>
                    {(balance.payBy.id === userdetails?.data.id ? balance.payTo.name : balance.payBy.name).substring(0,1).toUpperCase()}
                 </div>
                 <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate dark:text-white">
                       {balance.payBy.id === userdetails?.data.id ? balance.payTo.name : balance.payBy.name}
                    </p>
                    <p className="text-sm text-gray-500 truncate dark:text-gray-400">
                    {balance.payBy.id === userdetails?.data.id ? "You need to pay" : "You get back"}
                    </p>
                 </div>
                 <div className="inline-flex items-center text-base font-semibold text-gray-900 dark:text-white">
                 ₹{balance.amount} 
                 
                 {balance.payBy.id === userdetails?.data.id ? <svg className="w-5 p-1 ml-2 h-5 bg-red-600 rounded-full rotate-45 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 14">
            <path stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13V1m0 0L1 5m4-4 4 4"></path>
        </svg> : <svg className="w-5 p-1 ml-2 h-5 bg-green-600 rounded-full rotate-[225deg] text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 14">
            <path stroke="white" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13V1m0 0L1 5m4-4 4 4"></path>
        </svg> }
                 
         {balance.status === "Paid" ?
         
         <svg className="w-6 h-6 ml-5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
    <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z"/>
  </svg>
         :
        <svg onClick={() => Settle(balance)} className="ml-5 w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 21 21">
            <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m6.072 10.072 2 2 6-4m3.586 4.314.9-.9a2 2 0 0 0 0-2.828l-.9-.9a2 2 0 0 1-.586-1.414V5.072a2 2 0 0 0-2-2H13.8a2 2 0 0 1-1.414-.586l-.9-.9a2 2 0 0 0-2.828 0l-.9.9a2 2 0 0 1-1.414.586H5.072a2 2 0 0 0-2 2v1.272a2 2 0 0 1-.586 1.414l-.9.9a2 2 0 0 0 0 2.828l.9.9a2 2 0 0 1 .586 1.414v1.272a2 2 0 0 0 2 2h1.272a2 2 0 0 1 1.414.586l.9.9a2 2 0 0 0 2.828 0l.9-.9a2 2 0 0 1 1.414-.586h1.272a2 2 0 0 0 2-2V13.8a2 2 0 0 1 .586-1.414Z"></path>
        </svg>
         }
                 </div>
              </div>
           </li>

            )): <div className="dark:text-white text-center"><h2 className="text-lg">No balances yet !</h2> <h4 className="text-xs">Click on split expenses to finalize and get balances</h4> </div>}
            
        </ul>
      </div>
    )
}