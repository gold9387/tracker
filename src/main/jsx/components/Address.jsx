import React, { Component } from "react";
import SendAddress from "api/send-address";

class Address extends Component {
  constructor(props) {
    super(props);
    this.state = {
      startPoint: "",
      endPoint: "",
      name: "",
      phone: "",
      item: "",
    };
  }

  /**
   * handleInputChange 메서드는 input 필드의 값이 변경될 때마다 호출되어 state를 업데이트합니다.
   * @param {Event} e - 발생한 이벤트 객체
   */
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  /**
   * handleSubmit 메서드는 '전송' 버튼이 클릭될 때 호출되어 실제로 데이터를 전송하는 로직을 수행합니다.
   */
  handleSubmit = async () => {
    const { startPoint, endPoint, name, phone, item } = this.state;

    try {
      // Address 클래스의 sendAddress 메서드를 사용하여 데이터를 전송합니다.
      const response = await SendAddress.send(
        startPoint,
        endPoint,
        name,
        phone,
        item
      );
      console.log("서버로부터의 응답:", response);
    } catch (error) {
      console.error("데이터를 전송하는 중에 오류가 발생했습니다:", error);
    }
  };

  render() {
    return (
      <div className="address-component">
        <div className="title">배송 추적 서비스</div>
        <div className="input-container">
          <input
            type="text"
            name="startPoint"
            placeholder="출발지"
            value={this.state.startPoint}
            onChange={this.handleInputChange}
            className="input-field"
          />
          <input
            type="text"
            name="endPoint"
            placeholder="도착지"
            value={this.state.endPoint}
            onChange={this.handleInputChange}
            className="input-field"
          />
          <input
            type="text"
            name="name"
            placeholder="이름"
            value={this.state.name}
            onChange={this.handleInputChange}
            className="input-field"
          />
          <input
            type="text"
            name="phone"
            placeholder="전화번호"
            value={this.state.phone}
            onChange={this.handleInputChange}
            className="input-field"
          />
          <select
            name="item"
            value={this.state.item}
            onChange={this.handleInputChange}
            className="input-field"
          >
            <option value="">품목 선택</option>
            <option value="냉동">냉동</option>
            <option value="냉장">냉장</option>
          </select>
          <button onClick={this.handleSubmit} className="submit-button">
            전송
          </button>
        </div>
      </div>
    );
  }
}

export default Address;
