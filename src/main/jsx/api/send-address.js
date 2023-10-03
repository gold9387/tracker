import axios from "axios";

/**
 * SendAddress 클래스는 주소 정보를 서버로 전송합니다.
 */
export default class SendAddress {
  /**
   * send 메서드는 주소 정보를 서버로 전송합니다.
   * @param {string} startAddress - 시작점의 주소
   * @param {string} endAddress - 도착점의 주소
   * @param {string} name - 이름
   * @param {string} productName - 상품명
   * @param {string} item - 물품
   * @returns {Promise} - 서버 응답을 담은 프로미스 객체
   */
  static async send(startAddress, endAddress, name, productName, item) {
    try {
      const response = await axios.post("http://localhost:9000/api/address", {
        startAddress,
        endAddress,
        name,
        productName,
        item,
      });
      return response.data;
    } catch (error) {
      console.error("주소를 보내는 중 에러가 발생했습니다.", error);
      throw error;
    }
  }
}
