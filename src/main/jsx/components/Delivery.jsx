import React, { Component } from "react";
import SendAddress from "api/send-address";
import DaumPostcode from "react-daum-postcode";

class Delivery extends Component {
  constructor(props) {
    super(props);
    this.state = {
      startAddress: "",
      endAddress: "",
      additionStartAddress: "",
      additionEndAddress: "",
      name: "",
      productName: "",
      item: "",
      openPostcodeStart: false,
      openPostcodeEnd: false,
    };
  }

  /**
   * 입력 필드 값이 변경될 때 호출되는 함수.
   * @param {Event} event - 입력 필드 변경 이벤트
   */
  handleInputChange = (event) => {
    const { name, value } = event.target;
    this.setState({ [name]: value });
  };

  /**
   * Daum 우편번호 서비스 팝업을 열거나 닫는 함수.
   * @param {string} type - 팝업의 종류 ("start" 또는 "end")
   */
  togglePostcode = (type) => {
    if (type === "start") {
      this.setState({ openPostcodeStart: !this.state.openPostcodeStart });
    } else if (type === "end") {
      this.setState({ openPostcodeEnd: !this.state.openPostcodeEnd });
    }
  };

  /**
   * 선택한 주소를 상태에 저장하는 함수.
   * @param {object} data - 선택한 주소 데이터
   * @param {string} type - 주소의 종류 ("start" 또는 "end")
   */
  fillAddress = (data, type) => {
    if (type === "start") {
      this.setState({ startAddress: data.address, openPostcodeStart: false });
    } else if (type === "end") {
      this.setState({ endAddress: data.address, openPostcodeEnd: false });
    }
  };

  /**
   * '전송' 버튼 클릭 시 데이터를 서버로 전송하는 함수.
   */
  handleSubmit = async () => {
    const {
      startAddress,
      additionStartAddress,
      additionEndAddress,
      endAddress,
      name,
      productName,
      item,
    } = this.state;

    // 누락된 필드를 확인합니다.
    let missingField = null;
    if (!startAddress) missingField = ['출발지', 'startAddress'];
    else if (!endAddress) missingField = ['도착지', 'endAddress'];
    else if (!name) missingField = ['이름', 'name'];
    else if (!productName) missingField = ['상품명', 'productName'];
    else if (!item) missingField = ['품목', 'item'];

    // 누락된 필드가 있다면 경고창을 표시합니다.
    if (missingField) {
      alert(missingField[0] + '를 입력해 주세요.');
      document.getElementsByName(missingField[1])[0].focus(); // 누락된 필드로 포커스를 이동합니다.
      return;
    }

    try {
      // 주소 데이터와 사용자 정보를 서버로 전송
      const response = await SendAddress.send(
        startAddress + " " + additionStartAddress,
        endAddress + " " + additionEndAddress,
        name,
        productName,
        item
      );
      console.log("서버로부터의 응답:", response);
      this.props.onShowDashboard();
    } catch (error) {
      console.error("데이터를 전송하는 중에 오류가 발생했습니다:", error);
    }
  };

  render() {
    return (
      <div className="address-component">
        <div className="title">배송 추적 서비스</div>
        <div className="input-container">
          <input className="input-field" type="text" name="startAddress" placeholder="출발지" 
            value={this.state.startAddress} onChange={this.handleInputChange}/>
          <input className="input-field" type="text" name="additionStartAddress" placeholder="추가 출발지"
            value={this.state.additionStartAddress} onChange={this.handleInputChange}/>
          <button className="daum-button" onClick={() => this.togglePostcode("start")}>주소찾기</button>
          {this.state.openPostcodeStart && (
            <div className="postcode-popup">
              <DaumPostcode onComplete={(data) => this.fillAddress(data, "start")} autoClose={true}/>
            </div>
          )}
          <input className="input-field" type="text" name="endAddress" placeholder="도착지"
            value={this.state.endAddress} onChange={this.handleInputChange}/>
          <input className="input-field" type="text" name="additionEndAddress" placeholder="추가 도착지"
            value={this.state.additionEndAddress} onChange={this.handleInputChange}/>
          <button className="daum-button" onClick={() => this.togglePostcode("end")}>주소찾기</button>
          {this.state.openPostcodeEnd && (
            <div className="postcode-popup">
              <DaumPostcode onComplete={(data) => this.fillAddress(data, "end")} autoClose={true}/>
            </div>
          )}
          <input className="input-field" type="text" name="name" placeholder="이름"
            value={this.state.name} onChange={this.handleInputChange}/>
          <br />
          <input className="input-field" type="text" name="productName" placeholder="상품명"
            value={this.state.productName} onChange={this.handleInputChange}/>
          <br />
          <select className="input-field" name="item" value={this.state.item} onChange={this.handleInputChange}>
            <option value="">품목 선택</option>
            <option value="냉동">냉동</option>
            <option value="냉장">냉장</option>
          </select>
          <br />
          <button onClick={this.handleSubmit} className="submit-button">전송</button>
        </div>
      </div>
    );
  }
}

export default Delivery;
