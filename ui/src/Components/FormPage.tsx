import logo from "./logo.png"
import React, { ReactNode } from 'react';

type FormPageProps = {
  children: ReactNode;
  name : string;
}

const FormPage: React.FC<FormPageProps> = ({ children,name }) => {
    return (
        <section className="bg-gray-50 dark:bg-gray-900">
  <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
      <a href="/" className="flex items-center mb-6 text-2xl font-semibold text-gray-900 dark:text-white">
          <img className="w-8 h-8 mr-2" src={logo} alt="logo" />
          PayFairShare    
      </a>
      <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
              <h1 className="text-xl font-bold leading-tight mb-2 tracking-tight text-gray-900 md:text-2xl dark:text-white">
                  {name}
              </h1>
              {children}
              </div>
      </div>
  </div>
</section>
)
}
export default FormPage;
