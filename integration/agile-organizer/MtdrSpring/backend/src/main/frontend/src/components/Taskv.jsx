import { Avatar } from 'antd';
import React from 'react'
import { Draggable } from 'react-beautiful-dnd';
import styled from 'styled-components';

const Container = styled.div`
    border-radius: 10px;
    padding: 8px;
    color: #000;
    margin-bottom: 8px;
    min-height: 90px;
    margin-left: 10px;
    margin-right: 10px;
    background-color: ${(props) => bgcolorChange(props)};
    cursor: pointer;
    display: flex;
    justify-content: space-between;
    flex-direction: column;
`;

const TextContent = styled.div``;

const Icons = styled.div`
    display: flex;
    justify-content: end;
    padding: 2px;
`;

function bgcolorChange(props){
    return props.isDragging
    ? 'lightgreen'
    :props.isDraggable
    ? props.isBacklog
    ? '#f2d7d5'
    : '#dcdcdc'
    :props.isBacklog
    ? '#f2d7d5'
    : '#fffada';
}

export default function Task({task, index}){
    return (
    <Container>
    <div style={{display: 'flex', justifyContent: 'start', padding: 2}}>
        <span>
            <small>
                #{task.id}
                {''}
            </small>
        </span>
    </div>

    <div
        style={{display: 'flex', justifyContent: 'center', padding: 2}}
    >
        <TextContent>{task.title}</TextContent>
    </div>
    <Icons>
        <div>
            <Avatar src={'https://joesch.moe/api/v1/random?key=' + task.id}/>
        </div>
    </Icons>
    </Container>
    )
}


    // return(<Draggable draggableId={`${task.id}`} key={task.id} index={index} >
    //     {(provided, snapshot) => (
    //         <Container
    //             {...provided.draggableProps}
    //             {...provided.dragHandleProps}
    //             ref = {provided.innerRef}
    //             isDragging={snapshot.isDragging}
    //         >

    //         <div style={{display: 'flex', justifyContent: 'start', padding: 2}}>
    //             <span>
    //                 <small>
    //                     #{task.id}
    //                     {''}
    //                 </small>
    //             </span>
    //         </div>

    //         <div
    //             style={{display: 'flex', justifyContent: 'center', padding: 2}}
    //         >
    //             <TextContent>{task.title}</TextContent>
    //         </div>
    //         <Icons>
    //             <div>
    //                 <Avatar src={'https://joesch.moe/api/v1/random?key=' + task.id}/>
    //             </div>
    //         </Icons>
    //         {provided.placeholder}
    //         </Container>
    //     )}
    // </Draggable>);