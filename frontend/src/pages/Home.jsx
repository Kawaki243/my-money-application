import { Coins, Wallet, WalletCards } from "lucide-react";
import Dashboard from "../components/Dashboard";
import InfoCard from "../components/InfoCard";
import { useUser } from "../hooks/useUser";
import { addThousandsSeparator } from "../util/util";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axiosConfig from "../util/axiosConfig";
import { API_ENDPOINTS } from "../util/apiEndpoints";
import toast from "react-hot-toast";
import RecentTransactions from "../components/RecentTransactions";
import FinanceOverview from "../components/FinanceOverview";
import Transactions from "../components/Transactions";

const Home = () => {
	useUser();

	const navigate = useNavigate();
	const [dashboardData, setDashboardData] = useState(null);
	const [loading, setLoading] = useState(false);

	const fetchDashboardData = async () => {
		if(loading) return;
		setLoading(true);
		try{
			const response = await axiosConfig.get(API_ENDPOINTS.DASHBOARD_DATA);
			if(response.status === 200){
				setDashboardData(response.data);
			}
		} catch (error){
			console.error("Failed to fetch Dashboard Data", error);
			toast.error("Something went wrong");
		} finally{
			setLoading(false);
		}
	}

	useEffect(() => {
		fetchDashboardData();
		return () => {};
	},[]);

	return (
		<Dashboard activeMenu="Dashboard">
			<div className="my-5 mx-auto">
				<div className="grid grid-cols-1 md:grid-cols-3 gap-6">
					{/** Display the cards */}
					<InfoCard 
					      icon={<WalletCards />}
						label="Total Balance"
						value={addThousandsSeparator(dashboardData?.totalBalance || 0)}
						color="bg-[#fd6255]"
					/>
					<InfoCard 
					      icon={<Wallet />}
						label="Total Income"
						value={addThousandsSeparator(dashboardData?.totalIncome || 0)}
						color="bg-green-500"
					/>
					<InfoCard 
					      icon={<Coins />}
						label="Total Expense"
						value={addThousandsSeparator(dashboardData?.totalExpenses || 0)}
						color="bg-red-500"
					/>
				</div>
				<div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6" >
					{/** Recent Transactions */}
					<RecentTransactions 
					      transactions={dashboardData?.recentTransactions}
						onMore={() => navigate("/expense")}
					/>
					{/** Finance Overview */}
					<FinanceOverview
					      totalBalance={dashboardData?.totalBalance || 0}
						totalIncome={dashboardData?.totalIncome || 0}
						totalExpense={dashboardData ?.totalExpenses || 0} 
					/>

					{/** Expense Transactions */}
					<Transactions
					      transactions={dashboardData?.recent5Expenses || []}
						onMore={() => navigate("/expense")}
						type="expense"
						title="Recent Expenses"
					/>

					{/** Income Transactions */}
					<Transactions
					      transactions={dashboardData?.recent5Income || []}
						onMore={() => navigate("/income")}
						type="income"
						title="Recent Incomes"
					/>
				</div>
			</div>
		</ Dashboard>
	);
}

export default Home;