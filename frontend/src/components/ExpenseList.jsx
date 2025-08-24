import { Download, LoaderCircle, Mail } from "lucide-react";
import { useState } from "react";
import TransactionInfoCard from "./TransactionInfoCard";

const ExpenseList = ({transactions, onDelete, onDownload, onEmail}) => {

	const [loadingEmail, setLoadingEmail] = useState(false);
	const [loadingDownload, setLoadingDownload] = useState(false);

	const handleEmail = async () => {
		setLoadingEmail(true);
		try{
			await onEmail();
		} finally{
			setLoadingEmail(false);
		}
	}

	const handleDownload = async () => {
		setLoadingDownload(true);
		try{
			await onDownload();
		} finally{
			setLoadingDownload(false);
		}
	}

	return (
		<div className="card">
			<div className="flex justify-between">
				<h5 className="text-lg">Expense Sources</h5>
				<div className="flex items-center justify-end gap-2">
					<button
					      disabled={loadingEmail}
					      className="card-btn"
						onClick={handleEmail}
					>
						{ loadingEmail ? (
							<>
							      <LoaderCircle className="w-4 h-4 animate-spin" />
								<Mail size={15} className="text-base" /> Emailing...
							</>
						) : (
							<>
							      <Mail size={15} className="text-base" /> Email
							</> 
						)}
					</button>
					<button
					      disabled={loadingDownload}
					      className="card-btn"
						onClick={handleDownload}
					>
						{
							loadingDownload ? (
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
				{transactions?.map((expense) => (
					<TransactionInfoCard 
					      key={expense.id}
				            icon={expense.icon}
						type="expense"
						title={expense.name}
						date={expense.date.split('T')[0] + " at "+expense.updatedAt.split('T')[1].split('.')[0]+(
							parseInt(expense.updatedAt.split('T')[1].split('.')[0].split(':')[0]) >= 12 ? " pm " : " am "
				            )}
						amount={expense.amount}
						onDelete={() => onDelete(expense.id)}
					/>
				))}
			</div>
		</div>
	)
}

export default ExpenseList;