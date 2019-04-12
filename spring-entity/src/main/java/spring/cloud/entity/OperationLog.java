package spring.cloud.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author huwl
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("operation_log")
public class OperationLog extends Model<OperationLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 系统模块
     */
    @TableField("oper_moudel")
    private String operMoudel;
    /**
     * 操作类型1-新增2-编辑3-删除
     */
    private String type;
    /**
     * 操作的表
     */
    private String table;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 操作时间
     */
    private LocalDateTime time;
    /**
     * 操作人
     */
    private Integer operId;
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
