import "../webapp/css/home.css";
import React, { Component } from "react";
import { createRoot } from "react-dom/client";
import Test from "./components/Test";

class HomePage extends Component {
  render() {
    return (
      <div>
        <div className="home">no4gift 메인 페이지</div>
        <Test/>
      </div>
    );
  }
}

const root = createRoot(document.getElementById("root"));
root.render(<HomePage />);
