import React, { Component } from 'react';

import LogicalNode from './LogicalNode';

class LogicalNodes extends Component {

    render() {

        let { nodes } = this.props;

        let renders = nodes.map((node, idx) => {

            let { node_id, parameters } = node;

            return (
                <div key={idx}>
                    <LogicalNode node_id={node_id} parameters={parameters} />
                </div>
            );
        });

        return (
            <div>
                <h3>Logical Nodes</h3>
                <div>{renders}</div>
            </div>
        );
    }

}


export default LogicalNodes;