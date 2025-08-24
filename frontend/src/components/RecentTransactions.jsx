import { ArrowRight } from "lucide-react";
import TransactionInfoCard from "../components/TransactionInfoCard";

const RecentTransactions = ({transactions, onMore}) => {
	return (
		<div className="card">
			<div className="flex items-center justify-between">
				<h4 className="text-lg">
					Recent Transactions 
				</h4>
				<button 
				      onClick={onMore}
			            className="card-btn"
				>
					More Transactions <ArrowRight className="text-base" size={15}/> 
				</button>
			</div>

			<div className="mt-6">
				{transactions?.slice(0, 5)?.map(item => (
					<TransactionInfoCard 
					      key={crypto.randomUUID()}
						title={item.name}
						icon={item.icon}
						date={item.date.split('T')[0]}
						amount={item.amount}
						type={item.type}
						hideDeletebtn
					/>
				))}
			</div>
		</div>
	)
}

export default RecentTransactions;