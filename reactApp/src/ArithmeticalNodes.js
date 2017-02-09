import React, { Component } from 'react';

import ArithmeticalNode from './ArithmeticalNode';

class ArithmeticalNodes extends Component {

    render() {

        let { nodes } = this.props;

        let renders = nodes.map((node, idx) => {

            let { node_id, parameters } = node;

            return (
                <div key={idx}>
                    <ArithmeticalNode node_id={node_id} parameters={parameters} />
                </div>
            );
        });

        return (
            <div>
                <h3>ArithmeticalNodes</h3>
                <div>{renders}</div>
            </div>
        );
    }

}


export default ArithmeticalNodes;