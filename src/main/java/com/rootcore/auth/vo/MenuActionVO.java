package com.rootcore.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuActionVO {
    private String actionCode; // READ / CREATE / UPDATE / DELETE
    private boolean checked;   // true / false
}
