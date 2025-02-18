package com.jzo2o.customer.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.customer.mapper.BankAccountMapper;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.service.IBankAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银行账户 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    /**
     * 新增或更新
     * @param bankAccountUpsertReqDTO
     */
    @Override
    public void upsert(BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        BankAccount bankAccount = BeanUtil.toBean(bankAccountUpsertReqDTO, BankAccount.class);
        super.saveOrUpdate(bankAccount);
    }
}
