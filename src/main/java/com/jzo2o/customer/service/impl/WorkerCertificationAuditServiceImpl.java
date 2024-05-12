package com.jzo2o.customer.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.enums.CertificationStatusEnum;
import com.jzo2o.customer.mapper.WorkerCertificationAuditMapper;
import com.jzo2o.customer.model.domain.WorkerCertification;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.WorkerCertificationUpdateDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IServeProviderService;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import com.jzo2o.customer.service.IWorkerCertificationService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员认证审核表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
@Service
public class WorkerCertificationAuditServiceImpl extends ServiceImpl<WorkerCertificationAuditMapper, WorkerCertificationAudit> implements IWorkerCertificationAuditService {

    @Autowired
    private IWorkerCertificationService workerCertificationService;

    @Autowired
    private IServeProviderService serveProviderService;

    @Override
    @Transactional
    public void apply(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO) {
        WorkerCertificationAudit workerCertificationAudit = BeanUtil.toBean(workerCertificationAuditAddReqDTO, WorkerCertificationAudit.class);

        //默认未审核
        workerCertificationAudit.setCertificationStatus(CertificationStatusEnum.INIT.getStatus());
        baseMapper.insert(workerCertificationAudit);
        //super.save(workerCertification);

        //查询认证情况
        Long serveProviderId = workerCertificationAuditAddReqDTO.getServeProviderId();
        WorkerCertification certification = workerCertificationService.getById(serveProviderId);
        if (ObjectUtil.isNotNull(certification)) {
            certification.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());
            workerCertificationService.updateById(certification);

        }else{
            WorkerCertification workerCertification = new WorkerCertification();
            workerCertification.setId(serveProviderId);
            workerCertification.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());
            workerCertificationService.save(workerCertification);
        }
    }

    @Override
    public RejectReasonResDTO getLastRejectReason() {
        LambdaQueryWrapper<WorkerCertificationAudit> query = new LambdaQueryWrapper<WorkerCertificationAudit>()
                .eq(WorkerCertificationAudit::getServeProviderId, UserContext.currentUserId())
                .orderByDesc(WorkerCertificationAudit::getCreateTime)
                .last("limit 1");
        WorkerCertificationAudit workerCertificationAudit = baseMapper.selectOne(query);

        return BeanUtil.toBean(workerCertificationAudit, RejectReasonResDTO.class);
    }

    /**
     * 分页查询
     * @param workerCertificationAuditPageQueryReqDTO
     * @return
     */
    @Override
    public PageResult<WorkerCertificationAuditResDTO> pageQuery(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO) {
        Page<WorkerCertificationAudit> page = PageUtils.parsePageQuery(workerCertificationAuditPageQueryReqDTO,WorkerCertificationAudit.class);
        LambdaQueryWrapper<WorkerCertificationAudit> queryWrapper = Wrappers.<WorkerCertificationAudit>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getName()), WorkerCertificationAudit::getName, workerCertificationAuditPageQueryReqDTO.getName())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getIdCardNo()), WorkerCertificationAudit::getIdCardNo, workerCertificationAuditPageQueryReqDTO.getIdCardNo())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getAuditStatus()), WorkerCertificationAudit::getAuditStatus, workerCertificationAuditPageQueryReqDTO.getAuditStatus())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getCertificationStatus()), WorkerCertificationAudit::getCertificationStatus, workerCertificationAuditPageQueryReqDTO.getCertificationStatus());
        Page<WorkerCertificationAudit> result = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(result, WorkerCertificationAuditResDTO.class);
    }

    /**
     * 审核认证信息
     * @param id
     * @param certificationAuditReqDTO
     */
    @Override
    public void auditCertification(Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        //1 更新申请记录 -- 已审核
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        LambdaUpdateWrapper<WorkerCertificationAudit> query = new LambdaUpdateWrapper<WorkerCertificationAudit>();
        query.eq(WorkerCertificationAudit::getId, id)
                .set(WorkerCertificationAudit::getAuditStatus, CertificationStatusEnum.PROGRESSING.getStatus())
                .set(WorkerCertificationAudit::getAuditorId, currentUserInfo.getId())
                .set(WorkerCertificationAudit::getAuditorName, currentUserInfo.getName())//审核人名称
                .set(WorkerCertificationAudit::getAuditTime, LocalDateTime.now())//审核时间
                .set(WorkerCertificationAudit::getCertificationStatus, certificationAuditReqDTO.getCertificationStatus())//认证状态
                .set(ObjectUtil.isNotEmpty(certificationAuditReqDTO.getRejectReason()), WorkerCertificationAudit::getRejectReason, certificationAuditReqDTO.getRejectReason());//驳回原因
        super.update(query);

        //2 更新认证状态
        WorkerCertificationAudit workerCertificationAudit = baseMapper.selectById(id);
        WorkerCertificationUpdateDTO workerCertificationUpdateDTO = new WorkerCertificationUpdateDTO();
        workerCertificationUpdateDTO.setId(workerCertificationAudit.getServeProviderId());
        workerCertificationUpdateDTO.setCertificationStatus(certificationAuditReqDTO.getCertificationStatus());
        if (ObjectUtil.equal(CertificationStatusEnum.SUCCESS.getStatus(), certificationAuditReqDTO.getCertificationStatus())) {
            //如果认证成功，需要更新服务人员/机构名称
            serveProviderService.updateNameById(workerCertificationAudit.getServeProviderId(), workerCertificationAudit.getName());

            workerCertificationUpdateDTO.setName(workerCertificationAudit.getName());
            workerCertificationUpdateDTO.setIdCardNo(workerCertificationAudit.getIdCardNo());
            workerCertificationUpdateDTO.setFrontImg(workerCertificationAudit.getFrontImg());
            workerCertificationUpdateDTO.setBackImg(workerCertificationAudit.getBackImg());
            workerCertificationUpdateDTO.setCertificationMaterial(workerCertificationAudit.getCertificationMaterial());
            workerCertificationUpdateDTO.setCertificationTime(workerCertificationAudit.getAuditTime());
        }
        workerCertificationService.updateById(workerCertificationUpdateDTO);
    }
}
