package lv.bootcamp.team4.service;

public interface TransferService {

    /**
     * This method will handle credit TX.
     * @param accountNumber User account number
     * @param amount amount to be credited
     * @return true if success
     */
    boolean creditMoney(Integer accountNumber, Double amount);

    /**
     *
     * @param accountNumber
     * @param amount
     * @return
     */
    boolean debitMoney(Integer accountNumber, Double amount);

    /**
     *
     * @param accountNumber
     * @return
     */
    Double checkBalance(Integer accountNumber);

}
