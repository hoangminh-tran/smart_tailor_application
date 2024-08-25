use smart_tailor_be;

-- INSERT INTO ROLES
INSERT INTO roles(role_id, create_date, last_modified_date, role_name)
VALUES (GenerateCustomKeyString(), current_timestamp, NULL, 'CUSTOMER'),
       (GenerateCustomKeyString(), current_timestamp, NULL, 'ADMIN'),
       (GenerateCustomKeyString(), current_timestamp, NULL, 'MANAGER'),
       (GenerateCustomKeyString(), current_timestamp, NULL, 'EMPLOYEE'),
       (GenerateCustomKeyString(), current_timestamp, NULL, 'ACCOUNTANT'),
       (GenerateCustomKeyString(), current_timestamp, NULL, 'BRAND');


-- INSERT INTO SYSTEM PROPERTIES
INSERT INTO system_properties(property_id, create_date, last_modified_date, property_detail, property_name,
                              property_type, property_status, property_unit, property_value)
VALUES (GenerateCustomKeyString(), current_timestamp, null,
        'The period within which customers can cancel their order.', 'CANCELLATION_TIME', 'DURATION', true, 'MINUTES',
        '150'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The duration required to find a suitable option for the order.', 'MATCHING_TIME', 'DURATION', true, 'MINUTES',
        '120'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The cost associated with the brand, specified in a particular currency.', 'BRAND_COST', 'COST', true, 'VND',
        ''),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The percent for deposit.', 'DEPOSIT_PERCENT', 'PERCENT', true, '%',
        '50'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The product can produce in one day.', 'BRAND_PRODUCTIVITY', 'PIECE', true, 'PIECE',
        ''),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The number for divide.', 'DIVIDE_NUMBER', 'NUMBER', true, 'INT',
        '100'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The rate for Brand have Order Status ahead of schedule.', 'RATE_AHEAD_SCHEDULE', 'NUMBER', true, 'FLOAT',
        '0.25'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The rate for Brand have Order Status late of schedule.', 'RATE_LATE_SCHEDULE', 'NUMBER', true, 'FLOAT',
        '0.75'),
       (GenerateCustomKeyString(), current_timestamp, null, 'The duration for which an unverified account can exist before being deleted. Once this time expires, the account will be removed.',
        'TIME_BEFORE_ACCOUNT_DELETION', 'NUMBER', true, 'INT','12'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The percentage range within which the price for Material can vary. Prices outside this range will not be accepted for Material pricing.',
        'PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL', 'NUMBER', true, 'FLOAT', '0.08'),
        (GenerateCustomKeyString(), current_timestamp, null,
        'The period within which customers can verify their email address.', 'EMAIL_VERIFICATION_TIME', 'DURATION', true, 'MINUTE',
        '30'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'The maximum shipping weight for an order is 20 kg', 'MAX_SHIPPING_WEIGHT', 'WEIGHT', true, 'KG', '20'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'Charge a 5% fee on each Order.', 'ORDER_FEE_PERCENTAGE', 'PERCENTAGE', true, 'PERCENT', '5'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'Rating reduction if order is canceled before it starts.', 'RATING_REDUCTION_BEFORE_START', 'FLOAT', true, 'SCORE',
        '0.2'),
       (GenerateCustomKeyString(), current_timestamp, null,
        'Rating reduction if order is canceled after it starts.', 'RATING_REDUCTION_AFTER_START', 'FLOAT', true, 'SCORE',
        '0.8'),
        (GenerateCustomKeyString(), current_timestamp, null,
        'The actual pixel ratio is 4 times of the ratio of pixels on the web screen', 'PIXEL_RATIO_REAL_FROM_WEB',
        'FLOAT', true, 'RATIO', '4'),
        (GenerateCustomKeyString(), current_timestamp, null, 'The address of Smart Tailor Company', 'SMART_TAILOR_ADDRESS',
         'STRING', true, 'ADDRESS_LINE', '344 Lê Văn Việt'),
        (GenerateCustomKeyString(), current_timestamp, null, 'The ward of Smart Tailor Company', 'SMART_TAILOR_WARD',
         'STRING', true, 'WARD', 'Tăng Nhơn Phú B'),
        (GenerateCustomKeyString(), current_timestamp, null, 'The district of Smart Tailor Company', 'SMART_TAILOR_DISTRICT',
        'STRING', true, 'DISTRICT', 'Thủ Đức'),
        (GenerateCustomKeyString(), current_timestamp, null, 'The province of Smart Tailor Company', 'SMART_TAILOR_PROVINCE',
        'STRING', true, 'PROVINCE', 'Hồ Chí Minh'),
         (GenerateCustomKeyString(), current_timestamp, null, 'The conversion ratio from pixel to centimeters', 'PIXEL_TO_CENTIMETER',
        'DOUBLE', true, 'CENTIMETER', '0.02645833'),
        (GenerateCustomKeyString(), current_timestamp, null,
        '.', 'FEE_CANCEL_IN_DURATION', 'PERCENTAGE', true, 'PERCENT',
        '20');

