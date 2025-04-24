import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Button, Form, message, Progress, Select, Table } from 'antd';
import { FilterOutlined } from '@ant-design/icons';
import { API_KPI, API_USERS, API_PROJECTS, API_SPRINTS, API_TEAMS, API_LIST } from '../API';

import {  authenticatedFetch } from '../utils/authUtils';

import '../App.css';

const Dashboard = () => {
  const [form] = Form.useForm();
  const [kpis, setKpis] = useState([]);
  const [users, setUsers] = useState([]);
  const [projects, setProjects] = useState([]);
  const [sprints, setSprints] = useState([]);
  const [teams, setTeams] = useState([]);
  const [items, setItems] = useState([]);
  const [metrics, setMetrics] = useState({ totalCompletedTasks: 0, totalCompletedStoryPoints: 0 });
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [filters, setFilters] = useState({
    userId: undefined,
    projectId: undefined,
    sprintId: undefined,
    teamId: undefined
  });

  useEffect(() => {
    const fetchAllData = async () => {
      setLoading(true);
      try {
        const [usersRes, projectsRes, sprintsRes, teamsRes, listRes] = await Promise.all([
          authenticatedFetch(API_USERS),
          authenticatedFetch(API_PROJECTS),
          authenticatedFetch(API_SPRINTS),
          authenticatedFetch(API_TEAMS),
          authenticatedFetch(API_LIST),
        ]);
  
        if (!usersRes.ok || !projectsRes.ok || !sprintsRes.ok || !teamsRes.ok || !listRes.ok) {
          throw new Error('Error al cargar uno o más recursos');
        }
  
        const [usersData, projectsData, sprintsData, teamsData, itemsData] = await Promise.all([
          usersRes.json(),
          projectsRes.json(),
          sprintsRes.json(),
          teamsRes.json(),
          listRes.json(),
        ]);
  
        setUsers(usersData);
        setProjects(projectsData);
        setSprints(sprintsData);
        setTeams(teamsData);
        setItems(itemsData);

        const calculatedMetrics = calculateMetrics(itemsData);
        setMetrics(calculatedMetrics);
        fetchKPIs({});
      } catch (error) {
        console.error(error);
        messageApi.error('Error al cargar los datos');
      } finally {
        setLoading(false);
      }
    };
  
    fetchAllData();
  }, []);  

  // Effect to automatically fetch KPIs when filters change
useEffect(() => {
  fetchKPIs(filters);
  // Don't fetch items here, just recalculate metrics
  const calculatedMetrics = calculateMetrics(items, filters);
  setMetrics(calculatedMetrics);
}, [filters, items]);

  const fetchKPIs = async (values) => {
    setLoading(true);
    const queryParams = new URLSearchParams();
    if (values.userId) queryParams.append('userId', values.userId);
    if (values.teamId) queryParams.append('teamId', values.teamId);
    if (values.projectId) queryParams.append('projectId', values.projectId);
    if (values.sprintId) queryParams.append('sprintId', values.sprintId);

    try {
      const response = await authenticatedFetch(`${API_KPI}?${queryParams.toString()}`);
      if (!response.ok) throw new Error('Error al obtener los KPIs');
      const data = await response.json();
      setKpis(data);
    } catch (error) {
      console.error(error);
      messageApi.error('Error al cargar los KPIs');
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
    form.setFieldValue(key, value);
  };

  const getKpiAggregates = () => {
    const aggregates = {
      VISIBILITY: { sum: 0, total: 0 },
      ACCOUNTABILITY: { sum: 0, total: 0 },
      PRODUCTIVITY: { sum: 0, total: 0 },
    };
    kpis.forEach((kpi) => {
      if (aggregates[kpi.type]) {
        aggregates[kpi.type].sum += kpi.sum || 0;
        aggregates[kpi.type].total += kpi.total || 0;
      }
    });
    return aggregates;
  };

  const aggregates = getKpiAggregates();

  const kpiTypes = [
    { key: 'VISIBILITY', title: 'Visibility', color: '#49c2f2' },
    { key: 'ACCOUNTABILITY', title: 'Accountability', color: '#f5a623' },
    { key: 'PRODUCTIVITY', title: 'Productivity', color: '#7ed957' },
  ];

  const calculateMetrics = (todoItems, currentFilters = {}) => {
    // First apply filters
    let filteredItems = [...todoItems];
  
    console.log('Filter values:', currentFilters);
    console.log('Sample item before filtering:', todoItems[0]);
    console.log('Filtered items count:', filteredItems.length);
    
    if (currentFilters.userId) {
      const userId = Number(currentFilters.userId);
      filteredItems = filteredItems.filter(item => item.user && Number(item.user.id) === userId);
    }
    if (currentFilters.projectId) {
      const projectId = Number(currentFilters.projectId);
      filteredItems = filteredItems.filter(item => item.project && Number(item.project.id) === projectId);
    }
    if (currentFilters.sprintId) {
      const sprintId = Number(currentFilters.sprintId);
      filteredItems = filteredItems.filter(item => item.sprint && Number(item.sprint.id) === sprintId);
    }
    if (currentFilters.teamId) {
      const teamId = Number(currentFilters.teamId);
      filteredItems = filteredItems.filter(item => item.team && Number(item.team.id) === teamId);
    }
    
    console.log('Filtered items after applying filters:', filteredItems.length);
   
    
    // Then calculate metrics on filtered items
    const completedTasks = filteredItems.filter(item => item.done && !item.deleted);
    const completedStoryPoints = completedTasks.reduce(
      (sum, item) => sum + (item.storyPoints || 0),
      0
    );
    const completedHours = completedTasks.reduce(
      (sum, item) => sum + (item.real_hours || 0),
      0
    );
  
    return {
      totalCompletedTasks: completedTasks.length,
      totalCompletedStoryPoints: completedStoryPoints,
      totalCompletedHours: completedHours
    };
  };
  
  const calculateUserMetrics = () => {
    // Group completed tasks by user
    const userMetrics = {};
    
    // Initialize metrics for each user
    users.forEach(user => {
      userMetrics[user.id] = {
        userId: user.id,
        userName: user.name,
        tasksCompleted: 0,
        storyPointsCompleted: 0,
        hoursCompleted: 0
      };
    });
    
    // Apply current filters
    let filteredItems = [...items];
    
    if (filters.projectId) {
      const projectId = Number(filters.projectId);
      filteredItems = filteredItems.filter(item => item.project && Number(item.project.id) === projectId);
    }
    if (filters.sprintId) {
      const sprintId = Number(filters.sprintId);
      filteredItems = filteredItems.filter(item => item.sprint && Number(item.sprint.id) === sprintId);
    }
    if (filters.teamId) {
      const teamId = Number(filters.teamId);
      filteredItems = filteredItems.filter(item => item.team && Number(item.team.id) === teamId);
    }
    
    // Calculate metrics from completed tasks
    filteredItems
      .filter(item => item.done && !item.deleted && item.user)
      .forEach(task => {
        const userId = task.user.id;
        if (userMetrics[userId]) {
          userMetrics[userId].tasksCompleted++;
          userMetrics[userId].storyPointsCompleted += (task.storyPoints || 0);
          userMetrics[userId].hoursCompleted += (task.real_hours || 0);
        }
      });
    
    // Convert to array for table display and filter out users with no completed tasks
    return Object.values(userMetrics).filter(metrics => 
      metrics.tasksCompleted > 0 || metrics.storyPointsCompleted > 0 || metrics.hoursCompleted > 0
    );
  };
  
  return (
    <div style={{ padding: '40px', background: '#1d1d1d', minHeight: '100vh', color: 'white' }}>
      {contextHolder}
      <h1 style={{ color: 'white', marginBottom: '30px' }}>Dashboard</h1>

      <Form layout="inline" form={form} style={{ marginBottom: '40px' }}>
        <Form.Item name="userId">
          <Select
            showSearch
            className='white-select'
            placeholder="All user"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
            onChange={(value) => handleFilterChange('userId', value)}
            allowClear
            filterOption={(input, option) =>
              option.label.toLowerCase().includes(input.toLowerCase())
            }
            options={users.map(user => ({
              label: user.name,
              value: user.id
            }))}
          />
        </Form.Item>
        <Form.Item name="projectId">
          <Select
            showSearch
            className='white-select'
            placeholder="All project"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
            onChange={(value) => handleFilterChange('projectId', value)}
            allowClear
            filterOption={(input, option) =>
              option.label.toLowerCase().includes(input.toLowerCase())
            }
            options={projects.map(project => ({
              label: project.name,
              value: project.id
            }))}
          />
        </Form.Item>
        <Form.Item name="sprintId">
          <Select
            showSearch
            className='white-select'
            placeholder="All sprint"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
            onChange={(value) => handleFilterChange('sprintId', value)}
            allowClear
            filterOption={(input, option) =>
              option.label.toLowerCase().includes(input.toLowerCase())
            }
            options={sprints.map(sprint => ({
              label: sprint.name,
              value: sprint.id
            }))}
          />
        </Form.Item>
        <Form.Item name="teamId">
          <Select
            showSearch
            className='white-select'
            placeholder="All team"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
            onChange={(value) => handleFilterChange('teamId', value)}
            allowClear
            filterOption={(input, option) =>
              option.label.toLowerCase().includes(input.toLowerCase())
            }
            options={teams.map(team => ({
              label: team.name,
              value: team.id
            }))}
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" icon={<FilterOutlined />} loading={loading}/>
        </Form.Item>
      </Form>
      
      <Row gutter={24}>
        {kpiTypes.map(({ key, title, color }) => {
          const { sum, total } = aggregates[key];
          const percent = total > 0 ? Math.round((sum / total) * 100) : 0;

          return (
            <Col span={8} key={key}>
              <Card className='card-dashboard'>
                <h3 style={{ color: 'white', textAlign: 'center' }}>{title}</h3>
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '20px' }}>
                  <Progress
                    percent={percent}
                    strokeColor={color}
                    trailColor="#333"
                    status="active"
                    type="circle"
                  />
                </div>
              </Card>
            </Col>
          );
        })}

        <Col span={12}>
          <Card className='card-dashboard'>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Tasks Completed</h3>
            <h1>{metrics.totalCompletedTasks}</h1>
          </Card>
        </Col>
        <Col span={12}>
          <Card className='card-dashboard'>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Story Points Completed</h3>
            <h1>{metrics.totalCompletedStoryPoints}</h1>
          </Card>
        </Col>
        <Col span={12}>
          <Card className='card-dashboard'>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Hours Completed</h3>
            <h1>{metrics.totalCompletedHours || 0}</h1> 
          </Card>
        </Col>
      </Row>
      
      <h2 style={{ color: 'white', margin: '40px 0 20px' }}>User Performance Metrics</h2>
      <Table
        dataSource={calculateUserMetrics()}
        columns={[
          {
            title: 'User',
            dataIndex: 'userName',
            key: 'userName',
          },
          {
            title: 'Tasks Completed',
            dataIndex: 'tasksCompleted',
            key: 'tasksCompleted',
          },
          {
            title: 'Story Points Completed',
            dataIndex: 'storyPointsCompleted',
            key: 'storyPointsCompleted',
          },
          {
            title: 'Hours Completed',
            dataIndex: 'hoursCompleted',
            key: 'hoursCompleted',
          },
        ]}
        pagination={false}
        rowKey="userId"
        loading={loading}
        style={{ background: '#272727', borderRadius: '8px' }}
        className="custom-dark-table"
      />
    </div>
  );
};

export default Dashboard;