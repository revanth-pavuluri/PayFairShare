import React from 'react';
import './App.css';
import { Navigate, Route, Routes } from "react-router-dom";
import Login from './Components/Login';
import Dashboard from './Components/Dasboard';
import Register from './Components/register';
import Groupdash from './Components/Groupdash';
import { ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import Joingroup from './Components/Joingroup';
import ProtectedRoutes from './Components/ProtectedRoutes';

export type user = {
  id : number,
  name : string
}

export type userdata = {
  id : number,
  name : string,
  email : string,
  upiId : string
}

function App() {
  return (
    <div className="dark:bg-gray-800">
      <ToastContainer/>
     <Routes>
      <Route path="/login" element={<Login/>} />
      <Route path="/register" element={<Register/>}/>
    
     <Route element = {<ProtectedRoutes/>}>
      <Route path="/" element={<Dashboard />}/>
      <Route path="/g/:id" element={<Groupdash />} />
      <Route path="/join/:id" element={<Joingroup/>}/>
    </Route>
    
    <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  </div>
    
  );
}

export default App;
