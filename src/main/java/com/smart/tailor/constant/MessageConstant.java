package com.smart.tailor.constant;

public class MessageConstant {
    /**
     * SYSTEM MESSAGE
     */
    public static final String NEW_BRAND_REGISTERED = "New Brand Registered!";

    /**
     * ERROR MESSAGE
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    public static final String BAD_REQUEST = "BAD REQUEST";
    public static final String MISSING_ARGUMENT = "MISSING ARGUMENT";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found!";
    public static final String INVALID_DATA_TYPE = "Invalid Data Type";
    public static final String INVALID_EXCEL_FILE_FORMAT = "Invalid Excel File Format";
    public static final String DATA_IS_EMPTY = "Data is empty";
    public static final String INVALID_INPUT = "Invalid Input";
    /**
     * FORMATION ERROR
     */
    public static final String INVALID_EMAIL = "Invalid Email!";
    public static final String INVALID_PASSWORD = "Invalid Password!";
    public static final String INVALID_DATA_TYPE_COLUMN_NEED_TYPE_STRING = "Invalid Data Type Column Need Type String";
    public static final String INVALID_DATA_TYPE_COLUMN_NEED_TYPE_NUMERIC = "Invalid Data Type Column Need Type Numeric";
    public static final String INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER = "Invalid Negative Number Need Positive Number";
    public static final String ERROR_READING_EXCEL_FILE = "Error Reading Excel File";
    /**
     * SUCCESS MESSAGE
     */
    public static final String GET_DATA_FROM_EXCEL_SUCCESS = "Get Data From Excel Success";
    /**
     * ROLE
     */
    public static final String GET_ALL_ROLES_SUCCESSFULLY = "Get All Roles Successfully!";
    public static final String ROLE_LIST_IS_EMPTY = "Role List Is Empty!";
    public static final String CAN_NOT_FIND_ROLE = "Can Not Find Role";

    /**
     * BRAND
     */
    public static final String CAN_NOT_FIND_BRAND = "Can Not Find Brand";
    public static final String GET_BRAND_SUCCESSFULLY = "Get Brand Successfully!";
    public static final String REGISTER_NEW_BRAND_FAILED = "Failed To Register New Brand!";
    public static final String REGISTER_NEW_BRAND_SUCCESSFULLY = "Register New Brand Successfully!";
    public static final String ACCEPT_BRAND_SUCCESSFULLY = "Accept Brand Successfully!";
    public static final String REJECT_BRAND_SUCCESSFULLY = "Reject Brand Successfully!";
    public static final String ACCEPT_BRAND_FAILED = "Accept Brand Fail!";
    public static final String REJECT_BRAND_FAILED = "Reject Brand Fail!";
    public static final String CHANGE_IMAGE_STATUS_SUCCESS = "Image status changed successfully.";


    /**
     * AUTHENTICATION
     */
    /*    SUCCESS     */
    public static final String REGISTER_NEW_USER_SUCCESSFULLY = "Register New User Successfully!";
    public static final String SEND_MAIL_FOR_VERIFY_ACCOUNT_SUCCESSFULLY = "Check Email To Verify Account!";
    public static final String SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY = "Check Email To Update Password!";
    public static final String UPDATE_PASSWORD_SUCCESSFULLY = "Update Password Successfully!";
    public static final String LOGIN_SUCCESSFULLY = "Login Successfully!";
    public static final String ACCOUNT_VERIFIED_SUCCESSFULLY = "Account Verified Successfully!";
    public static final String ACCOUNT_IS_VERIFIED = "Account Is Verified!";
    public static final String ACCOUNT_NOT_VERIFIED = "Account Is Not Verified!";
    public static final String REFRESH_TOKEN_SUCCESSFULLY = "Refresh Token Successfully!";
    public static final String RESEND_MAIL_NEW_TOKEN_SUCCESSFULLY = "Resend Mail New Token Successfully";

