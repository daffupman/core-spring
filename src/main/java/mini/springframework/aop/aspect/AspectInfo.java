package mini.springframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mini.springframework.aop.PointcutLocator;

/**
 * 封装DefaultAspect和Order的属性值
 *
 * @author daffupman
 * @since 2020/6/13
 */
@AllArgsConstructor
@Getter
public class AspectInfo {

    private final int orderIndex;
    private final DefaultAspect aspectObject;
    private final PointcutLocator pointcutLocator;
}
