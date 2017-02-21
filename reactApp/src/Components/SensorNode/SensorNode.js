import React, { Component } from 'react';


class SensorNode extends Component {

    render() {

        let { node_id, parameters: { sensor_type, sensor_unit } } = this.props;

        return (
            <div>
                <div><b>{node_id+''}:</b> {sensor_type+''}, {sensor_unit+''}</div>
            </div>
        );
    }

}


export default SensorNode;