    /*    FAIL     */
    public static final String DUPLICATE_REGISTERED_EMAIL = "Duplicate Registered Email!";
    public static final String INVALID_VERIFICATION_TOKEN = "Invalid Verification Token!";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid Email Or Password!";
    public static final String EMAIL_IS_NOT_EXISTED = "Email is not existed!";
    public static final String PHONE_NUMBER_IS_EXISTED = "Phone Number is existed!";
    public static final String UPDATE_PASSWORD_FAILED = "Failed To Update Password!";
    public static final String USER_IS_NOT_FOUND = "User Is Not Found";
    public static final String REGISTER_NEW_USER_FAILED = "Failed To Register New User!";
    public static final String REFRESH_TOKEN_FAILED = "Failed To Refresh Token!";
    public static final String TOKEN_ALREADY_EXPIRED = "Token already expired!";
    public static final String TOKEN_IS_VALID = "Token is Valid!";

    /**
     * CUSTOMER
     */
    /*    SUCCESS     */
    public static final String UPDATE_PROFILE_CUSTOMER_SUCCESSFULLY = "Update Profile Customer Successfully!";

    /*    FAIL     */
    public static final String CUSTOMER_NOT_FOUND = "Customer not found!";
    public static final String UPDATE_PROFILE_CUSTOMER_FAIL = "Update Profile Customer Fail!";

    /**
     * MATERIAL
     */
    /*    SUCCESS     */
    public static final String GET_ALL_MATERIAL_SUCCESSFULLY = "Get All Material Successfully!";
    public static final String CAN_NOT_FIND_ANY_MATERIAL = "Can Not Find Any Material!";
    public static final String ADD_NEW_MATERIAL_SUCCESSFULLY = "Add New Material Successfully!";
    public static final String ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Category And Material By Excel File Successfully!";
    public static final String EXPORT_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Export Category And Material By Excel File Successfully!";
    public static final String UPDATE_MATERIAL_SUCCESSFULLY = "Update Material Successfully";
    public static final String GET_MATERIAL_BY_ID_SUCCESSFULLY = "Get Material By ID Successfully";
    public static final String GET_MATERIAL_BY_NAME_SUCCESSFULLY = "Get Material By Name Successfully";
    public static final String GET_LIST_MATERIAL_BY_CATEGORY_ID_SUCCESSFULLY = "Get List Material By Category ID Successfully";
    public static final String GET_LIST_MATERIAL_BY_EXPERT_TAILORING_ID_AND_CATEGORY_ID_SUCCESSFULLY = "Get List Material By Expert Tailoring ID And Category ID Successfully";
    public static final String GET_LIST_MATERIAL_BY_CATEGORY_NAME_SUCCESSFULLY = "Get List Material By Category Name Successfully";
    public static final String CHANGE_STATUS_MATERIAL_SUCCESSFULLY = "Change Status Material Successfully";
    public static final String GENERATE_SAMPLE_CATEGORY_MATERIAL_SUCCESSFULLY = "Generate Sample Category Material Successfully!";

    /*    FAIL     */
    public static final String ADD_NEW_MATERIAL_FAIL = "Add New Material Fail!";
    public static final String MATERIAL_IS_EXISTED = "Material is Existed";
    public static final String WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE = "Wrong Type Of Category And Material Excel File!";
    public static final String DUPLICATE_CATEGORY_AND_MATERIAL_IN_EXCEL_FILE = "Duplicate Category And Material in Excel File!";
    public static final String ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_FAIL = "Add New Category And Material By Excel File Fail!";
    public static final String CATEGORY_AND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA = "Category and Material Excel File has Empty Data";
    public static final String CATEGORY_AND_MATERIAL_IS_NOT_EXISTED = "Material is not Existed";

    /**
     * BRAND MATERIAL
     */
    /*    SUCCESS     */
    public static final String GET_ALL_BRAND_MATERIAL_SUCCESSFULLY = "Get All Brand Material Successfully!";
    public static final String CAN_NOT_FIND_ANY_BRAND_MATERIAL = "Can Not Find Any Brand Material!";
    public static final String ADD_NEW_BRAND_MATERIAL_SUCCESSFULLY = "Add New Brand Material Successfully!";
    public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Brand Material By Excel File Successfully!";
    public static final String UPDATE_BRAND_MATERIAL_SUCCESSFULLY = "Update Brand Material Successfully!";