-- INSERT INTO SYSTEM IMAGE
INSERT INTO system_image (image_id, image_name, image_url, image_status, image_type, is_premium, create_date, last_modified_date)
VALUES
    (GenerateCustomKeyString(), 'Happy Sad White', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1721382631/system-item/zldstwj0wsqw7kmaqxb0.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 1', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655342/system-item/wzfriiaq2bewvdcwhaif.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 2', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655341/system-item/hmcdufrdfgrq44ichayo.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 3', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655341/system-item/j3vt66lzvbxicytni4hw.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 4', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655341/system-item/invp5ppwq5tbthekgvb0.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 5', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655341/system-item/pperfml5canvfuykivbu.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 6', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655341/system-item/zhueteuwqvlr4bupdfo2.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 7', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1718867955/system-item/vbti4tliu6jwdcoxfuwi.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 8', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1718867955/system-item/d3up2zkfspqvo8qp8aom.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 9', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1717753160/system-item/fykmp5kljkkxhbjss3xb.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 10', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/jfmqrt52lhvkwhgtjdiv.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 11', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/edzpgtvovhhluezeq3uw.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 12', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655541/system-item/oq5nhwpjspgkq2kmu3lz.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 13', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/gfw7tfu0ivivstyrbtid.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 14', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/zjqt1sjyni4thawsbhqn.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 15', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/rh5epcpcn26dlyurjbkx.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 16', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/iit4iulitnij2miwmurs.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 17', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655540/system-item/ws7ptupntw1gejnrlqsk.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 18', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723655539/system-item/pugkxpp0caqmstwpyrnp.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 19', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/tb3vwtki6junca29mrjh.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 20', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/qngirnpxpju1rpyruisi.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 21', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734595/rbvm7hkx7ufaque7tbq0.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 22', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/opts7jwlr5g9hwdntacz.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 23', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/i3rwbqqxdw2of2bwr1vu.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 24', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/qmq4ax9doptkwkhu7kxp.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 25', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734596/aq3fdtcqessi6ncvfokt.png', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 26', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734597/x2lir6xkpx61nb9kkmzt.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 27', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734597/smxqfudp3jzpntwkon6m.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 28', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734597/dtp5nwfuyhoaoxm37oue.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL),
    (GenerateCustomKeyString(), 'Image 29', 'https://res.cloudinary.com/dby2saqmn/image/upload/v1723734597/adrnf82nxur8uctqakjf.jpg', TRUE, 'ICON', TRUE, current_timestamp, NULL);


-- INSERT INTO EXPERT TAILORING
INSERT INTO expert_tailoring(expert_tailoring_id, expert_tailoring_name, size_image_url, model_image_url, status,
                             create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), 'shirtModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536717/clothes/mplmusqeleocefrsqrzf.png', true,
        current_timestamp, null),
       (GenerateCustomKeyString(), 'hoodieModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/kn5egywxx2qj4wwzwxts.png', true,
        current_timestamp, null),
       (GenerateCustomKeyString(), 'longSkirtModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536717/clothes/qf9prnqsfwofv9khp3cr.png', true,
        current_timestamp, null),
       (GenerateCustomKeyString(), 'skirtFullModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/wibukocpklimkobvv5sa.png', true,
        current_timestamp, null),
       (GenerateCustomKeyString(), 'womenSkirtTopModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/bwzhszbvmtqckax4oomk.png', true,
        current_timestamp, null),
       (GenerateCustomKeyString(), 'womenSkirtBottomModel', 'IMAGE URL',
        'https://res.cloudinary.com/dby2saqmn/image/upload/v1720536716/clothes/jfsmttronovmyw9xz2cg.png', true,
        current_timestamp, null);

