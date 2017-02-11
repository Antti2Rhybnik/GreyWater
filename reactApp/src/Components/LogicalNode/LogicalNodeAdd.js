import React, { Component } from 'react';


class LogicalNodeAdd extends Component {


    render() {


        return (
            <div>
                <p>id: <input type="text" ref={inp => this.id_input = inp}/></p>
                <p>expr: <input type="text" ref={inp => this.msg_input = inp}/></p>
            </div>
        )
    }
}

export default LogicalNodeAdd;