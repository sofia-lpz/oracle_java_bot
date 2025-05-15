import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Spin } from 'antd';

// Registrar los componentes necesarios de Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const BarChart = ({ title, xField, yField, seriesField, data }) => {
  if (!data || data.length === 0) {
    return (
      <div style={{ height: '400px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <Spin />
      </div>
    );
  }

  // Definir colores según el título de la gráfica
  const getColors = () => {
    // Paleta para todas las gráficas con transparencia 0.8
    return [
      'rgba(148, 215, 105, 0.7)', // #94d769
      'rgba(108, 191, 237, 0.7)', // #6cbfed
      'rgba(233, 169, 68, 0.7)',  // #e9a944
      'rgba(247, 86, 124, 0.7)'   // #f7567c
    ];
  };

  // Preparar los datos para Chart.js
  const prepareChartData = () => {
    const colors = getColors();
    if (seriesField) {
      // Agrupar datos por sprint y usuario
      const groupedData = data.reduce((acc, item) => {
        if (!acc[item[xField]]) {
          acc[item[xField]] = {};
        }
        acc[item[xField]][item[seriesField]] = item[yField];
        return acc;
      }, {});

      // Obtener todos los sprints y usuarios únicos
      const sprints = [...new Set(data.map(item => item[xField]))];
      const users = [...new Set(data.map(item => item[seriesField]))];

      // Crear datasets para cada usuario
      const datasets = users.map((user, index) => ({
        label: user,
        data: sprints.map(sprint => groupedData[sprint]?.[user] || 0),
        backgroundColor: colors[index % colors.length],
        borderColor: colors[index % colors.length],
        borderWidth: 1,
      }));

      return {
        labels: sprints,
        datasets,
      };
    } else {
      // Datos simples sin agrupación
      return {
        labels: data.map(item => item[xField]),
        datasets: [{
          label: title,
          data: data.map(item => item[yField]),
          backgroundColor: data.map((_, i) => colors[i % colors.length]),
          borderColor: data.map((_, i) => colors[i % colors.length]),
          borderWidth: 1,
        }],
      };
    }
  };

  const chartData = prepareChartData();

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top',
        labels: {
          color: '#FFFFFF',
          font: {
            size: 12,
          },
        },
      },
      title: {
        display: true,
        text: title,
        color: '#FFFFFF',
        font: {
          size: 16,
          weight: 'bold',
        },
      },
      tooltip: {
        backgroundColor: '#2d2d2d',
        titleColor: '#FFFFFF',
        bodyColor: '#FFFFFF',
        borderColor: '#444',
        borderWidth: 1,
      },
    },
    scales: {
      x: {
        grid: {
          color: 'rgba(255, 255, 255, 0.1)',
        },
        ticks: {
          color: '#FFFFFF',
        },
      },
      y: {
        grid: {
          color: 'rgba(255, 255, 255, 0.1)',
        },
        ticks: {
          color: '#FFFFFF',
        },
      },
    },
  };

  return (
    <div style={{ marginBottom: 50 }}>
      <h2 style={{ color: 'white', marginBottom: '20px' }}>{title}</h2>
      <div style={{ height: '400px', background: '#2d2d2d', padding: '20px', borderRadius: '8px' }}>
        <Bar data={chartData} options={options} />
      </div>
    </div>
  );
};

export default BarChart; 