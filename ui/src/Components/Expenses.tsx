import axios from "axios";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { getRandomColor } from "./Groupdash";
import { useUserdetails } from "./UserContext";
import { Expenseprop } from "./GroupCard";
import { reverse } from "./Dasboard";
 

export default function Balances(){
   const {id} = useParams();
   const [expenses, setExpenses] = useState<Expenseprop[]>();
   const navigate = useNavigate();
   const {userdetails} = useUserdetails();
   const [update, setupdate] = useState<number>();


   function formatDate (timestamp:string){
      var formatteddate = new Date(timestamp)
      const day = formatteddate.getDate();
      const month = formatteddate.toLocaleString('default', { month: 'short' }).substring(0,3);
      return `${'\u00A0'} ${day} ${'\u00A0'} ${month}`;
   }
   const onLoad = async () => {
      axios.defaults.withCredentials = true;
      await axios.get(`/expense/group/${id}`, {
        headers: {
        },withCredentials : true
      })
        .then((response) => {
          if (response.status === 200){
            if (response) {
              setExpenses(reverse(response.data))
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

    const onFinalsplit = async () => {
      axios.defaults.withCredentials = true;
      await axios.get(`/finalsplit/do/${id}`, {
        headers: {
        },withCredentials : true
      })
        .then((response) => {
          if (response.status === 200 && response.data){
            toast.success("Amount splitted !")
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

    const onDeleteExpense = async (expenseid:number) => {
      axios.defaults.withCredentials = true;
      await axios.delete(`/expense/delete/${expenseid}/${userdetails?.data.id}`, {
        headers: {
        },withCredentials : true
      })
        .then((response) => {
          if (response.status === 200 && response.data){
            toast.success("Expense deleted !")
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

    return (<div>
        <span onClick={onFinalsplit} className="drop-shadow-md cursor-pointer inline-flex items-center text-center bg-green-100 text-green-800 text-sm font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-green-900 dark:text-green-300">
        <svg className="w-4 h-4 mr-2 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 16 20">
    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10v5m0 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4ZM3 5a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm0 0v3a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V5m0 0a2 2 0 1 0 0-4 2 2 0 0 0 0 4Z"/>
  </svg>
            Split expenses</span>
        <ul className="max-w-md divide-y max-h-64 overflow-scroll divide-gray-200 dark:divide-gray-700">
        {expenses ? expenses?.map(expense => (

           <li className="py-3 sm:py-4">
              <div className="flex items-center space-x-4">
              <div style={{"background":getRandomColor()}}
              className={`inline-block text-xs pt-1 break-words align-text-middle w-10 h-10 rounded-full text-center text-white`}>
                    {formatDate(expense.createdOn)}
                 </div>
                 <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate dark:text-white">
                       {expense.name}
                    </p>
                    <p className="text-sm text-gray-500 truncate dark:text-gray-400">
                        Paid by {expense.paidBy.name}
                    </p>
                 </div>
                 <div className="inline-flex items-center text-base font-semibold text-gray-900 dark:text-white">
                 ₹{expense.amount}
                  {expense.status === "Finalized" ? <svg className="w-5 ml-3 h-5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 16 20">
    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10v5m0 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4ZM3 5a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm0 0v3a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V5m0 0a2 2 0 1 0 0-4 2 2 0 0 0 0 4Z"/>
  </svg> : expense.status === "Pending" ? expense.paidBy.id === userdetails?.data.id ? <svg onClick={() => onDeleteExpense(expense.id)} className="cursor-pointer ml-3 w-5 h-5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 18 20">
    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M1 5h16M7 8v8m4-8v8M7 1h4a1 1 0 0 1 1 1v3H6V2a1 1 0 0 1 1-1ZM3 5h12v13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V5Z"/>
  </svg> : '' : ''}
                 
                 </div>
              </div>
           </li>
           )): ''}
           
        </ul>
        </div>
        );
}