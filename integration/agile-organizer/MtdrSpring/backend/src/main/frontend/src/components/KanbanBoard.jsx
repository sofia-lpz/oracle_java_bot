import React, { useState, useEffect} from 'react';
import Column from '../components/Column'
import {DragDropContext} from 'react-beautiful-dnd';

export default function KanbanBoard(){

    const [completed, setCompleted] = useState([]);
    const [incomplete, setIncomplete] = useState([]);

    useEffect(() => {
      fetch("https://jsonplaceholder.typicode.com/todos")
      .then((response) => response.json())
      .then((json) => {
        setCompleted(json.filter((task) => task.completed));
        setIncomplete(json.filter((task) => !task.completed));
      });
    }, []);

    // Do nothing if you only drag card
    const handleDragEnd = (result) => {
      const { destination, source, draggableId } = result;
      if(source.droppableId == destination.droppableId) return;

      if(source.droppableId == 2){
          setCompleted(removeItemById(draggableId, completed));
      }else{
        setIncomplete(removeItemById(draggableId, incomplete));
      }

      //get item
      const task = findItemById(draggableId, [...incomplete, ...completed]);

      //add item
      if(destination.droppableId == 2){
        setCompleted([{...task, completed: !task.completed }, ...completed]);
      } else {
        setIncomplete([{...task, completed: !task.completed }, ...incomplete]);
      }

    };

    function findItemById(id, array){
      return array.find((item) => item.id == id);
    }

    function removeItemById(id, array){
      return array.filter((item) => item.id != id);
    }


    return(
        // Move cards between columns 
        <DragDropContext onDragEnd={handleDragEnd}> 
            <h2 style={{textAlign: 'center'}}>PROGRESS BOARD</h2>

            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                flexDirection: 'row'
              }}

            >
              <Column title={'To Do'} task = {incomplete} id={'1'}/>
            </div>

        </DragDropContext>

        
    );
}