-- INSERT INTO CATEGORY
INSERT INTO category (category_id, create_date, status, last_modified_date, category_name)
VALUES (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Fabric'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Thread'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Cotton'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Ink'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Tape'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Label'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Button'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Bag'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Accessory'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Hang Tag'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Zipper'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Manual Printing'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Heat Printing'),
       (GenerateCustomKeyString(), current_timestamp, true, NULL, 'Embroidery');


-- INSERT INTO MATERIAL
INSERT INTO material (material_id, create_date, last_modified_date, base_price, hs_code, material_name, status, unit,
                      category_id)
VALUES
-- Fabric Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 5407101000, 'Cotton Fabric', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 5407101001, 'Silk Fabric', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 5407101002, 'Linen Fabric', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 58013600, 'Jacquard Fabric', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 52083900, 'Cool Cotton', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 58011000, 'Velvet Fleece', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 53092900, 'Silk Linen', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 51111100, 'Merino Wool', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 55161200, 'Pure Viscose', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 55162200, 'Bamboo Fabric', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 54071000, 'Chiffon Fabric', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 58021000, 'Terrycloth Fabric', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 54024600, 'Spandex Fabric', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Fabric')),


-- Thread Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 5508100000, 'Polyester Thread', 1, 'spool',
    (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 8000, 5508100001, 'Nylon Thread', 1, 'spool',
    (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 5508100002, 'Cotton Thread', 1, 'spool',
    (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 54033100, 'Viscose Thread', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 50040000, 'Silk Thread', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 54023100, 'Polyamide Thread', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 54011000, 'Core-Spun Thread', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 54033100, 'Rayon Thread', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 54023100, 'Weaving Thread', 1, 'spool',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 54023300, 'Serger Thread', 1, 'spool',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 54023900, 'Twist Thread', 1, 'spool',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 54023300, 'Knitting Thread', 1, 'spool',
     (SELECT category_id FROM category WHERE category_name = 'Thread')),

-- Cotton Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 5201000000, 'Organic Cotton', 1, 'kilogram',
    (SELECT category_id FROM category WHERE category_name = 'Cotton')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 5201000001, 'Recycled Cotton', 1, 'kilogram',
    (SELECT category_id FROM category WHERE category_name = 'Cotton')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 5201000002, 'Combed Cotton', 1, 'kilogram',
    (SELECT category_id FROM category WHERE category_name = 'Cotton')),

-- Ink Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 3215190000, 'Textile Ink', 1, 'liter',
    (SELECT category_id FROM category WHERE category_name = 'Ink')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 3215190001, 'Silk Screen Ink', 1, 'liter',
    (SELECT category_id FROM category WHERE category_name = 'Ink')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 3215190002, 'Dye Sublimation Ink', 1, 'liter',
    (SELECT category_id FROM category WHERE category_name = 'Ink')),

-- Tape Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 4823908500, 'Masking Tape', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Tape')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 9000, 4823908501, 'Double-Sided Tape', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Tape')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 4823908502, 'Packing Tape', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Tape')),

-- Label Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 4821102000, 'Woven Label', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Label')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 4821102001, 'Printed Label', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Label')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 4821102002, 'Heat Transfer Label', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Label')),

