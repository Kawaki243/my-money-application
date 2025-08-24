import React, { useMemo } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Plus } from "lucide-react";
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);





const ExpenseOverview = ({transactions, onAddExpense}) => {

      const chartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                  legend: {
                        position: 'top',
                  },
            title: {
                  display: true,
                  text: 'Total Expense Amount by Date',
                  font: {
                        size: 18,
                  },
            },
            tooltip: {
                  callbacks: {
                        label: function (context) {
                              let label = context.dataset.label || '';
                              if (label) {
                                    label += ': ';
                              }
                              if (context.parsed.y !== null) {
                                    label += new Intl.NumberFormat('en-US').format(context.parsed.y);
                              }
                              return label;
                        },
                  },
            },
            },
            scales: {
                  x: { title: { display: true, text: 'Date' } },
                  y: { title: { display: true, text: 'Amount' }, beginAtZero: true },
            },
      };

      function AmountChart({ rawData }) {
            const chartData = useMemo(() => {
                  const aggregatedData = rawData.reduce((acc, item) => {
                        acc[item.date] = (acc[item.date] || 0) + item.amount;
                        return acc;
                  }, {});
            const sortedDates = Object.keys(aggregatedData).sort((a, b) => new Date(a) - new Date(b));
            return {
                  labels: sortedDates,
                  datasets: [
                        {
                              label: 'Total Expense Amount',
                              data: sortedDates.map((date) => aggregatedData[date]),
                              borderColor: 'rgb(255, 95, 31)',
                              backgroundColor: 'rgba(255, 165, 0, 1)',
                              tension: 0.1,
                        },
                  ],
            };
            }, [rawData]);
            return <Line options={chartOptions} data={chartData} />;
      }

	
	
	return (
	      <div className="card">
			<div className="flex items-center justify-between">
				<div>
					<h5 className="text-lg">
                                    Expense Overview
                              </h5>
                              <p className="text-xs text-gray-400 mt-0 5">
                                    Track your earnings over time and analyze your Expense trends.
                              </p>
                        </div>
                        <button className="add-btn" onClick={onAddExpense}>
                              <Plus size={15} className="text-lg" /> Add Expense
                        </button>
                  </div>
                  <div className="mt-10">
                        <div className="flex flex-col items-center">
					<h2 className="text-xl font-bold mb-4">Expense Transactions Over Date</h2>
					<div className="relative h-[70vh] w-full p-4 bg-white rounded-xl shadow-lg">
                                    <AmountChart rawData={transactions} />
                              </div>
                        </div>
                  </div>
            </div>
      )
}

export default ExpenseOverview;