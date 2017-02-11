import React, { Component } from 'react';


class SensorNodeAdd extends Component {


    render() {


        return (
            <div>
                <p>id: <input type="text" ref={inp => this.id_input = inp}/></p>
                <p>sensor_name: <input type="text" ref={inp => this.msg_input = inp}/></p>
                <p>sensor_unit: <input type="text" ref={inp => this.msg_input = inp}/></p>
            </div>
        )
    }
}

export default SensorNodeAdd;