    /*    FAIL     */
    public static final String ADD_NEW_BRAND_MATERIAL_FAIL = "Add New Brand Material Fail!";
    public static final String ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_FAIL = "Add New Brand Material By Excel File Fail!";
    public static final String BRAND_MATERIAL_IS_EXISTED = "Brand Material is Existed";
    public static final String BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION = "Brand Price Must be Between Base Price Multiple With Percentage Fluctuation";
    public static final String BRAND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA = "Brand Material Excel File has Empty Data";
    public static final String DUPLICATE_BRAND_MATERIAL_IN_EXCEL_FILE = "Duplicate Brand Material in Excel File!";
    public static final String WRONG_TYPE_OF_BRAND_MATERIAL_EXCEL_FILE = "Wrong Type Of Brand Material Excel File!";
    /**
     * EXPERT TAILORING
     */
    /*    SUCCESS     */
    public static final String ADD_NEW_EXPERT_TAILORING_SUCCESSFULLY = "Add New Expert Tailoring Successfully!";
    public static final String GET_EXPERT_TAILORING_BY_NAME_SUCCESSFULLY = "Get Expert Tailoring By Name Successfully!";
    public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Expert Tailoring By Excel File Successfully!";
    public static final String ADD_BRAND_EXPERT_TAILORING_SUCCESSFULLY = "Add Expert Tailoring For Brand Successfully!";
    public static final String GET_ALL_EXPERT_TAILORING_SUCCESSFULLY = "Get All Expert Tailoring Successfully!";
    public static final String GENERATE_SAMPLE_EXPERT_TAILORING_SUCCESSFULLY = "Generate Sample Expert Tailoring Successfully!";
    public static final String UPDATE_EXPERT_TAILORING_SUCCESSFULLY = "Update Expert Tailoring Successfully";
    public static final String GET_EXPERT_TAILORING_BY_ID_SUCCESSFULLY = "Get Expert Tailoring By ID Successfully";
    public static final String CHANGE_STATUS_EXPERT_TAILORING_SUCCESSFULLY = "Change Status Expert Tailoring Successfully";
    /*    FAIL     */
    public static final String ADD_NEW_EXPERT_TAILORING_FAIL = "Add New Expert Tailoring Fail!";
    public static final String CAN_NOT_FIND_ANY_EXPERT_TAILORING = "Can Not Find Any Expert Tailoring!";
    public static final String ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_FAIL = "Add New Expert Tailoring By Excel File Fail!";
    public static final String EXPERT_TAILORING_IS_EXISTED = "Expert Tailoring is Existed";
    public static final String EXPERT_TAILORING_NAME_IS_EXISTED = "Expert Tailoring Name is Existed";
    public static final String DUPLICATE_EXPERT_TAILORING_DATA = "Duplicate Expert Tailoring Data";
    public static final String DUPLICATE_EXPERT_TAILORING_IN_EXCEL_FILE = "Duplicate Expert Tailoring in Excel File!";
    public static final String WRONG_TYPE_OF_EXPERT_TAILORING_EXCEL_FILE = "Wrong Type Of Expert Tailoring Excel File!";
    public static final String EXPERT_TAILORING_EXCEL_FILE_HAS_EMPTY_DATA = "Expert Tailoring Excel File has Empty Data";
    /**
     * NOTIFICATION
     */
    public static final String SEND_NOTIFICATION_SUCCESSFULLY = "Notification sent successfully!";
    public static final String GET_NOTIFICATION_SUCCESSFULLY = "Get Notification successfully!";
    public static final String UPDATE_NOTIFICATION_STATUS_SUCCESSFULLY = "Update Notification Status Successfully!";
    public static final String MARK_ALL_READ = "Mark All Read successfully!";
    public static final String SEND_NOTIFICATION_FAIL = "Fail To Send Notification!";

