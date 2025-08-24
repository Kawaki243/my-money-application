import React, { useState, useRef } from 'react';
import { Upload } from 'react-feather';
import toast from 'react-hot-toast';
import { User, Trash } from 'lucide-react';




const ProfileImageSelector = ({image, setImage}) => {

	const inputRef = useRef(null);
	const [previewUrl, setPreviewUrl] = useState(null);

	const handleImageChange = (e) => {
		e.preventDefault();
		const file = e.target.files[0];
		if (file) {
			const preview = URL.createObjectURL(file);
			setPreviewUrl(preview);
			setImage(file);
			toast.success("Profile image selected successfully!");
		}
	}

	const handleRemoveImage = () => {
		setImage(null);
		setPreviewUrl(null);
		toast.success("Profile image removed successfully!");
	}

	const onChooseFile = (e) => {
		e.preventDefault();
		if (inputRef.current) {
			inputRef.current.click();
		}
	}

	return (
		<div className="flex justify-center mb-6">
			<input type="file" 
			      ref={inputRef}
				onChange={handleImageChange}
				className="hidden"
			/>

			{!image ? (
				<div className="w-20 h-20 flex items-center justify-center bg-orange-100 rounded-full relative">
					<User className="text-orange-500" size={35}/>
					<button 
						onClick={onChooseFile} 
						className="w-8 h-8 flex items-center justify-center bg-primary text-white rounded-full absolute -bottom-1 -right-1"
					> 
					      <Upload size={15} className="text-[#fd6255]"/>
					</button>
				</div>
			) : (
				<div className="relative">
					<img src={previewUrl} alt="profile image" className="w-20 h-20 rouned-full object-cover"/>
					<button 
						onClick={handleRemoveImage} 
						className="w-8 h-8 flex items-center justify-center bg-red-800 text-white rounded-full absolute -bottom-1 -right-1"
					>
						<Trash size={15} className="text-[#fd6255]"/>
					</button>
				</div>
			)}
		</div>
	);
}

export default ProfileImageSelector;