-- Button Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 9000, 9606210000, 'Plastic Button', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Button')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 9606210001, 'Metal Button', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Button')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 9606210002, 'Wooden Button', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Button')),

-- Bag Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 8000, 4202929100, 'Eco-Friendly Bag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Bag')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 4202929101, 'Reusable Shopping Bag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Bag')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 4202929102, 'Canvas Bag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Bag')),

-- Accessory Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 3926909700, 'Sewing Accessory Kit', 1, 'set',
    (SELECT category_id FROM category WHERE category_name = 'Accessory')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 3926909701, 'Zipper Pulls', 1, 'set',
    (SELECT category_id FROM category WHERE category_name = 'Accessory')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 3926909702, 'Sewing Needles', 1, 'set',
    (SELECT category_id FROM category WHERE category_name = 'Accessory')),

-- Hang Tag Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 10000, 4911991000, 'Paper Hang Tag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 4911991001, 'Plastic Hang Tag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 4911991002, 'Metal Hang Tag', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Hang Tag')),

-- Zipper Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 9607110000, 'Metal Zipper', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Zipper')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 9607110001, 'Plastic Zipper', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Zipper')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 9607110002, 'Invisible Zipper', 1, 'meter',
    (SELECT category_id FROM category WHERE category_name = 'Zipper')),

-- Manual Printing Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 3215, 'Screen Printing Ink', 1, 'liter',
    (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 9000, 48169040, 'Heat Transfer Paper', 1, 'sheet',
    (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 55111010, 'Embroidery Thread', 1, 'spool',
    (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 84433000, 'Screen Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 84433900, 'Block Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 84433900, 'Silk Screen Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 84433900, 'Stencil Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 3000, 48239000, 'Wax Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 96110000, 'Batik Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Manual Printing')),


-- Heat Printing Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 84629950, 'Heat Press Machine', 1, 'unit',
    (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 48169040, 'Transfer Vinyl', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 2000, 39199010, 'Thermal Tape', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 39199020, 'Heat Transfer Film', 1, 'roll',
    (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 5000, 84433900, 'Sublimation Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 6000, 84433900, 'Foil Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 7000, 84433900, 'Holographic Printing', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 84433000, 'Heat Transfer Vinyl', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 4000, 84433900, 'Plastisol Transfers', 1, 'meter',
     (SELECT category_id FROM category WHERE category_name = 'Heat Printing')),

-- Embroidery Category
    (GenerateCustomKeyString(), current_timestamp, NULL, 9000, 44140000, 'Embroidery Hoops', 1, 'piece',
    (SELECT category_id FROM category WHERE category_name = 'Embroidery')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 9000, 73199010, 'Embroidery Needles', 1, 'pack',
    (SELECT category_id FROM category WHERE category_name = 'Embroidery')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 62043300, 'Embroidery Floss', 1, 'skein',
    (SELECT category_id FROM category WHERE category_name = 'Embroidery')),
    (GenerateCustomKeyString(), current_timestamp, NULL, 1000, 73199020, 'Embroidery Scissors', 1, 'pair',
    (SELECT category_id FROM category WHERE category_name = 'Embroidery'));

-- INSERT INTO SIZE
INSERT INTO size (size_id, size_name, status, create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), 'S', true, current_timestamp, null),
       (GenerateCustomKeyString(), 'L', true, current_timestamp, null),
       (GenerateCustomKeyString(), 'M', true, current_timestamp, null),
       (GenerateCustomKeyString(), 'XL', true, current_timestamp, null),
       (GenerateCustomKeyString(), 'XXL', true, current_timestamp, null),
       (GenerateCustomKeyString(), 'XXXL', true, current_timestamp, null);

-- INSERT INTO LABOR QUANTITY
INSERT INTO labor_quantity (labor_quantity_id, labor_quantity_min_quantity, labor_quantity_max_quantity,
                            labor_quantity_min_price, labor_quantity_max_price, status, create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), 1, 100, 7000, 10000, true, current_timestamp, NULL),
       (GenerateCustomKeyString(), 101, 500, 6000, 9000, true, current_timestamp, NULL),
       (GenerateCustomKeyString(), 501, 1000, 5000, 8000, true, current_timestamp, NULL),
       (GenerateCustomKeyString(), 1001, 2000, 4000, 7000, true, current_timestamp, NULL),
       (GenerateCustomKeyString(), 2001, 999999, 3000, 6000, true, current_timestamp, NULL);


-- INSERT INTO USERS with password Aa@123456
INSERT INTO users (user_id, email, password, full_name, language, phone_number, role_id, provider, user_status,
                   image_url, create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), 'nguyenvanquan@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Nguyen Van Quan', 'vietnam', '0902818618',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/736x/7e/fd/3b/7efd3b6a1b4c91a9f09a9cb250d25703.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'hoanganhduy1122@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ha Anh Duy', 'vietnam', '0961782569',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/b9/64/16/b9641628b3a9aa5e19c2b92190596c65.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'chuongquocviet123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Chuong Quoc Viet', 'vietnam', '0904357264',
        (SELECT role_id FROM roles WHERE role_name = 'CUSTOMER'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/98/d5/8f/98d58f38385159ac1fe6b5a3b5ba16eb.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'tammtse161087@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Thanh Tam', 'vietnam', '0903826475',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/3c/d2/61/3cd26116b55b4ef867cd09661a8a9b54.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'tunmse161130@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Minh Tu', 'vietnam', '0904659543',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/52/5c/47/525c47a8488fccfd2869c1f56160dbc1.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'truongnhlse160191@fpt.edu.vn',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Lam Truong', 'vietnam', '0905638465',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/e0/06/21/e00621b25fc54a63588e17311b079031.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'ngohongquang999@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Ngo Hong Quang', 'vietnam', '0903826475',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/ee/95/c2/ee95c2a641654be91b4c9afe6897730d.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'doanthuan97@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Doan Thu An', 'vietnam', '0904659543',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/736x/c9/10/20/c910208d86e221cde7e12e50b044f812.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'phamthanhgiang458@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Pham Thanh Giang', 'vietnam', '0905638465',
        (SELECT role_id FROM roles WHERE role_name = 'BRAND'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/db/2d/fa/db2dfa841ee54f5f557e77e78827cd2d.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'adminsmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Admin Smart Tailor', 'vietnam', '0914633465',
        (SELECT role_id FROM roles WHERE role_name = 'ADMIN'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/da/95/52/da955211558100aec2dbeee65582271c.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'managersmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Manager Smart Tailor', 'vietnam',
        '09146376465',
        (SELECT role_id FROM roles WHERE role_name = 'MANAGER'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/4e/0a/8e/4e0a8e68c15e6bd37dc86c6a91523b78.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'employeesmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Employee Smart Tailor', 'vietnam',
        '09146993465',
        (SELECT role_id FROM roles WHERE role_name = 'EMPLOYEE'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/b4/d9/47/b4d94742faef3e6870a57fb9ba1d2a05.jpg',
        current_timestamp, null),

       (GenerateCustomKeyString(), 'accountantsmarttailor123@gmail.com',
        '$2a$12$BJhxIHUhA3r/XRflGiRU9.K8T3b8.JDXveDWDV0ypkBL.KeLUUx/K', 'Accountant Smart Tailor', 'vietnam',
        '09146993290',
        (SELECT role_id FROM roles WHERE role_name = 'ACCOUNTANT'), 'LOCAL', 'ACTIVE', 'https://i.pinimg.com/564x/28/b3/64/28b3645f39dafd0f47ebe0c8b7a52433.jpg',
        current_timestamp, null);

-- INSERT INTO EMPLOYEE
INSERT INTO employee (employee_id, success_task, fail_task, pending_task, total_task, create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'employeesmarttailor123@gmail.com'), 0, 0, 0,
        0, current_timestamp, null);

-- INSERT INTO CUSTOMER
INSERT INTO customer (customer_id, gender, date_of_birth, address, ward, district, province, number_of_violations,
                      create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'nguyenvanquan@gmail.com'), true, '1990-01-01', '240 Phạm Văn Đồng',
        'Hiệp Bình Chánh', 'Thủ Đức', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'hoanganhduy1122@gmail.com'), true, '1990-01-02', '50 Lê Văn Việt',
        'Hiệp Phú', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'chuongquocviet123@gmail.com'), true, '1990-01-03', '81 Nguyễn Xiển',
        'Long Thạnh Mỹ', 'Quận 9', 'Thành phố Hồ Chí Minh', 0, current_timestamp, null);


-- INSERT INTO BRAND
INSERT INTO brand (brand_id, brand_name, rating, number_of_ratings, total_rating_score, bank_name, account_number,
                   account_name, qr_payment, address, ward,
                   district, province, brand_status, number_of_violations, create_date, last_modified_date)
VALUES ((SELECT user_id FROM users WHERE email = 'tammtse161087@fpt.edu.vn'), 'Nike Brand', 1, 1, 1, 'Brand Bank',
        '1234567890', 'Nike', 'http://example.com/qr_nike.jpg', '740 Nguyễn Xiển', ' Long Thạnh Mỹ', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'tunmse161130@fpt.edu.vn'), 'Adidas Brand', 1, 1, 1, 'Brand Bank',
        '0987654321', 'Adidas', 'http://example.com/qr_adidas.jpg', '269 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'truongnhlse160191@fpt.edu.vn'), 'Puma Brand', 1, 1, 1, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '441 Lê Văn Việt', 'Tăng Nhơn Phú A', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'ngohongquang999@gmail.com'), 'Hong Quang Brand', 1, 1, 1,
        'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '857 Phạm Văn Đồng', 'Linh Tây', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'doanthuan97@gmail.com'), 'Thuan An Brand', 1, 1, 1, 'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '432 Đ. Liên Phường', 'Phước Long B', 'Quận 9',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null),

       ((SELECT user_id FROM users WHERE email = 'phamthanhgiang458@gmail.com'), 'Thanh Giang Brand', 1, 1, 1,
        'Brand Bank',
        '1357924680', 'Puma', 'http://example.com/qr_puma.jpg', '77C Trần Ngọc Diện', 'Thảo Điền', 'Thủ Đức',
        'Thành phố Hồ Chí Minh', 'ACCEPT', 0, current_timestamp, null);

-- INSERT SIZE EXPERT TAILORING

INSERT INTO size_expert_tailoring (expert_tailoring_id, size_id, ratio, status, create_date, last_modified_date)
VALUES
-- hoodieModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, true, CURRENT_TIMESTAMP, NULL),

-- shirtModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
    (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.15, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.3, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.5, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.7, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 1.9, true, CURRENT_TIMESTAMP, NULL),

-- longSkirtModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
    (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.25, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.5, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.75, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 2, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2.25, true, CURRENT_TIMESTAMP, NULL),

-- skirtFullModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.3, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.5, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.7, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 1.9, true, CURRENT_TIMESTAMP, NULL),

-- womenSkirtTopModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
    (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, true, CURRENT_TIMESTAMP, NULL),

-- womenSkirtBottomModel
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'S'), 1, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'M'), 1.2, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'L'), 1.4, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XL'), 1.6, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXL'), 1.8, true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 (SELECT size_id FROM size WHERE size_name = 'XXXL'), 2, true, CURRENT_TIMESTAMP, NULL);

-- INSERT BRAND EXPERT TAILORING
INSERT INTO brand_expert_tailoring(brand_id, expert_tailoring_id, create_date, last_modified_date)
VALUES

-- Brand with email tunmse161130@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'), current_timestamp,
 null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email tammtse161087@fpt.edu.vn have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email ngohongquang999@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'hoodieModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'ngohongquang999@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'longSkirtModel'), current_timestamp,
 null),

-- Brand with email doanthuan97@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'shirtModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'doanthuan97@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null),

