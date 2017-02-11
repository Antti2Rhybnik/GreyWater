import React, { Component } from 'react';

import SensorNode from './SensorNode';

class SensorNodes extends Component {

    render() {

        let { nodes } = this.props;

        let renders = nodes.map((node, idx) => {

            let { node_id, parameters } = node;

            return (
                <div key={idx}>
                    <SensorNode node_id={node_id} parameters={parameters} />
                </div>
            );
        });

        return (
            <div>
                <h3>Sensor Nodes</h3>
                <div>{renders}</div>
            </div>
        );
    }

}


export default SensorNodes;