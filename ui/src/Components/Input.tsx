import { ChangeEvent } from "react";

type inputProps={
    placeholder : string,
    type : string,
    required? : boolean,
    onChangeinput? : (e: ChangeEvent<HTMLInputElement>) => void
    className?:string,
    name? : string,
    id? : string,
    value? : string,
    register?: any
    disabled? : boolean
}
export default function Input({placeholder, type, required, onChangeinput, disabled = false, register, className, name, id, value}:inputProps){
    if (type === "checkbox"){
        return (<div className="flex items-start">
        <div className="flex items-center h-5">
          <input  {...register} onChange={onChangeinput} type="checkbox" className="w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-3 focus:ring-blue-300 dark:bg-gray-700 dark:border-gray-600 dark:focus:ring-blue-600 dark:ring-offset-gray-800"/>
        </div>
        <div className="ml-3 text-sm">
          <label className="text-gray-500 dark:text-gray-300">{placeholder}</label>
        </div>
    </div>);
    }
    return (<>
        <div className="relative z-0 w-full mb-6 mt-1 group">
        
      <input type={type} name={name} {...register} id={id} disabled={disabled} value={value} onChange={onChangeinput} autoComplete="new-password" className="block py-2.5 pl-1 px-0 w-full text-md text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required={required} />
      <label htmlFor={id} className="peer-focus:font-medium absolute text-md text-gray-500 dark:text-gray-400 duration-200 transform -translate-y-6 scale-75 top-1 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">{placeholder}</label>
  </div>
    </>);
}