package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippingRequest {
    private OrderShippingDetailRequest order;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderShippingDetailRequest {

        public OrderShippingDetailRequest(String pick_address, String pick_province, String pick_district, String pick_ward, String address, String province, String district, String ward, Float weight) {
            this.pick_address = pick_address;
            this.pick_province = pick_province;
            this.pick_district = pick_district;
            this.pick_ward = pick_ward;
            this.address = address;
            this.province = province;
            this.district = district;
            this.ward = ward;
            this.weight = weight;
        }

        public OrderShippingDetailRequest(String id, String pick_name, String pick_address, String pick_province, String pick_district, String pick_ward, String pick_tel, String tel, String name, String address, String province, String district, String ward, String hamlet, String is_freeship, String pick_date, Integer pick_money, Float total_weight, Integer value) {
            this.id = id;
            this.pick_name = pick_name;
            this.pick_address = pick_address;
            this.pick_province = pick_province;
            this.pick_district = pick_district;
            this.pick_ward = pick_ward;
            this.pick_tel = pick_tel;
            this.tel = tel;
            this.name = name;
            this.address = address;
            this.province = province;
            this.district = district;
            this.ward = ward;
            this.hamlet = hamlet;
            this.is_freeship = is_freeship;
            this.pick_date = pick_date;
            this.pick_money = pick_money;
            this.total_weight = total_weight;
            this.value = value;
        }

        private String id; // mã đơn hàng thuộc hệ thống của đối tác
        private String pick_name; // Tên người liên hệ lấy hàng hóa
        private String pick_address; // Địa chỉ ngắn gọn để lấy nhận hàng hóa
        private String pick_province; // Tên tỉnh/thành phố nơi lấy hàng hóa
        private String pick_district; // Tên quận/huyện nơi lấy hàng hóa
        private String pick_ward; // Tên phường/xã nơi lấy hàng hóa
        private String pick_tel; // Số điện thoại liên hệ nơi lấy hàng hóa
        private String tel; // Số điện thoại người nhận hàng hóa
        private String name; // tên người nhận hàng
        private String address; // Địa chỉ chi tiết của người nhận hàng
        private String province; // Tên tỉnh/thành phố của người nhận hàng hóa
        private String district; // Tên quận/huyện của người nhận hàng hóa
        private String ward; // Tên phường/xã của người nhận hàng hóa
        private String hamlet; // Tên thôn/ấp/xóm/tổ/… của người nhận hàng hóa. Nếu không có, vui lòng điền “Khác”
        private String is_freeship; // Freeship cho người nhận hàng. Nếu bằng 1 COD sẽ chỉ thu người nhận hàng số tiền bằng pick_money
        private String pick_date; // Hẹn ngày lấy hàng (YYYY/MM/DD)
        private Integer pick_money; // Số tiền CoD. Nếu bằng 0 thì không thu tiền CoD. Tính theo VNĐ
        private Float total_weight; // Cân nặng này dùng để tạo Order Shipping. Tổng khối lượng của đơn hàng. Tính theo đơn vị KG
        private Integer value; // Giá trị đóng bảo hiểm, là căn cứ để tính phí bảo hiểm và bồi thường khi có sự cố.
        private Float weight; // Cân nặng này dùng để tính phí ship A -> B. Đơn vị sử dụng Gram
    }
}
