import React, { Component } from 'react';

import './Menu.css';

class Menu extends Component {

    constructor(props) {
        super(props);
        this.itemClass.bind(this);
        this.changeSelected.bind(this);

        this.state = {
            selected: 0
        };
    }

    changeSelected(item, idx) {
        this.props.onClick(item);
        this.setState({selected: idx});
    }

    itemClass(curr, last) {

        const first = 0;
        let css = 'Menu-item ';

        if (curr === first) css += 'first ';
        if (curr === last) css += 'last ';
        if (this.state.selected === curr) css += 'selected ';

        return css;
    }

    render() {

        let { items } = this.props;

        let renders = items.map((item, idx) =>
                <div className={this.itemClass(idx, items.length-1)} key={idx} onClick={() => this.changeSelected(item, idx)}>
                    {item}
                </div>
        );

        return(
          <div className="Menu">{renders}</div>
        );
    }

}

export default Menu;