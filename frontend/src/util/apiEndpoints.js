// export const BASE_URL = "https://my-money-api-quao.onrender.com/api/v1.0";
export const BASE_URL = "http://localhost:8080/api/v1.0";

export const CLOUDINARY_CLOUD_NAME = "deppls3fc";


export const API_ENDPOINTS = {
	LOGIN: "/login",
	REGISTER: "/register",
	GET_USER_INFO: "/profile",
	GET_ALL_CATEGORIES: "/categories",
	ADD_CATEGORY: "/categories",
	UPDATE_CATEGORY: (categoryId) => `/categories/${categoryId}`,
	GET_ALL_INCOMES: "/incomes",
	CATEGORY_BY_TYPE: (type) => `/categories/${type}`,
	ADD_INCOME: "/incomes",
	DELETE_INCOME: (id) => `/incomes/${id}`,
	INCOME_EXCEL_DOWNLOAD: "/excel/download/incomes",
	EMAIL_INCOME:"/email/income-excel",
	GET_ALL_EXPENSES: "/expenses",
	ADD_EXPENSE: "/expenses",
	EMAIL_EXPENSES: "/email/expense-excel",
	DOWNLOAD_EXPENSES: "/excel/download/expenses",
	DELETE_EXPENSE: (id) => `/expenses/${id}`,
	APPLY_FILTER: "/filter",
	DASHBOARD_DATA: "/dashboard",
	UPLOAD_IMAGE: "https://api.cloudinary.com/v1_1/deppls3fc/image/upload"
}