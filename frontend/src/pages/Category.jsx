import React, { useEffect } from 'react';
import Dashboard from '../components/Dashboard';
import { useState } from 'react';
import { useUser } from '../hooks/useUser';
import { Plus } from 'lucide-react'; // Assuming you want to use Plus icon for adding categories
import CategoryList from '../components/CategoryList'; // Importing the CategoryList component
import { API_ENDPOINTS } from '../util/apiEndpoints';
import axiosConfig from '../util/axiosConfig';
import toast from 'react-hot-toast';
import Model from '../components/Model';
import AddCategoryForm from '../components/AddCategoryForm';


const Category = () => {

	useUser();
	const [loading, setLoading] = useState(false);
	const [categoryData, setCategoryData] = useState([]);
	const [openAddCategoryModel, setOpenAddCategoryModel] = useState(false);
	const [openEditCategoryModel, setOpenEditCategoryModel] = useState(false);
	const [selectedCategory, setSelectedCategory] = useState(null);

	const fetchCategoriesDetails = async () => {
		if(loading) return;
		setLoading(true);

		try {
			const response = await axiosConfig.get(API_ENDPOINTS.GET_ALL_CATEGORIES);
			if(response.status === 200) {
				setCategoryData(response.data);
				console.log(response.data);
			}
		} catch (error) {
			console.error("Error fetching categories. Please try again ! ", error);
			toast.error(error.response?.data?.message || "Error fetching categories. Please try again!");
		} finally{
			setLoading(false);
		}
	}

	useEffect(() => {
		fetchCategoriesDetails();
	}, []);

	const handleAddCategory = async (category) => {
		const {name, type, icon} = category;

		if(!name.trim()){
			toast.error("Category Name is required ! ");
			return;
		}

		// Check if the Category already exists
		const isDuplicate = categoryData.some((category) => {
			return category.name.toLowerCase() === name.trim().toLowerCase();
		});

		if(isDuplicate){
			toast.error("Category name alrady exists");
			return ;
		}

		try{
			const response = await axiosConfig.post(API_ENDPOINTS.ADD_CATEGORY,{name,type,icon});
			if(response.status === 201){
				toast.success("Category added successfully ! ");
				setOpenAddCategoryModel(false);
				fetchCategoriesDetails();
			} 
		} catch(error){
			console.error("Error adding category : ",error);
			toast.error(error.response?.data?.message || "Failed to add category")
		}
	}

	const handleEditCategory = (categoryToEdit) => {
		setSelectedCategory(categoryToEdit);
		setOpenEditCategoryModel(true);
	}

	const handleUpdateCategory = async (updateCategory) => {
		const {id, name, type, icon} = updateCategory;
		if(!name.trim()){
			toast.error("Category Name is required ! ");
		}

		if(!id){
			toast.error("Category Id is missing for update !");
			return;
		}

		try{
			const response = await axiosConfig.put(API_ENDPOINTS.UPDATE_CATEGORY(id), {name, type, icon});
			setOpenEditCategoryModel(false);
			setSelectedCategory(null);
			toast.success("Category updated successfully ! ");
			fetchCategoriesDetails();
		} catch(error){
			console.error("Error updating Category : ",error.response?.data?.message || error.message);
			toast.error(error.response?.data?.message || "Failed to update category.");
		}
	}

	return (
	      <Dashboard activeMenu="Category">
			<div className="my-5 mx-auto">
			      {/** Add button to add category */}
				<div className="flex justify-between items-center mb-5">
					<h2 className="text-2xl font-semibold">
						All Categories
					</h2>
					<button
					      onClick={() => setOpenAddCategoryModel(true)} 
					      className="flex items-center gap-1 text-green-700 hover:text-green-500 border border-green-700 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2 dark:border-green-500 dark:text-green-500 dark:hover:text-white dark:hover:bg-green-600 dark:focus:ring-green-800"
					>
						<Plus size={15}/>
						Add Category
					</button>
				</div>

				{/** Category list */}
				<CategoryList categories={categoryData} onEditCategory={handleEditCategory}/>

				{/** Adding category model */}
				<Model
				      isOpen={openAddCategoryModel}
					onClose={() => setOpenAddCategoryModel(false)}
				      title="Add category"
				>
					<AddCategoryForm onAddCategory={handleAddCategory} onEditCategory={handleEditCategory}/>
				</Model>

				{/** Updating category */}
				<Model
				      isOpen={openEditCategoryModel}
					onClose={() => {
						setOpenEditCategoryModel(false)
						setSelectedCategory(null)
					}}
					title="Update Category"
				>
				      <AddCategoryForm
					      initialCategoryData={selectedCategory} 
					      onAddCategory={handleUpdateCategory}
						isEditing={true}   
					/>	
				</Model>	
			</div>	
		</ Dashboard>
	);
}

export default Category;