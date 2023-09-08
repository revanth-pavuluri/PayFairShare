export type ButtonProps = {
    value: React.ReactNode,
    size?: string,
    type?: 'button' | 'submit' | 'reset',
    className?:string,
    onclick? : ()=>void;
}
export default function Button({value,size,type, onclick}:ButtonProps){
    return (
        <button onClick={onclick} type={type} className={size+" inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-md font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-green-400 to-blue-600 group-hover:from-green-400 group-hover:to-blue-600 hover:text-white dark:text-white focus:ring-4 focus:outline-none focus:ring-green-200 dark:focus:ring-green-800"}>
            <span className={size+" inline-flex items-center justify-center dark:bg-gray-800 p-2 px-5.2 py-2.5 transition-all ease-in rounded-md duration-75 bg-white group-hover:bg-opacity-0"}>
            {value}
            </span>
        </button>
        );
}