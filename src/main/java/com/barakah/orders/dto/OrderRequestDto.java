package com.barakah.orders.dto;

import com.barakah.orders.model.Direction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotBlank(message="Asset must not be blank")
    private String asset;
    @NotNull(message="Amount must not be blank")
    @Positive(message="Amount must be greater than 0")
    private Float amount;
    @NotNull(message="Price must not be blank")
    @Positive(message="Price must be greater than 0")
    private Double price;
    @NotNull(message="direction must not be Empty and should only contain 'BUY' or 'SELL'")
    private Direction direction;
}
