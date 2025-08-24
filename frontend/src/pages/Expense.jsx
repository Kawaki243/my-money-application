import React, { useEffect, useState } from 'react';
import Dashboard from '../components/Dashboard';
import { useUser } from '../hooks/useUser';
import axiosConfig from '../util/axiosConfig';
import { API_ENDPOINTS } from '../util/apiEndpoints';
import toast from 'react-hot-toast';
import { Plus } from 'lucide-react';
import ExpenseList from '../components/ExpenseList';
import Model from '../components/Model';
import AddExpenseForm from '../components/AddExpenseForm';
import DeleteAlert from '../components/DeleteAlert';
import ExpenseOverview from '../components/ExpenseOverview';

const Expense = () => {
	useUser();
	const [expensedata, setExpensedata] = useState([]);
	const [openAddExpenseModel, setOpenAddExpenseModel] = useState(false);
	const [loading, setLoading] = useState(false);
	const [openDeleteAlert, setOpenDeleteAlert] = useState({
		show:false,
		data:null,
	});
	const [categories, setCategories] = useState([]);

	const fetchExpenseDetails = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.GET_ALL_EXPENSES);
			if(response.status === 200){
				setExpensedata(response.data);
			}
		} catch(error){
			console.error("Failed to fetch expense details : ",error);
			toast.error(error.response?.data?.message || "Failed to fetch income details");
		}
	}

	const fetchExpenseCategories = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.CATEGORY_BY_TYPE("expense"));
			if(response.status === 200){
				setCategories(response.data);
				console.log(response.data);
			}
		} catch(error){
			console.error("Failed to fetch expense details : ",error);
			toast.error(error.response?.data?.message || "Failed to fetch income details");
		}
	}

	useEffect(() => {
		fetchExpenseDetails();
		fetchExpenseCategories();
	},[]);

	const deleteExpense = async (id) => {
		setLoading(true);
		try{
			const response = await axiosConfig.delete(API_ENDPOINTS.DELETE_EXPENSE(id));
			setOpenDeleteAlert({show:false, data:null});
			toast.success("Expense deleted successfully !");
			fetchExpenseDetails();

		} catch(error){
			console.error("Failed to delete expense : ",error);
			toast.error(error.response?.data?.message || "Failed to delete expense");
		} finally{
			setLoading(false);
		}
	}

	const handleDownloadExpenseDetails = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.DOWNLOAD_EXPENSES, {responseType: "blob"});
                  let filename = "expense_details.xlsx";
                  const url = window.URL.createObjectURL(new Blob([response.data]));
                  const link = document.createElement("a");
                  link.href = url;
                  link.setAttribute("download", filename);
                  document.body.appendChild(link);
                  link.click();
                  link.parentNode.removeChild(link);
                  window.URL.revokeObjectURL(url);
                  toast.success("Download expense details successfully");
            }catch(error) {
                  console.error('Error downloading expense details:', error);
                  toast.error(error.response?.data?.message || "Failed to download expense");
            }
	}

	const handleEmailExpenseDetails = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.EMAIL_EXPENSES);
			if(response === 200){
				toast.success("Expense details sent to your email successfully");
			}
		} catch(error){
			console.error("Error to send Expense details to your email",error);
			toast.error(error.response?.data?.message || "Failed to email expense details");
		}
	}

	const handleAddExpense = async (expense) => {
		const {name, icon, date, amount, categoryId} = expense;
		if(!name.trim()){
			toast.error("Please add an expense name");
			return;
		}
		if(!date){
			toast.error("Please add an expense date");
			return;
		}
		if(!amount || isNaN(amount) || Number(amount) <= 0){
			toast.error("Please add a propre expense amount");
			return;
		}
		const today = new Date().toISOString().split("T")[0];
		if(date > today){
			toast.error("Date cannot be in the future");
			return;
		}
		if(!categoryId){
			toast.error("Please select a Category");
			return;
		}
		try{
			const response = await axiosConfig.post(API_ENDPOINTS.ADD_EXPENSE,{
				name,
				amount: Number(amount),
				icon,
				date,
				categoryId
			});
			if(response.status === 201){
				setOpenAddExpenseModel(false);
				toast.success("Expense added successfully");
				fetchExpenseDetails();
			}
		} catch(error){
			console.error("Error adding Expense",error);
			toast.error(error.response?.data?.message || "Failed to add an Expense");
		}

	}

	return (
	      <Dashboard activeMenu="Expense">
			<div className="my-5 mx-auto">
				<div className="grid gird-cols-1 gap-6">
					<ExpenseOverview transactions={expensedata} onAddExpense={() => setOpenAddExpenseModel(true)} />
				</div>
				{/** Expense List */}
				<ExpenseList 
				      transactions={expensedata}
					onDelete={(id) => setOpenDeleteAlert({show:true, data:id})}
					onDownload={handleDownloadExpenseDetails}
					onEmail={handleEmailExpenseDetails}
				/>
				{/** Add expense Model */}
				<Model
				      isOpen={openAddExpenseModel}
					onClose={() => setOpenAddExpenseModel(false)}
					title="Add Expense"
				>
					<AddExpenseForm 
					      onAddExpense={(expense) => handleAddExpense(expense)}
						categories={categories}
					/>
				</Model>
				{/** Delete expense Model */}
				<Model
				      isOpen={openDeleteAlert.show}
					onClose={() => setOpenDeleteAlert({show: false, data:null})}
					title="Delete Expense"
				>
					<DeleteAlert 
					      loading={loading}
						content="Are you sure want to delete this Expense details ?"
						onDelete={() => deleteExpense(openDeleteAlert.data)}
					/>

				</Model>
			</div>
		</ Dashboard>
	);
}

export default Expense;