import Button from "./Button"
import FormPage from "./FormPage"
import { useUserdetails } from './UserContext'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'
import { toast } from "react-toastify"
import { useEffect, useState } from "react"
import { Expenseprop } from "./GroupCard"
import { getRandomColor } from "./Groupdash"

type groupdetails = {
  id : number,
  name : string,
  expenses : Expenseprop[],
  groupUsers : any[]
}
export default function Joingroup(){

    const {userdetails} = useUserdetails();
    const navigate = useNavigate();
    const [groupData, setgroupData] = useState<groupdetails>();
    const {id} = useParams();

    const onSubmit = async () => {
        axios.defaults.withCredentials = true;
        await axios.post(`/groups/adduser/${id}/${userdetails?.data.id}`, {
          headers: {
          },withCredentials : true
        })
          .then((response) => {
            if (response.status === 200 && response.data){
              toast.success("Added to new group successfully !")
              navigate("/")
            }
          })
          .catch((error) => {
            console.error(error);
            if(error.response.data.error === "This user is already in the group."){ 
              toast.error(error.response.data.error)
              navigate("/")
            }else{
            toast.error(error.message);
            }
          })
      }

      const onLoad = async () => {
        axios.defaults.withCredentials = true;
        await axios.get(`/groups/get/${id}`, {
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
        
      },[userdetails])
    return(
        <FormPage name="Join new group">
              <div className="flex items-center space-x-4">
              <div style={{"background":getRandomColor()}}
              className={`inline-block text-xl pt-1.5 align-text-middle w-10 h-10 rounded-full text-center text-white`}>
                    {groupData?.name.slice(0,1).toUpperCase()}
                 </div>
                 <div className="flex-1 min-w-0">
                    <p className="text-lg font-medium text-gray-900 truncate dark:text-white">
                    {groupData?.name}
                    </p>
                    
                 </div>
                 
              </div>
               
                <Button type="submit" value="Join group" size="w-full" onclick={onSubmit} />
              </FormPage>
    );
}
