import { useContext, useState } from "react"
import { Link } from "react-router-dom"
import Modal from "./Modal"
import SplitExpense, { splituser } from "./SplitExpense"
import Button from "./Button"
import QrModal from "./QrModal"
import Input from "./Input"
import { toast } from "react-toastify";
import { updateref } from "./Dasboard"
import axios from "axios"
import { useUserdetails } from "./UserContext"

export type Groupcardprop = {
    id : number,
    name : string,
    expenses? : Expenseprop[],
    groupUsers : splituser[]
}
export type Expenseprop = {
    id : number,
    name : string,
    amount : number,
    paidBy : User,
    createdOn : string,
    status : string
}

export type User = {
    id : number,
    name : string
}
export default function GroupCard({id,name,expenses, groupUsers}:Groupcardprop) {

    const [isSharemodal, setisSharemodal] = useState<boolean>(false)
    const [isQRmodal, setisQRmodal] = useState<boolean>(false)
    const [isExpensemodal, setisExpensemodal] = useState<boolean>(false);
    const {update,setUpdate} = useContext(updateref)
    const {userdetails} = useUserdetails();
    
    const onDeletegroup = async (groupId : number) => {
        axios.defaults.withCredentials = true;
        await axios.delete(`/groups/delete/${groupId}/${userdetails?.data.id}`, {
          headers: {
          },withCredentials : true
        })
          .then((response) => {
            if (response.status === 200 && response.data){
              toast.success("Deleted group successfully !")
              setUpdate(Math.random()*1000)

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
        <>

<div key={id} className="w-full max-w-md p-8 bg-white hover:border-1 hover:border-purple-600 border border-gray-200 rounded-lg shadow sm:p-8 xs:p-8 md:p-8 dark:bg-gray-800 dark:border-gray-700">
    <div className="flex items-center justify-between mb-4">
        <h5 className="text-xl font-bold leading-none text-gray-900 dark:text-white">{name}</h5>
        <div className="float-right drop-shadow-md">
        
        <span onClick={() => setisSharemodal(true)} className="mr-2 bg-blue-100 mt-2 cursor-pointer text-blue-800 text-sm font-medium inline-flex items-center px-2.5 py-0.5 hover:underline rounded dark:bg-gray-700 dark:text-blue-400">
        <svg className="w-2.5 h-2.5 mr-1.5 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 18">
    <path d="M14.419 10.581a3.564 3.564 0 0 0-2.574 1.1l-4.756-2.49a3.54 3.54 0 0 0 .072-.71 3.55 3.55 0 0 0-.043-.428L11.67 6.1a3.56 3.56 0 1 0-.831-2.265c.006.143.02.286.043.428L6.33 6.218a3.573 3.573 0 1 0-.175 4.743l4.756 2.491a3.58 3.58 0 1 0 3.508-2.871Z"/>
  </svg>
        Share
        </span>
        
        <span onClick={() => setisQRmodal(true)} className="mr-2 bg-indigo-200 mt-2 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-300 cursor-pointer text-sm font-medium inline-flex items-center px-2.5 py-0.5 hover:underline rounded">
        <svg className="w-2.5 h-2.5 mr-1.5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
    <path d="M19 5h-1.382l-.171-.342A2.985 2.985 0 0 0 14.764 3H9.236a2.984 2.984 0 0 0-2.683 1.658L6.382 5H5a3 3 0 0 0-3 3v11a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V8a3 3 0 0 0-3-3Zm-3.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"/>
  </svg>
            Scan
        </span>
        
        <span onClick={()=> setisExpensemodal(true)} className="mr-2 bg-purple-100 mt-2 text-purple-800 cursor-pointer text-sm font-medium inline-flex items-center px-2.5 py-0.5 hover:underline rounded dark:bg-purple-900 dark:text-purple-300">
        <svg className="w-2.5 h-2.5 mr-1.5 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 19H1.933A.97.97 0 0 1 1 18V5.828a2 2 0 0 1 .586-1.414l2.828-2.828A2 2 0 0 1 5.828 1h8.239A.97.97 0 0 1 15 2v4M6 1v4a1 1 0 0 1-1 1H1m11 8h4m-2 2v-4m5 2a5 5 0 1 1-10 0 5 5 0 0 1 10 0Z"/>
  </svg>
            Expense
        </span>
        
        
        </div>
   </div>
   
   <div className="flow-root content-div">
        <ul className="divide-y divide-gray-200 dark:divide-gray-700">
        <Link to={"/g/"+id}>
            {expenses && expenses?.length !== 0 ? expenses?.map(expense => (
            <li className="py-3 sm:py-4">
                <div className="flex items-center space-x-4">
                    
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
                    </div>
                </div>
            </li>
            )) : <li className="py-3 sm:py-4">
            <div className="flex  items-center space-x-4">
                
                <div className="flex-1 justify-items-center min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate dark:text-white">
                        No expenses yet
                    </p>
                    <p className="text-sm text-gray-500 truncate dark:text-gray-400">
                            Click expense button to add new
                    </p>
                </div>
                
            </div>
        </li>}
        </Link>
        </ul>
        <div className="absolute opacity-0 fd-sh group-hover:opacity-100">
       <div className="text-center flex gap-5">
           


<span onClick={() => onDeletegroup(id)} className="bg-red-100 text-red-800 cursor-pointer text-xs font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-red-900 dark:text-red-300 inline-flex items-center border border-red-400">
<svg className="w-2.5 h-2.5 mr-1.5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 18 20">
    <path d="M17 4h-4V2a2 2 0 0 0-2-2H7a2 2 0 0 0-2 2v2H1a1 1 0 0 0 0 2h1v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V6h1a1 1 0 1 0 0-2ZM7 2h4v2H7V2Zm1 14a1 1 0 1 1-2 0V8a1 1 0 0 1 2 0v8Zm4 0a1 1 0 0 1-2 0V8a1 1 0 0 1 2 0v8Z"/>
  </svg>
    Delete group
</span>

           </div>
    </div>
   </div>
</div>
{isSharemodal? <Modal onClose={()=> setisSharemodal(false)}>
    <div>
        <h1 className="text-2xl relative -top-10 w-2/3 dark:text-white">Share</h1>
        <Input disabled={true} type="text" value={"http://localhost:3000/join/"+id} placeholder="Group link"/> 
        <Button value="Copy" size="w-full" onclick={() => {navigator.clipboard.writeText("http://localhost:3000/join/"+id); toast.success("Link copied !"); setisSharemodal(false) }} />
    </div>
    </Modal> : null}

{isExpensemodal? <Modal onClose={()=> setisExpensemodal(false)}><SplitExpense groupid={id} onsuccess={()=> {setisExpensemodal(false); setUpdate(Math.random()*1000) }} usersList={groupUsers} /> </Modal> : null}

{isQRmodal? <QrModal name={name} id={id} groupUsers={groupUsers} onclose={()=> setisQRmodal(false)}/>: null}
   

        </>
    );
}