    /**
     * CATEGORY
     */
    /*    SUCCESS  */
    public static final String GET_ALL_CATEGORY_SUCCESSFULLY = "Get All Category Successfully!";
    public static final String GET_CATEGORY_BY_ID_SUCCESSFULLY = "Get Category By ID Successfully!";
    public static final String ADD_NEW_CATEGORY_SUCCESSFULLY = "Add New Category Successfully!";
    public static final String UPDATE_CATEGORY_SUCCESSFULLY = "Update Category Successfully!";
    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_CATEGORY = "Can Not Find Any Category!";
    public static final String ADD_NEW_CATEGORY_FAIL = "Add New Category Fail!";
    public static final String CATEGORY_IS_EXISTED = "Category is Existed";
    public static final String CATEGORY_NAME_IS_EXISTED = "Category Name is Existed";

    /**
     * DESIGN
     */
    /*    SUCCESS  */
    public static final String ADD_NEW_DESIGN_SUCCESSFULLY = "Add New Design Successfully!";
    public static final String ADD_NEW_CLONE_DESIGN_SUCCESSFULLY = "Add New Clone Design Successfully!";
    public static final String GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY = "Get All Design By User ID Successfully!";
    public static final String GET_ALL_DESIGN_SUCCESSFULLY = "Get All Design Successfully!";
    public static final String GET_DESIGN_BY_ID_SUCCESSFULLY = "Get Design By ID Successfully!";
    public static final String GET_ALL_DESIGN_BY_CUSTOMER_ID_SUCCESSFULLY = "Get All Design By Customer ID Successfully!";
    public static final String GET_ALL_DESIGN_BY_BRAND_ID_SUCCESSFULLY = "Get All Design By Brand ID Successfully!";
    public static final String UPDATE_PUBLIC_STATUS_SUCCESSFULLY = "Update Public Status Successfully";
    public static final String UPDATE_DESIGN_SUCCESSFULLY = "Update Design Successfully";

    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_DESIGN_BY_USER_ID = "Can Not Find Any Design By User ID";
    public static final String CAN_NOT_FIND_ANY_DESIGN = "Can Not Find Any Design";
    public static final String CAN_NOT_FIND_ANY_DESIGN_BY_BRAND_ID = "Can Not Find Any Design By Brand ID";

    /**
     * PART OF DESIGN
     */
    /*    SUCCESS  */
    public static final String ADD_PART_OF_DESIGN_SUCCESSFULLY = "Add New Part Of Design Successfully!";
    public static final String GET_ALL_PART_OF_DESIGN_BY_DESIGN_ID_SUCCESSFULLY = "Get All Part Of Design By Design ID Successfully!";
    public static final String GET_ALL_PART_OF_DESIGN_SUCCESSFULLY = "Get All Part Of Design Successfully!";
    public static final String GET_PART_OF_DESIGN_BY_ID_SUCCESSFULLY = "Get Part Of Design By ID Successfully!";

    /*    FAIL     */
    public static final String ADD_PART_OF_DESIGN_FAIL = "Add New Part Of Design Fail!";
    public static final String PART_OF_DESIGN_LIST_REQUEST_IS_EMPTY = "Part Of Design List Request is Empty";
    public static final String CAN_NOT_FIND_ANY_PART_OF_DESIGN_BY_DESIGN_ID = "Can Not Find Any Part Of Design By Design ID";
    public static final String CAN_NOT_FIND_ANY_PART_OF_DESIGN = "Can Not Find Any Part Of Design";

    /**
     * ITEM MASK
     */
    /*    SUCCESS  */
    public static final String ADD_ITEM_MASK_SUCCESSFULLY = "Add New Item Mask Successfully!";
    public static final String GET_ALL_ITEM_MASK_BY_PART_OF_DESIGN_ID_SUCCESSFULLY = "Get All Item Mask By Part Of Design ID Successfully!";
    public static final String GET_ALL_ITEM_MASK_SUCCESSFULLY = "Get All Item Mask Successfully!";
    public static final String GET_ITEM_MASK_BY_ID_SUCCESSFULLY = "Get Item Mask By ID Successfully!";

    /*    FAIL     */
    public static final String ADD_ITEM_MASK_FAIL = "Add New Part Of Design Fail!";
    public static final String ITEM_MASK_LIST_REQUEST_IS_EMPTY = "Item Mask List Request is Empty";
    public static final String CAN_NOT_FIND_ANY_ITEM_MASK_BY_PART_OF_DESIGN_ID = "Can Not Find Any Item Mask By Part Of Design ID";
    public static final String CAN_NOT_FIND_ANY_ITEM_MASK = "Can Not Find Any Item Mask";

