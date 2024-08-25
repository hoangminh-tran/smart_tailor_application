package com.smart.tailor.constant;

public class APIConstant {
    /**
     * Default API
     */
    public static final String API = "/api/v1";

    /**
     * Authentication API
     */
    public class AuthenticationAPI {

        public static final String AUTHENTICATION = APIConstant.API + "/auth";
        public static final String REGISTER = "/register";
        public static final String VERIFY = "/verify";
        public static final String CHECK_VERIFY_ACCOUNT = "/check-verify-account";
        public static final String CHECK_VERIFY_FORGOT_PASSWORD = "/check-verify-forgot-password";
        public static final String CHECK_VERIFY_CHANGE_PASSWORD = "/check-verify-change-password";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String UPDATE_PASSWORD = "/update-password";
        public static final String GOOGLE_REGISTER = "/google-register";
        public static final String LOGIN = "/login";
        public static final String GOOGLE_LOGIN = "/google-login";
        public static final String LOG_OUT = "/log-out";
        public static final String REFRESH_TOKEN = "/refresh-token";
        public static final String RESEND_VERIFICATION_TOKEN = "/resend-verification-token";
    }

    /**
     * Brand API
     */
    public class BrandAPI {
        public static final String BRAND = APIConstant.API + "/brand";
        public static final String GET_BRAND = "/get-brand";
        public static final String ADD_NEW_BRAND = "/add-new-brand";
        public static final String CHECK_VERIFY = "/check-verify";
        public static final String UPLOAD_BRAND_INFOR = "/upload-brand-infor";
        public static final String GET_BRAND_REGISTRATION_PAYMENT = "/get-brand-registration-payment";
        public static final String ADD_EXPERT_TAILORING_FOR_BRAND = "/add-expert-tailoring-for-brand";
        public static final String UPDATE_EXPERT_TAILORING_FOR_BRAND = "/update-expert-tailoring-for-brand";
        public static final String GET_ALL_EXPERT_TAILORING_BY_BRAND_ID = "/get-all-expert-tailoring-by-brand-id";
        public static final String VERIFY = "/verify";
        public static final String ACCEPT_BRAND = "/accept-brand";
        public static final String REJECT_BRAND = "/reject-brand";
        public static final String GET_BRAND_INFORMATION_BY_BRAND_ID = "/get-brand-information-by-brand-id";
        public static final String CHANGE_IMAGE_STATUS = "/change-image-status";
        public static final String GET_ALL_BRAND_INFORMATION = "/get-all-brand-information";
    }

    /**
     * Role API
     */
    public class RoleAPI {
        public static final String ROLE = APIConstant.API + "/roles";
        public static final String GET_ALL_ROLES = "/get-all-roles";
        public static final String ADD_NEW_ROLES = "/add-new-roles";
    }

    /**
     * Category API
     */
    public class CategoryAPI {
        public static final String CATEGORY = APIConstant.API + "/category";
        public static final String ADD_NEW_CATEGORY = "/add-new-category";
        public static final String GET_ALL_CATEGORY = "/get-all-category";
        public static final String GET_CATEGORY_BY_ID = "/get-category-by-id";
        public static final String UPDATE_CATEGORY = "/update-category";
        public static final String CHANGE_STATUS_CATEGORY = "/change-status-category";
    }

    /**
     * Material API
     */
    public class MaterialAPI {
        public static final String MATERIAL = APIConstant.API + "/material";
        public static final String ADD_NEW_MATERIAL = "/add-new-material";
        public static final String GET_ALL_MATERIAL = "/get-all-material";
        public static final String GET_ALL_ACTIVE_MATERIAL = "/get-all-active-material";
        public static final String GET_MATERIAL_BY_ID = "/get-material-by-id";
        public static final String GET_LIST_MATERIAL_BY_CATEGORY_ID = "/get-list-material-by-category-id";
        public static final String GET_LIST_MATERIAL_BY_EXPERT_TAILORING_ID_AND_CATEGORY_ID = "/get-list-material-by-expert-tailoring-id-and-category-id";
        public static final String GET_LIST_MATERIAL_BY_CATEGORY_NAME = "/get-list-material-by-category-name";
        public static final String GET_MATERIAL_BY_MATERIAL_NAME = "/get-material-by-name";
        public static final String ADD_NEW_CATEGORY_MATERIAL_BY_EXCEL_FILE = "/add-new-category-material-by-excel-file";
        public static final String EXPORT_CATEGORY_MATERIAL_FOR_BRAND_BY_EXCEL = "/export-category-material-for-brand-by-excel";
        public static final String UPDATE_MATERIAL = "/update-material";
        public static final String UPDATE_STATUS_MATERIAL = "/update-status-material";
        public static final String GENERATE_SAMPLE_CATEGORY_MATERIAL_BY_EXCEL_FILE = "/generate-sample-category-material-by-excel-file";
    }

