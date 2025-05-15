import BarChart from '../components/BarChart';
import { Col, Row, Spin } from 'antd';
import { useEffect, useState } from 'react';
import { API_ITEM_LIST } from '../API';
import axios from 'axios';

const Charts = () => {
  const [chartData, setChartData] = useState({
    completedTasks: [],
    hoursPerDev: [],
    totalHoursPerSprint: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await axios.get(API_ITEM_LIST);
        const data = response.data;

        // Procesar datos para tareas completadas por usuario y sprint
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

        // Procesar datos para horas trabajadas por desarrollador y sprint
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

        // Procesar datos para horas totales por sprint
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

        // Ordenar los datos por sprint
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
    <div style={{ padding: '40px', background: '#1d1d1d', minHeight: '100vh', color: 'white' }}>
      <h1 style={{ color: 'white', marginBottom: '30px' }}>Charts</h1>
      <Row gutter={[32, 32]}>
        <Col xs={24} lg={12}>
          <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
            <BarChart
              title="Tareas completadas por usuario y sprint"
              xField="sprint"
              yField="value"
              seriesField="user"
              data={chartData.completedTasks}
            />
          </div>
        </Col>

        <Col xs={24} lg={12}>
          <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
            <BarChart
              title="Horas trabajadas por developer y sprint"
              xField="sprint"
              yField="value"
              seriesField="user"
              data={chartData.hoursPerDev}
            />
          </div>
        </Col>

        <Col xs={24} lg={12}>
          <div style={{ background: '#2d2d2d', padding: '20px', borderRadius: '8px', height: '100%' }}>
            <BarChart
              title="Horas trabajadas por sprint"
              xField="sprint"
              yField="value"
              data={chartData.totalHoursPerSprint}
            />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Charts;
