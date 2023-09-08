import { ChangeEvent, useEffect, useState } from "react";
import Button from "./Button";
import Input from "./Input";
import * as yup from 'yup';
import axios from "axios";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";


export type splituser = {
    name : string,
    id : number,
    amount? : number
}
type Groupmembers = {
    usersList : splituser[],
    scanresult?:string,
    onsuccess? : ()=>void;
    groupid : number;
}

export default function SplitExpense({usersList,scanresult,onsuccess, groupid}:Groupmembers){


    const [users,setUsers] = useState(usersList);
    const [amount, setAmount] = useState<number>(0);
    const [splitUsers, setSplitUsers] = useState<number[]>(users.map(user => user.id));
    const [equalsplit, setEqualsplit] = useState<boolean>(true);

    const [usersharechange, setusersharechange] = useState<splituser[]>();
    
    const validationSchema = yup.object().shape({
        name: yup
        .string()
        .required('Name is required!')
        .min(3, "Name shouldn't be less than 3 characters")
        .max(15, "Name shouldn't be more than 15 characters"),
        amount: yup
        .number()
        .required('Amount is required!')
        .min(1, "Minimum amount should be ₹1"),
        paidBy: yup
        .number()
        .required('Paid by is required!'),
        groupId : yup
        .number()
        .default(groupid),
        status : yup
        .string()
        .default(""),
        createdOn : yup
        .string()
        .default('')
  });

  const {register,handleSubmit, formState: { errors },} = useForm({
    resolver: yupResolver(validationSchema),
  })
  
const onSubmit = async (data : any) => {

    type expenseSplits = {
        userId: number;
        userResponsibleAmount: number;
      };
      
      var expensedata: expenseSplits[] = [];
      var totalamount : number = 0;
      users.forEach((user) => {
        
            if (user.id && user.amount) {

                if(splitUsers.indexOf(user.id) !== -1){
            expensedata.push({
                userId: user.id,
                userResponsibleAmount: user.amount,
            });
            console.log(user.amount)
            totalamount+=user.amount
            }
        }
      });
      console.log(totalamount,data.amount)
      
      const finaldata = {
        ...data,
        expenseSplits: expensedata,
      };

      console.log(finaldata);
      
      if(totalamount !== data.amount){
        toast.error("Split amount should be equal to total amount")
        return
      }
axios.defaults.withCredentials = true;
await axios.post(`/expense/create/${groupid}`, finaldata, {
  headers: {
  },withCredentials : true
})
  .then((response) => {
    if (response.status === 200 && response.data){
      toast.success("New expense added !")
      if(onsuccess){onsuccess()}
    }
    
  })
  .catch((error) => {
    console.error(error);
    if(error.response){
    
      toast.error(error.response.data.error)
    
    }else{
    toast.error(error.message);
    }
  })
}

   useEffect(()=>{
    if(amount){
        const share = parseFloat((amount/splitUsers.length).toFixed(2));
        users.map(user => (splitUsers.includes(user.id) ? user.amount = share : user.amount = user.amount))
        setUsers([...users]);
    }
   },[amount,splitUsers])

    const handleAmountChange = (e: ChangeEvent<HTMLInputElement>) => {
        setAmount(parseInt(e.target.value));   
      };

useEffect(() => {
    if(usersharechange){
        setUsers([...usersharechange])
    }
},[usersharechange])

    const handleUsersharechange = (e: ChangeEvent<HTMLInputElement>) => {
        const ref = e.target;
        var changedValue = parseFloat(ref.value);
        if(isNaN(changedValue)){
            changedValue = 0
        }
        var updatedUsers = null;
        if(equalsplit){
            const value = amount - changedValue;
            updatedUsers = users.map(user => {
            if (splitUsers.includes(user.id)){
                if(user.id === parseInt(ref.id.split("_")[0])){
                    return { ...user, amount: changedValue };    
                }else{
                    return { ...user, amount: parseFloat((value / (splitUsers.length - 1)).toFixed(2)) };
                }
            }
            return user;
            })        
        }else{
            updatedUsers = users.map(user => {
                if(user.id === parseInt(ref.id.split("_")[0])){
                    return {...user, amount: changedValue };
                }
                return user;    
        })
    }   
    setusersharechange(updatedUsers);
}
    
    
    const onEqualsharechange = (e:ChangeEvent<HTMLInputElement>) => {
        if(e.target.checked){
            setEqualsplit(true)
            e.target.parentElement?.setAttribute("class","underline");
        }else{
            setEqualsplit(false)
            e.target.parentElement?.setAttribute("class","line-through");
        }
    }
    const handleUserselect = (e: ChangeEvent<HTMLInputElement>)=>{
        const ref = e.target;
        
        if(ref.checked === false){
            setSplitUsers(prev => {
                return prev.filter(user => user !== parseInt(ref.name))
            })
            users.map(user => (user.id === parseInt(ref.name) ? user.amount = 0 : user.amount = user.amount))
            setUsers([...users]);

        }else{

            setSplitUsers(prev => [...prev, parseInt(ref.name)])
        }   
         console.log(splitUsers);
    }

    return(
        <div className=" space-y-4 md:space-y-6 sm:p-8">
              <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                Split an expense
              </h1>
              <form className="space-y-4 sm:mt-1 md:space-y-6" onSubmit={handleSubmit(onSubmit)}>
             <Input value={amount.toString()} register={{...register('amount')}} placeholder="Expense value" type="number" onChangeinput={handleAmountChange} />
             {errors.amount && (
                    <p className="relative -top-5 text-xs italic text-red-500">{errors.amount.message}</p>
                  )}
              <Input type="text" register={{...register('name')}} placeholder="Expense for ?"/>
              {errors.name && (
                    <p className="relative -top-5 text-xs italic text-red-500">{errors.name.message}</p>
                  )}
              <div className="flex">
                <label htmlFor="countries" className="block text-sm font-medium text-gray-600 dark:text-white">Expense paid by</label>
                <select id="underline_select" {...register('paidBy')} className="block py-2.5 px-0 w-full text-sm text-gray-500 bg-transparent border-0 border-b-2 border-grey-400 appearance-none dark:text-gray-400 dark:border-gray-700 focus:outline-none focus:ring-0 focus:border-gray-200 peer">
                    {users.map(user =>(
                        <option value={user.id}>{user.name}</option>
                    ))}
                </select>
             </div>

            <h3 className="mb-4 font-semibold text-gray-900 dark:text-white">Split expense <label className="underline"><input type="checkbox" defaultChecked onChange={onEqualsharechange}/> equally</label> among</h3>


<ul className="grid grid-cols-1 md:grid-cols-1 sm:grid-cols-1 lg:grid-cols-1 gap-4">
    
{users.map(user => (
    <li key={user.id}>
        <input type="checkbox" onChange={handleUserselect} defaultChecked id={user.name} name={user.id.toString()} className="hidden peer" />
        <label htmlFor={user.name} className="inline-flex items-center justify-between w-50 p-3 text-gray-500 bg-white border border-gray-200 rounded-lg cursor-pointer dark:hover:text-gray-300 dark:border-gray-700 dark:peer-checked:text-green-500 peer-checked:border-green-600 peer-checked:text-green-600 hover:text-gray-600 hover:bg-gray-100 dark:text-gray-400 dark:bg-gray-800 dark:hover:bg-gray-700">                           
            <div className="block">
                <div className="w-full">{user.name}</div>
            </div>
            <span className="z-30 ml-4">₹</span><input  value={user.amount} defaultValue={0}
       onChange={handleUsersharechange} id={user.id+"_"+user.name} className="z-20 float-right w-20 -ml-4 pl-5 mr-2 p-2 text-gray-900 border border-gray-300 rounded-lg sm:text-xs focus:ring-green-500 focus:border-green-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"/>
        </label>
    </li>
))}
</ul>

              {scanresult?<div className="mt-36"><Button value="Pay" size="w-full" onclick={ () => {window.open(scanresult.toString()+"&am="+amount, '_blank', 'noopener,noreferrer')}}/></div>:null}
              <Button value="Split expense" type="submit" size="w-full"/>
              </form>
              </div>
    );
}