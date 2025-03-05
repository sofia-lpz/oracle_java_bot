import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SideBar from './components/SideBar';

function App() {
  return (
    <Router>
      <SideBar />
    </Router>
  );
}

export default App;