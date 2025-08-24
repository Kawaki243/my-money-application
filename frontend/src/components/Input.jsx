import React, { useState } from 'react';
import { Eye, EyeOff } from 'react-feather';
/**
 * Reusable Input component.
 * 
 * This component renders a styled input field with a label.
 * It is fully controlled, meaning its value is managed by the parent component.
 *
 * @component
 * @example
 * // Usage example:
 * <Input 
 *    label="Email" 
 *    type="email" 
 *    placeholder="Enter your email" 
 *    value={email} 
 *    onChange={(e) => setEmail(e.target.value)} 
 * />
 *
 * @param {Object} props - The props for the Input component
 * @param {string} props.label - The text label displayed above the input field
 * @param {string} props.value - The current value of the input (controlled input)
 * @param {function} props.onChange - Callback fired when the input value changes
 * @param {string} props.placeholder - Placeholder text shown when the input is empty
 * @param {string} props.type - The type of input (e.g. "text", "password", "email")
 */
const Input = ({ label, value, onChange, placeholder, type , isSelect, options}) => {
  const [showPassword, setShowPassword] = useState(false);

  // Toggle password visibility
  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };
 

  return (
    <div className="mb-4">
      <label className="text-[13px] text-slate-800 block mb-1">
        {label}
      </label>
      <div className="relative">
        {isSelect ? (
          <select 
            className="w-full bg-transparent outline-none border border-gray-300 rounded-md py-2 px-3 text-gray-700 leading focus:outline-none focus:border-blue-500"
            value={value}
            onChange={(e) => onChange(e)}
          >
            {options.map((option) => (
              <option key={option.value} value={option.value} className="p-4">
                {option.label}
              </option>
            ))}
          </select>
        ) : (
        <input
          className="w-full bg-transparent outline-none border border-gray-300 rounded-md py-2 px-3 pr-10 text-gray-700 leading-tight focus:outline-none focus:border-blue-500"
          type={type === 'password' ? (showPassword ? 'text' : "password" ) : type}
          placeholder={placeholder}
          value={value}
          onChange={(e) => onChange(e)}
        />
        )}

        {type === 'password' && (
          <span className="absolute right-3 top-1/2 -translate-y-1/2 cursor-pointer">
            {
              showPassword ? (
                <Eye 
                      size={20} 
                      className="text-[#fd6255]"
                      onClick={togglePasswordVisibility}
                />
              ) : (
                <EyeOff 
                        size={20}
                        className="text-slate-400"
                        onClick={togglePasswordVisibility}
                />
              )
            }
          </span>
        )}
      </div>
    </div>
  );
};


export default Input;