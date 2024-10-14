package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SpendEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity category;

}