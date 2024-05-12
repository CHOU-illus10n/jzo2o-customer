package com.jzo2o.customer.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/5/1 18:23
 */
@RestController("operationWorkerCertificationAuditController")
@RequestMapping("/operation/worker-certification-audit")
@Api(tags = "运营端 - 服务人员审核认真相关接口")
public class WorkerCertificationAuditController {

    @Autowired
    private IWorkerCertificationAuditService workerCertificationAuditService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public PageResult<WorkerCertificationAuditResDTO> page(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO) {
        return workerCertificationAuditService.pageQuery(workerCertificationAuditPageQueryReqDTO);
    }

    @PutMapping("/audit/{id}")
    @ApiOperation("审核服务人员认证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "认证申请id", required = true, dataTypeClass = Long.class)
    })
    public void auditCertification(@PathVariable("id") Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        workerCertificationAuditService.auditCertification(id, certificationAuditReqDTO);
    }
}
