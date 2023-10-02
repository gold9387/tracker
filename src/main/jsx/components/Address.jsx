import React, { Component } from "react";
import AddressAPI from "api/address";

class Address extends Component {
  constructor(props) {
    super(props);
    this.state = {
      startPoint: "", // 시작점 주소를 저장할 state
      endPoint: "", // 도착점 주소를 저장할 state
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
    const { startPoint, endPoint } = this.state;

    try {
      // Address 클래스의 sendAddress 메서드를 사용하여 데이터를 전송합니다.
      const response = await AddressAPI.sendAddress(startPoint, endPoint);
      console.log("서버로부터의 응답:", response);
    } catch (error) {
      console.error("데이터를 전송하는 중에 오류가 발생했습니다:", error);
    }
  };

  render() {
    return (
      <div>
        <div>배송 추적 서비스</div>
        {/* 시작점 주소 입력 필드 */}
        <input
          type="text"
          name="startPoint"
          placeholder="출발지"
          value={this.state.startPoint}
          onChange={this.handleInputChange}
        />
        {/* 도착점 주소 입력 필드 */}
        <input
          type="text"
          name="endPoint"
          placeholder="도착지"
          value={this.state.endPoint}
          onChange={this.handleInputChange}
        />
        {/* 데이터 전송 버튼 */}
        <button onClick={this.handleSubmit}>전송</button>
      </div>
    );
  }
}

export default Address;
