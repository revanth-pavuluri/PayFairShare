import {useState } from "react";
import Navbar from "./Navbar";
import { Link} from "react-router-dom";
import Balances from "./Balances";
import Expenses from "./Expenses";

export function getRandomColor(): string {

    const randomRed = Math.floor(Math.random() * 256).toString(16).padStart(2, '0');
    const randomGreen = Math.floor(Math.random() * 256).toString(16).padStart(2, '0');
    const randomBlue = Math.floor(Math.random() * 256).toString(16).padStart(2, '0');
  
    const randomColor = `#${randomRed}${randomGreen}${randomBlue}`;
    return randomColor;
}
 

export default function Groupdash(){
  const [page,setpage] = useState<string>('balances');

    return(<>
    <Navbar/>
    <section className="bg-gray-50 dark:bg-gray-900">
  <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
      <div className="w-full  bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="h-96 p-6 space-y-4 md:space-y-6 sm:p-8">

               <nav className="bg-white dark:bg-gray-800 w-full top-0 left-0 ">
  <div className="max-w-screen-xl  flex flex-wrap items-center justify-between mx-auto">
  <div className="flex items-center">
    
    <Link to="/">
        <svg className="h-6 w-6 mr-3 -ml-3 float-left text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M13 5H1m0 0 4 4M1 5l4-4"></path>
    </svg></Link>
      <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">Group name</span>
  </div>
  
  <div className=" items-center justify-between w-full md:flex md:w-auto md:order-1">
    <ul className="flex flex-col p-4 md:p-0 mt-4 font-medium rounded-lg bg-gray-50 md:flex-row md:space-x-8 md:mt-0  md:bg-white dark:bg-gray-800 md:dark:bg-gray-800 dark:border-gray-700">
      <li>
      <span onClick={() => page !== "balances" ? setpage("balances") : null} className={" cursor-pointer block py-2 px-3 rounded md:p-1 md:dark:text-white" + (page === "balances" ? "text-blue-500 dark:text-blue-500" : '')}>Balances</span>
      </li>
      <li>
        <span onClick={() => page !== "expenses" ? setpage("expenses") : null} className={" block py-2 cursor-pointer px-3 rounded md:p-1 md:dark:text-white" + (page === "expenses" ? "text-blue-500 dark:text-blue-500" : '')}>Expenses</span>
      </li>
    </ul>
  </div>

  </div>
</nav>
  {page === "balances" ? <Balances/> : <Expenses/> }

        </div>
      </div>
  </div>
</section>
    
    </>
    );    
}
