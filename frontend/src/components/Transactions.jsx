import { ArrowRight } from "lucide-react";
import TransactionInfoCard from "./TransactionInfoCard";

const Transactions = ({transactions, onMore, type, title}) => {
	return (
		<div className="card">
			<div className="flex items-center justify-between">
				<h5 className="text-lg">{title}</h5>
				<button className="card-btn" onClick={onMore}>
					More <ArrowRight className="text-base" size={15} />
				</button>
			</div>

			<div className="mt-6">
				{transactions?.slice(0,5)?.map(item => (
					<TransactionInfoCard 
					      key={item.id}
						title={item.name}
						icon={item.icon}
						date={item.date.split('T')[0]}
						amount={item.amount}
						type={type}
						hideDeletebtn
					/>
				))}
			</div>
		</div>
	)
}

export default Transactions;