import React, { Component } from 'react';


class ArithmeticalNode extends Component {

    render() {

        let { node_id, parameters: { arithm_expr, arithm_integrable } } = this.props;

        return (
            <div>
                <div>id: {node_id+''}</div>
                <div>expr: {arithm_expr+''}</div>
                <div>integr: {arithm_integrable+''}</div>
            </div>
        );
    }

}


export default ArithmeticalNode;