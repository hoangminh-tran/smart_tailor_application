package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.BrandAPI;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.event.RegistrationCompleteEvent;
import com.smart.tailor.mapper.BrandMapper;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandExpertTailoringRequest;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(BrandAPI.BRAND)
@RequiredArgsConstructor
@Validated
public class BrandController {
    private final Logger logger = LoggerFactory.getLogger(BrandController.class);
    private final BrandService brandService;
    private final UserService userService;
    private final BrandMapper brandMapper;
    private final BrandExpertTailoringService brandExpertTailoringService;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotificationService notificationService;

    @GetMapping(BrandAPI.GET_BRAND + "/{id}")
    public ResponseEntity<ObjectNode> getBrandById(@PathVariable("id") String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            if (id == null) {
                response.put("status", 400);
                response.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(response);
            }
            Optional<Brand> brand = brandService.getBrandById(id);
            if (brand.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND + " with id: " + id);
                return ResponseEntity.ok(response);
            }
            response.put("status", 200);
            response.put("message", MessageConstant.GET_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(brand.get())));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET BRAND BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(BrandAPI.UPLOAD_BRAND_INFOR + "/{brandID}")
    public ResponseEntity<ObjectNode> uploadBrandInfor(@PathVariable("brandID") String brandID, @RequestBody BrandRequest brandRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            if (brandRequest == null) {
                respon.put("status", 401);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            if (brandRequest.getBankName() == null || !Utilities.isNonNullOrEmpty(brandRequest.getBrandName())) {
                respon.put("status", 401);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            var brand = brandService.saveBrand(brandID, brandRequest);

            if (brand != null) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.REGISTER_NEW_BRAND_SUCCESSFULLY);
                respon.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(brand)));
