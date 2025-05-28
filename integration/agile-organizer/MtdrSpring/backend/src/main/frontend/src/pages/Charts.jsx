import BarChart from '../components/BarChart';
import TaskTable from '../components/DataTable'
import { Col, Row, Spin } from 'antd';
import { useEffect, useState } from 'react';
import { API_ITEM_LIST, API_SPRINTS } from '../API';
import axios from 'axios';

const Charts = () => {
  const [chartData, setChartData] = useState({
    completedTasks: [],
    hoursPerDev: [],
    totalHoursPerSprint: []
  });
  const [loading, setLoading] = useState(true);
  const [tasks, setTasks] = useState([]); // All tasks
  const [latestSprintTasks, setLatestSprintTasks] = useState([]); // Tasks from latest sprint
  const [latestSprintName, setLatestSprintName] = useState(''); // Name of latest sprint

useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await axios.get(API_ITEM_LIST);
        const sprintResponse = await axios.get(API_SPRINTS);
        const data = response.data;
        const allSprints = sprintResponse.data;

        // Store all task data
        setTasks(data.map(item => ({
          id: item.id,
          title: item.title,
          user: item.user,
          estimatedHours: item.estimated_hours || 0,
          realHours: item.real_hours || 0
        })));

        // Find the latest sprint by highest ID
        if (allSprints && allSprints.length > 0) {
          // Sort sprints by ID in descending order and take the first one
          const latestSprint = [...allSprints].sort((a, b) => b.id - a.id)[0];
          setLatestSprintName(latestSprint.name);

          // Filter tasks for the latest sprint
          const latestSprintTasksData = data
            .filter(item => item.sprint && item.sprint.id === latestSprint.id)
            .map(item => ({
              id: item.id,
              title: item.title,
              user: item.user,
              estimatedHours: item.estimated_hours || 0,
              realHours: item.real_hours || 0,
              storyPoints: item.storyPoints || 0,
              done: item.done
            }));
          
          setLatestSprintTasks(latestSprintTasksData);
        }

        // Process data for charts (unchanged)
        const completedTasksByUserAndSprint = data.reduce((acc, item) => {
          if (!item.done || !item.sprint || !item.user) return acc;
          const key = `${item.sprint.name}-${item.user.name}`;
          if (!acc[key]) {
            acc[key] = {
              sprint: item.sprint.name,
              user: item.user.name,
              value: 0
            };
          }
          acc[key].value += 1;
          return acc;
        }, {});

        // Process data for hours worked by developer and sprint
        const hoursByDevAndSprint = data.reduce((acc, item) => {
          if (!item.sprint || !item.user) return acc;
          const key = `${item.sprint.name}-${item.user.name}`;
          if (!acc[key]) {
            acc[key] = {
              sprint: item.sprint.name,
              user: item.user.name,
              value: 0
            };
          }
          acc[key].value += parseFloat(item.real_hours) || 0;
          return acc;
        }, {});

        // Process data for total hours by sprint
        const totalHoursBySprint = data.reduce((acc, item) => {
          if (!item.sprint) return acc;
          const sprintName = item.sprint.name;
          if (!acc[sprintName]) {
            acc[sprintName] = {
              sprint: sprintName,
              value: 0
            };
          }
          acc[sprintName].value += parseFloat(item.real_hours) || 0;
          return acc;
        }, {});

        // Sort data by sprint
        const sortBySprint = (a, b) => a.sprint.localeCompare(b.sprint);

        setChartData({
          completedTasks: Object.values(completedTasksByUserAndSprint).sort(sortBySprint),
          hoursPerDev: Object.values(hoursByDevAndSprint).sort(sortBySprint),
          totalHoursPerSprint: Object.values(totalHoursBySprint).sort(sortBySprint)
        });
      } catch (error) {
        console.error('Error fetching chart data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div style={{ padding: '40px', background: '#1d1d1d', minHeight: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <Spin size="large" />
      </div>
    );
  }

  return (
  <div style={{ 
    padding: '40px', 
    background: '#1d1d1d', 
    minHeight: '100vh', 
    color: 'white',
    overflowY: 'auto',
    maxHeight: '100vh'
  }}>
    <h1 style={{ color: 'white', marginBottom: '30px' }}>Charts</h1>
    <Row gutter={[32, 32]}>
      <Col xs={24} lg={24}>
        <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
          <BarChart
            title="Tareas completadas por usuario y sprint"
            xField="sprint"
            yField="value"
            seriesField="user"
            data={chartData.completedTasks}
            unit="tareas"
          />
        </div>
      </Col>

      <Col xs={24} lg={24}>
        <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
          <BarChart
            title="Horas trabajadas por developer y sprint"
            xField="sprint"
            yField="value"
            seriesField="user"
            data={chartData.hoursPerDev}
            unit="horas"
          />
        </div>
      </Col>

      <Col xs={24} lg={24}>
        <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
          <BarChart
            title="Horas trabajadas por sprint"
            xField="sprint"
            yField="value"
            data={chartData.totalHoursPerSprint}
            unit="horas"
            singleColor={true}
          />
        </div>
      </Col>
      
      <Col xs={24} lg={24}>
          <TaskTable 
            tasks={latestSprintTasks}
            title={`Reporte de tareas terminadas ${latestSprintName}`}
          />
      </Col>
    </Row>
  </div>
  );
};

export default Charts;