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
import { Spin, Tag } from 'antd';
import { label } from 'framer-motion/client';

// Registrar los componentes necesarios de Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const BarChart = ({ title, xField, yField, seriesField, data, unit = 'units' , singleColor = false}) => {
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
      'rgba(148, 215, 105, 0.8)', // #94d769
      'rgba(108, 191, 237, 0.8)', // #6cbfed
      'rgba(233, 169, 68, 0.8)',  // #e9a944
      'rgba(247, 86, 124, 0.8)'   // #f7567c
    ];
  };

  // Preparar los datos para Chart.js
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
      backgroundColor: singleColor ? colors[0] : colors[index % colors.length],
      borderColor: singleColor ? colors[0] : colors[index % colors.length],
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
        backgroundColor: singleColor ? colors[0] : data.map((_, i) => colors[i % colors.length]),
        borderColor: singleColor ? colors[0] : data.map((_, i) => colors[i % colors.length]),
        borderWidth: 1,
      }],
    };
  }
};

  const chartData = prepareChartData();

  // Calcular el valor máximo y redondearlo a un número más amigable
  const calculateMaxValue = () => {
    const maxValue = Math.max(...chartData.datasets.map(dataset => 
      Math.max(...dataset.data)
    ));
    
    // Redondear a un número más amigable
    const magnitude = Math.pow(10, Math.floor(Math.log10(maxValue)));
    const roundedMax = Math.ceil(maxValue / magnitude) * magnitude;
    
    return roundedMax;
  };

  const maxValue = calculateMaxValue();
  const yAxisValues = [0, maxValue * 0.25, maxValue * 0.5, maxValue * 0.75, maxValue].map(value => 
    Math.round(value)
  ).reverse();

  // Calcular el stepSize para que la grid coincida con los valores fijos
  const stepSize = (maxValue - 0) / (yAxisValues.length - 1);

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: false,
      },
      tooltip: {
        backgroundColor: '#2d2d2d',
        titleColor: '#FFFFFF',
        bodyColor: '#FFFFFF',
        borderColor: '#444',
        borderWidth: 1,
        callbacks:{
          label: function (context) {
            let label = context.dataset.label || '';

            if (label) {
              label += ': ';
            }

            if (context.parsed.y !== null) {
              label += context.parsed.y + ' ' + unit;
            }

            return label;
          }
        }
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
          color: 'rgba(255, 255, 255, 0.3)',
          drawOnChartArea: true,
        },
        ticks: {
          color: '#FFFFFF',
          display: false,
          stepSize: stepSize,
        },
        min: 0,
        max: maxValue,
        display: true,
        border: {
          display: false,
        },
        beginAtZero: true,
        suggestedMin: 0,
        suggestedMax: maxValue,
        stepSize: stepSize,
        title: {
          display: true,
          text: unit.charAt(0).toUpperCase() + unit.slice(1),
          color: '#FFFFFF',
          font: {
            size: 14,
            weight: 'bold'
          },
          padding: {
            top: 0,
            bottom: 10
          }
        }
      },
    },
  };

  // Obtener las etiquetas únicas para mostrar
  const uniqueLabels = seriesField 
    ? [...new Set(data.map(item => item[seriesField]))]
    : [title];

  return (
    <div style={{ marginBottom: 50 }}>
      <div style={{ marginBottom: '20px' }}>
        <h2 style={{ color: 'white', marginBottom: '10px' }}>{title}</h2>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
{uniqueLabels.map((label, index) => (
  <Tag 
    key={index}
    color={getColors()[index % getColors().length].replace('0.7', '1')}
    style={{ 
      color: '#1d1d1d',  // Changed from 'white' to dark color for better readability
      fontWeight: 'bold', // Added bold to improve contrast
      border: 'none',
      padding: '4px 8px',
      fontSize: '12px'
    }}
  >
    {label}
  </Tag>
))}
        </div>
      </div>
      <div style={{ 
        height: '400px',
        background: '#2d2d2d', 
        padding: '20px', 
        borderRadius: '8px',
        position: 'relative',
        minHeight: 200,
        maxHeight: 500,
        boxSizing: 'border-box',
      }}>
        <div style={{ 
           display: 'flex',
           height: '100%'
        }}>
          <div style={{ 
            overflowX: 'auto',
            overflowY: 'hidden',
            flex: 1,
            height: '100%'
          }}>
            <div style={{ 
              minWidth: '800px',
              height: '100%'
            }}>
              <Bar 
                data={chartData} 
                options={{
                  ...options,
                  scales: {
                    ...options.scales,
                    y: {
                      ...options.scales.y,
                      ticks: {
                        ...options.scales.y.ticks,
                        display: true,
                        color: '#FFFFFF',
                        font: { size: 12 },
                      },
                      display: true,
                    }
                  }
                }}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BarChart; 