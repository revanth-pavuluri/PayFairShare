import GroupCard, {Groupcardprop } from "./GroupCard";
import Input from "./Input";
import Modal from "./Modal";
import Navbar from "./Navbar";
import { createContext, useEffect, useMemo, useState } from "react";
import Button from "./Button";
import Newgroup from "./Newgroup";
import axios from "axios";
import { toast } from "react-toastify";
import { useUserdetails } from "./UserContext";


interface Updateref{
  update : number,
  setUpdate : React.Dispatch<React.SetStateAction<number>>
}
export const updateref = createContext<Updateref>({} as Updateref)
export function reverse (list:any) {
  const latest = [...list].reverse()
  return latest
}
export default function Dashboard(){
  const [update, setUpdate] = useState<number>(0);
  const [isNewgroupmodal, setisNewgroupmodal] = useState<boolean>(false)
  const {userdetails} = useUserdetails();
  const [groupData, setgroupData] = useState<Groupcardprop[]>();
  const [search, setSearch] = useState("")

  const onLoad = async () => {
  axios.defaults.withCredentials = true;
  await axios.get(`/groups/user/${userdetails?.data.id}`, {
    headers: {
    },withCredentials : true
  })
    .then((response) => {
      if (response.status === 200){
        if (response) {
          setgroupData(response.data)
        }
      }  
    })
    .catch((error) => {
      console.error(error);
      if(error.response){
      if(error.response.data.error !== '404 NOT_FOUND "No groups found"'){
        toast.error(error.response.data.error)
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

const filteredGroups = useMemo(()=> {
  return groupData?.filter(group => {
      return (
          (search === "" || group.name.toLowerCase().includes(search.toLowerCase()))
      )
  })
}, [search, groupData])

    return(
    <div className="dashboard dark:bg-gray-800 pb-96">
       
    <Navbar/>

<div className="flex items-center mx-20 dark:bg-gray-800">   
    
    <div className="relative w-full mr-10">
        <Input type="text" placeholder="Search groups..." value={search}
                onChangeinput = {e => setSearch(e.target.value)} />
    </div>
    
    <Button onclick={()=> setisNewgroupmodal(true)} value={[<svg className="w-5 mr-3 h-5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 19">
    <path d="M14.5 0A3.987 3.987 0 0 0 11 2.1a4.977 4.977 0 0 1 3.9 5.858A3.989 3.989 0 0 0 14.5 0ZM9 13h2a4 4 0 0 1 4 4v2H5v-2a4 4 0 0 1 4-4Z"/>
    <path d="M5 19h10v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2ZM5 7a5.008 5.008 0 0 1 4-4.9 3.988 3.988 0 1 0-3.9 5.859A4.974 4.974 0 0 1 5 7Zm5 3a3 3 0 1 0 0-6 3 3 0 0 0 0 6Zm5-1h-.424a5.016 5.016 0 0 1-1.942 2.232A6.007 6.007 0 0 1 17 17h2a1 1 0 0 0 1-1v-2a5.006 5.006 0 0 0-5-5ZM5.424 9H5a5.006 5.006 0 0 0-5 5v2a1 1 0 0 0 1 1h2a6.007 6.007 0 0 1 4.366-5.768A5.016 5.016 0 0 1 5.424 9Z"/>
  </svg>,"New group"]} size="w-64"/>

</div>

    
      <div className='p-3'>

      <div className="p-1 pt-10 mb-96 flex flex-row flex-wrap justify-center m-auto gap-4">
      
      {filteredGroups ? filteredGroups?.map(group => (
        
        <updateref.Provider value={{update,setUpdate}}>
          <GroupCard id={group.id} name={group.name} expenses={group.expenses} groupUsers={group.groupUsers} />
        </updateref.Provider>
        )) : 
      <div className="mt-5 w-96 p-5 max-w-sm bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">
          <div className="p-5">

                  <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">No groups yet. Tap to create one</h5>

              <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">Start a new group with your friends. Your group is waiting for you</p>
              <Button onclick={()=> setisNewgroupmodal(true)} value={["Create new",<svg className="w-3.5 h-3.5 ml-2" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
                      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
                  </svg>]}/>
          </div>
      </div>
      }


      </div>

      </div>
      
      {isNewgroupmodal? <Modal onClose={()=> setisNewgroupmodal(false)}>
      <h1 className="text-2xl relative -top-10 w-2/3 dark:text-white">New group</h1>
    <Newgroup onsuccess={()=> {setisNewgroupmodal(false); setUpdate(Math.random()*1000);}}/>
    </Modal> : null}
    
    </div>);
}