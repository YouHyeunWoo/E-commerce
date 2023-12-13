# E-commerce
인터넷을 통한 상품의 구매 및 판매 서비스

## 프로젝트 기능 및 설계
* 상품 검색 기능
  * 상품 검색 기능은 모든 사용자가 권한 없이 이용 가능하다.

* 회원가입 기능
  * 사용자는 회원가입을 할 수 있다. 일반 사용자는 고객 권한, 상품을 판매하는 판매자는 관리자 권한을 가지게 된다.
  * 판매자는 판매를 하는 관리자 권한을 가지지만, 상품을 구입하는 고객 권한도 가질 수 있다.
  * 회원가입시 아이디, 패스워드, 전화번호, 집주소를 입력받으며 아이디는 unique하다.
  
* 상품 등록, 상품 삭제 기능
  * 상품 등록, 상품 삭제 기능은 관리자 권한으로 회원가입을 한 사용자만 이용 가능하다.
  * 등록된 상품은 고유의 상품 번호를 가지게 되며 상품 번호는 unique하다

* 장바구니 기능
  * 장바구니 기능(상품 담기, 장바구니 조회, 장바구니 상품 삭제)은 고객 권한으로 회원가입을 한 사용자만 이용 가능하다.