-- Brand with email doanthuan97@gmail.com have ExpertTailoringName: shirtModel,
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtBottomModel'),
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'skirtFullModel'), current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'phamthanhgiang458@gmail.com'),
 (SELECT expert_tailoring_id from expert_tailoring WHERE expert_tailoring_name = 'womenSkirtTopModel'),
 current_timestamp, null);


-- INSERT BRAND LABOR QUANTITY
INSERT INTO brand_labor_quantity(brand_id, labor_quantity_id, brand_labor_cost_per_quantity, status, create_date,
                                 last_modified_date)
values
-- Brand Labor Quantity with Email tunmse161130@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 8000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 7000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 6000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 5000, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 4000, true,
 current_timestamp, null),

-- Brand Labor Quantity with Email tammtse161087@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 7000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 7000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 6000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 6000, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 4000, true,
 current_timestamp, null),

-- Brand Labor Quantity with Email truongnhlse160191@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1), 7000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 101), 7000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 501), 6000, true, current_timestamp,
 null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 1001), 5000, true,
 current_timestamp, null),

((SELECT b.brand_id
  FROM users u
           JOIN brand b on u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT labor_quantity_id from labor_quantity WHERE labor_quantity_min_quantity = 2001), 5000, true,
 current_timestamp, null);

-- INSERT INTO BRAND MATERIAL
INSERT INTO brand_material (brand_id, material_id, brand_price, create_date, last_modified_date)
 VALUES
