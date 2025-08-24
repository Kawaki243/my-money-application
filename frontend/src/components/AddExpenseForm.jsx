import { useEffect, useState } from "react";
import EmojiPickerPopup from "./EmojiPickerPopup";
import Input from "./Input";
import { LoaderCircle } from "lucide-react";

/**
 * A form component for adding new expenses.
 * @component
 * @param {Object} props - Component props
 * @param {Function} props.onAddExpense - Callback function to handle adding a new expense
 * @param {Array<Object>} props.categories - Array of category objects containing id and name
 * @param {string} props.categories[].id - Unique identifier for the category
 * @param {string} props.categories[].name - Name of the category
 * @returns {JSX.Element} A form with inputs for expense details including emoji picker, name, category selection, amount and date
 */
const AddExpenseForm = ({onAddExpense, categories}) => {
	const [expense, setExpense] = useState({
		name:"",
		icon:"",
		date:"",
		amount:"",
		categoryId:""
	});

	const [loading, setLoading] = useState(false);

	const categoryOptions = categories.map((category) => ({
		value: category.id,
		label: category.name,
	}));

	const handleChange = (key,value) => {
		setExpense({...expense, [key]:value})
	}

	const handleAddExpense = async () => {
		setLoading(true);
		try{
			await onAddExpense(expense);
		} finally{
			setLoading(false);
		}
	}

	useEffect(() => {
		if (categories.length > 0 && !expense.categoryId) {
			setExpense((prev) => ({ ...prev, categoryId: categories[0].id }));
		}
	}, [categories, expense.categoryId] );


	return (
		<div>
			<EmojiPickerPopup 
			      icon={expense.icon}
				onSelect={(selectedIcon) => handleChange("icon", selectedIcon)}
			/>
			<Input 
			      label="Expense Source"
				value={expense.name}
				onChange={({target}) => handleChange("name", target.value)}
				placeholder="e.g, Rent, Food, Utilities"
				type="text"
			/>
			<Input 
			      label="Category"
				value={expense.categoryId}
				onChange={({target}) => handleChange("categoryId", target.value)}
				isSelect={true}
				options={categoryOptions}
			/>
			<Input 
			      label="Amount"
				value={expense.amount}
				onChange={({target}) => handleChange("amount", target.value)}
				placeholder="e.g, 100000 200000 500000 ( Vnd )"
				type="text"
			/>
			<Input 
			      label="Date"
				value={expense.date}
				onChange={({target}) => handleChange("date", target.value)}
				placeholder=""
				type="date"
			/>
			<div className="flex justify-end mt-6">
				<button
				      disabled={loading}
				      onClick={handleAddExpense} 
				      className="add-btn add-btn-fill"
				>
					{loading ? (
						<>
						      <LoaderCircle className="w-4 h-4 animate-spin" /> Adding...
						</>
					) : (
						<>
						      Add Expense
						</>
					)}

				</button>
			</div>
		</div>
	)
}

export default AddExpenseForm;