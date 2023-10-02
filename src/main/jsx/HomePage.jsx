import React, { Component } from "react";
import { createRoot } from "react-dom/client";
import "../webapp/css/home.css";
import Address from "./components/Address";

class HomePage extends Component {
  render() {
    return (
      <div>
        <Address />
      </div>
    );
  }
}

// 루트 요소에 HomePage 컴포넌트를 렌더링합니다.
const root = createRoot(document.getElementById("root"));
root.render(<HomePage />);