--     BRAND MATERIAL WITH EMAIL tunmse161130@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 10000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cool Cotton'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Cool Cotton'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Velvet Fleece'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Velvet Fleece'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Linen'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Linen'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Merino Wool'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Merino Wool'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Pure Viscose'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Pure Viscose'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Viscose Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Viscose Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyamide Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Polyamide Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Rayon Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Rayon Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Spandex Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Spandex Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Weaving Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Weaving Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Serger Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Serger Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Twist Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Twist Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Knitting Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Knitting Thread'),
 CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Stencil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Stencil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wax Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Wax Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Batik Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Batik Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Block Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Block Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

-- Heat Printing
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sublimation Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Sublimation Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Foil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Foil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Holographic Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Holographic Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 CURRENT_TIMESTAMP, NULL),

-- BRAND MATERIAL WITH EMAIL tammtse161087@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 10000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cool Cotton'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Cool Cotton'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Velvet Fleece'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Velvet Fleece'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Linen'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Linen'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Merino Wool'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Merino Wool'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Pure Viscose'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Pure Viscose'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Viscose Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Viscose Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyamide Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Polyamide Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Rayon Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Rayon Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Spandex Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Spandex Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Weaving Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Weaving Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Serger Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Serger Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Twist Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Twist Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Knitting Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Knitting Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Stencil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Stencil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wax Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Wax Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Batik Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Batik Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Block Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Block Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

-- Heat Printing
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sublimation Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Sublimation Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Foil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Foil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Holographic Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Holographic Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'tammtse161087@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 CURRENT_TIMESTAMP, NULL),

-- BRAND MATERIAL WITH EMAIL truongnhlse160191@fpt.edu.vn
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Fabric'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Linen Fabric'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyester Thread'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Nylon Thread'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cotton Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Organic Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Recycled Cotton'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Combed Cotton'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Textile Ink'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Ink'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Dye Sublimation Ink'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Masking Tape'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Double-Sided Tape'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Packing Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Woven Label'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Printed Label'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Label'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Button'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Button'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wooden Button'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Eco-Friendly Bag'), 8000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Reusable Shopping Bag'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Canvas Bag'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Accessory Kit'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Zipper Pulls'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sewing Needles'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Paper Hang Tag'), 10000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Hang Tag'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Hang Tag'), 4000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Metal Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastic Zipper'), 7000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Invisible Zipper'), 5000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing Ink'), 3000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Paper'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Thread'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Press Machine'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Transfer Vinyl'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Thermal Tape'), 2000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Film'), 6000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Hoops'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Needles'), 9000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Floss'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Embroidery Scissors'), 1000, CURRENT_TIMESTAMP, NULL),
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Jacquard Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Cool Cotton'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Cool Cotton'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Velvet Fleece'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Velvet Fleece'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Linen'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Linen'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Merino Wool'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Merino Wool'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Pure Viscose'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Pure Viscose'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Bamboo Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Chiffon Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Viscose Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Viscose Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Polyamide Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Polyamide Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Core-Spun Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Rayon Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Rayon Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Terrycloth Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Spandex Fabric'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Spandex Fabric'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Weaving Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Weaving Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Serger Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Serger Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Twist Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Twist Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
  JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Knitting Thread'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Knitting Thread'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Stencil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Stencil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Wax Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Wax Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Batik Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Batik Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Block Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Block Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Silk Screen Printing'),
 CURRENT_TIMESTAMP, NULL),

