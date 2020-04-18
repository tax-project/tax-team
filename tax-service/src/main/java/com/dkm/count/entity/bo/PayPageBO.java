package com.dkm.count.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @Author: HuangJie
 * @Date: 2020/4/18 13:37
 * @Version: 1.0V
 */
@Data
public class PayPageBO {
    private Integer pageMuch;
    private List<PayPageDataBO> payPages;
}
