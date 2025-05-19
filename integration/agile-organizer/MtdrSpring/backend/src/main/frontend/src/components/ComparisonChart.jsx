import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js';
import { Card, Select, Row, Col, Radio } from 'antd';

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const ComparisonChart = ({ users = [], sprints = [], items = [] }) => {
  const [comparisonType, setComparisonType] = useState('users');
  const [selectedEntities, setSelectedEntities] = useState([]);
  const [metricType, setMetricType] = useState('tasksCompleted');
  const [chartData, setChartData] = useState({});
  const [loading, setLoading] = useState(false);

  const handleEntityChange = (values) => setSelectedEntities(values);

  const handleComparisonTypeChange = (e) => {
    setComparisonType(e.target.value);
    setSelectedEntities([]);
  };

  const handleMetricTypeChange = (e) => setMetricType(e.target.value);

  const generateData = () => {
    const data = [];
    const labels = [];

    const entities = comparisonType === 'users' ? users : sprints;

    selectedEntities.forEach(id => {
      const entity = entities.find(e => e?.id === id);
      if (!entity) return;

      const filteredItems = items.filter(item =>
        item?.done &&
        !item.deleted &&
        ((comparisonType === 'users' && item?.user?.id === id) ||
         (comparisonType === 'sprints' && item?.sprint?.id === id))
      );

      const tasksCompleted = filteredItems.length;
      const storyPointsCompleted = filteredItems.reduce((sum, t) => sum + (t.storyPoints || 0), 0);
      const hoursCompleted = filteredItems.reduce((sum, t) => sum + (t.real_hours || 0), 0);

      labels.push(entity.name);
      data.push(
        metricType === 'tasksCompleted' ? tasksCompleted :
        metricType === 'storyPointsCompleted' ? storyPointsCompleted :
        hoursCompleted
      );
    });

    const backgroundColors = {
      tasksCompleted: '#49c2f2',
      storyPointsCompleted: '#f5a623',
      hoursCompleted: '#7ed957',
    };

    setChartData({
      labels,
      datasets: [
        {
          label: metricLabels[metricType],
          data,
          backgroundColor: backgroundColors[metricType],
        },
      ],
    });
  };

  useEffect(() => {
    if (selectedEntities.length) generateData();
    else setChartData({});
  }, [selectedEntities, metricType, comparisonType, items]); // eslint-disable-line react-hooks/exhaustive-deps

  const metricLabels = {
    tasksCompleted: 'Tasks Completed',
    storyPointsCompleted: 'Story Points',
    hoursCompleted: 'Hours Completed',
  };

  const hasData = users.length > 0 && sprints.length > 0;
  const hasChart = chartData?.labels?.length > 0;

  return (
    <Card className="card-dashboard" style={{ marginTop: '20px' }}>
      <h2 style={{ color: 'white', textAlign: 'center', marginBottom: '20px' }}>
        Performance Comparison
      </h2>

      <Row gutter={16} style={{ marginBottom: '20px' }}>
        <Col span={8}>
          <div style={{ marginBottom: '10px', color: 'white' }}>Compare by:</div>
          <Radio.Group
            value={comparisonType}
            onChange={handleComparisonTypeChange}
            buttonStyle="solid"
            disabled={!hasData || loading}
          >
            <Radio.Button value="users">Users</Radio.Button>
            <Radio.Button value="sprints">Sprints</Radio.Button>
          </Radio.Group>
        </Col>

        <Col span={8}>
          <div style={{ marginBottom: '10px', color: 'white' }}>
            Select {comparisonType === 'users' ? 'Users' : 'Sprints'} to Compare:
          </div>
          <Select
            mode="multiple"
            className="white-select"
            placeholder={`Select ${comparisonType === 'users' ? 'users' : 'sprints'} to compare`}
            style={{ width: '100%' }}
            onChange={handleEntityChange}
            value={selectedEntities}
            disabled={!hasData || loading}
            options={
              comparisonType === 'users'
                ? users.map(user => user && ({ label: user.name, value: user.id })).filter(Boolean)
                : sprints.map(sprint => sprint && ({ label: sprint.name, value: sprint.id })).filter(Boolean)
            }
          />
        </Col>

        <Col span={8}>
          <div style={{ marginBottom: '10px', color: 'white' }}>Metric:</div>
          <Radio.Group
            value={metricType}
            onChange={handleMetricTypeChange}
            buttonStyle="solid"
            disabled={!hasData || loading}
          >
            <Radio.Button value="tasksCompleted">Tasks</Radio.Button>
            <Radio.Button value="storyPointsCompleted">Story Points</Radio.Button>
            <Radio.Button value="hoursCompleted">Hours</Radio.Button>
          </Radio.Group>
        </Col>
      </Row>

      {loading ? (
        <div style={{ height: '400px', display: 'flex', justifyContent: 'center', alignItems: 'center', color: '#888' }}>
          Loading chart data...
        </div>
      ) : hasChart ? (
        <div style={{ height: '400px' }}>
          <Bar
            data={chartData}
            options={{
              responsive: true,
              plugins: {
                legend: { labels: { color: 'white' } },
                tooltip: {
                  callbacks: {
                    label: (context) => `${context.dataset.label}: ${context.parsed.y}`,
                  },
                },
              },
              scales: {
                x: {
                  ticks: { color: 'white' },
                  title: { color: 'white' },
                },
                y: {
                  ticks: { color: 'white' },
                  title: { color: 'white' },
                },
              },
            }}
          />
        </div>
      ) : (
        <div style={{ height: '400px', display: 'flex', justifyContent: 'center', alignItems: 'center', color: '#888' }}>
          {!hasData
            ? "No data available"
            : `Select ${comparisonType === 'users' ? 'users' : 'sprints'} to view comparison`}
        </div>
      )}
    </Card>
  );
};

export default ComparisonChart;
