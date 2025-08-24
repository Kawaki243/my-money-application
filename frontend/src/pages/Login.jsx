import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { assets } from "../assets/assets.js";
import Input from "../components/Input.jsx";
import { Link } from "react-router-dom";
import { validateEmail } from "../util/validation.js";
import axiosConfig from "../util/axiosConfig.js";
import toast from "react-hot-toast";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import { LoaderCircle } from "lucide-react";
import {AppContext} from "../context/AppContext";
import { useContext } from "react";

const Login = () => {

	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [error, setError] = useState(null);
	const [isLoading, setIsLoading] = useState(false);
	const {setUser} = useContext(AppContext);

	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		setIsLoading(true);

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

		try {
			const response = await axiosConfig.post(API_ENDPOINTS.LOGIN, {
				email,
				password
			});
			const {token, user} = response.data;
			if(token){
				localStorage.setItem("token", token);
				setUser(user);
				navigate("/dashboard");
				toast.success("Login successful ! Welcome back.");
			}
		} catch(err) {
			console.error("Error during login: ", err);
			if(err.response && err.response.data && err.response.data.message){
				setError(err.response.data.message);
			} else {
				setError("An unexpected error occurred. Please try again.");
			}
			setIsLoading(false);
		} finally {
			setIsLoading(false);
		}
	};

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
						Welcome back to MyMoney
					</h3>
					<p className="text-sm text-slate-700 text-center mb-8">
						Please login to continue
					</p>

					<form onSubmit={handleSubmit} className="space-y-4">
						<div className="flex flex-col gap-4">
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
							{isLoading ? (
								<>
									<LoaderCircle className="animate-spin w-5 h-5 mr-2" size={20} />
									Logging In ...
								</>
							) : (
								"LOGIN"
							)}
						</button>

						<p className="text-sm text-slate-800 text-center mt-4">
							Do not to have any account ?
							<Link to="/signup" className="font-medium text-primary underline hover:text-primary-dark transition-colors">
								Sign Up
							</Link>
						</p>
					</form>
				</div>

			</div>
		</div>
	);
}

export default Login;