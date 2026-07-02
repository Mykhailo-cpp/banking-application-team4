package lv.bootcamp.team4.service;

import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService{


    @Override
    public boolean creditMoney(Integer accountNumber, Double amount) {
        //get account details
        //add amount
        //save amount, if success return true
        return false;
    }

    @Override
    public boolean debitMoney(Integer accountNumber, Double amount) {
        return false;
    }

    @Override
    public Double checkBalance(Integer accountNumber) {
        return 0.0;
    }

}
