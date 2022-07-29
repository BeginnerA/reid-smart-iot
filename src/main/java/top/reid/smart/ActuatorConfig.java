package top.reid.smart;

import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 *  Factories 机制注入 Bean<br>
 *  SpringBoot 自动扫描注入
 * </p>
 *
 * @Author REID
 * @Blog <a href="https://blog.csdn.net/qq_39035773">Blog</a>
 * @GitHub <a href="https://github.com/BeginnerA">GitHub</a>
 * @Data 2022/7/28
 * @Version V1.0
 **/
@ComponentScan(basePackages = {"top.reid.smart.iot"})
public class ActuatorConfig {
}
