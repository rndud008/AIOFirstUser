package hello.aiofirstuser.dto.kakaopay;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class KakaoCancelResponseDTO {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private ApprovedCancelAmount approved_cancel_amount;
    private CanceledAmount canceled_amount;
    private CancelAvailableAmount cancel_available_amount;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private String created_at;
    private String approved_at;
    private String canceled_at;
    private String payload;


    @Data
    public static class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    public static class ApprovedCancelAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    public static class CanceledAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    public static class CancelAvailableAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }
}
