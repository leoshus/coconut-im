package com.sdw.soft.cocoim.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:43
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private Long userId;
    private String username;

}
