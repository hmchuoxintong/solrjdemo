package com.mc.solrjdemo.solrjdemo.entity;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableField;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;
    import org.apache.solr.client.solrj.beans.Field;

/**
* <p>
    * 公寓
    * </p>
*
* @author mc
* @since 2019-06-16
*/
    @Data
    @Accessors(chain = true)
    public class SysFloor {

    private static final long serialVersionUID = 1L;
    @Field("id")
    private String id;
            /**
            * x y两字段是经度和纬度
            */
    private BigDecimal x;

            /**
            * x y两字段是经度和纬度
            */
    private BigDecimal y;

            /**
            * 管家userid
            */
    private Integer userId;

            /**
            * 安全出口图片id
            */
    private Integer imgId;

            /**
            * 合同id
            */
    private Integer contractId;

            /**
            * 公寓名称
            */
    @Field("floor_name")
    private String fname;

            /**
            * 水费单价
            */
    private BigDecimal waterCharge;

            /**
            * 电费单价
            */
    private BigDecimal eleCharge;

            /**
            * 楼的添加时间
            */
    private LocalDateTime ftime;

            /**
            * 备注信息
            */
            @Field("floor_info")
    private String finfo;

            /**
            * 地址
            */
    private String address;

            /**
            * 签名id
            */
        @TableField("signatureId")
    private Integer signatureId;

            /**
            * 管家id  不使用
            */
    private Integer managerId;

            /**
            * 房东id
            */
    private Integer landlordId;

            /**
            * 周边服务
            */
        @TableField("Peripaservices")
    private String Peripaservices;

            /**
            * 允许逾期天数
            */
    private Integer overduedays;

            /**
            * 提前几天生成账单
            */
    private Integer advanceCreateDay;

            /**
            * 合同流水号
            */
    private Integer contractSerialNumber;

            /**
            * 周边地铁站信息
            */
    private String subway;

            /**
            * 是否在使用 1-使用 0-不使用
            */
    private Integer isUse;

            /**
            * 是否使用公寓水电表设备 0-不使用 1-使用
            */
    private Integer isUseEquipment;


}
