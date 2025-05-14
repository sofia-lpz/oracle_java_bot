import React, { useEffect, useState } from 'react';
import { Bar } from '@ant-design/plots';
import { Spin } from 'antd';
import { API_ITEM_LIST } from '../API';
import { authenticatedFetch } from '../utils/authUtils';


const BarChart = ({ title, xField, yField, seriesField, dataKey }) => {
  const [summaryData, setSummaryData] = useState([]);
  const [processedData, setProcessedData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const res = await authenticatedFetch(API_ITEM_LIST);
        if (!res.ok) throw new Error('Error fetching summary');
        const json = await res.json();
        setSummaryData(json);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchSummary();
  }, []);

  useEffect(() => {
    if (!summaryData.length) return;

    const computeData = () => {
      switch (dataKey) {
        case 'completedTasks':
          const completed = summaryData
            .filter(item => item.done)
            .map(item => ({
              sprint: `Sprint ${item.sprintId}`,
              user: item.assignedUser?.name || 'Sin usuario',
              value: 1,
            }))
            .reduce((acc, curr) => {
              const key = `${curr.user}-${curr.sprint}`;
              acc[key] = acc[key] ? acc[key] + 1 : 1;
              return acc;
            }, {});
          return Object.entries(completed).map(([key, value]) => {
            const [user, sprint] = key.split('-');
            return { sprint, user, value };
          });

        case 'hoursPerDev':
          const hours = summaryData
            .map(item => ({
              sprint: `Sprint ${item.sprintId}`,
              user: item.assignedUser?.name || 'Sin usuario',
              value: item.hoursWorked || 0,
            }))
            .reduce((acc, curr) => {
              const key = `${curr.user}-${curr.sprint}`;
              acc[key] = (acc[key] || 0) + curr.value;
              return acc;
            }, {});
          return Object.entries(hours).map(([key, value]) => {
            const [user, sprint] = key.split('-');
            return { sprint, user, value };
          });

        case 'totalHoursPerSprint':
          const total = summaryData
            .map(item => ({
              sprint: `Sprint ${item.sprintId}`,
              value: item.hoursWorked || 0,
            }))
            .reduce((acc, curr) => {
              acc[curr.sprint] = (acc[curr.sprint] || 0) + curr.value;
              return acc;
            }, {});
          return Object.entries(total).map(([sprint, value]) => ({
            sprint,
            value,
          }));

        default:
          return [];
      }
    };

    setProcessedData(computeData());
  }, [summaryData, dataKey]);

  if (loading) return <Spin />;

  return (
    <div style={{ marginBottom: 50 }}>
      <h2 style={{ color: 'white' }}>{title}</h2>
      <Bar
        data={processedData}
        isGroup={!!seriesField}
        xField={yField}
        yField={xField}
        seriesField={seriesField}
        barStyle={{ radius: [2, 2, 0, 0] }}
        tooltip={{ fields: [xField, seriesField, yField].filter(Boolean) }}
      />
    </div>
  );
};

export default BarChart;
