import { LoaderCircle } from "lucide-react";

const DeleteAlert = ({loading, content, onDelete}) => {
	return (
		<div>
			<p className="text-sm">{content}</p>
			<div className="flex justify-end mt-6">
				<button
				      disabled={loading}
				      onClick={onDelete}
					type="button" 
				      className="add-btn add-btn-fill"
				>
					{loading ? (
						<>
						      <LoaderCircle className="w-4 h-4 animate-spin" />
                                          Deleting...
                                    </>
                                    ) :  (
                                          <>Delete</>
                                    )
					}
				</button>
			</div>
		</div>
	)
}

export default DeleteAlert;