    /**
     * Brand Material API
     */
    public class BrandMaterialAPI {
        public static final String BRAND_MATERIAL = APIConstant.API + "/brand-material";
        public static final String ADD_NEW_BRAND_MATERIAL = "/add-new-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL = "/get-all-brand-material";
        public static final String GET_ALL_BRAND_MATERIAL_BY_BRAND_ID = "/get-all-brand-material-by-brand-id";
        public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE = "/add-new-brand-material-by-excel-file";
        public static final String UPDATE_BRAND_MATERIAL = "/update-brand-material";
    }

    /**
     * Customer API
     */
    public class CustomerAPI {
        public static final String CUSTOMER = APIConstant.API + "/customer";
        public static final String UPDATE_CUSTOMER_PROFILE = "/update-customer-profile";
    }

    /**
     * Expert Tailoring API
     */
    public class ExpertTailoringAPI {
        public static final String EXPERT_TAILORING = APIConstant.API + "/expert-tailoring";
        public static final String ADD_NEW_EXPERT_TAILORING = "/add-new-expert-tailoring";
        public static final String GET_ALL_EXPERT_TAILORING = "/get-all-expert-tailoring";
        public static final String GET_ALL_EXPERT_TAILORING_BY_EXPERT_TAILORING_NAME = "/get-expert-tailoring-by-name";
        public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE = "/add-new-expert-tailoring-by-excel-file";
        public static final String GENERATE_SAMPLE_EXPERT_TAILORING_BY_EXCEL_FILE = "/generate-sample-expert-tailoring-by-excel-file";
        public static final String GET_EXPERT_TAILORING_BY_ID = "/get-expert-tailoring-by-id";
        public static final String UPDATE_EXPERT_TAILORING = "/update-expert-tailoring";
        public static final String UPDATE_STATUS_EXPERT_TAILORING = "/update-status-expert-tailoring";

    }

    /**
     * Notification API
     */
    public class NotificationAPI {
        public static final String Notification = APIConstant.API + "/notification";
        public static final String SEND_PUBLIC_NOTIFICATION = "/send-public-notification";
        public static final String SEND_NOTIFICATION = "/send-notification";
        public static final String GET_ALL_NOTIFICATION_BY_USER_ID = "/get-all-notification-user-id";
        public static final String UPDATE_NOTIFICATION_STATUS = "/update-notification-status";
        public static final String MARK_ALL_READ = "/mark-all-read";
    }

    /**
     * Design API
     */
    public class DesignAPI {
        public static final String DESIGN = APIConstant.API + "/design";
        public static final String ADD_NEW_DESIGN = "/add-new-design";
        public static final String ADD_NEW_CLONE_DESIGN = "/add-new-clone-design";
        public static final String GET_DESIGN_BY_ID = "/get-design-by-id";
        public static final String GET_ALL_DESIGN_BY_USER_ID = "/get-all-design-by-user-id";
        public static final String GET_ALL_DESIGN = "/get-all-design";
        public static final String GET_ALL_DESIGN_BY_CUSTOMER_ID = "/get-all-design-by-customer-id";
        public static final String GET_ALL_DESIGN_BY_BRAND_ID = "/get-all-design-by-brand-id";
        public static final String UPDATE_PUBLIC_STATUS_BY_DESIGN_ID = "/update-public-status-design-id";
        public static final String UPDATE_DESIGN = "/update-design";
        public static final String CREATE_CLONE_DESIGN_FROM_BASE_DESIGN = "/create-clone-design-from-base-design";
    }

    /**
     * PartOfDesign API
     */
    public class PartOfDesignAPI {
        public static final String PART_OF_DESIGN = APIConstant.API + "/part-of-design";
        public static final String GET_PART_OF_DESIGN_BY_ID = "/get-part-of-design-by-id";
        public static final String GET_ALL_PART_OF_DESIGN_BY_DESIGN_ID = "/get-all-part-of-design-by-design-id";
        public static final String GET_ALL_PART_OF_DESIGN = "/get-all-part-of-design";
    }

