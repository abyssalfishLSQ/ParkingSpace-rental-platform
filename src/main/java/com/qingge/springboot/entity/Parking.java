package com.qingge.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 李思琪
 * @since 2023-06-09
 */
@Getter
@Setter
@TableName("parking_info")
public class Parking implements Serializable {
    private static final long serialVersionUID = 1L;
    //为了避免在类的结构发生变化时导致反序列化失败，我们可以显式地声明一个serialVersionUID，并将其设为固定值。

    @TableId(value = "parkingid", type = IdType.AUTO)
    private Integer parkingid;

    private String parkingAddress;

    private int ownerId;

    private String ownerphone;

    private String status; //spare表示空闲，occupied表示此时间段已被预约

    private double priceNow;

    private double monthAvgPrice;

    private double weekAvgPrice;

    private int weekUseCnt;

    private int monthUseCnt;

    private String timeslot;

    private int bookedbywho;//被谁预约，id是谁

    private double grades;
    private String parkingUrl;
}
