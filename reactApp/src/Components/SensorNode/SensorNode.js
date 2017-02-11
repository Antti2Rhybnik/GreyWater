import React, { Component } from 'react';


class SensorNode extends Component {

    render() {

        let { node_id, parameters: { sensor_type, sensor_unit } } = this.props;

        return (
            <div>
                <div>id: {node_id+''}</div>
                <div>name: {sensor_type+''}</div>
                <div>units: {sensor_unit+''}</div>
            </div>
        );
    }

}


export default SensorNode;