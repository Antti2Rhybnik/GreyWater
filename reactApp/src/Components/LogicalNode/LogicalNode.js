import React, { Component } from 'react';


class LogicalNode extends Component {

    render() {

        let { node_id, parameters: { logic_expr } } = this.props;

        return (
            <div>
                <div>id: {node_id+''}</div>
                <div>expr: {logic_expr+''}</div>
            </div>
        );
    }

}


export default LogicalNode;