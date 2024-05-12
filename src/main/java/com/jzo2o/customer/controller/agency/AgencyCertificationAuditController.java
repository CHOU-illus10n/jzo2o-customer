package com.jzo2o.customer.controller.agency;

import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 18:03
 */
@RestController("agencyAgencyCertificationAuditController")
@RequestMapping("/agency/agency-certification-audit")
@Api(tags = "机构端 - 机构端提交认证接口设计")
public class AgencyCertificationAuditController {

    @Autowired
    private IAgencyCertificationAuditService agencyCertificationAuditService;

    @PostMapping
    @ApiOperation("机构提交认证申请")
    public void apply(@RequestBody  AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO) {
        agencyCertificationAuditAddReqDTO.setServeProviderId(UserContext.currentUserId());
        agencyCertificationAuditService.apply(agencyCertificationAuditAddReqDTO);
    }

    @GetMapping("/rejectReason")
    @ApiOperation("查询最新的驳回原因")
    public RejectReasonResDTO queryCurrentUserLastRejectReason() {
        return agencyCertificationAuditService.queryCurrentUserLastRejectReason();
    }
}
