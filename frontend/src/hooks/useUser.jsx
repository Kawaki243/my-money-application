import { AppContext } from '../context/AppContext';
import { useNavigate } from 'react-router-dom';
import { useEffect, useRef } from 'react';
import axiosConfig from '../util/axiosConfig';
import { useContext } from 'react';
import { API_ENDPOINTS } from '../util/apiEndpoints';


export const useUser = () => {
	const {user, setUser, clearUser} = useContext(AppContext);
	const navigate = useNavigate();

	useEffect(() => {
		if(user) {
			return;
		}

		let isMounted = true;
		const fetchUserData = async () => {
			try {
				const response = await axiosConfig.get(API_ENDPOINTS.GET_USER_INFO);
				if (isMounted && response.data) {
					setUser(response.data);
				}
			} catch (error) {
				console.log("Failed to fetch user data :", error);
				if (isMounted) {
					clearUser();
					navigate("/login");
				}
			}
		}

		fetchUserData();
		return () => {
			isMounted = false;
		}
	}, [setUser, clearUser, navigate]);
}