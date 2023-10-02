import axios from "axios";

export default class SendAddress {
  /**
   * sendAddress 메소드는 주어진 시작점과 도착점 좌표를 백엔드 서버로 전송합니다.
   * @param {string} startCoordinate - 시작점의 좌표
   * @param {string} endCoordinate - 도착점의 좌표
   * @returns {Promise} 서버 응답을 담은 프로미스 객체를 반환합니다.
   */
  static async send(startPoint, endPoint, name, phone, item) {
    try {
      const response = await axios.post("http://localhost:9000/api/address", {
        startCoordinate: startPoint,
        endCoordinate: endPoint,
        name: name,
        phone: phone,
        item: item,
      });
      return response.data;
    } catch (error) {
      console.error("주소를 보내는 중 에러가 발생했습니다.", error);
      throw error;
    }
  }
}
