package lv.bootcamp.team4.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String note;
    private LocalDateTime createdAt;
}
