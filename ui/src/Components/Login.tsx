import Button from "./Button"
import FormPage from "./FormPage"
import Input from "./Input"
import { useUserdetails } from './UserContext'
import { Link, useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { object, string } from 'yup'
import { yupResolver } from '@hookform/resolvers/yup'
import axios from 'axios'
import { toast } from "react-toastify"

export function getCookie(name: string): string | null{
  const nameLenPlus = (name.length + 1);
  return document.cookie
    .split(';')
    .map(c => c.trim())
    .filter(cookie => {
      return cookie.substring(0, nameLenPlus) === `${name}=`;
    })
    .map(cookie => {
      return decodeURIComponent(cookie.substring(nameLenPlus));
    })[0] || null;
}

export const deleteCookie = function(name:string) {
  document.cookie = name+"=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
};

export default function Login(){

    const {setUserdetails} = useUserdetails();

    const navigate = useNavigate();
    
    const validationSchema = object().shape({
        username: string()
          .required('Email is required !')
          .email("Invalid email !")
          .min(4,'Invalid email !'),
        password: string()
          .required('Password is required !')
          .min(4, 'Password must be at least 4 characters'),
        remember : string(),
      })

      const {register,handleSubmit, formState: { errors },} = useForm({
        resolver: yupResolver(validationSchema),
      })
      
  const onSubmit = async (data : any) => {
    console.log(data)
    const reqBody = data;
    axios.defaults.withCredentials = true;
    await axios.post("/login", reqBody, {
      headers: {
      },withCredentials : true
    })
      .then((response) => {
        if (response.status === 200){
          if (response) {
            setUserdetails(response)
            window.localStorage.setItem("userdata",JSON.stringify(response))
            var cookie =  getCookie("LoginToken");
            axios.defaults.headers.common["Login"] = `Bearer ${cookie}`
            toast.success("Login Successful !")
            setTimeout(() => {
              navigate("/");  
            }, 500); 
          }
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
        <FormPage name="Sign in to your account">
              <form className="space-y-3 md:space-y-5" onSubmit={handleSubmit(onSubmit)} >
                  <Input type="email" placeholder="Email" register={{...register('username')}} required={true} />
                  {errors.username && (
                    <p className="relative -top-5 text-xs italic text-red-500">{errors.username.message}</p>
                  )}
                  <Input type="password" placeholder="Password" register={{...register('password')}} required={true} />
                  {errors.password && (
                    <p className="relative -top-5 text-xs italic text-red-500">{errors.password.message}</p>
                  )}
                  <div className="flex items-center justify-between">
                    <Input type="checkbox" placeholder="Remember me" register={{...register('remember')}}/>
                    <a href="/reset" className="text-sm font-medium text-blue-600 hover:underline dark:text-blue-500">Forgot password?</a>
                  </div>
                  
                  <Button type="submit" value="Sign in" size="w-full" />
                  {/* <Hr value="or"/>
                  <Button type="button" value={[<svg className="w-4 h-4 mr-1.5 eUuXwBkW5W4__eatjSfd RRXFBumaW2SHdseZaWm6 _gmxfZ2BpOHxa6nWwqBB" viewBox="0 0 21 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <g clip-path="url(#clip0_13183_10121)"><path d="M20.3081 10.2303C20.3081 9.55056 20.253 8.86711 20.1354 8.19836H10.7031V12.0492H16.1046C15.8804 13.2911 15.1602 14.3898 14.1057 15.0879V17.5866H17.3282C19.2205 15.8449 20.3081 13.2728 20.3081 10.2303Z" fill="#3F83F8"></path><path d="M10.7019 20.0006C13.3989 20.0006 15.6734 19.1151 17.3306 17.5865L14.1081 15.0879C13.2115 15.6979 12.0541 16.0433 10.7056 16.0433C8.09669 16.0433 5.88468 14.2832 5.091 11.9169H1.76562V14.4927C3.46322 17.8695 6.92087 20.0006 10.7019 20.0006V20.0006Z" fill="#34A853"></path><path d="M5.08857 11.9169C4.66969 10.6749 4.66969 9.33008 5.08857 8.08811V5.51233H1.76688C0.348541 8.33798 0.348541 11.667 1.76688 14.4927L5.08857 11.9169V11.9169Z" fill="#FBBC04"></path><path d="M10.7019 3.95805C12.1276 3.936 13.5055 4.47247 14.538 5.45722L17.393 2.60218C15.5852 0.904587 13.1858 -0.0287217 10.7019 0.000673888C6.92087 0.000673888 3.46322 2.13185 1.76562 5.51234L5.08732 8.08813C5.87733 5.71811 8.09302 3.95805 10.7019 3.95805V3.95805Z" fill="#EA4335"></path></g><defs><clipPath id="clip0_13183_10121"><rect width="20" height="20" fill="white" transform="translate(0.5)"></rect></clipPath></defs>
                      </svg>,"Sign in with Google"]} size="w-full" />
                   */}
                  <p className="text-sm font-light text-gray-500 dark:text-gray-400">
                      Don’t have an account yet? <Link to="/register" className="font-medium text-blue-600 hover:underline dark:text-blue-500">Sign up</Link>
                  </p>
              </form>
              </FormPage>
    );
}