//                NotificationRequest request = NotificationRequest.builder().sender(brand.getUser().getEmail()).type("BRAND REGISTRATION").message(MessageConstant.NEW_BRAND_REGISTERED).recipient("smarttailor.ma@gmail.com").build();
//                notificationService.sendPrivateNotification(request);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REGISTER_NEW_BRAND_FAILED);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN UPLOAD BRAND INFOR. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(BrandAPI.GET_BRAND_REGISTRATION_PAYMENT + "/{brandID}")
    public ResponseEntity<ObjectNode> getBrandRegistrationPayment(@PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var brand = brandService.findBrandById(brandID);

            if (brand == null) {
                respon.put("status", ErrorConstant.INVALID_ID.getStatusCode());
                respon.put("message", ErrorConstant.INVALID_ID.getMessage());
                return ResponseEntity.ok(respon);
            }


        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET BRAND REGISTRATION PAYMENT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
        return null;
    }

    @PostMapping(BrandAPI.ADD_NEW_BRAND)
    public ResponseEntity<ObjectNode> addNewBrand(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            // Check if enough argument?
            if (userRequest == null || userRequest.getEmail() == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            String email = userRequest.getEmail();
            String password = userRequest.getPassword();

            // Check email is valid?
            if (!Utilities.isValidEmail(email)) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.INVALID_EMAIL);
                return ResponseEntity.ok(respon);
            }

            // Check email is not verify?
            if (userService.getUserByEmail(userRequest.getEmail()) != null) {
                if (userService.getUserByEmail(userRequest.getEmail()).getUserStatus().equals(UserStatus.INACTIVE)) {
                    respon.put("status", ErrorConstant.ACCOUNT_NOT_VERIFIED.getStatusCode());
                    respon.put("message", ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
                    return ResponseEntity.ok(respon);
                }
            }

            // Check password is valid? Only check when it's not google registration
            if (userRequest.getProvider() != Provider.GOOGLE) {
                if (!Utilities.isValidPassword(password)) {
                    respon.put("status", 400);
                    respon.put("message", MessageConstant.INVALID_PASSWORD);
                    return ResponseEntity.ok(respon);
                }
            }

            // Check email is duplicated?
            if (userService.getUserByEmail(userRequest.getEmail()) != null) {
                respon.put("status", 409);
                respon.put("message", MessageConstant.DUPLICATE_REGISTERED_EMAIL);
                return ResponseEntity.ok(respon);
            }

            var authenResponse = authenticationService.register(userRequest);
            var registeredUser = userService.getUserByEmail(authenResponse.getUser().getEmail());

            if (registeredUser == null) {
                respon.put("status", ErrorConstant.REGISTER_NEW_USER_FAILED.getStatusCode());
                respon.put("message", ErrorConstant.REGISTER_NEW_USER_FAILED.getMessage());
                return ResponseEntity.ok(respon);
            }

            if (registeredUser.getProvider().equals(Provider.LOCAL)) {
                applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(registeredUser, TypeOfVerification.VERIFY_ACCOUNT));
                logger.info("Publish Event When Register By Local Successfully");
                respon.put("message", MessageConstant.SEND_MAIL_FOR_VERIFY_ACCOUNT_SUCCESSFULLY);
            } else {
                respon.put("message", MessageConstant.REGISTER_NEW_BRAND_SUCCESSFULLY);
            }
            respon.put("status", 200);
            respon.set("data", objectMapper.valueToTree(authenResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN REGISTER BRAND. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(BrandAPI.ADD_EXPERT_TAILORING_FOR_BRAND)
    public ResponseEntity<ObjectNode> addExpertTailoringForBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                 @Valid @RequestBody BrandExpertTailoringRequest brandExpertTailoringRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        brandExpertTailoringService.createExpertTailoringForBrand(jwtToken, brandExpertTailoringRequest);
        response.put("status", 200);
        response.put("message", MessageConstant.ADD_BRAND_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandAPI.GET_ALL_EXPERT_TAILORING_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllBrandExpertTailoringByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                          @PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        response.put("status", 200);
        response.put("message", "Get All Brand Expert Tailoring By Brand ID Successfully");
        response.set("data", objectMapper.valueToTree(brandExpertTailoringService.getAllBrandExpertTailoringByBrandID(jwtToken, brandID)));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandAPI.ACCEPT_BRAND + "/{brandID}")
    public ResponseEntity<ObjectNode> acceptBrand(@PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            Optional<Brand> findedBrand = brandService.getBrandById(brandID);

            if (findedBrand.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND + " with infor: " + brandID);
                return ResponseEntity.ok(response);
            }

            findedBrand.get().setBrandStatus(BrandStatus.ACCEPT);
            brandService.updateBrand(findedBrand.get());

            response.put("status", 200);
            response.put("message", MessageConstant.ACCEPT_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(findedBrand.get())));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN ACCEPT BRAND. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    //    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(BrandAPI.REJECT_BRAND + "/{brandID}")
    public ResponseEntity<ObjectNode> rejectBrand(@PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            Optional<Brand> findedBrand = brandService.getBrandById(brandID);

            if (findedBrand.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND + " with infor: " + brandID);
                return ResponseEntity.ok(response);
            }

            findedBrand.get().setBrandStatus(BrandStatus.REJECT);
            brandService.updateBrand(findedBrand.get());

            response.put("status", 200);
            response.put("message", MessageConstant.REJECT_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandMapper.mapperToBrandResponse(findedBrand.get())));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN REJECT BRAND. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(BrandAPI.GET_BRAND_INFORMATION_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> findBrandInformationByBrandID( @PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var brand = brandService.findBrandInformationByBrandID(brandID);
            if (brand == null) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND + " with brandID: " + brandID);
                return ResponseEntity.ok(response);
            }
            response.put("status", 200);
            response.put("message", MessageConstant.GET_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brand));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET BRAND BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(BrandAPI.CHANGE_IMAGE_STATUS + "/{imageId}")
    public ResponseEntity<ObjectNode> changeImageStatus(
            @PathVariable("imageId") String imageId) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            boolean result = brandService.changeBrandImageStatus(imageId);
            if (result) {
                response.put("status", 200);
                response.put("message", MessageConstant.CHANGE_IMAGE_STATUS_SUCCESS);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", MessageConstant.RESOURCE_NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("LỖI KHI THAY ĐỔI TRẠNG THÁI HÌNH ẢNH. THÔNG TIN LỖI: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(BrandAPI.GET_ALL_BRAND_INFORMATION)
    public ResponseEntity<ObjectNode> getAllBrandInformation() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var brandResponseList = brandService.getAllBrandInformation();
            if (brandResponseList.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND);
                return ResponseEntity.ok(response);
            }
            response.put("status", 200);
            response.put("message", MessageConstant.GET_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandResponseList));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET BRAND BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
