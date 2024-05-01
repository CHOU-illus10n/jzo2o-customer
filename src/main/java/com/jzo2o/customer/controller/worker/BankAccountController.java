package com.jzo2o.customer.controller.worker;


import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 16:51
 */
@RestController("workerBankAccountController")
@RequestMapping("/worker/bank-account")
@Api(tags = "服务端 - 银行账户接口")
public class BankAccountController {

    @Autowired
    private IBankAccountService bankAccountService;

    @PostMapping
    @ApiOperation("新增或更新银行账户信息")
    public void queryByServeProviderId(@RequestBody BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        bankAccountUpsertReqDTO.setId(currentUserInfo.getId());
        bankAccountUpsertReqDTO.setType(currentUserInfo.getUserType());
        bankAccountService.upsert(bankAccountUpsertReqDTO);
    }

    @GetMapping("/currentUserBankAccount")
    @ApiOperation("查询当前用户银行账户信息")
    public BankAccountResDTO queryByServeProviderId() {
        CurrentUserInfo currentUserInfo = UserContext.currentUser(); //currentUserId也可以
        BankAccount account = bankAccountService.getById(currentUserInfo.getId());
        return BeanUtil.toBean(account, BankAccountResDTO.class);
    }
}
