import React from 'react';

const Home = () => {
  return <h1>Home</h1>;
};

export default Home;

<style>{`
  h1 {
    text-align: center;
    font-size: 2em;
    margin: 20px 0;
  }

  @media (min-width: 768px) {
    h1 {
      font-size: 3em;
    }
  }
`}</style> 