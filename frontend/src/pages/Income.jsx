import { useEffect, useState } from 'react';
import Dashboard from '../components/Dashboard';
import { useUser } from '../hooks/useUser';
import axiosConfig from '../util/axiosConfig';
import { API_ENDPOINTS } from '../util/apiEndpoints';
import toast from 'react-hot-toast';
import IncomeList from '../components/IncomeList';
import Model from '../components/Model';
import { Plus } from 'lucide-react';
import AddIncomeForm from '../components/AddIncomeForm';
import DeleteAlert from '../components/DeleteAlert';
import IncomeOverview from '../components/IncomeOverview';

const Income = () => {
	useUser();

	const [incomeData, setIncomeData] = useState([]);
	const [categories, setCategories] = useState([]);
	const [loading, setLoading] = useState(false);

	const [openAddIncomeModel, setOpenAddIncomeModel] = useState(false);
	const [openDeleteAlert, setOpenDeleteAlert] = useState({
		show:false,
		data:null,
	});

	const fetchIncomeDetails = async () => {
		if(loading) return;

		setLoading(true);

		try{
			const response = await axiosConfig.get(API_ENDPOINTS.GET_ALL_INCOMES);
			if(response.status === 200){
				setIncomeData(response.data);
				console.log(response.data);
			}
		} catch(error){
			console.error("Failed to fetch income details : ",error);
			toast.error(error.response?.data?.message || "Failed to fetch income details ! ");
		} finally{
			setLoading(false);
		}
	}

	// Fetch categories for income
	const fetchIncomeCategories = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.CATEGORY_BY_TYPE("income"));
			if(response.status === 200){
				setCategories(response.data);
			}
		} catch(error){
			console.error("Failed to fetch income categories : ", error);
			toast.error(error.data?.message || "Failed to fetch income categories");
		}
	}

	// Save Income Details
	const handleAddIncome = async(income) => {
		const {name, amount, date, icon, categoryId} = income
		if(!name.trim()){
			toast.error("Please enter an Income name");
			return;
		}
		if(!amount || isNaN(amount) || Number(amount) <= 0){
			toast.error("Amount should be a valid number greater than 0");
			return;
		}
		if(!date){
			toast.error("Please select a date");
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
			const response = await axiosConfig.post(API_ENDPOINTS.ADD_INCOME,{
				name,
				amount: Number(amount),
				date,
				icon,
				categoryId
			})
			if(response.status === 201){
				setOpenAddIncomeModel(false);
				toast.success("Income added successfully");
				fetchIncomeDetails();
			}
		} catch(error){
			console.error("Error adding Income",error);
			toast.error(error.response?.data?.message || "Failed to add an Income");
		}
	}

	// Delete income details
	const deleteIncome = async (id) => {
		setLoading(true)
		try{
			const response = await axiosConfig.delete(API_ENDPOINTS.DELETE_INCOME(id));
			setOpenDeleteAlert({show:false, data:null});
			toast.success("Income deleted successfully");
			fetchIncomeDetails();
		} catch(error){
			console.error("Error deleting income",error);
			toast.error(error.response?.data?.message || "Failed to delete income");
		} finally{
			setLoading(false);
		}
	}

	useEffect(() => {
		fetchIncomeDetails();
		fetchIncomeCategories();
	},[]);

	const handleDownloadIncomeDetails = async() => {
            try {
                  const response = await axiosConfig.get(API_ENDPOINTS.INCOME_EXCEL_DOWNLOAD, {responseType: "blob"});
                  let filename = "income_details.xlsx";
                  const url = window.URL.createObjectURL(new Blob([response.data]));
                  const link = document.createElement("a");
                  link.href = url;
                  link.setAttribute("download", filename);
                  document.body.appendChild(link);
                  link.click();
                  link.parentNode.removeChild(link);
                  window.URL.revokeObjectURL(url);
                  toast.success("Download income details successfully");
            }catch(error) {
                  console.error('Error downloading income details:', error);
                  toast.error(error.response?.data?.message || "Failed to download income");
            } 
      }

	const handleEmailIncomeDetals = async () => {
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.EMAIL_INCOME);
			if(response === 200){
				toast.success("Income details sent to your email successfully");
			}
		} catch(error){
			console.error("Error to send Income details to your email",error);
			toast.error(error.response?.data?.message || "Failed to email income");
		} 
	}

	return (
	      <Dashboard activeMenu="Income">
			<div className="my-5 mx-auto">
			      <div className="grid grid-cols-1 gap-6">
				      {/** overview for income with line char */}
                              <IncomeOverview transactions={incomeData} onAddIncome={() => setOpenAddIncomeModel(true)} />
				</div>
				<IncomeList 
				      transactions={incomeData}
					onDelete={(id) => setOpenDeleteAlert({show:true, data:id})}
					onDownload={handleDownloadIncomeDetails}
					onEmail={handleEmailIncomeDetals}
				/>
				{/** Add income Model */}
				<Model 
				      isOpen={openAddIncomeModel}
					onClose={() => setOpenAddIncomeModel(false)}
					title="Add Income"
				>
					<AddIncomeForm 
					      onAddIncome={(income) => handleAddIncome(income)}
						categories={categories}
					/>
				</Model>

				{/** Delete Income Model */}
				<Model 
				      isOpen={openDeleteAlert.show}
					onClose={() => setOpenDeleteAlert({show: false, data: null})}
					title="Delete Income"
				>
				      <DeleteAlert
					      loading={loading} 
					      content="Are you sure want to delete this income details ?"
						onDelete={() => deleteIncome(openDeleteAlert.data)}
					/>	
				</Model>	
			</div>	
		</ Dashboard>
	);
}

export default Income;