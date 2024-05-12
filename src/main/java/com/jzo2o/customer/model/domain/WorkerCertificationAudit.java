package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务人员认证审核表
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("worker_certification_audit")
@ApiModel(value="WorkerCertificationAudit对象", description="服务人员认证审核表")
public class WorkerCertificationAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务人员id")
    @TableField("serve_provider_id")
    private Long serveProviderId;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "身份证号")
    @TableField("id_card_no")
    private String idCardNo;

    @ApiModelProperty(value = "身份证正面")
    @TableField("front_img")
    private String frontImg;

    @ApiModelProperty(value = "身份证反面")
    @TableField("back_img")
    private String backImg;

    @ApiModelProperty(value = "证明资料")
    @TableField("certification_material")
    private String certificationMaterial;

    @ApiModelProperty(value = "审核状态，0：未审核，1：已审核")
    @TableField("audit_status")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核人id")
    @TableField("auditor_id")
    private Long auditorId;

    @ApiModelProperty(value = "审核人姓名")
    @TableField("auditor_name")
    private String auditorName;

    @ApiModelProperty(value = "审核时间")
    @TableField("audit_time")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "认证状态，1：认证中，2：认证成功，3认证失败")
    @TableField("certification_status")
    private Integer certificationStatus;

    @ApiModelProperty(value = "驳回原因")
    @TableField("reject_reason")
    private String rejectReason;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
