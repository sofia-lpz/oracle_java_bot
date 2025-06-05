import { useState} from 'react';
import '../components/scroll.css';
import KanbanBoard from '../components/KanbanBoard';
import Task from '../components/Taskv';

function App(){
    const [count, setCount] = useState(0);

    return(
        <div>
            <Task task={{id:123, title:'Make a progress board application'}} index='1' />
            <KanbanBoard/>
        </div>


    );
}

export default App;