    /**
     * ItemMask API
     */
    public class ItemMaskAPI {
        public static final String ITEM_MASK = APIConstant.API + "/item-mask";
        public static final String GET_ITEM_MASK_BY_ID = "/get-item-mask-by-id";
        public static final String GET_ALL_ITEM_MASK_BY_PART_OF_DESIGN_ID = "/get-all-item-mask-by-part-of-design-id";
        public static final String GET_ALL_ITEM_MASK = "/get-all-item-mask";
    }

    /**
     * LaborQuantity API
     */
    public class LaborQuantityAPI {
        public static final String LABOR_QUANTITY = APIConstant.API + "/labor-quantity";
        public static final String ADD_NEW_LABOR_QUANTITY = "/add-new-labor-quantity";
        public static final String GET_ALL_LABOR_QUANTITY = "/get-all-labor-quantity";
        public static final String UPDATE_LABOR_QUANTITY = "/update-labor-quantity";
    }

    /**
     * BrandLaborQuantity API
     */
    public class BrandLaborQuantityAPI {
        public static final String BRAND_LABOR_QUANTITY = APIConstant.API + "/brand-labor-quantity";
        public static final String ADD_NEW_BRAND_LABOR_QUANTITY = "/add-new-brand-labor-quantity";
        public static final String GET_ALL_BRAND_LABOR_QUANTITY_BY_BRAND_ID = "/get-all-brand-labor-quantity-by-brand-id";
        public static final String UPDATE_BRAND_LABOR_QUANTITY = "/update-brand-labor-quantity";
    }

    /**
     * User API
     */
    public class UserAPI {
        public static final String USER = APIConstant.API + "/user";
        public static final String GET_ALL_CUSTOMER = "/get-all-customer";
        public static final String GET_ALL_BRAND = "/get-all-brand";
        public static final String GET_ALL_EMPLOYEE = "/get-all-employee";
        public static final String GET_ALL_ACCOUNTANT = "/get-all-accountant";
        public static final String GET_ALL_MANAGER = "/get-all-manager";
        public static final String GET_ALL_ADMIN = "/get-all-admin";
        public static final String CALCULATE_USER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK = "/calculate-user-growth-percentage-for-current-and-previous-week";
        public static final String CALCULATE_NEW_CUSTOMER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK = "/calculate-new-customer-growth-percentage-for-current-and-previous-week";
        public static final String CALCULATE_NEW_USER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_DAY_BY_ROLE_NAME = "/calculate-new-user-growth-percentage-for-current-and-previous-day-by-role-name";
        public static final String CALCULATE_TOTAL_OF_USER = "/calculate-total-of-user";
        public static final String UPDATE_USER = "/update-user";
    }

    /**
     * Size API
     */
    public class SizeAPI {
        public static final String SIZE = APIConstant.API + "/size";
        public static final String ADD_NEW_SIZE = "/add-new-size";
        public static final String GET_ALL_SIZE = "/get-all-size";
        public static final String UPDATE_SIZE = "/update-size";
    }

    /**
     * Size Expert Tailoring API
     */
    public class SizeExpertTailoringAPI {
        public static final String SIZE_EXPERT_TAILORING = APIConstant.API + "/size-expert-tailoring";
        public static final String ADD_NEW_SIZE_EXPERT_TAILORING = "/add-new-size-expert-tailoring";
        public static final String GET_ALL_SIZE_EXPERT_TAILORING = "/get-all-size-expert-tailoring";
        public static final String GET_ALL_SIZE_BY_EXPERT_TAILORING = "/get-all-size-by-expert-tailoring-id";
        public static final String UPDATE_SIZE_EXPERT_TAILORING = "/update-size-expert-tailoring";
        public static final String ADD_NEW_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE = "/add-new-size-expert-tailoring-by-excel-file";
        public static final String GENERATE_SAMPLE_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE = "/generate-sample-size-expert-tailoring-by-excel-file";
    }

