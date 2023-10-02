import React, { Component } from "react";
import { createRoot } from "react-dom/client";
import "../webapp/css/address.css";
import Address from "components/Address";

class App extends Component {
  render() {
    return (
      <div>
        <Address />
      </div>
    );
  }
}

const root = createRoot(document.getElementById("root"));
root.render(<App />);