    /**
     * LABOR QUANTITY
     */
    /*    SUCCESS  */
    public static final String ADD_LABOR_QUANTITY_SUCCESSFULLY = "Add New Labor Quantity Successfully!";
    public static final String GET_ALL_LABOR_QUANTITY_SUCCESSFULLY = "Get All Labor Quantity Successfully!";
    public static final String UPDATE_LABOR_QUANTITY_SUCCESSFULLY = "Update Labor Quantity Successfully!";

    /*    FAIL     */
    public static final String LABOR_QUANTITY_IS_EXISTED = "Labor Quantity is Existed";
    public static final String CAN_NOT_FIND_ANY_LABOR_QUANTITY = "Can Not Find Any Labor Quantity";

    /**
     * BRAND LABOR QUANTITY
     */
    /*    SUCCESS  */
    public static final String ADD_BRAND_LABOR_QUANTITY_SUCCESSFULLY = "Add New Brand Labor Quantity Successfully!";
    public static final String GET_BRAND_LABOR_QUANTITY_BY_BRAND_ID_SUCCESSFULLY = "Get Brand Labor Quantity By Brand ID Successfully!";

    /*    FAIL     */
    public static final String BRAND_LABOR_QUANTITY_IS_EXISTED = "Brand Labor Quantity is Existed";
    public static final String CAN_NOT_FIND_ANY_BRAND_LABOR_QUANTITY = "Can Not Find Any Brand Labor Quantity";
    public static final String CAN_NOT_FIND_ANY_BRAND_LABOR_QUANTITY_BY_BRAND_ID = "Can Not Find Any Brand Labor Quantity By Brand ID";

    /**
     * USER
     */
    /*    SUCCESS  */
    public static final String GET_ALL_CUSTOMER_SUCCESSFULLY = "Get All Customer Successfully";
    public static final String GET_ALL_BRAND_SUCCESSFULLY = "Get All Brand Successfully";
    public static final String GET_ALL_ACCOUNTANT_SUCCESSFULLY = "Get All Accountant Successfully";
    public static final String GET_ALL_EMPLOYEE_SUCCESSFULLY = "Get All Employee Successfully";
    public static final String GET_ALL_MANAGER_SUCCESSFULLY = "Get All Manager Successfully";
    public static final String GET_ALL_ADMIN_SUCCESSFULLY = "Get All Admin Successfully";

    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_CUSTOMER = "Can not find any customer";
    public static final String CAN_NOT_FIND_ANY_BRAND = "Can not find any brand";
    public static final String CAN_NOT_FIND_ANY_ACCOUNTANT = "Can not find any accountant";
    public static final String CAN_NOT_FIND_ANY_EMPLOYEE = "Can not find any employee";
    public static final String CAN_NOT_FIND_ANY_MANAGER = "Can not find any manager";
    public static final String CAN_NOT_FIND_ANY_ADMIN = "Can not find any admin";

    /**
     * SIZE
     */
    /*    SUCCESS  */
    public static final String ADD_SIZE_SUCCESSFULLY = "Add New Size Successfully!";
    public static final String GET_ALL_SIZE_SUCCESSFULLY = "Get All Size Successfully!";
    public static final String UPDATE_SIZE_SUCCESSFULLY = "Update Size Successfully!";

    /*    FAIL     */
    public static final String SIZE_IS_EXISTED = "Size is Existed";
    public static final String CAN_NOT_FIND_ANY_SIZE = "Can Not Find Any Size";

    /**
     * SIZE_EXPERT_TAILORING
     */
    /*    SUCCESS  */
    public static final String ADD_SIZE_EXPERT_TAILORING_SUCCESSFULLY = "Add New Size Expert Tailoring Successfully!";
    public static final String ADD_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Size Expert Tailoring By Excel File Successfully!";
    public static final String GET_ALL_SIZE_EXPERT_TAILORING_SUCCESSFULLY = "Get All Size Expert Tailoring Successfully!";
    public static final String UPDATE_SIZE_EXPERT_TAILORING_SUCCESSFULLY = "Update Size Expert Tailoring Successfully!";
    public static final String GENERATE_SAMPLE_SIZE_EXPERT_TAILORING_SUCCESSFULLY = "Generate Sample Size Expert Tailoring Successfully!";