    /**
     * Design Detail API
     */
    public class DesignDetailAPI {
        public static final String DESIGN_DETAIL = APIConstant.API + "/design-detail";
        public static final String ADD_NEW_DESIGN_DETAIL = "/add-new-design-detail";
        public static final String GET_ALL_DESIGN_DETAIL_BY_ORDER_ID = "/get-all-design-detail-by-order-id";
        public static final String GET_DESIGN_DETAIL_BY_ID = "/get-design-detail-by-id";
        public static final String CALCULATE_TOTAL_PRICE_BY_PARENT_ORDER_ID = "/calculate-total-price-by-parent-order-id";
    }

    /**
     * Order API
     */
    public class OrderAPI {
        public static final String ORDER = APIConstant.API + "/order";
        public static final String CREATE_ORDER = "/create-order";
        public static final String GET_ORDER_BY_ID = "/get-order-by-id";
        public static final String GET_ORDER_FULL_PROP = "/get-full-of-order";
        public static final String GET_ORDER_FULL_PROP_BY_BRAND_ID = "/get-full-of-order-by-brand-id";
        public static final String GET_ORDER_BY_BRAND_ID = "/get-order-by-brand-id";
        public static final String GET_ORDER_BY_USER_ID = "/get-order-by-user-id";
        public static final String GET_ORDER_DETAIL_BY_ID = "/get-order-detail-by-id";
        public static final String GET_ORDER_STAGE_BY_ID = "/get-order-stage-by-id";
        public static final String GET_ALL_ORDER = "/get-all-order";
        public static final String CHANGE_STATUS_ORDER = "/change-order-status";
        public static final String BRAND_PICK_ORDER = "/brand-pick_order";
        public static final String GET_PARENT_ORDER_BY_DESIGN_ID = "/get-parent-order-by-design-id";
        public static final String RATING_ORDER = "/rating-order";
        public static final String ORDER_TIME_LINE_BY_PARENT_ORDER_ID = "/order-time-line-by-parent-order-id";
        public static final String ORDER_TIME_LINE_BY_SUB_ORDER_ID = "/order-time-line-by-sub-order-id";
        public static final String GET_ORDER_SHIPPING_DETAIL_BY_LABEL_ID = "/get-order-shipping-detail-by-label-id";
        public static final String GET_ORDER_STATUS_DETAIL = "/get-order-status-detail";
        public static final String CALCULATE_ORDER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_MONTH = "/calculate-order-growth-percentage-for-current-and-previous-month";
        public static final String GET_TOTAL_ORDER_OF_EACH_BRAND = "/get-order-detail-of-each-brand";
        public static final String GET_SUB_ORDER_INVOICE_BY_SUB_ORDER_ID = "/get-sub-order-invoice-by-sub-order-id";
    }

    /**
     * System Image
     */
    public class SystemImageAPI {
        public static final String SYSTEM_IMAGE = APIConstant.API + "/system-image";
        public static final String ADD_NEW_SYSTEM_IMAGE = "/add-new-system-image";
        public static final String GET_ALL_SYSTEM_IMAGE = "/get-all-system-image";
        public static final String GET_ALL_SYSTEM_IMAGE_BY_ID = "/get-all-system-image-by-id";
        public static final String GET_ALL_SYSTEM_IMAGE_BY_IMAGE_TYPE = "/get-all-system-image-by-image-type";
        public static final String GET_ALL_SYSTEM_IMAGE_BY_IMAGE_TYPE_AND_PREMIUM = "/get-all-system-image-by-image-type-and-premium";
    }

    /**
     * ExpertTailoringMaterial API
     */
    public class ExpertTailoringMaterialAPI {
        public static final String EXPERT_TAILORING_MATERIAL = APIConstant.API + "/expert-tailoring-material";
        public static final String ADD_NEW_EXPERT_TAILORING_MATERIAL = "/add-new-expert-tailoring-material";
        public static final String ADD_NEW_EXPERT_TAILORING_MATERIAL_BY_EXCEL_FILE = "/add-new-expert-tailoring-material-by-excel-file";
        public static final String GET_ALL_EXPERT_TAILORING_MATERIAL = "/get-all-expert-tailoring-material";
        public static final String GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_ID = "/get-all-expert-tailoring-material-by-expert-tailoring-id";
        public static final String GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_NAME = "/get-all-expert-tailoring-material-by-expert-tailoring-name";
        public static final String CHANGE_STATUS_EXPERT_TAILORING_MATERIAL = "/change-status-expert-tailoring-material";
        public static final String GENERATE_SAMPLE_CATEGORY_MATERIAL_EXPERT_TAILORING_BY_EXCEL_FILE = "/generate-sample-category-material-expert-tailoring-by-excel-file";
    }

