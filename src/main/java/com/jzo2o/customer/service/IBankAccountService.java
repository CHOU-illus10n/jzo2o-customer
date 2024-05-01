package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 16:53
 */
public interface IBankAccountService extends IService<BankAccount> {
    void upsert(BankAccountUpsertReqDTO bankAccountUpsertReqDTO);
}
