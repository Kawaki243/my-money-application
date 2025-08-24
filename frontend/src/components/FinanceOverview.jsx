import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Pie } from 'react-chartjs-2';
import React from 'react';

ChartJS.register(ArcElement, Tooltip, Legend);

const FinanceOverview = ({totalBalance, totalIncome, totalExpense}) => {

	const data = {
		labels: ['Total Balance', 'Total Income', 'Total Expense'],
            datasets: [
			{
                        label: 'Total Balance',
                        data: [totalBalance, totalIncome, totalExpense],
                        backgroundColor: [
					'rgb(255, 165, 0)',
                              'rgb(60, 179, 113)',
                              'rgb(255, 0, 0)'
                        ],
                        borderColor: [
					'rgba(255, 159, 64, 1)',
					'rgba(75, 192, 192, 1)',
                              'rgba(255, 99, 132, 1)'
				],
                        borderWidth: 1,
                  },
            ],
      };

	const options = {
		maintainAspectRatio: false,
		aspectRatio: 1
	}

	return (
		<div className="card">
			<div className="flex items-center justify-between">
				<h5 className="text-lg mb-2">Financial Overview</h5>
			</div> 
			<div className="h-[400px] w-full p-2">   
				<Pie data={data} options={options}  />
			</div>
		</div>
	)
}

export default FinanceOverview;