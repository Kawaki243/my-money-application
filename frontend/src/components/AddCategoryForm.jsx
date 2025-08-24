import { useEffect, useState } from 'react';
import Input from './Input';
import EmojiPickerPopup from './EmojiPickerPopup';
import { LoaderCircle } from 'lucide-react';

/**
 * A form component for adding or editing categories
 * @component
 * @param {Object} props - Component props
 * @param {Function} props.onAddCategory - Callback function to handle category addition/update
 * @param {Object} [props.initialCategoryData] - Initial category data for editing mode
 * @param {string} [props.initialCategoryData.name] - Category name
 * @param {string} [props.initialCategoryData.type] - Category type (income/expense)
 * @param {string} [props.initialCategoryData.icon] - Category icon
 * @param {boolean} [props.isEditing] - Flag to determine if form is in edit mode
 * @returns {JSX.Element} A form with emoji picker, name input, and type selector
 */
const AddCategoryForm = ({onAddCategory, initialCategoryData, isEditing}) => {
	/**
            * Local state for category fields: name, type, icon.
      */
	const [category, setCategory] = useState({
		name:"",
		type:"income",
		icon:""
	});

	const [loading, setLoading] = useState(false);

	const categoryTypeOptions = [
		{value:"income", label: "Income"},
		{value:"expense", label:"Expense"}
	];
	const handleChange = (key, value) => {
		setCategory({...category, [key]:value})
	};

	const handleSubmit = async () => {
		setLoading(true);
		try{
			await onAddCategory(category);
		} finally{
			setLoading(false);
		}
	}

	useEffect(() => {
		if(isEditing && initialCategoryData){
			setCategory(initialCategoryData);
		} else{
			setCategory({name:"",type:"income",icon:""});
		}
	},[])

	return (
		<div className="p-4">

			<EmojiPickerPopup 
			      icon={category.icon}
				onSelect={(selectedIcon) => handleChange("icon", selectedIcon)}
			/>

			<Input 
			      value={category.name}
			      onChange={({target}) => handleChange("name", target.value)}
				label="Category Name"
				placeholder="e.g., Freelance, Salary, Bonus"
				type="text"
			/>

			<Input 
			      label="Category Type"
				value={category.type}
				onChange={({target}) => handleChange("type", target.value)}
				isSelect={true}
				options={categoryTypeOptions}
			/>

			<div className="flex justify-end mt-6">
				<button
				      type="button"
					disabled={loading}
				      onClick={handleSubmit} 
				      className="bg-[#fd6255] hover:bg-orange-600 p-2 px-4 rounded-lg text-white text-[14px] text-slate-800 block"
				>
					{loading ? (
						<>
						      <LoaderCircle className="w-4 h-4 animate-spin" />
						      {isEditing ? "Updating..." : "Adding..."}
						</>
					) : (
						<>
						      {isEditing ? "Update Category" : "Add Category"}
						</>
					)}
				</button>
			</div>
		</div>
	)
}

export default AddCategoryForm;