import { useState } from 'react';
import { Button } from 'antd'; // Importa el botón de Ant Design
import NavigationMenu from './components/NavigationMenu'; // Importa el menú de navegación
import './App.css';

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <h1>Vite + React + Ant Design</h1>
      <div className="card">
        <Button type="primary" onClick={() => setCount(count + 1)}>
          Count is {count}
        </Button>
      </div>
      {/* Agrega el menú de navegación aquí */}
      <NavigationMenu />
    </>
  );
}

export default App;