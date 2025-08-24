import React, { useState, useRef, useEffect } from 'react';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import {AppContext} from '../context/AppContext';
import { X, User, Menu, LogOut } from 'lucide-react';
import { assets } from "../assets/assets.js";
import Sidebar from './Sidebar.jsx';


const Menubar = ({activeMenu}) => {

	const [openSideMenu, setOpenSideMenu] = useState(false);
	const [showDropdown, setShowDropdown] = useState(false);
	const dropdownRef = useRef(null);
	const {user, clearUser} = useContext(AppContext);
	const navigate = useNavigate();

	const handleLogout = () => {
		localStorage.clear();
		clearUser();
		setShowDropdown(false);
		navigate("/login");
		toast.success("Logout successful ! See you again.");
	}

	useEffect(() => {
		const handleClickOutside = (event) => {
			if(dropdownRef.current && !dropdownRef.current.contains(event.target)) {
				setShowDropdown(false);
			}
		};

		if (showDropdown) {
			document.addEventListener('mousedown', handleClickOutside);
		}

		return () => {
			document.removeEventListener('mousedown', handleClickOutside);
		}
	}, [showDropdown]);


	return (
		<div className="flex items-center justify-between gap-5 bg-white border border-b border-gray-200/50 backdrop-blur-[2px] py-4 sm:px-7 sticky top-0 z-30">
			{/** Left side - Menu button and title */}
			<div className="flex items-center gap-5">
				<button 
				      onClick={() => setOpenSideMenu(!openSideMenu)}
				      className="block lg:hidden text-black hover:bg-gray-100 p-1 rouned transition-colors"
				>
					{openSideMenu ? (
						<X className="text-2xl"/>
					) : (
						<Menu className="text-2xl"/>
					)}

				</button>

				<div className="flex items-center gap-2">
					<img src={assets.logo} alt="logo" className="h-16 w-16"/>
					<span className="text-lg font-medium text-black truncate">My Money</span>
				</div>
			</div>

			{/** Right side - Avatar Image */}
			<div className="relative" ref={dropdownRef}>
				<button 
				      onClick={() => setShowDropdown(!showDropdown)}
				      className="flex items-center justify-center w-10 h-10 bg-gray hover:bg-gray-200 rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-orange-700 focus:ring-offset-2"
				>
					<User className="text-[#fd6255]"/>
				</button>

				{/** Dropdown Menu */}
				{showDropdown && (
					<div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-50">
						<div className="px-4 py-3 border-b border-gray-100">
							<div className="flex items-center gap-3">
								<div className="flex items-center justify-center w-8 h-8 bg-gray-100 rounded-full">
									<User className="text-[#fd6255] w-4 h-4"/>
								</div>
								<div className="flex-1 min-w-0">
									<p className="text-sm font-medium text-gray-800 truncate">
										{user.fullName || "User Name"}
									</p>
									<p className="text-xs text-gray-500 truncate">
										{user.email || "Email Address"}
									</p>
								</div>
							</div>
						</div>

						{/* Dropdown options */}
						<div className="py-1">
							<button
							      onClick={handleLogout}
							      className="flex items-center gap-3 w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 transition-colors duration-150">
								<LogOut className="text-gray-500 w-4 h-4"/>
								<span className="flex-1">Logout</span>
							</button>
						</div>
					</div>
				)}
			</div>

			{/** Mobile side menu */}
			{openSideMenu && (
				<div className="fixed top-[73px] left-0 right-0 bg-white border-b border-gray-200 lg:hidden z-20">
					<Sidebar activeMenu={activeMenu} />
                        </div>
                  )}
		</div>
	)
}

export default Menubar;