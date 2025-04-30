import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Button, Form, message, Progress, Select } from 'antd';
import { FilterOutlined } from '@ant-design/icons';
import { API_KPI, API_USERS, API_PROJECTS, API_SPRINTS, API_TEAMS } from '../API';

import {  authenticatedFetch } from '../utils/authUtils';

import '../App.css';

const Dashboard = () => {
  const [form] = Form.useForm();
  const [kpis, setKpis] = useState([]);
  const [users, setUsers] = useState([]);
  const [projects, setProjects] = useState([]);
  const [sprints, setSprints] = useState([]);
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    const fetchAllData = async () => {
      setLoading(true);
      try {
        const [usersRes, projectsRes, sprintsRes, teamsRes] = await Promise.all([
          authenticatedFetch(API_USERS),
          authenticatedFetch(API_PROJECTS),
          authenticatedFetch(API_SPRINTS),
          authenticatedFetch(API_TEAMS),
        ]);
  
        if (!usersRes.ok || !projectsRes.ok || !sprintsRes.ok || !teamsRes.ok) {
          throw new Error('Error al cargar uno o más recursos');
        }
  
        const [usersData, projectsData, sprintsData, teamsData] = await Promise.all([
          usersRes.json(),
          projectsRes.json(),
          sprintsRes.json(),
          teamsRes.json(),
        ]);
  
        setUsers(usersData);
        setProjects(projectsData);
        setSprints(sprintsData);
        setTeams(teamsData);
  
        // 👉 Estimación global sin filtros
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

  return (
    <div style={{ padding: '40px', background: '#1d1d1d', minHeight: '100vh', color: 'white' }}>
      {contextHolder}
      <h1 style={{ color: 'white', marginBottom: '30px' }}>Dashboard</h1>

      <Form layout="inline" form={form} onFinish={fetchKPIs} style={{ marginBottom: '40px' }}>
        <Form.Item name="userId">
          <Select
            showSearch
            className='white-select'
            placeholder="Select a user"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
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
            placeholder="Select a project"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
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
            placeholder="Select a sprint"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
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
            placeholder="Select a team"
            style={{ color: 'white', width: 200 }}
            optionFilterProp="children"
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
            {/* Number task completed header */}
          </Card>
        </Col>
        <Col span={12}>
          <Card className='card-dashboard'>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Story Points Completed</h3>
            {/* Number story point completed header */}
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
