package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
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