-- Heat Printing
((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Sublimation Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Sublimation Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Foil Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Foil Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Holographic Printing'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Holographic Printing'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Heat Transfer Vinyl'),
 CURRENT_TIMESTAMP, NULL),

((SELECT b.brand_id
  FROM users u
           JOIN brand b ON u.user_id = b.brand_id
  WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
 (SELECT m.material_id FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 (SELECT m.base_price FROM material m WHERE m.material_name = 'Plastisol Transfers'),
 CURRENT_TIMESTAMP, NULL);

-- INSERT INTO BRAND PROPERTY
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES
-- Insert for email tunmse161130@fpt.edu.vn
(GenerateCustomKeyString(), (SELECT b.brand_id
                                   FROM users u
                                            JOIN brand b ON u.user_id = b.brand_id
                                   WHERE u.email = 'tunmse161130@fpt.edu.vn'),
 (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 50, true, CURRENT_TIMESTAMP,
 NULL);

-- Insert for email tammtse161087@fpt.edu.vn
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), (SELECT b.brand_id
                                          FROM users u
                                                   JOIN brand b ON u.user_id = b.brand_id
                                          WHERE u.email = 'tammtse161087@fpt.edu.vn'),
        (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 70, true,
        CURRENT_TIMESTAMP, NULL);

-- Insert for email truongnhlse160191@fpt.edu.vn
INSERT INTO brand_properties (brand_property_id, brand_id, property_id, brand_property_value, brand_property_status,
                              create_date, last_modified_date)
VALUES (GenerateCustomKeyString(), (SELECT b.brand_id
                                          FROM users u
                                                   JOIN brand b ON u.user_id = b.brand_id
                                          WHERE u.email = 'truongnhlse160191@fpt.edu.vn'),
        (SELECT property_id FROM system_properties WHERE property_name = 'BRAND_PRODUCTIVITY'), 60, true,
        CURRENT_TIMESTAMP, NULL);

-- INSERT INTO Expert Tailoring Material
INSERT INTO expert_tailoring_material (expert_tailoring_id, material_id, status, create_date, last_modified_date)
VALUES
-- ExpertTailoring : shirtModel and Category : Fabric
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Cotton Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Silk Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Linen Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
(SELECT material_id FROM material WHERE material_name = 'Jacquard Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Cool Cotton'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Velvet Fleece'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Silk Linen'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Merino Wool'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Pure Viscose'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Bamboo Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Chiffon Fabric'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Terrycloth Fabric'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Spandex Fabric'), true, CURRENT_TIMESTAMP, NULL),


-- ExpertTailoring : shirtModel and Category : Thread
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Polyester Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Nylon Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Cotton Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Viscose Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Silk Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Polyamide Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Core-Spun Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Rayon Thread'), true, current_timestamp, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Weaving Thread'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Serger Thread'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Twist Thread'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Knitting Thread'), true, CURRENT_TIMESTAMP, NULL),

-- Manual Printing
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Screen Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Stencil Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Wax Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Batik Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Block Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Silk Screen Printing'), true, CURRENT_TIMESTAMP, NULL),

-- Heat Printing
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Sublimation Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Foil Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Holographic Printing'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Heat Transfer Vinyl'), true, CURRENT_TIMESTAMP, NULL),
((SELECT expert_tailoring_id FROM expert_tailoring WHERE expert_tailoring_name = 'shirtModel'),
 (SELECT material_id FROM material WHERE material_name = 'Plastisol Transfers'), true, CURRENT_TIMESTAMP, NULL);
