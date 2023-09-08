import React, { ReactNode } from 'react';
import Button from './Button';

type ModalProps = {
    children: ReactNode;
    onClose? : () => void;
    onShow ? : () => void;
}
  
  
const Modal: React.FC<ModalProps> = ({ children,onClose, onShow }) => {
  if (onShow) {
    onShow();
  }
    return (
    <div className="relative  z-40" aria-labelledby="modal-title" role="dialog" aria-modal="true">

  <div className="fixed inset-0 bg-gray-200 bg-opacity-1 dark:bg-gray-800 transition-opacity"></div>

  <div className="fixed inset-0 z-10 overflow-y-auto">
    <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
      <div className="border-2 dark:border-gray-700 relative transform overflow-hidden rounded-lg bg-white  text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
      <div className="bg-white dark:bg-gray-800 px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
      <div className="flex justify-end"><Button onclick={onClose}  type="button" value={<svg className="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                    </svg>}/>
        </div>         

        {children}
        </div>
        
      </div>
    </div>
  </div>
</div>
)
}
export default Modal;
