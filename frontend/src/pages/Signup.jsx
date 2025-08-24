import { useNavigate } from "react-router-dom";
import React, { useState } from "react";
import {assets} from "../assets/assets.js";
import Input from "../components/Input.jsx";
import { Link } from "react-router-dom";
import { validateEmail } from "../util/validation.js";
import axiosConfig from "../util/axiosConfig.js";
import toast from "react-hot-toast";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import { LoaderCircle } from "lucide-react";
import ProfileImageSelector from "../components/ProfileImageSelector.jsx";
import uploadProfileImage from "../util/uploadProfileimage.js";




const Signup = () => {
	const [fullName, setFullName] = useState("");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [error, setError] = useState(null);
	const [isLoading, setIsLoading] = useState(false);
	const [profileImage, setProfileImage] = useState(null);

	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		let profileImageUrl = "";
		setIsLoading(true);

		if(!fullName.trim()){
			setError("Please enter your full name ! ");
			setIsLoading(false);
			return;
		}

		if(!validateEmail(email)){
			setError("Please valid your email address ! ");
			setIsLoading(false);
			return;
		}

		if(!password.trim()){
			setError("Please enter your password ! ");
			setIsLoading(false);
			return;
		}

		setError("");

		// Here you would typically send the data to your backend API
		try{
			// upload profile image if presented
			if(profileImage){
				const imageUrl =  await uploadProfileImage(profileImage);
				profileImageUrl = imageUrl || "";
			} 

			const response = await axiosConfig.post(API_ENDPOINTS.REGISTER, {
				fullName,
				email,
				password,
				profileImageUrl
			})
			if(response.status === 201){
				toast.success("Account created successfully ! Please login.");
				navigate("/login");
			}
		} catch(err){
			console.error("Error during signup : ", err);
			if(err.response && err.response.data && err.response.data.message){
				setError(err.response.data.message);
			} else {
				setError("An error occurred during signup. Please try again.");
			}
		} finally{
			setIsLoading(false);
		}
	}


	return (
		<div className="h-screen w-full relative flex items-center justify-center overflow-hidden">
			{/* Background Image with blur */}
			<img
				src={assets.login_background}
				alt="background"
				className="absolute inset-0 w-full h-full object-cover filter blur-sm -z-10"
			/>
			{/* Add your signup form or content here */}

			<div className="relative z-10 w-full max-w-lg px-6">
				<div className="bg-white bg-opacity-95 backdrop-blur-sm rounded-lg shadow-2xl p-8 mx-h-[90vh] overflow-y-auto">
					<h3 className="text-2xl font-semibold text-black text-center mb-2">
						Create An Account
					</h3>
					<p className="text-sm text-slate-700 text-center mb-8">
						Start tracking your expenses today !
					</p>

					<form onSubmit={handleSubmit} className="space-y-4">
						<div className="flex justify-center mb-6">
							<ProfileImageSelector image={profileImage} setImage={setProfileImage}/>
						</div>
						<div className="flex flex-col gap-4">
							<Input 
								value={fullName}
								onChange={(e) => setFullName(e.target.value)}
								label="Full Name"
								placeholder="Enter your full name"
								type="text"
							/>

							<Input 
								value={email}
								onChange={(e) => setEmail(e.target.value)}
								label="Email Address"
								placeholder="fullname@example.com"
								type="email"
							/>

							<Input 
								value={password}
								onChange={(e) => setPassword(e.target.value)}
								label="Password"
								placeholder="*********"
								type="password"
							/>
						</div>
						{error && (
							<p className="text-red-500 text-sm text-center bg-red-50 p-2 rounded">
								{error}
							</p>
						)}

						<button disabled={isLoading} className={`w-full h-4/5 py-2.5 text-base font-medium color text-white bg-orange-500 hover:bg-[#fd6255] rounded-md shadow-xl/20 flex items-center justify-center gap 2 ${isLoading ? `opacity-60 cursor-not-allowed`: ``}`} type="submit">
							{
								isLoading ? (
									<>
									      <LoaderCircle className="animate-spin w-5 h-5 mr-2" size={20} />
									      Signing Up ...
									</>
								) : (
									"Sign Up"
								)
				                  }
						</button>

						<p className="text-sm text-slate-800 text-center mt-4">
							Already have an account?
							<Link to="/login" className="font-medium text-primary underline hover:text-primary-dark transition-colors">
								Login
							</Link>
						</p>
					</form>
				</div>

			</div>
		</div>
	);
}

export default Signup;