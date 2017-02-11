import React, { Component } from 'react';



class EventNodeAdd extends Component {


    render() {


        return (
            <div>
                <p>id: <input type="text" ref={inp => this.id_input = inp}/></p>
                <p>message: <input type="text" ref={inp => this.msg_input = inp}/></p>
                <p>importance: <input type="text" ref={inp => this.importance_input = inp}/></p>
            </div>
        )
    }
}

export default EventNodeAdd;