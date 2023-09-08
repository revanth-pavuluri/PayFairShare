import Button from "./Button"
import Input from "./Input"
import { useForm } from 'react-hook-form'
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup'
import axios from 'axios'
import { toast } from "react-toastify"
import { useUserdetails } from "./UserContext"

type Newgroupprop ={
    onsuccess : ()=>void;
}
export default function Newgroup({onsuccess}:Newgroupprop){

    const {userdetails} = useUserdetails();
    const validationSchema = yup.object().shape({
          groupname: yup
          .string()
          .required('Name is required!')
          .min(3, "Name shouldn't be less than 3 characters")
          .max(15, "Name shouldn't be more than 15 characters"),
    });

      const {register,handleSubmit, formState: { errors },} = useForm({
        resolver: yupResolver(validationSchema),
      })
      
  const onSubmit = async (data : any) => {
    console.log(data)
    axios.defaults.withCredentials = true;
    await axios.post(`/groups/${userdetails?.data.id}/create`, data.groupname, {
      headers: {
      },withCredentials : true
    })
      .then((response) => {
        if (response.status === 200 && response.data){
          toast.success("New group created successfully !")
          onsuccess();
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
    return(
              <form className="space-y-5 md:space-y-5" onSubmit={handleSubmit(onSubmit)} >
                  <Input type="text" placeholder="Group name" register={{...register('groupname')}} required={true} />
                  {errors.groupname && (
                    <p className="relative -top-5 text-xs italic text-red-500">{errors.groupname.message}</p>
                  )}
                  
                  <Button type="submit" value="Create" size="w-full" />
              </form>
    );
}