    /*    FAIL     */
    public static final String ADD_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE_FAIL = "Add New Size Expert Tailoring By Excel File Fail!";
    public static final String SIZE_EXPERT_TAILORING_IS_EXISTED = "Size Expert Tailoring is Existed";
    public static final String CAN_NOT_FIND_ANY_SIZE_EXPERT_TAILORING = "Can Not Find Any Size Expert Tailoring";

    /**
     * DESIGN DETAIL
     */
    /*    SUCCESS  */
    public static final String ADD_NEW_DESIGN_DETAIL_SUCCESSFULLY = "Add New Design Detail Successfully!";
    public static final String GET_ALL_DESIGN_DETAIL_BY_ORDER_ID_SUCCESSFULLY = "Get All Design Detail By Order ID Successfully!";
    public static final String GET_ALL_DESIGN_DETAIL_SUCCESSFULLY = "Get All Design Detail Successfully!";
    public static final String GET_DESIGN_DETAIL_BY_ID_SUCCESSFULLY = "Get Design Detail By ID Successfully!";
    public static final String GET_ALL_DESIGN_DETAIL_BY_CUSTOMER_ID_SUCCESSFULLY = "Get All Design Detail By Customer ID Successfully!";
    public static final String GET_ALL_DESIGN_DETAIL_BY_BRAND_ID_SUCCESSFULLY = "Get All Design Detail By Brand ID Successfully!";
    public static final String UPDATE_DESIGN_DETAIL_PUBLIC_STATUS_SUCCESSFULLY = "Update Public Status Successfully";

    /*    FAIL     */
    public static final String ADD_NEW_DESIGN_DETAIL_FAIL = "Add New Design Detail Fail!";
    public static final String GET_ALL_DESIGN_DETAIL_FAIL = "Get All Design Detail Fail!";
    public static final String CAN_NOT_FIND_ANY_DESIGN_DETAIL_BY_USER_ID = "Can Not Find Any Design Detail By User ID";
    public static final String CAN_NOT_FIND_ANY_DESIGN_DETAIL = "Can Not Find Any Design Detail";
    public static final String UPDATE_DESIGN_DETAIL_PUBLIC_STATUS_FAIL = "Update Public Status Fail";

    /**
     * ORDER
     */
    public static final String CREATE_ORDER_SUCCESSFULLY = "Create Order Successfully";
    public static final String GET_ORDER_SUCCESSFULLY = "Get Order Successfully";
    public static final String GET_ORDER_STAGE_SUCCESSFULLY = "Get Order Successfully";
    public static final String CHANGE_ORDER_STATUS_SUCCESSFULLY = "Change Order Status Successfully";
    public static final String BRAND_PICK_ORDER_SUCCESSFULLY = "Brand Picked Order Successfully";
    public static final String RATING_ORDER_SUCCESSFULLY = "Rating Order Successfully";

    /**
     * System Image
     */
    public static final String GET_ALL_SYSTEM_IMAGE_SUCCESSFULLY = "Get All System Image Successfully!";
    public static final String ADD_NEW_SYSTEM_IMAGE_SUCCESSFULLY = "Add New System Image Successfully!";
    public static final String GET_SYSTEM_IMAGE_SUCCESSFULLY = "Get System Image Successfully!";
    public static final String CAN_NOT_FIND_ANY_SYSTEM_IMAGE = "Can not find any System Image";
    public static final String ADD_NEW_SYSTEM_IMAGE_FAIL = "Fail To Add New System Image";

    /**
     * Expert Tailoring Material
     */
    /*    SUCCESS  */
    public static final String ADD_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY = "Add New Expert Tailoring Material Successfully!";
    public static final String ADD_EXPERT_TAILORING_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY = "Add New Expert Tailoring Material By Excel File Successfully!";
    public static final String GET_ALL_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY = "Get All Expert Tailoring Material Successfully!";
    public static final String GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_ID_SUCCESSFULLY = "Get All Expert Tailoring Material By Expert Tailoring ID Successfully!";
    public static final String GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_NAME = "Get All Expert Tailoring Material By Expert Tailoring Name Successfully!";
    public static final String UPDATE_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY = "Update Expert Tailoring Material Successfully!";
    public static final String GENERATE_SAMPLE_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY = "Generate Sample Expert Tailoring Material Successfully!";

