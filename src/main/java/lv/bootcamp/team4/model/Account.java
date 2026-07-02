package lv.bootcamp.team4.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Account {
    private String id;
    private String userId;
    private String accountNumber;
    private double balance;
}
