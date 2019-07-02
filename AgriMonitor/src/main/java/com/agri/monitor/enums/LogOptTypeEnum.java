package com.agri.monitor.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LogOptTypeEnum {
	 QUERY(1, "查询"), ADD(2, "新增"), UPDATE(3, "修改"), DEL(4, "删除"), IMPORT(5, "导入"), LOGIN(6, "登陆"), SAVE(7, "保存");
	 
    private int typeId;
    private String typeName;
 
    LogOptTypeEnum(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
 
    public int getTypeId() {
        return typeId;
    }
 
    public String getTypeName() {
        return typeName;
    }
 
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        LogOptTypeEnum[] values = values();
        for (int i = 0; i < values.length; ++i) {
        	LogOptTypeEnum value = values[i];
            item.put("typeId", Integer.valueOf(value.getTypeId()));
            item.put("typeName", value.getTypeName());
            list.add(item);
        }
        return list;
    }
 
    public static String getTypeNameById(int typeId) {
        String value = "";
        LogOptTypeEnum[] values = values();
 
        for(int i = 0; i < values.length; ++i) {
        	LogOptTypeEnum type = values[i];
            if(type.getTypeId() == typeId) {
                value = type.getTypeName();
                break;
            }
        }
 
        return value;
    }

}
