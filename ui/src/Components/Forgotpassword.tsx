// import Button from "./Button"
// import FormPage from "./FormPage"
// import Input from "./Input"
// import { useNavigate } from 'react-router-dom'
// import { useForm } from 'react-hook-form'
// import * as yup from 'yup';
// import { yupResolver } from '@hookform/resolvers/yup'


// export default function Forgotpassword(){
//     const navigate = useNavigate();
//     const validationSchema = yup.object().shape({
//         email: yup
//           .string()
//           .required('Email is required!')
//           .email('Invalid email format')
//           .min(4, 'Invalid email format'),
// });


//       const {register,handleSubmit, formState: { errors },} = useForm({
//         resolver: yupResolver(validationSchema),
//       })
      
//   const onSubmit = async (data : any) => {
//     console.log(data)
//     const reqBody = data;
//     axios.defaults.withCredentials = true;
//     await axios.post("/login", reqBody, {
//       headers: {
//       },withCredentials : true
//     })
//       .then((response) => {
//         if (response.status === 200) return response;
//         else if (response.status === 401 || response.status === 403) {
//           console.error("Invalid username or password");
//         } else {
//           console.error(
//             "Something went wrong, try again later "
//           );
//         }
//       })
//       .then((data) => {
//         if (data) {
//           setUserdetails(data)
//           sessionStorage.setItem("userdetails",JSON.stringify(data))
//           axios.defaults.headers.common["Login"] = `Bearer ${data.data['password']}` 
//             navigate("/dashboard");
//         }
//       })
//       .catch((error) => {
//         console.error(error);
//       })
//   }
//     return(
//         <FormPage name="Forgot your password ?">
//               <form className="space-y-5 md:space-y-5" onSubmit={handleSubmit(onSubmit)} >
//               <p className="text-sm font-light text-gray-500 dark:text-gray-400">
//                     Just type in your email and we’ll email you the instructions to reset your password.
//                </p>
//                   <Input type="email" placeholder="Email" register={{...register('email')}} required={true} />
//                   {errors.email && (
//                     <p className="relative -top-5 text-xs italic text-red-500">{errors.email.message}</p>
//                   )}
                  
//                   <Button type="submit" value="Reset password" />
//                   <a href="/login" className="font-medium text-blue-600 hover:underline dark:text-blue-500">Return to login</a>
//               </form>
//               </FormPage>
//     );
// }
export default function Forgotpassword(){
    return ('Forgot Password');
}