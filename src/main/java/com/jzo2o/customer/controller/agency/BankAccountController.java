package com.jzo2o.customer.controller.agency;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.common.model.CurrentUserInfo;
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
 * @date 2024/5/1 17:11
 */
@RestController("agencyBankAccountController")
@RequestMapping("/agency/bank-account")
@Api(tags = "机构端 - 银行账户接口设计")
public class BankAccountController {

    @Autowired
    private IBankAccountService bankAccountService;

    @PostMapping
    @ApiOperation("新增或更新银行账户信息")
    public BankAccountResDTO saveOrUpdate(@RequestBody BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        bankAccountUpsertReqDTO.setId(currentUserInfo.getId());
        bankAccountUpsertReqDTO.setType(currentUserInfo.getUserType());
        bankAccountService.upsert(bankAccountUpsertReqDTO);
        return BeanUtil.toBean(bankAccountService.getById(currentUserInfo.getId()),BankAccountResDTO.class);
    }

    @GetMapping("/currentUserBankAccount")
    @ApiOperation("获取当前用户银行账号接口")
    public BankAccountResDTO getCurrentUserBankAccount() {
        Long id = UserContext.currentUserId();
        return BeanUtil.toBean(bankAccountService.getById(id),BankAccountResDTO.class);
    }

}
