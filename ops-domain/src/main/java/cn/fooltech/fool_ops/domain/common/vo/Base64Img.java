package cn.fooltech.fool_ops.domain.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Derek on 2017/2/13.
 */
@NoArgsConstructor
@Getter
@Setter
public class Base64Img{
    String base64;//图片base64数据
    String imgType;//文件名后缀
}