    /*    FAIL     */
    public static final String ADD_EXPERT_TAILORING_MATERIAL_BY_EXCEL_FILE_FAIL = "Add New Expert Tailoring Material By Excel File Fail!";
    public static final String EXPERT_TAILORING_MATERIAL_IS_EXISTED = "Expert Tailoring Material is Existed";
    public static final String CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL = "Can Not Find Any Expert Tailoring Material";
    public static final String WRONG_TYPE_OF_EXPERT_TAILORING_MATERIAL_EXCEL_FILE = "Wrong Type Of Expert Tailoring Material Excel File!";
    public static final String DUPLICATE_EXPERT_TAILORING_MATERIAL_IN_EXCEL_FILE = "Duplicate Expert Tailoring Material in Excel File!";

    /**
     * System Property
     */
    public static final String GET_ALL_SYSTEM_PROPERTY_SUCCESSFULLY = "Get All System Properties Successfully!";
    public static final String GET_SYSTEM_PROPERTY_SUCCESSFULLY = "Get System Properties Successfully!";
    public static final String ADD_NEW_SYSTEM_PROPERTY_SUCCESSFULLY = "Add New System Property Successfully!";
    public static final String UPDATE_SYSTEM_PROPERTY_SUCCESSFULLY = "Update System Property Successfully!";
    public static final String CAN_NOT_FIND_SYSTEM_PROPERTY = "Can Not Find System Property";

    /**
     * Brand Property
     */
    public static final String GET_ALL_BRAND_PROPERTY_SUCCESSFULLY = "Get All Brand Properties Successfully!";
    public static final String GET_BRAND_PROPERTY_SUCCESSFULLY = "Get Brand Properties Successfully!";
    public static final String ADD_NEW_BRAND_PROPERTY_SUCCESSFULLY = "Add New Brand Property Successfully!";
    public static final String CAN_NOT_FIND_BRAND_PROPERTY = "Can Not Find Brand Property";

    /**
     * Payment
     */
    public static final String GET_PAYMENT_SUCCESSFULLY = "Get Payment Info Successfully!";
    public static final String CREATE_PAYMENT_SUCCESSFULLY = "Create Payment Successfully!";
    public static final String CONFIRM_PAYMENT_SUCCESSFULLY = "Confirm Payment Successfully!";

    /**
     * Report
     */
    /*    SUCCESS  */
    public static final String CREATE_REPORT_SUCCESSFULLY = "Create Report Successfully!";
    public static final String GET_ALL_REPORT_SUCCESSFULLY = "Get All Report Successfully!";
    public static final String GET_ALL_REPORT_BY_ORDER_ID_SUCCESSFULLY = "Get All Report By Order ID Successfully!";
    public static final String GET_ALL_REPORT_BY_PARENT_ORDER_ID_SUCCESSFULLY = "Get All Report By Parent Order ID Successfully!";
    public static final String GET_ALL_REPORT_BY_USER_ID_SUCCESSFULLY = "Get All Report By User ID Successfully!";
    public static final String GET_ALL_REPORT_BY_BRAND_ID_SUCCESSFULLY = "Get All Report By Brand ID Successfully!";

    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_REPORT = "Can not find Any Report";

    /**
     * SAMPLE PRODUCT DATA
     */
    public static final String GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY = "Get Sample Product Data Successfully!";
    public static final String ADD_SAMPLE_PRODUCT_DATA_SUCCESSFULLY = "Add Sample Product Data Successfully!";
    public static final String UPDATE_SAMPLE_PRODUCT_DATA_SUCCESSFULLY = "Update Sample Product Data Successfully!";

    /*    FAIL     */
    public static final String CAN_NOT_FIND_ANY_SAMPLE_PRODUCT_DATA = "Can not find Any Sample Product Data";
}
