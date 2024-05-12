package com.jzo2o.customer.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.AgencyCertificationAudit;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;

/**
 * <p>
 * 机构认证审核表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
public interface IAgencyCertificationAuditService extends IService<AgencyCertificationAudit> {

    void apply(AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO);

    RejectReasonResDTO queryCurrentUserLastRejectReason();

    PageResult<AgencyCertificationAuditResDTO> pageQuery(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO);

    void auditCertification(Long id, CertificationAuditReqDTO certificationAuditReqDTO);
}
