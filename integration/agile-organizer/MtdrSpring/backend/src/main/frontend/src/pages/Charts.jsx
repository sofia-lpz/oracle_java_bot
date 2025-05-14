import BarChart from '../components/BarChart';
import { Col } from 'antd'

const Charts = () => (
  <div style={{ padding: '40px', background: '#1d1d1d', minHeight: '100vh', color: 'white' }}>
    <h1 style={{ color: 'white', marginBottom: '30px' }}>Charts</h1>
    <Col span={12}>
      <BarChart
        title="Tareas completadas por usuario y sprint"
        xField="sprint"
        yField="value"
        seriesField="user"
        dataKey="completedTasks"
      />

      <BarChart
        title="Horas trabajadas por developer y sprint"
        xField="sprint"
        yField="value"
        seriesField="user"
        dataKey="hoursPerDev"
      />

      <BarChart
        title="Horas trabajadas por sprint"
        xField="sprint"
        yField="value"
        dataKey="totalHoursPerSprint"
      />
    </Col>
  </div>
);

export default Charts;
