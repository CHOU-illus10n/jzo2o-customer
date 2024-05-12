package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;

/**
 * <p>
 * 服务人员认证审核表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
public interface IWorkerCertificationAuditService extends IService<WorkerCertificationAudit> {

    void apply(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO);


    RejectReasonResDTO getLastRejectReason();

    PageResult<WorkerCertificationAuditResDTO> pageQuery(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO);

    void auditCertification(Long id, CertificationAuditReqDTO certificationAuditReqDTO);
}
