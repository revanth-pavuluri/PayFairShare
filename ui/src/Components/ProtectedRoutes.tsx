import { Navigate, Outlet, useLocation } from "react-router-dom";

const useAuth = () => {
  const user = window.localStorage.getItem('userdata');
  return user;
};

const ProtectedRoutes = () => {
  const location = useLocation();
  const isAuth = useAuth();
  return isAuth ? <Outlet /> : <Navigate to="/login" replace state={{ from: location }} />;
};

export default ProtectedRoutes;