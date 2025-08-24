import { Mail, Download, LoaderCircle } from "lucide-react";
import TransactionInfoCard from "./TransactionInfoCard";
import { useState } from "react";

const IncomeList = ({transactions, onDelete, onDownload, onEmail}) => {
	const [loadingEmail, setLoadingEmail] = useState(false);
	const [loading, setLoading] = useState(false);

	const handleEmail = async () => {
		setLoadingEmail(true);
		try{
			await onEmail();
		} finally{
			setLoadingEmail(false);
		}
	}

	const handleDownload = async () => {
		setLoading(true);
		try{
			await onDownload();
		} finally{
			setLoading(false);
		}
	}

	return (
		<div className="card">
			<div className="flex items-center justify-between">
				<h5 className="text-lg">
					Income Sources 
				</h5>
				<div className="flex items-center justify-end gap-2 ">
					<button
					      disabled={loadingEmail}
						onClick={handleEmail}
						className="card-btn" 
						>
						{
							loadingEmail ? (
								<>
								      <LoaderCircle className="w-4 h-4 animate-spin" />
								      <Mail size={15} className="text-base" /> Emailing... 
								</>
							) : (
								<>
								      <Mail size={15} className="text-base" /> Email 
								</>
							)
						}
					</button>
					<button
					      disabled={loading}
					      onClick={handleDownload}
						className="card-btn"
					>
						{
							loading ? (
								<>
								      <LoaderCircle className="w-4 h-4 animate-spin" />
								      <Download size={15} className="text-base" /> Downloading... 
								</>
							) : (
								<>
								      <Download size={15} className="text-base" /> Download 
								</>
							)
						}
					</button>
				</div>
			</div>

			<div className="grid grid-cols-1 md:grid-cols-2">
				{/** display the incomes */}
				{transactions?.map((income) => (
					<TransactionInfoCard
						key={income.id}
						icon={income.icon}
						title={income.name}
						date={income.date.split('T')[0] + " at "+income.updatedAt.split('T')[1].split('.')[0]+(
							parseInt(income.updatedAt.split('T')[1].split('.')[0].split(':')[0]) >= 12 ? " pm " : " am "
				            )}
						amount={income.amount}
						type="income"
						onDelete={() => onDelete(income.id)}
					/>
				))}
			</div>
		</div>
	)
}

export default IncomeList;