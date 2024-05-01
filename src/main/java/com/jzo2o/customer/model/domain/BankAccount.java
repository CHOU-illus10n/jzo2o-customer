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
 * 银行账户
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bank_account")
@ApiModel(value="BankAccount对象", description="银行账户")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务人员/机构id")
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "类型，2：服务人员，3：服务机构")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "户名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "银行名称")
    @TableField("bank_name")
    private String bankName;

    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "区")
    @TableField("district")
    private String district;

    @ApiModelProperty(value = "网点")
    @TableField("branch")
    private String branch;

    @ApiModelProperty(value = "银行账号")
    @TableField("account")
    private String account;

    @ApiModelProperty(value = "开户证明")
    @TableField("account_certification")
    private String accountCertification;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
