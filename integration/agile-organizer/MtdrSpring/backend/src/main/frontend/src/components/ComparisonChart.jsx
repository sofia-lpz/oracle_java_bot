import React, { useState, useEffect } from 'react';
import { 
  BarChart, 
  Bar, 
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  Legend, 
  ResponsiveContainer
} from 'recharts';
import { Card, Select, Row, Col, Radio } from 'antd';

const ComparisonChart = ({ users, sprints, items }) => {
  const [comparisonType, setComparisonType] = useState('users'); // 'users' or 'sprints'
  const [selectedEntities, setSelectedEntities] = useState([]);
  const [metricType, setMetricType] = useState('tasksCompleted');
  const [chartData, setChartData] = useState([]);
  
  // Handle entity selection (users or sprints)
  const handleEntityChange = (selectedValues) => {
    setSelectedEntities(selectedValues);
  };
  
  // Handle comparison type change (users vs sprints)
  const handleComparisonTypeChange = (e) => {
    setComparisonType(e.target.value);
    setSelectedEntities([]); // Reset selection when changing comparison type
  };
  
  // Handle metric type change
  const handleMetricTypeChange = (e) => {
    setMetricType(e.target.value);
  };
  
  // Generate chart data when selections change
  useEffect(() => {
    if (selectedEntities.length === 0) {
      setChartData([]);
      return;
    }
    
    if (comparisonType === 'users') {
      generateUserComparisonData();
    } else {
      generateSprintComparisonData();
    }
  }, [selectedEntities, metricType, comparisonType]);
  
  // Generate data for user comparison
  const generateUserComparisonData = () => {
    // Initialize data structure
    const data = [];
    
    // For each selected user, calculate metrics
    selectedEntities.forEach(userId => {
      const user = users.find(u => u.id === userId);
      if (!user) return;
      
      // Filter tasks by user
      const userTasks = items.filter(item => 
        item.done && 
        !item.deleted && 
        item.user && 
        item.user.id === userId
      );
      
      // Calculate metrics
      const tasksCompleted = userTasks.length;
      const storyPointsCompleted = userTasks.reduce(
        (sum, task) => sum + (task.storyPoints || 0), 0
      );
      const hoursCompleted = userTasks.reduce(
        (sum, task) => sum + (task.real_hours || 0), 0
      );
      
      // Add to chart data
      data.push({
        name: user.name,
        tasksCompleted,
        storyPointsCompleted,
        hoursCompleted
      });
    });
    
    setChartData(data);
  };
  
  // Generate data for sprint comparison
  const generateSprintComparisonData = () => {
    // Initialize data structure
    const data = [];
    
    // For each selected sprint, calculate metrics
    selectedEntities.forEach(sprintId => {
      const sprint = sprints.find(s => s.id === sprintId);
      if (!sprint) return;
      
      // Filter tasks by sprint
      const sprintTasks = items.filter(item => 
        item.done && 
        !item.deleted && 
        item.sprint && 
        item.sprint.id === sprintId
      );
      
      // Calculate metrics
      const tasksCompleted = sprintTasks.length;
      const storyPointsCompleted = sprintTasks.reduce(
        (sum, task) => sum + (task.storyPoints || 0), 0
      );
      const hoursCompleted = sprintTasks.reduce(
        (sum, task) => sum + (task.real_hours || 0), 0
      );
      
      // Add to chart data
      data.push({
        name: sprint.name,
        tasksCompleted,
        storyPointsCompleted,
        hoursCompleted
      });
    });
    
    setChartData(data);
  };

  // Define colors for different metrics
  const colors = {
    tasksCompleted: '#49c2f2',  // Visibility blue
    storyPointsCompleted: '#f5a623',  // Accountability orange
    hoursCompleted: '#7ed957'  // Productivity green
  };
  
  // Map metric types to display names
  const metricLabels = {
    tasksCompleted: 'Tasks Completed',
    storyPointsCompleted: 'Story Points',
    hoursCompleted: 'Hours Completed'
  };
  
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
          >
            <Radio.Button value="users">Users</Radio.Button>
            <Radio.Button value="sprints">Sprints</Radio.Button>
          </Radio.Group>
        </Col>
        
        <Col span={8}>
          <div style={{ marginBottom: '10px', color: 'white' }}>Select {comparisonType === 'users' ? 'Users' : 'Sprints'} to Compare:</div>
          <Select
            mode="multiple"
            className="white-select"
            placeholder={`Select ${comparisonType === 'users' ? 'users' : 'sprints'} to compare`}
            style={{ width: '100%' }}
            onChange={handleEntityChange}
            value={selectedEntities}
            options={
              comparisonType === 'users' 
                ? users.map(user => ({ label: user.name, value: user.id }))
                : sprints.map(sprint => ({ label: sprint.name, value: sprint.id }))
            }
          />
        </Col>
        
        <Col span={8}>
          <div style={{ marginBottom: '10px', color: 'white' }}>Metric:</div>
          <Radio.Group 
            value={metricType} 
            onChange={handleMetricTypeChange}
            buttonStyle="solid"
          >
            <Radio.Button value="tasksCompleted">Tasks</Radio.Button>
            <Radio.Button value="storyPointsCompleted">Story Points</Radio.Button>
            <Radio.Button value="hoursCompleted">Hours</Radio.Button>
          </Radio.Group>
        </Col>
      </Row>
      
      {chartData.length > 0 ? (
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            data={chartData}
            margin={{ top: 20, right: 30, left: 20, bottom: 70 }}
          >
            <CartesianGrid strokeDasharray="3 3" stroke="#333" />
            <XAxis 
              dataKey="name" 
              tick={{ fill: 'white' }}
              angle={-45}
              textAnchor="end"
              height={70}
            />
            <YAxis tick={{ fill: 'white' }} />
            <Tooltip 
              contentStyle={{ backgroundColor: '#272727', border: '1px solid #444', color: 'white' }}
              formatter={(value) => [`${value}`, `${metricLabels[metricType]}`]}
            />
            <Legend wrapperStyle={{ color: 'white' }} />
            <Bar 
              dataKey={metricType} 
              name={metricLabels[metricType]} 
              fill={colors[metricType]} 
              animationDuration={1000} 
            />
          </BarChart>
        </ResponsiveContainer>
      ) : (
        <div style={{ height: '400px', display: 'flex', justifyContent: 'center', alignItems: 'center', color: '#888' }}>
          Select {comparisonType === 'users' ? 'users' : 'sprints'} to view comparison
        </div>
      )}
    </Card>
  );
};

export default ComparisonChart;