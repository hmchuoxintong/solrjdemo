package com.mc.solrjdemo.solrjdemo.entity;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import lombok.Data;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author mc
* @since 2019-06-16
*/
    @Data
    @Accessors(chain = true)
    public class SysRoom{

    private static final long serialVersionUID = 1L;
    private Integer id;
            /**
            * 公寓id
            */
    private Integer floorId;

            /**
            * 用户id
            */
    private Integer userId;

            /**
            * 楼层
            */
    private Integer storey;

            /**
            * 合同id
            */
    private Integer contractId;

            /**
            * 房间名称
            */
    private String name;

            /**
            * 押金
            */
    private Integer deposit;

            /**
            * 租金
            */
    private BigDecimal rent;

            /**
            * 户型
            */
    private String type;

            /**
            * 房间面积
            */
    private BigDecimal area;

            /**
            * 管理费
            */
    private BigDecimal managercost;

            /**
            * 0未租1在租2申请退房3申请退房成功4退房5申请入住6同意申请入住7看房申请8同意看房9已签合同未付款 11-同意续租
            */
    private String status;

            /**
            * 房子的添加时间
            */
    private LocalDateTime time;

            /**
            * 房间标签 (1,2,3)
            */
    private String roomLabel;

            /**
            * 家具配备标签 (1,2,3)
            */
    private String furnitureLabel;

            /**
            * 押
            */
    private Integer bet;

            /**
            * 付
            */
    private Integer pay;

            /**
            * 续租跳转页面
            */
    private String releturl;


}