    /**
     * SystemProperty
     */
    public class SystemPropertyAPI {
        public static final String SYSTEM_PROPERTY = APIConstant.API + "/system-property";
        public static final String ADD_NEW_SYSTEM_PROPERTY = "/add-new-system-property";
        public static final String UPDATE_SYSTEM_PROPERTY = "/update-system-property";
        public static final String GET_ALL_SYSTEM_PROPERTY = "/get-all-system-properties";
        public static final String GET_ALL_SYSTEM_PROPERTY_BY_TYPE = "/get-all-system-properties-by-type";
        public static final String GET_SYSTEM_PROPERTY = "/get-system-property";
    }

    /**
     * BrandProperty
     */
    public class BrandPropertyAPI {
        public static final String BRAND_PROPERTY = APIConstant.API + "/brand-property";
        public static final String ADD_NEW_BRAND_PROPERTY = "/add-new-brand-property";
        public static final String GET_ALL_BRAND_PROPERTY = "/get-all-brand-properties";
        public static final String GET_ALL_BRAND_PROPERTY_BY_BRAND_ID = "/get-all-brand-properties-by-brand-id";
        public static final String GET_BRAND_PROPERTY = "/get-brand-property";
        public static final String UPDATE_BRAND_PROPERTY = "/update-brand-property";
        public static final String GET_BRAND_PRODUCTIVITY_BY_BRAND_ID = "/get-brand-productivity-by-brand-id";
    }

    /**
     * Payment
     */
    public class PaymentAPI {
        public static final String PAYMENT = API + "/payment";
        public static final String CREATE_PAYMENT = "/create-payment";
        public static final String PAYMENT_INFO = "/payment-info";
        public static final String MANUAL_PAYMENT_INFO = "/manual-payment-info";
        public static final String GET_PAYMENT_BY_USER_ID = "/get-payment-by-user-id";
        public static final String GET_ALL_PAYMENT = "/get-all-payment";
        public static final String CONFIRM_PAYMENT = "/confirm-payment";
        public static final String CALCULATE_PAYMENT_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK = "/calculate-payment-growth-percentage-for-current-and-previous-week";
        public static final String CALCULATE_INCOME_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK = "/calculate-income-growth-percentage-for-current-and-previous-week";
        public static final String CALCULATE_REFUND_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_MONTH = "/calculate-refund-growth-percentage-for-current-and-previous-month";
        public static final String GET_TOTAL_PAYMENT_OF_EACH_MONTH = "/get-total-payment-of-each-month";
        public static final String GET_TOTAL_INCOME_OF_EACH_MONTH = "/get-total-income-of-each-month";
        public static final String GET_TOTAL_REFUND_OF_EACH_MONTH = "/get-total-refund-of-each-month";
    }

    /**
     * Report API
     */
    public class ReportAPI {
        public static final String REPORT = APIConstant.API + "/report";
        public static final String CREATE_REPORT = "/create-report";
        public static final String GET_ALL_REPORT = "/get-all-report";
        public static final String GET_ALL_REPORT_BY_ORDER_ID = "/get-all-report-by-order-id";
        public static final String GET_ALL_REPORT_BY_USER_ID = "/get-all-report-by-user-id";
        public static final String GET_ALL_REPORT_BY_BRAND_ID = "/get-all-report-by-brand-id";
        public static final String GET_ALL_REPORT_BY_PARENT_ORDER_ID = "/get-all-report-by-parent-order-id";
    }

    /**
     * Sample Product Data
     */
    public class SampleProductDataAPI {
        public static final String SAMPLE_PRODUCT_DATA = APIConstant.API + "/sample-product-data";
        public static final String ADD_SAMPLE_PRODUCT_DATA = "/add-sample-product-data";
        public static final String UPDATE_SAMPLE_PRODUCT_DATA = "/update-sample-product-data";
        public static final String GET_SAMPLE_PRODUCT_DATA_BY_ID = "/get-sample-product-data-by-id";
        public static final String GET_SAMPLE_PRODUCT_DATA_BY_PARENT_ORDER_ID = "/get-sample-product-data-by-parent-order-id";
        public static final String GET_SAMPLE_PRODUCT_DATA_BY_PARENT_ORDER_ID_AND_STAGE_ID = "/get-sample-product-data-by-parent-order-id";
    }
}
