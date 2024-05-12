package com.jzo2o.customer.controller.worker;

import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import com.jzo2o.customer.service.IWorkerCertificationService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 17:22
 */
@RestController("workerCertificationAuditController")
@RequestMapping("/worker/worker-certification-audit")
@Api(tags = "服务端 - 实名认证相关接口")
public class WorkerCertificationAuditController {

    @Autowired
    private IWorkerCertificationAuditService workerCertificationAuditService;

    @PostMapping
    @ApiOperation("认证申请提交")
    public void certificationApply(@RequestBody WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO) {
        workerCertificationAuditAddReqDTO.setServeProviderId(UserContext.currentUserId());
        workerCertificationAuditService.apply(workerCertificationAuditAddReqDTO);
    }

    @GetMapping("rejectReason")
    @ApiOperation("认证申请驳回原因")
    public RejectReasonResDTO getRejectReason() {
        return workerCertificationAuditService.getLastRejectReason();
    }
}
