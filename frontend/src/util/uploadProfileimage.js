import axiosConfig from "../util/axiosConfig.js";
import { API_ENDPOINTS } from "../util/apiEndpoints.js";
import { toast } from "react-hot-toast";

const CLOUDINARY_UPLOAD_PRESET = "mymoney";

const uploadProfileImage = async (image) => {
	const formData = new FormData();
	formData.append("file", image);
	formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);

	try{
		const response = await fetch(API_ENDPOINTS.UPLOAD_IMAGE, {
			method: "POST",
			body: formData
		});
		const data = await response.json();
		if (!response.ok) {
			throw new Error("Failed to upload image to Cloudinary : ", data.error.message);
		} else{
			console.log("Image uploaded successfully to Cloudinary : ", data.secure_url);
			return data.secure_url; // Return the URL of the uploaded image
		}
	} catch (error) {
		console.error("Error uploading image to Cloudinary : ", error);
		throw error;
	}
}

export default uploadProfileImage;