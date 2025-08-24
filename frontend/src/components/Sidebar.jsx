import React, { use, useContext } from "react";
import { AppContext } from "../context/AppContext";
import { User } from "lucide-react";
import { assets } from "../assets/assets.js";
import { SIDE_BAR_DATA } from "../assets/assets.js";
import { useNavigate } from "react-router-dom";


const Sidebar = ({activeMenu}) => {
     
	const { user } = useContext(AppContext);
	const navigate = useNavigate();

	return (
		<div className="w-64 h-[calc(100vh-61px)] bg-white border-gray-200 p-5 sticky top-[61px] z-20">
			<div className="flex flex-col items-center justify-center gap-3 mt-3 mb-7">
				{user?.profileImageUrl ? (
					<img src={user.profileImageUrl || assets.avatar} alt="User Profile Image" className="w-22 h-22 p-2 bg-white rounded-full object-cover border-2 border-[#fd6255] hover:ring-2 hover:ring-orange-500" />
				) : (
					<User className="w-22 h-22 p-2 bg-white rounded-full object-cover border-2 border-[#fd6255] hover:ring-2 hover:ring-orange-500" />
				)}
				<h5 className="text-gray-950 font-medium leading-6">{user.fullName || ""}</h5>
			</div>
			{SIDE_BAR_DATA.map((item, index) => 
			      <button
				      onClick={() => navigate(item.path)} 
				      key={`menu_${index}`}
				      className={`cursor-pointer w-full flex items-center gap-4 text-[15px] py-3 px-6 rounded-lg mb-3 ${activeMenu === item.label ? "bg-[#fd6255] text-white" : ""}`}
				>
				      <item.icon className="text-xl" />
				      {item.label}
				</button>
			)}
		</div>
	);
}

export default Sidebar;