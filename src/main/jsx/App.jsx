import React, { Component } from "react";
import { createRoot } from "react-dom/client";
import "../webapp/css/address.css";
import Delivery from "components/Delivery";
import Dashboard from "components/Dashboard";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showDashboard: false,
    };
  }

  handleShowDashboard = () => {
    this.setState({ showDashboard: true });
  };

  render() {
    return (
      <div>
        {this.state.showDashboard ? (<Dashboard />) : (<Delivery onShowDashboard={this.handleShowDashboard} />)}
      </div>
    );
  }
}

const root = createRoot(document.getElementById("root"));
root.render(<App />);
