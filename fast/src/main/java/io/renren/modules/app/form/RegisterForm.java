
/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 注册表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@Schema(name = "注册表单")
public class RegisterForm {
    @Schema(name = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @Schema(name = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

}
