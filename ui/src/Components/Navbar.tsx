import { Link, useNavigate } from 'react-router-dom';
import { getRandomColor } from './Groupdash';
import { useUserdetails } from './UserContext';
import logo from './logo.png';
import { toast } from 'react-toastify';
export default function Navbar() {
  const {userdetails,logout} = useUserdetails();
  const navigate= useNavigate();
    return (
        <>
        <nav className="fixed top-0 z-50 w-full bg-white border-b border-gray-200 dark:bg-gray-800 dark:border-gray-700">
  <div className="px-3 py-3 lg:px-5 lg:pl-3">
    <div className="flex items-center justify-between">
      <div className="flex items-center justify-start">
        <button data-drawer-target="logo-sidebar" data-drawer-toggle="logo-sidebar" aria-controls="logo-sidebar" type="button" className="inline-flex items-center p-2 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600">
            <span className="sr-only">Open sidebar</span>
            <svg className="w-6 h-6" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
               <path clip-rule="evenodd" fill-rule="evenodd" d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z"></path>
            </svg>
         </button>
        <Link to="/" className="flex ml-2 md:mr-24">
          <img src={logo} className="h-8 mr-3" alt="Main Logo" />
          <span className="self-center text-xl font-semibold sm:text-2xl whitespace-nowrap dark:text-white">PayFairShare</span>
        </Link>
      </div>
      <div className="flex items-center">
          <div className="flex items-center ml-3">

            <div style={{"background":getRandomColor()}}
              className={`inline-block text-xl pt-1.5 align-text-middle w-10 h-10 rounded-full text-center text-white`}>
                    {userdetails?.data.name.substring(0,1).toUpperCase()}
                 </div>
              
              <h2 className="text-md dark:text-white ml-2">{userdetails?.data.name}</h2>
              <button type="button" onClick={() => {logout();navigate("/login");toast.success("Logged out !")}} className="ml-3 text-red-700 hover:text-white border border-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-3 py-2 text-center dark:border-red-500 dark:text-red-500 dark:hover:text-white dark:hover:bg-red-600 dark:focus:ring-red-900">Logout</button>
          </div>
        </div>
    </div>
  </div>
</nav>

        </>
    );
}