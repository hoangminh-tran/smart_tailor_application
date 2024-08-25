package com.smart.tailor.utils.request;

import com.smart.tailor.enums.PrintType;
import com.smart.tailor.validate.ValidEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskRequest {
    @NotBlank(message = "Item Mask Name is required")
    @Size(max = 50, message = "Item Mask Name must not exceed 50 characters")
    private String itemMaskName;

    @NotBlank(message = "Type Of Item is required")
    @Size(max = 50, message = "Type Of Item must not exceed 50 characters")
    private String typeOfItem;

    private String materialID;

    @NotNull(message = "System Item status is required")
    private Boolean isSystemItem;

    @NotNull(message = "Position X is required")
    private Float positionX;

    @NotNull(message = "Position Y is required")
    private Float positionY;

    @NotNull(message = "Scale X is required")
    private Float scaleX;

    @NotNull(message = "Scale Y is required")
    private Float scaleY;

    @NotNull(message = "Rotate is required")
    private Float rotate;

    @NotNull(message = "Top Left Radius is required")
    private Float topLeftRadius;

    @NotNull(message = "Top Right Radius is required")
    private Float topRightRadius;

    @NotNull(message = "Bottom Left Radius is required")
    private Float bottomLeftRadius;

    @NotNull(message = "Bottom Right Radius is required")
    private Float bottomRightRadius;

    @NotNull(message = "Index Z is required")
    private Integer indexZ;

    @NotBlank(message = "Item Mask Image URL is required")
    private String imageUrl;

    @NotBlank(message = "Print Type is required")
    @ValidEnumValue(name = "printType", enumClass = PrintType.class)
    private String printType;
}
